package de.fh_luebeck.jsn.doit.acitivites;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.fh_luebeck.jsn.doit.R;
import de.fh_luebeck.jsn.doit.asyncTasks.TodoSynchronisationTask;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.EventHandler;
import de.fh_luebeck.jsn.doit.interfaces.ToDoListEventHandler;
import de.fh_luebeck.jsn.doit.interfaces.ToDoPersistenceEvents;
import de.fh_luebeck.jsn.doit.util.AppConstants;
import de.fh_luebeck.jsn.doit.util.ToDoAdapter;

public class OverviewActivity extends AppCompatActivity implements ToDoListEventHandler, ToDoPersistenceEvents {

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
            new TodoSynchronisationTask().execute();
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
        // TODO jsn: Umbauen auf startAcitivityForResult
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
        // TODO Persistierung
    }

    @Override
    public void handleDoneClick(long toDoId) {
        // TODO Persistierung

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
}
