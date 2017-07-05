package de.fh_luebeck.jsn.doit.tasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import de.fh_luebeck.jsn.doit.data.AssociatedContact;
import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.EventHandler;
import de.fh_luebeck.jsn.doit.webservice.ToDoWebserviceFactory;
import retrofit2.Response;

/**
 * Created by USER on 03.07.2017.
 */

public class UpdateToDoTask extends AsyncTask<Void, Void, Void> {

    private ToDo task;
    private List<AssociatedContact> associatedContactDatas;

    public UpdateToDoTask(ToDo task, List<AssociatedContact> associatedContactDatas) {
        this.task = task;
        this.associatedContactDatas = associatedContactDatas;
    }

    @Override
    protected Void doInBackground(Void... params) {

        // Lokal speichern

        // Bei einem Update aus der Overview ist null erlaubt.
        if (associatedContactDatas == null) {
            associatedContactDatas = AssociatedContact.find(AssociatedContact.class, "task_id = ?", task.getId().toString());
        }

        List<AssociatedContact> oldContacts = AssociatedContact.find(AssociatedContact.class, "task_id = ?", task.getId().toString());
        if (oldContacts != null) {
            for (AssociatedContact associatedContactData : oldContacts) {
                associatedContactData.delete();
            }
        }

        for (AssociatedContact associatedContactData : associatedContactDatas) {
            associatedContactData.setTaskId(task.getId());
            associatedContactData.save();
        }
        task.save();

        try {
            task.setContacts(associatedContactDatas);
            Response resp = ToDoWebserviceFactory.getToDoWebserice().updateTodo(task.getId(), task).execute();
            if (resp.isSuccessful() == false) {
                EventHandler.webServiceError(resp);
                return null;
            }
            EventHandler.dataChangedSignal();
        } catch (IOException e) {
            EventHandler.webServiceException(e);
            return null;
        }

        return null;
    }
}
