package de.fh_luebeck.jsn.doit.webservice;

import java.util.List;

import de.fh_luebeck.jsn.doit.data.ToDo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by USER on 02.07.2017.
 */

public interface ToDoWebService {

    @POST("/api/todos")
    public Call<ToDo> createTodo(@Body ToDo item);

    @GET("/api/todos")
    public Call<List<ToDo>> readAllTodos();

    @GET("/api/todos/{id}")
    public Call<ToDo> readTodo(@Path("id") long id);

    @PUT("/api/todos/{id}")
    public Call<ToDo> updateTodo(@Path("id") long id, @Body ToDo item);

    @DELETE("/api/todos")
    public Call<Boolean> deleteAllTodos();

    @DELETE("/api/todos/{id}")
    public Call<Boolean> deleteTodo(@Path("id") long dataItemId);
}
