package ca.bcit.avoidit;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import ca.bcit.avoidit.model.MyPoint;
import ca.bcit.avoidit.model.UserRoute;

public class RouteDetailActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener,
        OnMapReadyCallback {

    //The passed-in data/.
    String routeID;
    String routeName;
    String routePointA;
    String routePointB;
    String notificationTime;
    ArrayList<Boolean> notificationDays;
    Boolean notificationEnabled;

    //The fields referenced.
    EditText fieldRouteName;
    EditText fieldRoutePointA;
    EditText fieldRoutePointB;
    TextView currentTime;
    Switch switchNotifications;

    //Other variables.
    DatabaseReference database;
    ArrayList<Button> buttonList;

    static ArrayList<MyPoint> list = new ArrayList<>();

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        //set database reference
        database = FirebaseDatabase.getInstance().getReference("routes");

        //retrieve data from intent
        Intent intent = getIntent();
        UserRoute retrievedRoute = (UserRoute) intent.getSerializableExtra("route");

        routeID = retrievedRoute.getRouteID();
        routeName = retrievedRoute.getRouteName();
        routePointA = retrievedRoute.getRoutePointA();
        routePointB = retrievedRoute.getRoutePointB();
        notificationTime = retrievedRoute.getNotificationTime();
        notificationDays = retrievedRoute.getNotificationDays();
        notificationEnabled = retrievedRoute.getNotificationEnabled();

        //set fields based on retrieved information
        fieldRouteName = findViewById(R.id.plaintext_route_name);
        fieldRouteName.setText(routeName);

        fieldRoutePointA = findViewById(R.id.plaintext_start_location);
        fieldRoutePointA.setText(routePointA);

        fieldRoutePointB = findViewById(R.id.plaintext_end_location);
        fieldRoutePointB.setText(routePointB);

        currentTime = findViewById((R.id.textview_current));
        currentTime.setText(notificationTime);

        switchNotifications = findViewById(R.id.switch_notifications);
        switchNotifications.setChecked(notificationEnabled);

        //create a list of buttons, and assign colors / an onclick event
        buttonList = new ArrayList<>();
        Button buttonSun = findViewById(R.id.button_sunday); buttonList.add(buttonSun);
        Button buttonMon = findViewById(R.id.button_monday); buttonList.add(buttonMon);
        Button buttonTue = findViewById(R.id.button_tuesday); buttonList.add(buttonTue);
        Button buttonWed = findViewById(R.id.button_wednesday); buttonList.add(buttonWed);
        Button buttonThu = findViewById(R.id.button_thursday); buttonList.add(buttonThu);
        Button buttonFri = findViewById(R.id.button_friday); buttonList.add(buttonFri);
        Button buttonSat = findViewById(R.id.button_saturday); buttonList.add(buttonSat);

        for (int i = 0; i < 7; i++) {
            Button button = buttonList.get(i);

            if (notificationDays.get(i)) {
                button.setBackgroundColor(Color.parseColor("#aa0000"));
            } else {
                button.setBackgroundColor(Color.parseColor("#666666"));
            }

            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int index = buttonList.indexOf(v);
                    boolean newButtonState = !notificationDays.get(index);
                    notificationDays.set(index, newButtonState);
                    Button editedButton = buttonList.get(index);

                    if (newButtonState) {
                        editedButton.setBackgroundColor(Color.parseColor("#aa0000"));
                    } else {
                        editedButton.setBackgroundColor(Color.parseColor("#666666"));
                    }

                }
            });
        }

        //assign functions to set time, save, and delete
        Button buttonTime = findViewById(R.id.button_edit_time);
        buttonTime.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                editTime();
            }
        });

        Button saveRoute = findViewById(R.id.button_save_changes);
        saveRoute.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                editRoute();
            }
        });

        Button deleteRoute = findViewById(R.id.button_delete_route);
        deleteRoute.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                deleteRoute();
            }
        });

    }

    public void editTime() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "Time Picker");
    }

    public void editRoute() {
        String routeName = fieldRouteName.getText().toString().trim();
        String routePointA = fieldRoutePointA.getText().toString().trim();
        String routePointB = fieldRoutePointB.getText().toString().trim();
        String noteTime = currentTime.getText().toString().trim();
        boolean noteEnabled = switchNotifications.isEnabled();

        if (TextUtils.isEmpty(routeName)) {
            Toast.makeText(this, "You must enter a route name.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(routePointA)) {
            Toast.makeText(this, "You must enter a start point.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(routePointB)) {
            Toast.makeText(this, "You must enter an end point.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(noteTime)) {
            Toast.makeText(this, "You must enter a notification time.", Toast.LENGTH_LONG).show();
            return;
        }

        UserRoute userRoute = new UserRoute(routeID, routeName, routePointA, routePointB,
                noteTime, notificationDays, noteEnabled);
        Task setValueTask = database.child(routeID).setValue(userRoute);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(RouteDetailActivity.this, "Route modified!", Toast.LENGTH_LONG).show();
                cancelAlarm();
                if (noteEnabled) {
                    Calendar calendar = Calendar.getInstance();

                    //Extract time from our time field.
                    String time = (String) currentTime.getText();
                    time = time.replace(':', ' ');
                    Scanner scanner = new Scanner(time);
                    int newHour = scanner.nextInt();
                    int newMinute = scanner.nextInt();

                    //Set alarms for today + the next six days.
                    calendar.set(Calendar.HOUR_OF_DAY, newHour);
                    calendar.set(Calendar.MINUTE, newMinute);
                    calendar.set(Calendar.SECOND, 0);

                    for (int i = 0; i < 7; i++) {
                        setAlarm(calendar, calendar.get(Calendar.DAY_OF_WEEK),routePointA,routePointB);
                        calendar.add(Calendar.DATE, 1);
                    }
                    scanner.close();
                }
                finish();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RouteDetailActivity.this, "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteRoute() {
        Task removeValueTask = database.child(routeID).removeValue();
        removeValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(RouteDetailActivity.this, "Route deleted!", Toast.LENGTH_LONG).show();
                cancelAlarm();
                finish();
            }
        });

        removeValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RouteDetailActivity.this, "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Sets an alarm for the current day of the week.
     */
    public void setAlarm(Calendar c, int dayOfWeek,String a, String b) {
        if (notificationDays.get(dayOfWeek-1)) {
            LatLng pointA = ViewRoutesMapActivity.getLocationFromAddress(this,a);
            LatLng pointB = ViewRoutesMapActivity.getLocationFromAddress(this,b);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            MyPoint aFirst = new MyPoint(pointA.latitude,pointA.longitude);
            MyPoint bFirst = new MyPoint(pointB.latitude,pointB.longitude);
            list.clear();
            list.add(aFirst);
            list.add(bFirst);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, dayOfWeek - 1, intent, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        }
    }

    /**
     * Cancels all alarms, whether they're active or not.
     */
    public void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        for (int i = 0; i < 7; i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        TextView ct = currentTime;
        String properHour = String.format("%02d", hour);
        String properMinute = String.format("%02d", min);
        ct.setText(properHour + ":" + properMinute);
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

        String routePointA = fieldRoutePointA.getText().toString().trim();
        String routePointB = fieldRoutePointB.getText().toString().trim();

        LatLng pointA = getLocationFromAddress(fieldRoutePointA.getContext(), routePointA);
        LatLng pointB = getLocationFromAddress(fieldRoutePointB.getContext(), routePointB);

        mMap.addMarker(new MarkerOptions().position(pointA));
        mMap.addMarker(new MarkerOptions().position(pointB));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((pointA),12.0f));

        drawRoute(pointA, pointB);

    }

    private void drawRoute(LatLng a, LatLng b){

        List<LatLng> path = new ArrayList();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD3lGC8RKD4XWuKRcEDlnw1es060HF8yhM")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context,
                a.latitude+","+a.longitude, b.latitude+","
                        +b.longitude);

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

    private LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try
        {
            address = coder.getFromLocationName(strAddress, 5);
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
}