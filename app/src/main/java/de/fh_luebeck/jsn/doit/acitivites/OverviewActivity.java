package de.fh_luebeck.jsn.doit.acitivites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.fh_luebeck.jsn.doit.R;
import de.fh_luebeck.jsn.doit.tasks.TodoSynchronisationTask;
import de.fh_luebeck.jsn.doit.tasks.UpdateToDoTask;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.EventHandler;
import de.fh_luebeck.jsn.doit.events.ToDoListEvents;
import de.fh_luebeck.jsn.doit.events.ToDoPersistenceEvents;
import de.fh_luebeck.jsn.doit.util.AppConstants;
import de.fh_luebeck.jsn.doit.adapter.ToDoAdapter;

/**
 * Übersicht über alle ToDos.
 */
public class OverviewActivity extends AppCompatActivity implements ToDoListEvents, ToDoPersistenceEvents {

    private boolean isWebAppReachable;
    private ToDoAdapter _adapter;
    private RecyclerView.LayoutManager _layoutManager;

    @InjectView(R.id.toolbar)
    Toolbar _toolbar;

    @InjectView(R.id.todo_list_recyclerview)
    RecyclerView _recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ButterKnife.inject(this);

        setSupportActionBar(_toolbar);

        // Intent auswerten
        Intent intent = getIntent();
        isWebAppReachable = intent.getBooleanExtra(AppConstants.INTENT_EXTRA_WEB_APP_AVAILABLE, false); // False als Default, dann kommt es nicht zu Problemen

        // Event-Hanlder
        EventHandler.registerForDataChangedSignal(this);

        // Datenabgleich mit der Web-App
        if (isWebAppReachable) {
            new TodoSynchronisationTask(getContentResolver()).execute();
        }

        // RecyclerView aufbauen
        _recyclerView.setHasFixedSize(true);
        _layoutManager = new LinearLayoutManager(this);
        _recyclerView.setLayoutManager(_layoutManager);
        List<ToDo> toDos = ToDo.listAll(ToDo.class);
        _adapter = new ToDoAdapter(this, toDos);
        _recyclerView.setAdapter(_adapter);
    }

    private void updateToDos() {
        List<ToDo> toDos = ToDo.listAll(ToDo.class);
        _adapter.updateData(toDos);
    }

    @OnClick(R.id.add_todo)
    public void createNewTodo() {
        Intent intent = new Intent(this, ToDoActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void handleEditClick(long toDoId) {
        Intent intent = new Intent(this, ToDoActivity.class);
        intent.putExtra("TODO_ID", toDoId);
        startActivityForResult(intent, 1);
    }

    @Override
    public void handleFavoriteClick(long toDoId) {
        ToDo task = ToDo.findById(ToDo.class, toDoId);

        task.setFavourite(!task.getFavourite());

        if (task == null) {
            Toast.makeText(this, "Fehler ToDo konnte nicht gefunden werden", Toast.LENGTH_LONG);
        }

        new UpdateToDoTask(task, null).execute();
    }

    @Override
    public void handleDoneClick(long toDoId) {
        ToDo task = ToDo.findById(ToDo.class, toDoId);

        task.setDone(!task.getDone());

        if (task == null) {
            Toast.makeText(this, "Fehler ToDo konnte nicht gefunden werden", Toast.LENGTH_LONG);
        }

        new UpdateToDoTask(task, null).execute();
    }

    @Override
    public void dataChangedSignal() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateToDos();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.change_order:
                _adapter.changeSortMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
