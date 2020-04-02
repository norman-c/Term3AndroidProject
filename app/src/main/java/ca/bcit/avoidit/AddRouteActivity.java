package ca.bcit.avoidit;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ca.bcit.avoidit.model.UserRoute;

public class AddRouteActivity extends AppCompatActivity {

    EditText editName;
    EditText editPointA;
    EditText editPointB;
    Button addRouteButton;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route);

        database = FirebaseDatabase.getInstance().getReference("routes");

        editName = findViewById(R.id.edt_route_name);
        editPointA = findViewById(R.id.edt_start_location);
        editPointB = findViewById(R.id.edt_end_location);
        addRouteButton = findViewById(R.id.btn_add_route);

        addRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoute();
            }
        });
    }


    /**
     * Adds a route to the Firebase Database.
     */
    private void addRoute() {
        String routeName = editName.getText().toString().trim();
        String routePointA = editPointA.getText().toString().trim();
        String routePointB = editPointB.getText().toString().trim();
        ArrayList<Boolean> dayList = new ArrayList<Boolean>();
        for (int i = 0; i < 7; i++) {
            dayList.add(true);
        }

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

        String id = database.push().getKey();
        UserRoute userRoute = new UserRoute(id, routeName, routePointA, routePointB,
                "12:00", dayList, true);
        Task setValueTask = database.child(id).setValue(userRoute);

        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(AddRouteActivity.this, "Route added!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddRouteActivity.this, "Something went wrong...", Toast.LENGTH_LONG).show();
            }
        });
    }
}
