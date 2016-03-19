package com.github.leonardpieper.ceciVPlan;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.leonard.ceciVPlan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimetableActivity_old extends AppCompatActivity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        setTitle("Stundenplan");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tableLayout = (TableLayout)findViewById(R.id.timetable_table);
//        getLessons();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getLessons(){
        String rawData = new String();
        ArrayList daysHours = new ArrayList();
        rawData = MainActivity.theActivity.readFromFile("facher.json");
        if(!rawData.isEmpty()){
            try {
                JSONObject root = new JSONObject(rawData);
                JSONArray jaFaecher = new JSONArray();
                jaFaecher = root.getJSONArray("Faecher");
                for(int i = 0; i<jaFaecher.length(); i++){
                    JSONObject joFach = jaFaecher.getJSONObject(i);
                    String fach = joFach.getString("Fach");
                    String abk = joFach.getString("Abk");
                    String fachkrzl = joFach.getString("Fachkrzl");
                    String lehrer = joFach.getString("Lehrer");

                    JSONArray jaTage = new JSONArray();
                    jaTage = joFach.getJSONArray("Tage");
                    for(int j = 0; j < jaTage.length(); j++){
                        JSONObject joTag = jaTage.getJSONObject(j);
                        daysHours.add(joTag.getInt("Wochentag"));
                        JSONArray jaStunden = joTag.getJSONArray("Stunden");
                        daysHours.add(jaStunden.get(0));
                        daysHours.add(jaStunden.get(1));
                        daysHours.add(jaStunden.get(2));
                        daysHours.add(joTag.getString("Raum"));

                        for(int k = 0; k<3; k++) {
                            if(!jaStunden.get(k).toString().isEmpty() && jaStunden.get(k) != null) {
                                int zahl = Integer.parseInt(jaStunden.get(k).toString());
                                updateTable(zahl, joTag.getInt("Wochentag") + 1, abk, joTag.getString("Raum"));
                            }
                        }
                    }
                    System.out.println("Hi");
                }
                System.out.println("H2");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("H3");
        }
        System.out.println("H4");
    }

    private void updateTable(int rowNumber,int colNumber, String fachAbk, String raum){
        TextView tv = (TextView)tableLayout.findViewWithTag("tv" + rowNumber + colNumber);
        tv.setText(fachAbk + "\n" + raum);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tv.setBackgroundResource(R.drawable.cell_shape);
    }
}
