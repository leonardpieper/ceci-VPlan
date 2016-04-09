package com.github.leonardpieper.ceciVPlan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonard.ceciVPlan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout ll;
    private List<String> listForSorting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Aufgaben & Prüfungen");

        ll = (LinearLayout)findViewById(R.id.examLayout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewExam();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listForSorting = sortedList();


        loadCards();

        if(ll.getChildCount()>1){
            int i = 0;
            TextView tvNoExams = (TextView)findViewById(R.id.noExams);
            tvNoExams.setVisibility(View.GONE);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_vplan:
                Intent intent = new Intent(ExamsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_schedule:
                Intent scheduleIntent = new Intent(this, TimeTableActivity.class);
                scheduleIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(scheduleIntent);
                break;
            case R.id.nav_test:
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

    private void addNewExam(){
        Intent ExamIntent = new Intent(ExamsActivity.this, AddExamActivity.class);
        startActivity(ExamIntent);
    }

    public void loadCards(){
        String rawData = new String();
        if(MainActivity.theActivity.readFromFile("ha.json")!=null){
            rawData = MainActivity.theActivity.readFromFile("ha.json");
        }else {rawData= null;}

        if(rawData != null){







            try {
                JSONObject root = new JSONObject(rawData);
                JSONArray jaAufgaben = new JSONArray();
                jaAufgaben = root.getJSONArray("Aufgaben");
                for(int i = 0; i<listForSorting.size(); i++){
//                    final LinearLayout ll = (LinearLayout)findViewById(R.id.examLayout);

                    CardView.LayoutParams params = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);

                    RelativeLayout.LayoutParams insideParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            insideParams.setMargins(16,16,16,16);

                    RelativeLayout.LayoutParams tvFachParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tvFachParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    tvFachParams.setMargins(16,0,0,0);

                    RelativeLayout.LayoutParams tvDatumParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tvDatumParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    RelativeLayout.LayoutParams tvDescParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tvDescParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    tvDescParams.addRule(RelativeLayout.BELOW, R.id.examDateTV);

                    RelativeLayout.LayoutParams btnDelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    btnDelParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    btnDelParams.addRule(RelativeLayout.BELOW, R.id.examDescTV);

                    RelativeLayout.LayoutParams colorParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    colorParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                    CardView cardView = new CardView(this);
                    cardView.setLayoutParams(params);


                    RelativeLayout rl = new RelativeLayout(this);
                    rl.setLayoutParams(insideParams);


                    TextView colorTV = new TextView(this);
                    colorTV.setLayoutParams(colorParams);
                    colorTV.setHeight(10);


                    TextView tvFach = new TextView(this);
                    tvFach.setLayoutParams(tvFachParams);
                    tvFach.setTextSize(20);

                    TextView tvDatum = new TextView(this);
                    tvDatum.setLayoutParams(tvDatumParams);
                    tvDatum.setId(R.id.examDateTV);
                    tvDatum.setTextSize(20);

                    TextView tvDesc = new TextView(this);
                    tvDesc.setLayoutParams(tvDescParams);
                    tvDesc.setId(R.id.examDescTV);
                    tvDesc.setTextSize(14);

                    final Button btndel = new Button(this);
                    btndel.setLayoutParams(btnDelParams);
                    btndel.setText("Erledigt");
                    btndel.setTextColor(getResources().getColor(R.color.defaultFont));
                    btndel.setBackgroundResource(R.drawable.stroke);
                    btndel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String delUUID = (String) btndel.getTag();
                            delUUID = delUUID.substring(0, delUUID.length()-1);
                            RelativeLayout delRL = (RelativeLayout)btndel.getParent();
                            final CardView delCV  =(CardView)delRL.getParent();

                            AnimationSet set = new AnimationSet(true);

                            Animation fadeOut = new AlphaAnimation(1, 0);
                            fadeOut.setInterpolator(new AccelerateInterpolator());
//                            fadeOut.setStartOffset(1000);
                            fadeOut.setDuration(250);
//                            delCV.setAnimation(fadeOut);

                            Animation moveleftToRight = new TranslateAnimation(0,500,0,0);
                            moveleftToRight.setDuration(750);
                            moveleftToRight.setFillAfter(true);

                            set.addAnimation(moveleftToRight);
                            set.addAnimation(fadeOut);

                            delCV.startAnimation(set);

                            delCV.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ll.removeView(delCV);
                                    if(ll.getChildCount()<=1){
                                        TextView tvNoExams = (TextView)findViewById(R.id.noExams);
                                        tvNoExams.setVisibility(View.VISIBLE);
                                    }
                                }
                            }, 750);

//                            ll.removeView(delCV);
                            final String bckpExams = MainActivity.theActivity.readFromFile("ha.json");

                            boolean examDeleted = delExam(delUUID);
                            if(examDeleted){
                                Snackbar snackbar = Snackbar.make(ll, "Fach gelöscht", Snackbar.LENGTH_LONG);
                                snackbar.setAction("Rückgängig", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        MainActivity.theActivity.writeToFile("ha.json", bckpExams);

                                        clearCardViews();
                                        listForSorting = sortedList();
                                        loadCards();
                                        TextView tvNoExams = (TextView)findViewById(R.id.noExams);
                                        tvNoExams.setVisibility(View.GONE);

                                    }
                                });
                                snackbar.show();


                            }else {
                                Toast toast = Toast.makeText(ExamsActivity.this, "Fach konnte nicht gelöscht werden", Toast.LENGTH_SHORT);
                                toast.show();
                            }


                        }
                    });

                    for(int j= 0; j<jaAufgaben.length(); j++){
                        JSONObject joAufgabe = jaAufgaben.getJSONObject(j);
                        String listDate = listForSorting.get(i);

                        int chckday = joAufgabe.getInt("Tag");
                        int chckmonth = joAufgabe.getInt("Monat") + 1;

                        String chckdayString = ""+chckday;
                        String chckmonthString = ""+chckmonth;
                        if(chckdayString.length()==1){
                            chckdayString = "0"+chckdayString;
                        }
                        if(chckmonthString.length()==1){
                            chckmonthString = "0" + chckmonthString;
                        }

                        String jsonDate = chckmonthString+chckdayString;
                        if(listDate.equals(jsonDate)){
                            String lesson = joAufgabe.getString("Fach");
                            int day = joAufgabe.getInt("Tag");
                            int month = joAufgabe.getInt("Monat") + 1;
                            String description = joAufgabe.getString("Beschreibung");

                            if(getColorFromName(lesson)!= null&&!getColorFromName(lesson).isEmpty()){
                                String color = "#" + getColorFromName(lesson);
                                int farbe = Color.parseColor(color);
                                colorTV.setBackgroundColor(farbe);
                            }

                            if(joAufgabe.getString("Typ").equals("Klausur") || joAufgabe.getString("Typ").equals("Mündl. Prüfung") || joAufgabe.getString("Typ").equals("Test")){
                                int cardColor = Color.parseColor("#B2DFDB") ;
                                cardView.setCardBackgroundColor(cardColor);
                                lesson= lesson + " - " + joAufgabe.getString("Typ");
                            }

                            tvFach.setText(lesson);
                            tvDatum.setText(day + "." + month);
                            tvDesc.setText(description);

                            btndel.setTag(joAufgabe.getString("UUID") + "1");
                            cardView.setTag(joAufgabe.getString("UUID"));




                            rl.addView(colorTV);
                            rl.addView(tvFach);
                            rl.addView(tvDatum);
                            rl.addView(tvDesc);
                            rl.addView(btndel);


                            cardView.addView(rl);


                            ll.addView(cardView);

                            joAufgabe.remove("Tag");
                            joAufgabe.put("Tag", 99);
                            break;
                        }
                    }
//                    String lesson = joAufgabe.getString("Fach");
//                    int day = joAufgabe.getInt("Tag");
//                    int month = joAufgabe.getInt("Monat") + 1;
//                    String description = joAufgabe.getString("Beschreibung");
//
//                    if(getColorFromName(lesson)!= null&&!getColorFromName(lesson).isEmpty()){
//                        String color = "#" + getColorFromName(lesson);
//                        int farbe = Color.parseColor(color);
//                        colorTV.setBackgroundColor(farbe);
//                    }
//
//                    if(joAufgabe.getString("Typ").equals("Klausur") || joAufgabe.getString("Typ").equals("Mündl. Prüfung") || joAufgabe.getString("Typ").equals("Test")){
//                        int cardColor = Color.parseColor("#B2DFDB") ;
//                        cardView.setCardBackgroundColor(cardColor);
//                        lesson= lesson + " - " + joAufgabe.getString("Typ");
//                    }
//
//                    tvFach.setText(lesson);
//                    tvDatum.setText(day + "." + month);
//                    tvDesc.setText(description);
//
//                    btndel.setTag(joAufgabe.getString("UUID") + "1");
//                    cardView.setTag(joAufgabe.getString("UUID"));
//
//
//
//
//                    rl.addView(colorTV);
//                    rl.addView(tvFach);
//                    rl.addView(tvDatum);
//                    rl.addView(tvDesc);
//                    rl.addView(btndel);
//
//
//                    cardView.addView(rl);
//
//
//                    ll.addView(cardView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private List<String> sortedList(){
        List<String> sortedByDate = new ArrayList<>();
        String rawData = MainActivity.theActivity.readFromFile("ha.json");
        if(rawData!=null){
            JSONObject root = null;
            try {
                root = new JSONObject(rawData);
                JSONArray jaExams = root.getJSONArray("Aufgaben");
                for(int i=0; i<jaExams.length(); i++){
                    JSONObject joExam = jaExams.getJSONObject(i);
                    int day = joExam.getInt("Tag");
                    int month = joExam.getInt("Monat")+1;

                    String dayString = ""+day;
                    if(dayString.length()==1){
                        dayString = "0"+dayString;
                    }
                    String monthString = ""+month;
                    if(monthString.length()==1){
                        monthString="0"+monthString;
                    }

                    String dateString = monthString+dayString;
//                    int date = Integer.parseInt(dateString);
                    sortedByDate.add(dateString);
                }
                Collections.sort(sortedByDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return sortedByDate;
    }

    private String getColorFromName(String fach)throws JSONException{
        String rawData = new String();
        String color = new String();
        if (MainActivity.theActivity.readFromFile("facher.json") != null) {
            rawData = MainActivity.theActivity.readFromFile("facher.json");
        } else {
            rawData = null;
        }
        if (rawData != null) {
            JSONObject root = new JSONObject(rawData);
            JSONArray jaFacher = root.getJSONArray("Faecher");
            for(int i = 0; i<jaFacher.length(); i++){
                JSONObject joFach = jaFacher.getJSONObject(i);
                if(fach.equals(joFach.getString("Fach"))) {
                    color = joFach.getString("Farbe");
                }
            }
        }
        return color;
    }

    private boolean delExam(String delUUID){
        String rawData = new String();
        if (MainActivity.theActivity.readFromFile("ha.json") != null) {
            rawData = MainActivity.theActivity.readFromFile("ha.json");
        } else {
            rawData = null;
        }

        if (rawData != null) {
            try {
                JSONObject root = new JSONObject(rawData);
                JSONArray jaExams = root.getJSONArray("Aufgaben");

                for(int i = 0; i<jaExams.length(); i++){
                    JSONObject joExam = jaExams.getJSONObject(i);
                    String uuid = joExam.getString("UUID");
                    if(delUUID.equals(uuid)){
                        //TODO: Only API-19
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            jaExams.remove(i);
                            String data = root.toString();
                            MainActivity.theActivity.writeToFile("ha.json", data);
                            return true;
                        }
                    }
                }

                String data = root.toString();
                MainActivity.theActivity.writeToFile("ha.json", data);

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    private void clearCardViews(){
        int childCount = ll.getChildCount();
        for(int i=0; i<childCount; i++){
            View currentChild = ll.getChildAt(i);
            if(currentChild instanceof CardView){
                ll.removeView(currentChild);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
