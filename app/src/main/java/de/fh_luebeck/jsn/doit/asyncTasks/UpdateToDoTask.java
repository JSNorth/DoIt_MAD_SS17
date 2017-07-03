package de.fh_luebeck.jsn.doit.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.events.EventHandler;
import de.fh_luebeck.jsn.doit.webservice.ToDoWebserviceFactory;
import retrofit2.Response;

/**
 * Created by USER on 03.07.2017.
 */

public class UpdateToDoTask extends AsyncTask<Void, Void, Void> {

    private ToDo task;

    public UpdateToDoTask(ToDo task) {
        this.task = task;
    }

    @Override
    protected Void doInBackground(Void... params) {

        // Lokal speichern
        task.save();

        try {
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
