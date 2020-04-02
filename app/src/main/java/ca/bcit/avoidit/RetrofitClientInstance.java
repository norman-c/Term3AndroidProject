package ca.bcit.avoidit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://opendata.vancouver.ca/api/records/1.0/";
    private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/geocode";

    public static Retrofit getClient() {
        if (retrofit == null) {
            //Creates an instance of Retrofit, only if it hasn't been created yet. Singleton model!
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }

        return retrofit;
    }

    public static Retrofit getDirectionsClient() {
        if (retrofit == null) {
            //Creates an instance of Retrofit, only if it hasn't been created yet. Singleton model!
            retrofit = new Retrofit.Builder()
                    .baseUrl(DIRECTIONS_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
