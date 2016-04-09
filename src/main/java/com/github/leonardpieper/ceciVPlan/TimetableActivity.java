package com.github.leonardpieper.ceciVPlan;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.leonard.ceciVPlan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class TimeTableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Stundenplan");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tableLayout = (TableLayout)findViewById(R.id.timetable_table);

        getLessons();
        changeRowSize(3);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_vplan:
                Intent intent = new Intent(TimeTableActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_schedule:
                break;
            case R.id.nav_test:
                Intent examsIntent = new Intent(this, ExamsActivity.class);
                examsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(examsIntent);
                break;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                settingsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingsIntent);
                break;
            case R.id.nav_help:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                helpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(helpIntent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getLessons(){
        String rawData = new String();
        ArrayList daysHours = new ArrayList();
        if(MainActivity.theActivity.readFromFile("facher.json")!=null){
            rawData = MainActivity.theActivity.readFromFile("facher.json");
        }else {rawData= null;}

        if(rawData!=null){
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
                    String color = joFach.getString("Farbe");

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
                                updateTable(zahl, joTag.getInt("Wochentag") + 1, abk, joTag.getString("Raum"), color);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTable(int rowNumber,int colNumber, String fachAbk, String raum, String farbe){
        TextView tv = (TextView)tableLayout.findViewWithTag("tv" + rowNumber + colNumber);
        tv.setText(fachAbk + "\n" + raum);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        if(!farbe.isEmpty() && farbe != null) {
            int color = new BigInteger(farbe, 16).intValue();
            if (getBrightness(color)) {
                tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            } else {
                tv.setTextColor(Color.WHITE);
            }
        }else {
            tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tv.setBackground(bgColor(farbe));
        } else {
        tv.setBackgroundResource(R.drawable.cell_shape);
        }
    }

    private void changeRowSize(int minLines){
        for(int i=1; i<=9; i++){
            for(int j = 1; j<=5; j++) {
                TextView tv = (TextView) tableLayout.findViewWithTag("tv" + i + j);
                tv.setMinLines(minLines);
            }
        }
    }

//    private ShapeDrawable bgColor(){
//        ShapeDrawable rect = new ShapeDrawable(new RectShape());
//        rect.getPaint().setColor(Color.GREEN);
//        return rect;
//    }

    private GradientDrawable bgColor(String farbe){
        int color;
        if(!farbe.isEmpty() && farbe != null){
            farbe = "#" + farbe;
            color = Color.parseColor(farbe);
        }else {
            color = 000000;
        }

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setColor(color);
        gd.setStroke(3, getResources().getColor(R.color.colorPrimary));
        return gd;
    }

    private boolean getBrightness(int color){

        int[] rgb = {Color.red(color), Color.green(color), Color.blue(color)};

        int brightness = (int) Math.sqrt(rgb[0] * rgb[0] * .241 + rgb[1] * rgb[1] * .691 + rgb[2] * rgb[2] * .068);

        //color is light
        if(brightness >= 200){
            return true;
        }else {
            return false;
        }



    }

}
