package de.fh_luebeck.jsn.doit.events;

/**
 * Created by USER on 03.07.2017.
 */

public interface ContactEvents {

    void sendEMail(int position);

    void removeContact(int position);

    void sendMessage(int position);

}
