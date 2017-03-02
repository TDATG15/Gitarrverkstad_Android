package com.example.chris.gitarrverkstad;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by stefa_000 on 2017-02-28.
 */

public interface InstrumentClient {
    @GET("/ProjectEE-war/webresources/entities.instrument")
    Call<InstrumentList> getInstruments();

    @DELETE("/ProjectEE-war/webresources/entities.instrument/{index}")
    Call<Instrument> deleteInstrument(
            @Path("index") String index
    );

    @PUT("/ProjectEE-war/webresources/entities.instrument/{index}")
    Call<Instrument> putInstrument(
            @Body Instrument instrument,
            @Path("index") String index
    );

    @POST("/ProjectEE-war/webresources/entities.instrument")
    Call<Instrument> postInstrument(
            @Body Instrument instrument
    );

    @GET("/WebApplication6/webresources/testbokning.bokning")
    Call <Bookings> getCalendar();
}
