package de.fh_luebeck.jsn.doit.acitivites;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.fh_luebeck.jsn.doit.R;
import de.fh_luebeck.jsn.doit.asyncTasks.DeleteToDoTask;
import de.fh_luebeck.jsn.doit.asyncTasks.ToDoCreateTask;
import de.fh_luebeck.jsn.doit.asyncTasks.UpdateToDoTask;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.fragments.DatePicker;
import de.fh_luebeck.jsn.doit.fragments.TimePicker;

public class ToDoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {


    @InjectView(R.id.toolbar)
    Toolbar _toolbar;

    @InjectView(R.id.input_todo_name)
    EditText _nameText;

    @InjectView(R.id.input_todo_description)
    EditText _descriptionText;

    @InjectView(R.id.input_todo_favorite)
    CheckBox _favoriteBox;

    @InjectView(R.id.input_todo_done)
    CheckBox _doneBox;

    @InjectView(R.id.input_todo_duedate)
    EditText _dueDateText;

    @InjectView(R.id.todo_id)
    TextView _id;

    @InjectView(R.id.btn_delete_todo)
    AppCompatButton _deleteButton;

    private SimpleDateFormat dateFormat;
    private ToDo _item = null;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        ButterKnife.inject(this);

        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date currentDate = new Date();
        _dueDateText.setText(dateFormat.format(currentDate));
        _id.setText("-1");

        Long todoId = -1L;
        if (getIntent() != null && getIntent().getExtras() != null) {
            todoId = getIntent().getExtras().getLong("TODO_ID", -1);
        }
        if (todoId != -1L) {
            // Edit
            _item = ToDo.findById(ToDo.class, todoId);

            if (_item == null) {
                Toast.makeText(this, "Fehler ToDo konnte nicht gefunden werden", Toast.LENGTH_LONG);
            }

            _dueDateText.setText(dateFormat.format(_item.getExpiry()));
            _descriptionText.setText(_item.getDescription());
            _nameText.setText(_item.getName());
            _favoriteBox.setChecked(_item.getFavourite());
            _id.setText(_item.getId().toString());
            _doneBox.setChecked(_item.getDone());

            _doneBox.setVisibility(View.VISIBLE);
            _deleteButton.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_submit_todo)
    public void saveToDo() {
        // Validate

        // Prüfung auf Update
        if (_item == null) {
            // Neues Objekt anlegen
            _item = new ToDo(_nameText.getText().toString(), _descriptionText.getText().toString(), false, _favoriteBox.isChecked(), getDueDate());

            new ToDoCreateTask(_item).execute();

        } else {
            _item.setDescription(_descriptionText.getText().toString());
            _item.setName(_nameText.getText().toString());
            _item.setFavourite(_favoriteBox.isChecked());
            _item.setExpiry(getDueDate().getTime());

            new UpdateToDoTask(_item).execute();
        }

        finish();
    }

    @OnClick(R.id.btn_delete_todo)
    public void deleteTodo() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Löschen")
                .setMessage("Sind Sie sicher, dass Sie die Aufgabe " + _item.getName() + " löschen möchten?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteToDoTask(_item).execute();
                        finish();
                    }

                })
                .setNegativeButton("Nein", null)
                .show();
    }

    private Date getDueDate() {
        Date dueDate = new Date();
        try {
            dueDate = dateFormat.parse(_dueDateText.getText().toString());
        } catch (ParseException ex) {
            Toast.makeText(this, "Fehler beim Parsen des Fälligkeitsdatums", Toast.LENGTH_LONG).show();
        }
        return dueDate;
    }

    @OnClick(R.id.input_todo_duedate)
    public void pickDate() {
        DatePicker picker = new DatePicker();
        picker.setDefaultDate(getDueDate());
        picker.setOnDateSetListener(this);
        picker.show(getSupportFragmentManager(), "DatePicker");
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;

        TimePicker picker = new TimePicker();
        picker.setDefaultDate(getDueDate());
        picker.setOnDateSetListener(this);
        picker.show(getFragmentManager(), "TimePicker");
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, hour, minute);
        _dueDateText.setText(dateFormat.format(new Date(c.getTimeInMillis())));
    }
}
