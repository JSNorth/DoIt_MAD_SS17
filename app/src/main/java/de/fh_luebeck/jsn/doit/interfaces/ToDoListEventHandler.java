package de.fh_luebeck.jsn.doit.interfaces;

/**
 * Created by USER on 01.07.2017.
 */

public interface ToDoListEventHandler {

    void handleEditClick(long toDoId);

    void handleFavoriteClick(long toDoId);

    void handleDoneClick(long toDoId);
}
