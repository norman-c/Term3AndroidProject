package ca.bcit.avoidit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.bcit.avoidit.model.Coordinate;
import ca.bcit.avoidit.model.Hazard;
import ca.bcit.avoidit.model.Record;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ca.bcit.avoidit.MainActivity.coords;
import static ca.bcit.avoidit.MainActivity.coords2;

public class ViewMapsObstructionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Record> records;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_maps_obstruction);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        }

    private void addMarker2Map(Location location) {
        String msg = String.format("Current Location: %4.3f Lat %4.3f Long.",
                location.getLatitude(),
                location.getLongitude());

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlng).title(msg));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                addMarker2Map(lastKnownLocation);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
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

        addPolyLines(coords);
        addPolyLines(coords2);
    }

    private void addPolyLines(ArrayList<ArrayList<LatLng>> coords){
        for(int i = 0; i<coords.size(); i++) {
            System.out.println(coords.get(i).size());
            if(coords.get(i).size() == 2) {
                mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                coords.get(i).get(0), coords.get(i).get(1)
                        )
                        .width(10f)
                        .color(Color.RED));
            }else if(coords.get(i).size() == 3) {
                mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                coords.get(i).get(0), coords.get(i).get(1),  coords.get(i).get(2)
                        )
                        .width(10f)
                        .color(Color.RED));
            }else if(coords.get(i).size() == 4) {
                mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                coords.get(i).get(0), coords.get(i).get(1),  coords.get(i).get(2),  coords.get(i).get(3)
                        )
                        .width(10f)
                        .color(Color.RED));
            }else if(coords.get(i).size() == 5) {
                mMap.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                coords.get(i).get(0), coords.get(i).get(1),  coords.get(i).get(2),  coords.get(i).get(3),coords.get(i).get(4)
                        )
                        .width(10f)
                        .color(Color.RED));
            }


        }
    }
}
