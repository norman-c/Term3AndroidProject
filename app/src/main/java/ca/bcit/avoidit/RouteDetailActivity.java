package ca.bcit.avoidit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import ca.bcit.avoidit.model.UserRoute;

public class RouteDetailActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_route_detail);

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
                    //almost there

                    //Set alarms for today(?) + the next six days.
                    calendar.set(Calendar.HOUR, 1);
                    calendar.set(Calendar.MINUTE, 1);

                    for (int i = 0; i < 7; i++) {
                        setAlarm(calendar, calendar.get(Calendar.DAY_OF_WEEK));
                        calendar.add(Calendar.DATE, 1);
                    }
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
    public void setAlarm(Calendar c, int dayOfWeek) {
        if (notificationDays.get(dayOfWeek-1)) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
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
}
