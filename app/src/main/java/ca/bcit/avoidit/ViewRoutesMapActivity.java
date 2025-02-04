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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;

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
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.avoidit.model.MyPoint;
import ca.bcit.avoidit.model.UserRoute;

import static ca.bcit.avoidit.MainActivity.coords;
import static ca.bcit.avoidit.MainActivity.coords2;

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





        listViewRoutes = findViewById(R.id.list_routes);
        routeList = new ArrayList<>();

        //Set on-click listeners for each item in the list.
        listViewRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                UserRoute routeUsed = routeList.get(position);
                LatLng pointA = getLocationFromAddress(view.getContext(),routeUsed.getRoutePointA());
                LatLng pointB = getLocationFromAddress(view.getContext(),routeUsed.getRoutePointB());
                mMap.addMarker(new MarkerOptions().position(pointA).title(routeUsed.getRoutePointA()));
                mMap.addMarker(new MarkerOptions().position(pointB).title(routeUsed.getRoutePointA()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((pointA),12.0f));

                drawRoute(pointA,pointB);
                MyPoint aFirst = new MyPoint(pointA.latitude,pointA.longitude);
                MyPoint bFirst = new MyPoint(pointB.latitude,pointB.longitude);

                checkIntersects(coords,aFirst,bFirst);
                checkIntersects(coords2,aFirst,bFirst);

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
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latlng));

    }

    public static LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try
        {
            address = coder.getFromLocationName(strAddress, 1);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            //Invalid address
            e.printStackTrace();
        }
        return p1;
    }

    private String getLocationFromLatlng(Context context, double lat, double lng)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        Address location = null;
        LatLng p1 = null;
        try
        {
            address = coder.getFromLocation(lat, lng,1);
            if(address==null)
            {
                return null;
            }
            location = address.get(0);
        }
        catch (Exception e)
        {
            //Invalid address
            e.printStackTrace();
        }
        return location.getThoroughfare();
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

    private void drawRoute(LatLng a, LatLng b){

        List<LatLng> path = new ArrayList();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD3lGC8RKD4XWuKRcEDlnw1es060HF8yhM")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, a.latitude+","+a.longitude, b.latitude+","+b.longitude);
        try {
            DirectionsResult res = req.await();
            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            System.out.println(ex);
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }
    }

    public static boolean doLineSegmentsIntersect(MyPoint p, MyPoint p2, MyPoint q, MyPoint q2) {
        MyPoint r = subtractPoints(p2, p);
        MyPoint s = subtractPoints(q2, q);

        float uNumerator = crossProduct(subtractPoints(q, p), r);
        float denominator = crossProduct(r, s);

        if (denominator == 0) {
            // lines are paralell
            return false;
        }

        float u = uNumerator / denominator;
        float t = crossProduct(subtractPoints(q, p), s) / denominator;

        return (t >= 0) && (t <= 1) && (u > 0) && (u <= 1);
    }

    /**
     * Calculate the cross product of the two points.
     *
     * @param {Object} point1 point object with x and y coordinates
     * @param {Object} point2 point object with x and y coordinates
     *
     * @return the cross product result as a float
     */
    public static float crossProduct(MyPoint point1, MyPoint point2) {
        return (float) (point1.x * point2.y - point1.y * point2.x);
    }

    /**
     * Subtract the second point from the first.
     *
     * @param {Object} point1 point object with x and y coordinates
     * @param {Object} point2 point object with x and y coordinates
     *
     * @return the subtraction result as a point object
     */
    public static MyPoint subtractPoints(MyPoint point1,MyPoint point2) {
        MyPoint result = new MyPoint();
        result.x = point1.x - point2.x;
        result.y = point1.y - point2.y;

        return result;
    }

    public static boolean checkIntersects(ArrayList<ArrayList<LatLng>> coords, MyPoint aFirst, MyPoint bFirst) {
        for (int i = 0; i < coords.size(); i++) {
            if(coords.get(i).size() == 2) {
                MyPoint aSecond = new MyPoint(coords.get(i).get(0).latitude,coords.get(i).get(0).longitude);
                MyPoint bSecond = new MyPoint(coords.get(i).get(1).latitude,coords.get(i).get(1).longitude);
                if(doLineSegmentsIntersect(aFirst,bFirst,aSecond,bSecond)){
//                    String temp = getLocationFromLatlng(listViewRoutes.getContext(),coords.get(i).get(0).latitude,coords.get(i).get(0).longitude);
//                    if(temp == null){
//                        Toast.makeText(this, "Route intersects with obstruction", Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(this, "Route intersects with obstruction at "+ temp, Toast.LENGTH_LONG).show();
//                    }
                    return true;
                }
            }
        }
        return false;
    }
}



