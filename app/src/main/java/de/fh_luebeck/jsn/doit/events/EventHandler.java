package de.fh_luebeck.jsn.doit.events;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * Created by USER on 03.07.2017.
 */

public class EventHandler {

    private static List<ToDoPersistenceEvents> handler = new ArrayList<>();

    public static void registerForDataChangedSignal(ToDoPersistenceEvents eventHandler) {
        handler.add(eventHandler);
    }

    public static void dataChangedSignal() {
        for (ToDoPersistenceEvents hand : handler) {
            hand.dataChangedSignal();
        }
    }

    public static void webServiceError(Response webServiceResponse) {

    }

    public static void webServiceException(Exception ex) {

    }
}
