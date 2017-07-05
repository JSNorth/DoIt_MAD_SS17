package de.fh_luebeck.jsn.doit.asyncTasks;

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

public class DeleteToDoTask extends AsyncTask<Void, Void, Void> {

    private ToDo task;

    public DeleteToDoTask(ToDo task) {
        this.task = task;
    }

    @Override
    protected Void doInBackground(Void... params) {

        // Lokal durchführen
        List<AssociatedContact> associatedContactDatas = AssociatedContact.find(AssociatedContact.class, "task_id = ?", task.getId().toString());
        for (AssociatedContact associatedContactData : associatedContactDatas) {
            associatedContactData.delete();
        }

        task.delete();

        // Remote
        try {
            Response resp = ToDoWebserviceFactory.getToDoWebserice().deleteTodo(task.getId()).execute();
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
