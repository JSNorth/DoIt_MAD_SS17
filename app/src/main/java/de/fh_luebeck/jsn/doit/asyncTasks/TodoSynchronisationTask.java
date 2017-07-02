package de.fh_luebeck.jsn.doit.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import de.fh_luebeck.jsn.doit.data.ToDo;
import de.fh_luebeck.jsn.doit.webservice.ToDoWebserviceFactory;

/**
 * Created by USER on 14.04.2017.
 */

public class TodoSynchronisationTask extends AsyncTask<Void, Void, Void> {

    @Override
    public Void doInBackground(Void... params) {

        // Liegen lokale ToDos vor, werden die Webservice ToDos gelöscht und die lokalen ToDos übernommen.
        if (ToDo.first(ToDo.class) != null) {
            try {
                if (ToDoWebserviceFactory.getToDoWebserice().deleteAllTodos().execute().isSuccessful() != true) {
                    // Problembehandlung
                    // TODO jsn
                    Log.e(TodoSynchronisationTask.class.getSimpleName(), "Fehler beim Webservice Aufruf");
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Problembehandlung
                // TODO jsn
                Log.e(TodoSynchronisationTask.class.getSimpleName(), "Fehler beim Webservice Aufruf");
            }

            for (ToDo toDo : ToDo.listAll(ToDo.class)) {
                try {
                    if (ToDoWebserviceFactory.getToDoWebserice().createTodo(toDo).execute().isSuccessful() != true) {
                        // TODO jsn: Fehlerbehandlung
                        Log.e(TodoSynchronisationTask.class.getSimpleName(), "Fehler beim Webservice Aufruf");
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // TODO jsn: Fehlerbehandlung
                }
            }
        } else {
            // Liegen keine lokalen ToDos vor, werden die ToDos aus dem Webservice übernommen.
            try {
                for (ToDo toDo : ToDoWebserviceFactory.getToDoWebserice().readAllTodos().execute().body()) {
                    toDo.save();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // TODO jsn: Fehlerbehandlung
                Log.e(TodoSynchronisationTask.class.getSimpleName(), "Fehler beim Webservice Aufruf");
            }
        }


        return null;
    }
}
