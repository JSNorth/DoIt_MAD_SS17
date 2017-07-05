package de.fh_luebeck.jsn.doit.tasks;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import de.fh_luebeck.jsn.doit.data.AssociatedContact;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.EventHandler;
import de.fh_luebeck.jsn.doit.util.ContactQueries;
import de.fh_luebeck.jsn.doit.webservice.ToDoWebserviceFactory;
import retrofit2.Response;

/**
 * Created by USER on 14.04.2017.
 */

public class TodoSynchronisationTask extends AsyncTask<Void, Void, Void> {

    private ContentResolver resolver;

    public TodoSynchronisationTask(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Void doInBackground(Void... params) {

        // Liegen lokale ToDos vor, werden die Webservice ToDos gelöscht und die lokalen ToDos übernommen.
        if (ToDo.first(ToDo.class) != null) {
            try {
                Response resp = ToDoWebserviceFactory.getToDoWebserice().deleteAllTodos().execute();
                if (resp.isSuccessful() != true) {
                    EventHandler.webServiceError(resp);
                    return null;
                }
            } catch (IOException e) {
                EventHandler.webServiceException(e);
                return null;
            }

            for (ToDo toDo : ToDo.listAll(ToDo.class)) {
                try {

                    List<AssociatedContact> associatedContactDatas = AssociatedContact.find(AssociatedContact.class, "task_id = ?", toDo.getId().toString());
                    toDo.setContacts(associatedContactDatas);
                    Response resp = ToDoWebserviceFactory.getToDoWebserice().createTodo(toDo).execute();
                    if (resp.isSuccessful() != true) {
                        EventHandler.webServiceError(resp);
                        return null;
                    }
                } catch (IOException e) {
                    EventHandler.webServiceException(e);
                    return null;
                }
            }
        } else {
            // Liegen keine lokalen ToDos vor, werden die ToDos aus dem Webservice übernommen.
            try {
                Response<List<ToDo>> resp = ToDoWebserviceFactory.getToDoWebserice().readAllTodos().execute();
                if (resp.isSuccessful() == false) {
                    EventHandler.webServiceError(resp);
                    return null;
                }
                for (ToDo toDo : resp.body()) {
                    // Associated contacts

                    toDo.save();

                    for (String uri : toDo.getContacts()) {
                        new AssociatedContact(toDo.getId(), uri, ContactQueries.queryName(Uri.parse(uri), resolver), ContactQueries.queryPhone(Uri.parse(uri), resolver), ContactQueries.queryMail(Uri.parse(uri), resolver)).save();
                    }


                }
                EventHandler.dataChangedSignal();
            } catch (IOException e) {
                EventHandler.webServiceException(e);
            }
        }

        return null;
    }
}
