package de.fh_luebeck.jsn.doit.data;

/**
 * Created by USER on 02.07.2017.
 */

public class User {

    private String pwd;

    private String email;

    public User(String email, String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
