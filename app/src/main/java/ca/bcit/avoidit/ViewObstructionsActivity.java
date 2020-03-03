package ca.bcit.avoidit;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.avoidit.model.Hazard;
import ca.bcit.avoidit.model.Record;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewObstructionsActivity extends AppCompatActivity {

    List<Record> records;

    ListView obstruction_project_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_obstructions);

        obstruction_project_list = findViewById(R.id.obstruction_project_list);

        APIInterface apiInterface = RetrofitClientInstance.getClient()
                .create(APIInterface.class);

        Call<Hazard> call = apiInterface
                .getHazardData("road-ahead-current-road-closures",
                        "comp_date");

        System.out.println("====== call url : " + call.request().url().toString());

        call.enqueue(new Callback<Hazard>() {
            @Override
            public void onResponse(Call<Hazard> call, Response<Hazard> response) {
                records = response.body().getRecords();

                ObstructionListAdapter adapter =
                        new ObstructionListAdapter(
                                ViewObstructionsActivity.this, records);

                obstruction_project_list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Hazard> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });

    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Toast toast = Toast.makeText(getApplicationContext(), "Boop!", Toast.LENGTH_SHORT);
//        toast.show();
//    }
}
