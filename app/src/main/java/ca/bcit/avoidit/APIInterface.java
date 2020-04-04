package ca.bcit.avoidit;

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


}
