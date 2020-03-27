package ca.bcit.avoidit;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.logging.Logger;

import ca.bcit.avoidit.model.Hazard;
import ca.bcit.avoidit.model.Record;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewObstructionsOldActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    List<Record> records;

    private GoogleMap mMap;

//    ListView obstruction_project_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_obstructions);

//        obstruction_project_list = findViewById(R.id.obstruction_project_list);

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

                for(int i = 0; i < records.size(); i++){
//                    for(int j = 0; j < records.get(i).getFields().getGeom().getCoordinates().size(); j++){
//                        System.out.println(j);
//                    }
                    System.out.println(records.get(i).getFields().getGeom().getCoordinates());
//                    loop size
//                            draw polyline
                }

//                ObstructionListAdapter adapter =
//                        new ObstructionListAdapter(
//                                ViewObstructionsOldActivity.this, records);

//                obstruction_project_list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Hazard> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

//    drawPolyLIne (coordinate) {
//
//        get size of coordinate
//        build  parameter
//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//
//                        parameter
//                )
//                .width(10f)
//                .color(Color.RED));
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng vancouver = new LatLng(49.246292, -123.116226);

        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        vancouver,
                        12f
                )
        );
//===>
//
//        loop i for records
//            loop j of records.get(i).getFields().getGeom().getCoordinates()
//
//                call drayPolyline(records.get(i).getFields().getGeom().getCoordinates()[j])


        mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(49.28855588490259, -123.12399521470071)
                    )
                    .add(
                            new LatLng(49.288212987522826, -123.12278017401694)
                    )
//                    .add(
//
//                            new LatLng(49.26966673529961, -123.10073107481001),
//                            new LatLng(49.269645733608826, -123.10088127851485),
//                            new LatLng(49.26965623445531, -123.10144990682602),
//                            new LatLng(49.26967023558055, -123.10217946767806)
//                    )
//                    .add(
//                            new LatLng(49.269701738097694, -123.10275346040726),
//                            new LatLng(49.269719239487415, -123.10342401266097)
//                    )
//                    .add(
//                            new LatLng(49.229815951994, -123.11634290921),
//                            new LatLng(49.22987193918101, -123.11796539484001)
//                    )
//                    .add(
//                            new LatLng(49.22987193918101, -123.11796539484001),
//                            new LatLng(49.22991929949799, -123.11934009271002)
//                    )
//                    .add(
//                            new LatLng(49.22959325571955, -123.1179481744766),
//                            new LatLng(49.226223136089885, -123.11807692050935)
//                    )
//                    .add(
//                            new LatLng(49.238672517118516, -123.10345217585564),
//                            new LatLng(49.23863924355149, -123.10336902737619),
//                            new LatLng(49.23863749231052, -123.1032107770443)
//                    )
//                    .add(
//                            new LatLng(49.22887456628601, -123.12904290958),
//                            new LatLng(49.22884746033199, -123.12905385442998),
//                            new LatLng(49.228420430270006, -123.12906816644)
//                    )
//                    .add(
//                            new LatLng(49.229330104187994, -123.12902689964001),
//                            new LatLng(49.22887456628601, -123.12904290958)
//                    )
//                    .add(
//                            new LatLng(49.229783122497, -123.12901167949),
//                            new LatLng(49.229330104187994, -123.12902689964001)
//                    )
//                    .add(
//                            new LatLng(49.23024669260599, -123.12899610196001),
//                            new LatLng(49.229783122497, -123.12901167949)
//                    )
//                    .add(
//                            new LatLng(49.22611983937199, -123.12907458982001),
//                            new LatLng(49.22569471644999, -123.12908858998999)
//                    )
//                    .add(
//                            new LatLng(49.226581421768, -123.12913151242),
//                            new LatLng(49.226469317935994, -123.12906340021001),
//                            new LatLng(49.22611983937199, -123.12907458982001)
//                    )
//                    .add(
//                            new LatLng(49.227058279206005, -123.12911499643002),
//                            new LatLng(49.226581421768, -123.12913151242)
//                    )
//                    .add(
//                            new LatLng(49.227511864788, -123.12909943586),
//                            new LatLng(49.227058279206005, -123.12911499643002)
//                    )
//                    .add(
//                            new LatLng(49.227966359004995, -123.12908374004),
//                            new LatLng(49.227511864788, -123.12909943586)
//                    )
//                    .add(
//                            new LatLng(49.228420430270006, -123.12906816644),
//                            new LatLng(49.227966359004995, -123.12908374004)
//                    )
//                    .add(
//                            new LatLng(49.27853215051712, -123.09888571500777),
//                            new LatLng(49.27852165155994, -123.09825271368025)
//                    )
//                    .add(
//                            new LatLng(49.27104786665791, -123.168236564129),
//                            new LatLng(49.26502368383339, -123.168461869686),
//                            new LatLng(49.26414152211129, -123.168258021801)
//                    )
//                    .add(
//                            new LatLng(49.24421663104957, -123.16686898469925),
//                            new LatLng(49.24420262270209, -123.16589802503586)
//                    )
//                    .add(
//                            new LatLng(49.28122479496298, -123.11615861406999),
//                            new LatLng(49.280094467748995, -123.11786601657)
//                    )
//                    .add(
//                            new LatLng(49.282354702834, -123.11444927638001),
//                            new LatLng(49.28122479496298, -123.11615861406999)
//                    )
//                    .add(
//                            new LatLng(49.283388995449, -123.11288531143002),
//                            new LatLng(49.28307158951199, -123.11336531549001)
//                    )
//                    .add(
//                            new LatLng(49.283706426284, -123.11240538377),
//                            new LatLng(49.283388995449, -123.11288531143002)
//                    )
//                    .add(
//                            new LatLng(49.28307158951199, -123.11336531549001),
//                            new LatLng(49.282354702834, -123.11444927638001)
//                    )
//                    .add(
//                            new LatLng(49.28403764733599, -123.11190416819998),
//                            new LatLng(49.283706426284, -123.11240538377)
//                    )
//                    .add(
//                            new LatLng(49.284677941105, -123.11095341032),
//                            new LatLng(49.284368595496005, -123.11140382534)
//                    )
//                    .add(
//                            new LatLng(49.284368595496005, -123.11140382534),
//                            new LatLng(49.28403764733599, -123.11190416819998)
//                    )
//                    .add(
//                            new LatLng(49.27904054068199, -123.11945789744999),
//                            new LatLng(49.27798741871101, -123.12105008373)
//                    )
//                    .add(
//                            new LatLng(49.280094467748995, -123.11786601657),
//                            new LatLng(49.27904054068199, -123.11945789744999)
//                    )
                    .width(10f)
                    .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.26966673529961, -123.10073107481001),
//                        new LatLng(49.269645733608826, -123.10088127851485),
//                        new LatLng(49.26965623445531, -123.10144990682602),
//                        new LatLng(49.26967023558055, -123.10217946767806)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.269701738097694, -123.10275346040726),
//                        new LatLng(49.269719239487415, -123.10342401266097)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.229815951994, -123.11634290921),
//                        new LatLng(49.22987193918101, -123.11796539484001)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.22987193918101, -123.11796539484001),
//                        new LatLng(49.22991929949799, -123.11934009271002)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.22959325571955, -123.1179481744766),
//                        new LatLng(49.226223136089885, -123.11807692050935)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.238672517118516, -123.10345217585564),
//                        new LatLng(49.23863924355149, -123.10336902737619),
//                        new LatLng(49.23863749231052, -123.1032107770443)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.22887456628601, -123.12904290958),
//                        new LatLng(49.22884746033199, -123.12905385442998),
//                        new LatLng(49.228420430270006, -123.12906816644)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.229330104187994, -123.12902689964001),
//                        new LatLng(49.22887456628601, -123.12904290958)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.229783122497, -123.12901167949),
//                        new LatLng(49.229330104187994, -123.12902689964001)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.23024669260599, -123.12899610196001),
//                        new LatLng(49.229783122497, -123.12901167949)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.22611983937199, -123.12907458982001),
//                        new LatLng(49.22569471644999, -123.12908858998999)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.226581421768, -123.12913151242),
//                        new LatLng(49.226469317935994, -123.12906340021001),
//                        new LatLng(49.22611983937199, -123.12907458982001)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.227058279206005, -123.12911499643002),
//                        new LatLng(49.226581421768, -123.12913151242)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.227511864788, -123.12909943586),
//                        new LatLng(49.227058279206005, -123.12911499643002)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.227966359004995, -123.12908374004),
//                        new LatLng(49.227511864788, -123.12909943586)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.228420430270006, -123.12906816644),
//                        new LatLng(49.227966359004995, -123.12908374004)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.27853215051712, -123.09888571500777),
//                        new LatLng(49.27852165155994, -123.09825271368025)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.27104786665791, -123.168236564129),
//                        new LatLng(49.26502368383339, -123.168461869686),
//                        new LatLng(49.26414152211129, -123.168258021801)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.24421663104957, -123.16686898469925),
//                        new LatLng(49.24420262270209, -123.16589802503586)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.28122479496298, -123.11615861406999),
//                        new LatLng(49.280094467748995, -123.11786601657)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.282354702834, -123.11444927638001),
//                        new LatLng(49.28122479496298, -123.11615861406999)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.283388995449, -123.11288531143002),
//                        new LatLng(49.28307158951199, -123.11336531549001)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.283706426284, -123.11240538377),
//                        new LatLng(49.283388995449, -123.11288531143002)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.28307158951199, -123.11336531549001),
//                        new LatLng(49.282354702834, -123.11444927638001)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.28403764733599, -123.11190416819998),
//                        new LatLng(49.283706426284, -123.11240538377)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.284677941105, -123.11095341032),
//                        new LatLng(49.284368595496005, -123.11140382534)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.284368595496005, -123.11140382534),
//                        new LatLng(49.28403764733599, -123.11190416819998)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.27904054068199, -123.11945789744999),
//                        new LatLng(49.27798741871101, -123.12105008373)
//                )
//                .width(10f)
//                .color(Color.RED));

//        mMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.280094467748995, -123.11786601657),
//                        new LatLng(49.27904054068199, -123.11945789744999)
//                )
//                .width(10f)
//                .color(Color.RED));

    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Toast toast = Toast.makeText(getApplicationContext(), "Boop!", Toast.LENGTH_SHORT);
//        toast.show();
//    }
}
