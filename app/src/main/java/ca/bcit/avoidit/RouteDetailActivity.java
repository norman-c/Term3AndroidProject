package ca.bcit.avoidit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;

import java.time.LocalTime;
import java.util.ArrayList;

import ca.bcit.avoidit.model.UserRoute;

public class RouteDetailActivity extends AppCompatActivity {

    private String routeID;
    private String routeName;
    private String routePointA;
    private String routePointB;
    private String notificationTime;
    private ArrayList<Boolean> notificationDays;
    private Boolean notificationEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

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

        //set fields based on information
        EditText fieldRouteName = findViewById(R.id.plaintext_route_name);
        fieldRouteName.setText(routeName);

        EditText fieldRoutePointA = findViewById(R.id.plaintext_start_location);
        fieldRoutePointA.setText(routePointA);

        EditText fieldRoutePointB = findViewById(R.id.plaintext_end_location);
        fieldRoutePointB.setText(routePointB);

        //TODO: set day buttons color based on day state

        EditText fieldTime = findViewById((R.id.edittext_time));
        fieldTime.setText(notificationTime);

        Switch switchNotifications = findViewById(R.id.switch_notifications);
        switchNotifications.setChecked(notificationEnabled);
    }
}
