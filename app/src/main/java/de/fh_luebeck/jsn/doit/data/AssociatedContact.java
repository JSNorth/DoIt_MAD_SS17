package de.fh_luebeck.jsn.doit.data;

import com.orm.SugarRecord;

/**
 * Created by USER on 03.07.2017.
 */

public class AssociatedContact extends SugarRecord {

    private long taskId;
    private String contactUri;

    private String name;
    private String mobile;
    private String eMail;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public AssociatedContact() {
    }

    public AssociatedContact(long taskId, String contactUri, String name, String mobile, String eMail) {
        this.taskId = taskId;
        this.contactUri = contactUri;
        this.name = name;
        this.mobile = mobile;
        this.eMail = eMail;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getContactUri() {
        return contactUri;
    }

    public void setContactUri(String contactUri) {
        this.contactUri = contactUri;
    }
}
