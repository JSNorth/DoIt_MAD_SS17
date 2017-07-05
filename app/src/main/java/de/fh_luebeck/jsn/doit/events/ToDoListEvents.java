package de.fh_luebeck.jsn.doit.events;

/**
 * Created by USER on 01.07.2017.
 */

public interface ToDoListEvents {

    void handleEditClick(long toDoId);

    void handleFavoriteClick(long toDoId);

    void handleDoneClick(long toDoId);
}
