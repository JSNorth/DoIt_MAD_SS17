package de.fh_luebeck.jsn.doit.interfaces;

import de.fh_luebeck.jsn.doit.data.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

/**
 * Created by USER on 02.07.2017.
 */

public interface UserWebService {

    @PUT("api/users/auth")
    public Call<Boolean> authenticateUser(@Body User user);
}
