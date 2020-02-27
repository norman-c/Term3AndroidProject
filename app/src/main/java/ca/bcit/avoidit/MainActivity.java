package ca.bcit.avoidit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.bcit.avoidit.model.Fields;
import ca.bcit.avoidit.model.Hazard;
import ca.bcit.avoidit.model.Record;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();

        Button btn_view_routes = findViewById(R.id.btn_view_routes);
        btn_view_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ViewRoutesActivity.class);
                startActivity(intent);
            }
        });

        Button btn_add_routes = findViewById(R.id.btn_add_route);
        btn_add_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        AddRouteActivity.class);
                startActivity(intent);
            }
        });

        Button btn_view_obstructions = findViewById(R.id.btn_view_obstructions);
        btn_view_obstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ViewObstructionsActivity.class);
                startActivity(intent);
            }
        });

        APIInterface apiInterface = RetrofitClientInstance.getClient()
                .create(APIInterface.class);

        Call<Hazard> call = apiInterface
                .getHazardData("road-ahead-current-road-closures", "comp_date");

        System.out.println("====== call url : " + call.request().url().toString());

        call.enqueue(new Callback<Hazard>() {
            @Override
            public void onResponse(Call<Hazard> call, Response<Hazard> response) {
                System.out.println("================== Success but...");
                ArrayList<Record> records = response.body().getRecords();
                System.out.println("================== Success");
//                for(int i = 0; i < records.size(); i++){
//                    System.out.println(records.get(i));
//                }
            }

            @Override
            public void onFailure(Call<Hazard> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_language:
                Toast.makeText(this, "Language clicked..",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_dark_mode:
                Toast.makeText(this, "Dark_mode clicked..",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_metric:
                Toast.makeText(this, "Metric clicked..",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_notification:
                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_about:
                Toast.makeText(this, "About clicked..",
                        Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AvoidIt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher_round);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_main_drawer_root);
        navigationView = (NavigationView) findViewById(R.id.nv_main_navigation_root);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
