package ca.bcit.avoidit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.avoidit.model.UserRoute;

public class ViewRoutesActivity extends AppCompatActivity {

    ListView listViewRoutes; //The list view itself.
    List<UserRoute> routeList; //The list of routes, extracted from Firebase.

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routes);

        database = FirebaseDatabase.getInstance().getReference("routes");

        listViewRoutes = findViewById(R.id.list_routes);
        routeList = new ArrayList<UserRoute>();

        //Set on-click listeners for each item in the list.
        listViewRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewRoutesActivity.this, RouteDetailActivity.class);
                //set passed information based on the index of the item pressed

                startActivity(intent);
            }
        });


        /*
        RoutesAdapter adapter = new RoutesAdapter();
        routeList.setAdapter(adapter);
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewRoutesActivity.this,
                        RouteDetailActivity.class);
                startActivity(intent);
            }
        });
        */
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
                RouteListAdapter adapter = new RouteListAdapter(ViewRoutesActivity.this, routeList);
                listViewRoutes.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //empty
            }
        });
    }

    /*
    class RoutesAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater()
                    .inflate(R.layout.routes_list, parent, false);


            return convertView;
        }
    }
    */
}
