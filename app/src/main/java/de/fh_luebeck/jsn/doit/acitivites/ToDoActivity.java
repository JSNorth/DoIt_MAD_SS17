package de.fh_luebeck.jsn.doit.acitivites;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.fh_luebeck.jsn.doit.R;
import de.fh_luebeck.jsn.doit.adapter.ContactAdapter;
import de.fh_luebeck.jsn.doit.data.AssociatedContact;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.ContactEvents;
import de.fh_luebeck.jsn.doit.fragments.DatePicker;
import de.fh_luebeck.jsn.doit.fragments.TimePicker;
import de.fh_luebeck.jsn.doit.tasks.CreateToDoTask;
import de.fh_luebeck.jsn.doit.tasks.DeleteToDoTask;
import de.fh_luebeck.jsn.doit.tasks.UpdateToDoTask;
import de.fh_luebeck.jsn.doit.util.ContactQueries;

public class ToDoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ContactEvents {

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

    @InjectView(R.id.contacts_list)
    ListView _listView;

    @InjectView(R.id.no_associated_contacts)
    TextView _noContacts;

    MenuItem _deleteButton;

    private SimpleDateFormat dateFormat;
    private ToDo _item = null;
    private List<AssociatedContact> associatedContactDatas;
    private ContactAdapter contactAdapter;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private static final int CONTACT_PICKER_RESULT = 1001;

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
            associatedContactDatas = AssociatedContact.find(AssociatedContact.class, "task_id = ?", _item.getId().toString());
        }

        if (associatedContactDatas == null) {
            associatedContactDatas = new ArrayList<>();
        }

        contactAdapter = new ContactAdapter(this, R.layout.card_contact, associatedContactDatas, this);
        _listView.setAdapter(contactAdapter);

        if (associatedContactDatas.isEmpty()) {
            _noContacts.setVisibility(View.VISIBLE);
        }

    }

    public void saveToDo() {
        // Validate

        // Prüfung auf Update
        if (_item == null) {
            // Neues Objekt anlegen
            _item = new ToDo(_nameText.getText().toString(), _descriptionText.getText().toString(), false, _favoriteBox.isChecked(), getDueDate());

            new CreateToDoTask(_item, associatedContactDatas).execute();

        } else {
            _item.setDescription(_descriptionText.getText().toString());
            _item.setName(_nameText.getText().toString());
            _item.setFavourite(_favoriteBox.isChecked());
            _item.setExpiry(getDueDate().getTime());

            new UpdateToDoTask(_item, associatedContactDatas).execute();
        }

        finish();
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_menu, menu);
        if (_item != null) {
            _deleteButton = menu.findItem(R.id.delete);
            _deleteButton.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete:
                deleteTodo();
                return true;
            case R.id.save:
                saveToDo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.add_contact)
    public void addContact() {

        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1111);

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // handle contact results
                    Uri result = data.getData();
                    queryRelatedContatactInformation(result);
                    break;
            }

        } else {
            // gracefully handle failure
            Log.w(ToDoActivity.class.getSimpleName(), "Warning: activity result not ok");
        }
    }

    private void queryRelatedContatactInformation(Uri uri) {

        String id = uri.getLastPathSegment();
        String email = ContactQueries.queryMail(uri, getContentResolver());
        String displayName = ContactQueries.queryName(uri, getContentResolver());
        String phoneNumer = ContactQueries.queryPhone(uri, getContentResolver());

        AssociatedContact contact = new AssociatedContact();
        contact.setName(displayName);
        contact.setContactUri(id);
        contact.seteMail(email);
        contact.setMobile(phoneNumer);

        associatedContactDatas.add(contact);

        if (associatedContactDatas.isEmpty() == false) {
            _noContacts.setVisibility(View.GONE);
        }

        contactAdapter.updateData(associatedContactDatas);
    }

    @Override
    public void sendEMail(int position) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{associatedContactDatas.get(position).geteMail()});
        if (_item != null && _item.getName() != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, "Bezüglich: " + _item.getName());
        } else {
            intent.putExtra(Intent.EXTRA_SUBJECT, "Bezüglich einer Aufgabe");
        }
        if (_item != null && _item.getDescription() != null) {
            intent.putExtra(Intent.EXTRA_TEXT, "Beschreibung: " + _item.getDescription());
        }

        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    @Override
    public void removeContact(int position) {
        associatedContactDatas.remove(position);
        contactAdapter.updateData(associatedContactDatas);

        if (associatedContactDatas.isEmpty()) {
            _noContacts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void sendMessage(int position) {

        Uri uri = Uri.parse("smsto:" + associatedContactDatas.get(position).getMobile());
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        if (_item != null && _item.getName() != null) {
            it.putExtra("sms_body", _item.getName());
        }
        startActivity(Intent.createChooser(it, "Send SMS"));
    }
}
