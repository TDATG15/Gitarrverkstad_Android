package com.example.chris.gitarrverkstad;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by stefa_000 on 2017-03-03.
 */

public interface Client {

    @GET("/ProjectEE-war/webresources/entities.consultation")
    Call<ConsultationList> getConsultation();

    @GET("/ProjectEE-war/webresources/entities.consultation/{index}")
    Call<Consultation> getConsultationOfIndex(
        @Path("index") String index
    );

    @DELETE("/ProjectEE-war/webresources/entities.consultation/{index}")
    Call<Consultation> deleteConsultation(
            @Path("index") String index
    );

    @PUT("/ProjectEE-war/webresources/entities.instrument/{index}")
    Call<Consultation> putConsultation(
            @Body Consultation consultation,
            @Path("index") String index
    );

    @POST("/ProjectEE-war/webresources/entities.instrument")
    Call<Consultation> postConsultation(
            @Body Consultation consultationt
    );

    @GET("/ProjectEE-war/webresources/entities.event")
    Call<EventList> getEvent();

    @GET("/ProjectEE-war/webresources/entities.event/{index}")
    Call<Event> getEventOfIndex(
            @Path("index") String index
    );

    @DELETE("/ProjectEE-war/webresources/entities.event/{index}")
    Call<Event> deleteEvent(
            @Path("index") String index
    );

    @PUT("/ProjectEE-war/webresources/entities.event/{index}")
    Call<Event> putEvent(
            @Body Event event,
            @Path("index") String index
    );

    @POST("/ProjectEE-war/webresources/entities.event")
    Call<Event> postEvent(
            @Body Event event
    );

}
