package ca.bcit.avoidit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.avoidit.model.Geolocation;
import ca.bcit.avoidit.model.UserRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRoutesMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    ListView listViewRoutes; //The list view itself.
    List<UserRoute> routeList; //The list of routes, extracted from Firebase.

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routes_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = FirebaseDatabase.getInstance().getReference("routes");

        APIInterface apiInterface = RetrofitClientInstance.getDirectionsClient()
                .create(APIInterface.class);



        listViewRoutes = findViewById(R.id.list_routes);
        routeList = new ArrayList<>();

        //Set on-click listeners for each item in the list.
        listViewRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserRoute routeUsed = routeList.get(position);
                String address = "address=" + routeUsed.getRoutePointA().replaceAll(" ","+") + "&key=AIzaSyBonFeYm8XzCDPYLDdn5wvI4WhwUgJZVy0";
                Call<Geolocation> call = apiInterface
                        .getDirections("json",
                                address);
                System.out.println(address);
                call.enqueue(new Callback<Geolocation>() {
                    @Override
                    public void onResponse(Call<Geolocation> call, Response<Geolocation> response) {
                        System.out.println(response);

                    }

                    @Override
                    public void onFailure(Call<Geolocation> call, Throwable t) {
                        System.out.println("Penis");
                    }
                });
            }
            });

        listViewRoutes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewRoutesMapActivity.this, RouteDetailActivity.class);

                UserRoute routeUsed = routeList.get(position);
                intent.putExtra("route", routeUsed);
                startActivity(intent);
                return true;
            }
        });
        }


    @Override
    protected void onStart() {
        super.onStart();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                routeList.clear();
                for (DataSnapshot routeSnapshot : dataSnapshot.getChildren()) {
                    UserRoute route = routeSnapshot.getValue(UserRoute.class);
                    routeList.add(route);

                }
                RouteListAdapter adapter = new RouteListAdapter(ViewRoutesMapActivity.this, routeList);
                listViewRoutes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //empty
            }
        });
    }

    private void addMarker2Map(Location location) {
        String msg = String.format("Current Location: %4.3f Lat %4.3f Long.",
                location.getLatitude(),
                location.getLongitude());

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlng).title(msg));

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap = googleMap;
        Location lastKnownLocation = null;

        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number",0) == 0 ) {

            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    addMarker2Map(location);
                }
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {}

                @Override
                public void onProviderEnabled(String s) {}

                @Override
                public void onProviderDisabled(String s) {}

            };
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                addMarker2Map(lastKnownLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()),12.0f));
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

        }

    }
}

