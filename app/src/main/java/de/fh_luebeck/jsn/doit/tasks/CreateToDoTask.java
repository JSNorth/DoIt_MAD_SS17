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
 * Created by USER on 02.07.2017.
 */

public class CreateToDoTask extends AsyncTask<Void, Void, Void> {

    private ToDo item;
    private List<AssociatedContact> associatedContactDatas;

    public CreateToDoTask(ToDo item, List<AssociatedContact> associatedContactDatas) {
        this.item = item;
        this.associatedContactDatas = associatedContactDatas;
    }

    @Override
    protected Void doInBackground(Void... params) {

        item.save();
        for (AssociatedContact associatedContactData : associatedContactDatas) {
            associatedContactData.setTaskId(item.getId());
            associatedContactData.save();
        }

        try {
            item.setContacts(associatedContactDatas);
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
