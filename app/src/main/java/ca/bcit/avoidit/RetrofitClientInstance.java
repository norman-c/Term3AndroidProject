package ca.bcit.avoidit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://opendata.vancouver.ca/api/records/1.0/search/?dataset=road-ahead-current-road-closures&facet=comp_date";

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
}
