package ca.bcit.avoidit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    List<Record> records;
    List<Record> records2;

    static SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    static ArrayList<ArrayList<LatLng>> coords;
    static ArrayList<ArrayList<LatLng>> coords2;
    static boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isDarkMode == true) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppThemeLight);
            isDarkMode = false;
        }
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String language = sharedPreferences.getString("language", "en");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        MainActivity.this.setContentView(R.layout.activity_main);

        initLayout();

        Button btn_view_routes = findViewById(R.id.btn_view_routes);
        btn_view_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ViewRoutesMapActivity.class);
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
                        ViewMapsObstructionActivity.class);
                startActivity(intent);
            }
        });

        APIInterface apiInterface = RetrofitClientInstance.getClient()
                .create(APIInterface.class);

        Call<Hazard> call = apiInterface
                .getHazardData("road-ahead-current-road-closures",
                        "comp_date");
        Call<Hazard> call2 = apiInterface
                .getHazardData("road-ahead-projects-under-construction",
                        "comp_date");

        System.out.println("====== call url : " + call.request().url().toString());
        coords = new ArrayList<>();
        coords2 = new ArrayList<>();
        call.enqueue(new Callback<Hazard>() {
            @Override
            public void onResponse(Call<Hazard> call, Response<Hazard> response) {
                records = response.body().getRecords();
                for (int i = 0; i < records.size(); i++) {

                    List temp = records.get(i).getFields().getGeom().getCoordinates();
                    if(records.get(i).getFields().getGeom().getType().equals("MultiLineString")){
                        for (int j = 0; j < temp.size(); j++) {
                            List c = (List) temp.get(j);
                            ArrayList<LatLng> tempArray = new ArrayList<>();
                            for (int k = 0; k < c.size(); k++) {
                                double x = (double) ((List) c.get(k)).get(0);
                                double y = (double) ((List) c.get(k)).get(1);
                                tempArray.add(new LatLng(y, x));
                            }
                            coords.add(tempArray);
                        }

                    }else if(records.get(i).getFields().getGeom().getType().equals("LineString")){
                        for (int j = 0; j < temp.size(); j++) {
                            ArrayList<LatLng> tempArray = new ArrayList<>();
                            List c = (List) temp.get(j);
                            double x = (double) (c.get(0));
                            double y = (double) (c.get(1));
                            tempArray.add(new LatLng(y, x));
                            coords.add(tempArray);
                        }
                    }else{
                        System.out.println("GeometryCollection type not supported");
                    }
                }
            }
            @Override
            public void onFailure(Call<Hazard> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });
        System.out.println("====== call url : " + call2.request().url().toString());
        call2.enqueue(new Callback<Hazard>() {
            @Override
            public void onResponse(Call<Hazard> call, Response<Hazard> response) {
                records2 = response.body().getRecords();
                for (int i = 0; i < records2.size(); i++) {

                    List temp = records2.get(i).getFields().getGeom().getCoordinates();
                    if(records2.get(i).getFields().getGeom().getType().equals("MultiLineString")){
                        for (int j = 0; j < temp.size(); j++) {
                            List c = (List) temp.get(j);
                            ArrayList<LatLng> tempArray = new ArrayList<>();
                            for (int k = 0; k < c.size(); k++) {
                                double x = (double) ((List) c.get(k)).get(0);
                                double y = (double) ((List) c.get(k)).get(1);
                                tempArray.add(new LatLng(y, x));
                            }
                            coords2.add(tempArray);
                        }

                    }else if(records2.get(i).getFields().getGeom().getType().equals("LineString")){
                        for (int j = 0; j < temp.size(); j++) {
                            ArrayList<LatLng> tempArray = new ArrayList<>();
                            List c = (List) temp.get(j);
                            double x = (double) (c.get(0));
                            double y = (double) (c.get(1));
                            tempArray.add(new LatLng(y, x));
                            coords2.add(tempArray);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Hazard> call, Throwable t) {
                Log.e("out", t.toString());
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_language:
                Toast.makeText(this, "Language clicked..",
                        Toast.LENGTH_SHORT).show();
                AlertDialog.Builder langBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                langBuilder.setTitle("Language Change");
                String[] langItems = {"English", "French"};
                langBuilder.setSingleChoiceItems(langItems, -1,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Toast.makeText(MainActivity.this,
                                        "Clicked English", Toast.LENGTH_LONG).show();
                                editor.putString("language", "en");
                                editor.apply();
                                recreate();
                                dialog.dismiss();
                            break;
                            case 1:
                                Toast.makeText(MainActivity.this,
                                        "Clicked French", Toast.LENGTH_LONG).show();
                                editor.putString("language", "fr");
                                editor.apply();
                                recreate();
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                AlertDialog langDialog = langBuilder.create();
                langDialog.setCanceledOnTouchOutside(false);
                langDialog.show();
                break;
            case R.id.menu_dark_mode:
                Toast.makeText(this, "Dark_mode clicked..",
                        Toast.LENGTH_SHORT).show();
                if(isDarkMode == true){
                    isDarkMode = false;
                }else{
                    isDarkMode = true;
                }
                recreate();
                break;
            case R.id.menu_metric:
                Toast.makeText(this, "Metric clicked..",
                        Toast.LENGTH_SHORT).show();
                AlertDialog.Builder metricBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                metricBuilder.setTitle("Metric Change");
                String[] metricItems = {"km", "mi"};
                metricBuilder.setSingleChoiceItems(metricItems, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Toast.makeText(MainActivity.this,
                                                "Clicked km", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        Toast.makeText(MainActivity.this,
                                                "Clicked mi", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        });
                AlertDialog metricDialog = metricBuilder.create();
                metricDialog.setCanceledOnTouchOutside(false);
                metricDialog.show();
                break;
            case R.id.menu_notification:
                Intent intent = new Intent(MainActivity.this,
                        NotificationsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_about:
                Toast.makeText(this, "About clicked..",
                        Toast.LENGTH_SHORT).show();

                final AlertDialog.Builder aboutBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                aboutBuilder.setTitle("About the App\n");

                final View aboutLayout = getLayoutInflater()
                        .inflate(R.layout.about_layout, null);
                aboutBuilder.setView(aboutLayout);

                aboutBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog aboutDialog = aboutBuilder.create();
                aboutDialog.show();
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
