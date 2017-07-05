package de.fh_luebeck.jsn.doit.webservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static de.fh_luebeck.jsn.doit.util.AppConstants.WEB_APP_URL;

/**
 * Created by USER on 02.07.2017.
 */

public class ToDoWebserviceFactory {

    private static ToDoWebService service = null;

    public static ToDoWebService getToDoWebserice() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WEB_APP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(ToDoWebService.class);
        }
        return service;
    }
}
