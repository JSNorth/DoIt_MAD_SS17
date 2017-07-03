package de.fh_luebeck.jsn.doit.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

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

    public ToDoCreateTask(ToDo item) {
        this.item = item;
    }

    @Override
    protected Void doInBackground(Void... params) {

        item.save();

        try {
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
