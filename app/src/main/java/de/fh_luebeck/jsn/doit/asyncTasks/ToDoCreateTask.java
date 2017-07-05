package de.fh_luebeck.jsn.doit.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import de.fh_luebeck.jsn.doit.data.AssociatedContact;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.EventHandler;
import de.fh_luebeck.jsn.doit.interfaces.ToDoPersistenceEvents;
import de.fh_luebeck.jsn.doit.webservice.ToDoWebserviceFactory;
import retrofit2.Response;

/**
 * Created by USER on 02.07.2017.
 */

public class ToDoCreateTask extends AsyncTask<Void, Void, Void> {

    private ToDo item;
    private List<AssociatedContact> associatedContacts;

    public ToDoCreateTask(ToDo item, List<AssociatedContact> associatedContacts) {
        this.item = item;
        this.associatedContacts = associatedContacts;
    }

    @Override
    protected Void doInBackground(Void... params) {

        item.save();
        for (AssociatedContact associatedContact : associatedContacts) {
            associatedContact.setTaskId(item.getId());
            associatedContact.save();
        }

        try {
            item.setContacts(associatedContacts);
            Response response = ToDoWebserviceFactory.getToDoWebserice().createTodo(item).execute();

            if (response.isSuccessful() == false) {
                EventHandler.webServiceError(response);
                return null;
            }

            EventHandler.dataChangedSignal();
        } catch (IOException e) {
            EventHandler.webServiceException(e);
        }

        return null;
    }
}
