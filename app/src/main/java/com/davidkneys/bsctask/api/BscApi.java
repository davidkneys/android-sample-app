package com.davidkneys.bsctask.api;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Interface for construction of Retrofit service bridging BSC API.
 */
public interface BscApi {

    String ROOT_ENDPOINT = "http://private-9aad-note10.apiary-mock.com/";

    @GET("/notes")
    Single<List<Note>> getNotes();

    @GET("/notes/{id}")
    Single<Note> getNote(@Path("id") int id);

    @POST("/notes")
    Single<Note> createNote(@Body PutNoteRequest note);

    @PUT("/notes/{id}")
    Single<Note> updateNote(@Path("id") int id, @Body PutNoteRequest note);

    @DELETE("/notes/{id}")
    Single<Response<Object>> removeNote(@Path("id") int id);
}

