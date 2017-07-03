package de.fh_luebeck.jsn.doit.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.EventHandler;
import de.fh_luebeck.jsn.doit.webservice.ToDoWebserviceFactory;
import retrofit2.Response;
import retrofit2.http.DELETE;

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
