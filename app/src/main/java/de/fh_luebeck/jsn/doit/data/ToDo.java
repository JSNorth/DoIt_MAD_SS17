package de.fh_luebeck.jsn.doit.data;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 14.04.2017.
 */

public class ToDo extends SugarRecord {

    String name;
    String description;
    Boolean done;
    Boolean favourite;
    long expiry;
    @Ignore
    List<String> contacts;

    public ToDo() {
    }

    public ToDo(String name, String description, Boolean done, Boolean favourite, Date dueDate) {
        this.name = name;
        this.description = description;
        this.done = done;
        this.favourite = favourite;
        this.expiry = dueDate.getTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public Date getExpiry() {
        return new Date(expiry);
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public void setContacts(List<AssociatedContact> contacts) {
        this.contacts = new ArrayList<>();
        if (contacts != null) {
            for (AssociatedContact associatedContact : contacts) {
                this.contacts.add(associatedContact.getContactUri());
            }
        }
    }

    public List<String> getContacts() {
        return contacts;
    }
}
