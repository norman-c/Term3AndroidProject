package ca.bcit.avoidit;

import java.util.List;

import ca.bcit.avoidit.model.Geolocation;
import ca.bcit.avoidit.model.Hazard;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("search/")
    Call<Hazard> getHazardData(

            @Query("dataset") String dataset,
            @Query("facet") String facet
    );

    @GET("/")
    Call<Geolocation> getDirections(

            @Query("outputFormat") String outputFormat,
            @Query("parameters") String parameters
    );
}
