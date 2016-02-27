package com.example.leonard.app;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PendingIntent mEverydayPendingIntent;

    private SinginActivity signin;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int defaultYear;
    private String oldDatum = new String("");

    private TableLayout tableLayout;

    private com.getbase.floatingactionbutton.FloatingActionButton fabEF;
    private com.getbase.floatingactionbutton.FloatingActionButton fabQ1;
    private com.getbase.floatingactionbutton.FloatingActionButton fabQ2;
    private com.getbase.floatingactionbutton.FloatingActionButton fabPersonal;
    private com.getbase.floatingactionbutton.FloatingActionsMenu fabMenu;

    public static MainActivity theActivity;

    public MainActivity() {
        theActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getResources().getString(R.string.title_activity_main));

        //Wenn man nach unten wischt wird der Inhalt aktualiesiert
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
//                notification();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fabMenu = (com.getbase.floatingactionbutton.FloatingActionsMenu)findViewById(R.id.fab_year);
        fabEF = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.fabEF);
        fabQ1 = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.fabQ1);
        fabQ2 = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.fabQ2);
        fabPersonal = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.fabPersonal);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tableLayout = (TableLayout) findViewById(R.id.table_layout);

        checkFirstRun();

        if (isDefaultYearAvailable() == true) {
            defaultYear = Integer.parseInt(readFromFile("defaultyear"));
        } else {
            defaultYear = 0;
        }

        setAlarmSchedule();

        boolean personal = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prefPersonal", true);

        clearTableLayout();
        loadVPlan(defaultYear, personal);
        loadVPlan(defaultYear + 3, personal);
        changeFabColor(3);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Wenn das Navigationsmenu aufgerufen wird
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_vplan:
                break;
            case R.id.nav_schedule:
                break;
            case R.id.nav_test:
                Intent examsIntent = new Intent(this, ExamsActivity.class);
                startActivity(examsIntent);
                break;
            case R.id.nav_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.nav_help:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Wird aufgerufen, wenn der login-button gedruckt wird
     */
    public void loginPost(String username, String password) {
        clearTableLayout();
        writeToFile("uname", username);
        writeToFile("pwd", password);
        saveVPlan(username, password);
    }

    /**
     * Wie swype-refreh nur der button in der action-bar oben rechts
     */
    public void refreshDataButton(MenuItem item) {
        refreshData();
    }

    /**
     * Ladt Username + Passwort aus der Datei und aktualisiert den VPlan entsprechend,
     * und speichert den entsprechend.
     */
    public void refreshData() {
        String loadedUsername = readFromFile("uname");
        String loadedPassword = readFromFile("pwd");
        saveVPlan(loadedUsername, loadedPassword);
    }

    /**
     * Speichert den VPlan, nachdem die Methode diesen von der Seite
     * 'ceciliengymnasium.de/vertretungsplan
     */
    private void saveVPlan(final String username, final String password) {
        signin = new SinginActivity(this);
        signin.addEventListener(new SigninFinishListener() {
            @Override
            public void handleSigninFinishEvent(EventObject e) {
                //Wandelt den Quellcode in die Werte der Tabelle um
                String html = signin.html;

                Document doc = Jsoup.parse(html);

                JSONObject jobj = new JSONObject();


                int stufeCount = 0;
                for (Element table : doc.select("table")) {
                    String stufe = new String();
                    switch (stufeCount) {
                        case 0:
                            stufe = "EF";
                            break;
                        case 1:
                            stufe = "Q1";
                            break;
                        case 2:
                            stufe = "Q2";
                            break;
                        case 3:
                            stufe = "Klausur EF";
                            break;
                        case 4:
                            stufe = "Klausur Q1";
                            break;
                        case 5:
                            stufe = "Klausur Q2";
                            break;
                    }
                    try {
                        JSONArray jarray = new JSONArray();
                        for (Element row : table.select("tr")) {
                            Elements tds = row.select("td");

                            JSONObject jsonAdd = new JSONObject();
                            for (int i = 0; i < tds.size(); i++) {
                                switch (i) {
                                    case 0:
                                        jsonAdd.put("Tag", tds.get(i).text());
                                        break;
                                    case 1:
                                        jsonAdd.put("Datum", tds.get(i).text());
                                        break;
                                    case 2:
                                        jsonAdd.put("Klasse(n)", tds.get(i).text());
                                        break;
                                    case 3:
                                        jsonAdd.put("Stunde", tds.get(i).text());
                                        break;
                                    case 4:
                                        jsonAdd.put("Fach", tds.get(i).text());
                                        break;
                                    case 5:
                                        jsonAdd.put("Vertreter", tds.get(i).text());
                                        break;
                                    case 6:
                                        jsonAdd.put("Raum", tds.get(i).text());
                                        break;
                                    case 7:
                                        jsonAdd.put("Vertretungs-Text", tds.get(i).text());
                                        break;
                                }

                            }//Ende for-Schleife von Td
                            jarray.put(jsonAdd);

                        }//Ende for-Schleife von Tr
                        jobj.put(stufe, jarray);

                    } catch (JSONException je) {
                        System.out.println(je);
                    }
                    stufeCount++;
                }//Ende for-Schleife table

                writeToFile("vertretungsplan.json", jobj.toString());

            }
        });
        signin.execute(username, password);
    }

    protected void loadVPlan(int year, boolean personal) {
        String vPlan = readFromFile("vertretungsplan.json");
        String analyzedJSON = new String();

        if (vPlan != null) {
            try {
                JSONObject mainObject = new JSONObject(vPlan);
                String jahrgang = new String();
                switch (year) {
                    case 0:
                        jahrgang = "EF";
                        break;
                    case 1:
                        jahrgang = "Q1";
                        break;
                    case 2:
                        jahrgang = "Q2";
                        break;
                    case 3:
                        jahrgang = "Klausur EF";
                        break;
                    case 4:
                        jahrgang = "Klausur Q1";
                        break;
                    case 5:
                        jahrgang = "Klausur Q2";
                        break;
                }
                JSONArray stufe = mainObject.getJSONArray(jahrgang);
//                    analyzedJSON = analyzeJSON(stufe, i, 0);
                analyzeJSON(stufe, year, personal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeTextView(View view) {
        switch (view.getId()) {
            case (R.id.fabEF):
                clearTableLayout();
                loadVPlan(0, false);
                loadVPlan(3, false);
                setTitle(getResources().getString(R.string.title_activity_main) + " - EF");
                changeFabColor(0);
                break;
            case (R.id.fabQ1):
                clearTableLayout();
                loadVPlan(1, false);
                loadVPlan(4, false);
                setTitle(getResources().getString(R.string.title_activity_main) + " - Q1");
                changeFabColor(1);
                break;
            case (R.id.fabQ2):
                clearTableLayout();
                loadVPlan(2, false);
                loadVPlan(5, false);
                setTitle(getResources().getString(R.string.title_activity_main) + " - Q2");
                changeFabColor(2);
                break;
            case (R.id.fabPersonal):
                clearTableLayout();
                loadVPlan(defaultYear, true);
                loadVPlan(defaultYear + 3, true);
                setTitle("Dein " + getResources().getString(R.string.title_activity_main));
                changeFabColor(3);
                break;
        }
    }

    private void changeFabColor(int stufe){
        switch (stufe){
            case 0:
                fabPersonal.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabEF.setColorNormal(getResources().getColor(R.color.colorAccent));
                fabQ1.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabQ2.setColorNormal(getResources().getColor(R.color.colorPrimary));
                break;
            case 1:
                fabPersonal.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabEF.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabQ1.setColorNormal(getResources().getColor(R.color.colorAccent));
                fabQ2.setColorNormal(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                fabPersonal.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabEF.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabQ1.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabQ2.setColorNormal(getResources().getColor(R.color.colorAccent));
                break;
            case 3:
                fabPersonal.setColorNormal(getResources().getColor(R.color.colorAccent));
                fabEF.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabQ1.setColorNormal(getResources().getColor(R.color.colorPrimary));
                fabQ2.setColorNormal(getResources().getColor(R.color.colorPrimary));
                break;
        }
        fabMenu.collapse();
    }

    protected void clearTableLayout() {
        int count = tableLayout.getChildCount();
        for (int i = 0; i < count; i++)
            tableLayout.removeView(tableLayout.getChildAt(0));
    }

    public void vertretungTable(int year, String fach, String stunde, String lehrer, String raum, String vertretungsText, String tag, String datum) {
        TableRow row = new TableRow(this);


        TextView lesson = new TextView(this);
        lesson.setBackgroundResource(R.drawable.cell_shape);
        lesson.setPadding(5, 15, 15, 15);
        TextView time = new TextView(this);
        time.setBackgroundResource(R.drawable.cell_shape);
        time.setPadding(15, 15, 15, 15);
        TextView tutor = new TextView(this);
        tutor.setBackgroundResource(R.drawable.cell_shape);
        tutor.setPadding(15, 15, 15, 15);
        TextView room = new TextView(this);
        room.setBackgroundResource(R.drawable.cell_shape);
        room.setPadding(15, 15, 15, 15);
        TextView extra = new TextView(this);
        extra.setBackgroundResource(R.drawable.cell_shape);
        extra.setPadding(15, 15, 5, 15);
        TextView tests = new TextView(this);
        tests.setBackgroundResource(R.drawable.cell_shape);
        tests.setPadding(15, 15, 5, 15);

        TextView day = new TextView(this);
        //day.setBackgroundResource(R.drawable.cell_shape);
        day.setPadding(15, 15, 5, 15);
        day.setTextSize(20);

        TextView date = new TextView(this);
        //date.setBackgroundResource(R.drawable.cell_shape);
        date.setPadding(15, 15, 5, 15);
        date.setTextSize(20);


        android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                android.widget.TableRow.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams oneColParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                android.widget.TableRow.LayoutParams.WRAP_CONTENT);
        oneColParam.span = 1;
        TableRow.LayoutParams twoColParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                android.widget.TableRow.LayoutParams.WRAP_CONTENT);
        twoColParam.span = 2;
        TableRow.LayoutParams threeColParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                android.widget.TableRow.LayoutParams.WRAP_CONTENT);
        threeColParam.span = 3;

        lesson.setLayoutParams(trparams);
        time.setLayoutParams(trparams);
        tutor.setLayoutParams(trparams);
        room.setLayoutParams(trparams);
        extra.setLayoutParams(trparams);
        day.setLayoutParams(twoColParam);
        date.setLayoutParams(threeColParam);
        tests.setLayoutParams(oneColParam);

        lesson.setText(fach);
        time.setText(stunde);
        tutor.setText(lehrer);
        room.setText(raum);
        extra.setText(vertretungsText);
        day.setText(tag);
        date.setText(datum);
        tests.setText(fach);
//        TableLayout layoutINNER = new TableLayout(this);
//        layoutINNER.setLayoutParams(params);
//        TableRow tr = new TableRow(this);

//        tr.setLayoutParams(params);
        if (oldDatum.equals(datum)) {
            row.addView(lesson);
            row.addView(time);
            row.addView(tutor);
            row.addView(room);
            row.addView(extra);
        } else if (!tag.equals("Tag")) {
            row.addView(day);
            row.addView(date);
            tableLayout.addView(row);
            row = new TableRow(this);
            row.addView(lesson);
            row.addView(time);
            row.addView(tutor);
            row.addView(room);
            row.addView(extra);
        }

        tableLayout.addView(row);
        oldDatum = datum;


    }

    protected void writeToFile(String filename, String data) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String analyzeJSON(JSONArray jsonArray, int year, boolean personal) {
        String output = new String();
        try {

            String oldKlassen = new String();

            for (int j = 0; j < jsonArray.length(); j++) {


                JSONObject inhalt = jsonArray.getJSONObject(j);

                String currentKlassen = inhalt.getString("Klasse(n)");
                if (currentKlassen.startsWith("Klausur") && !currentKlassen.equals(oldKlassen)) {
                    macheEineZeile("Klausuren:");
                    oldKlassen = currentKlassen;
                }
                if(personal == false) {
                    vertretungTable(year,
                            inhalt.getString("Fach"),
                            inhalt.getString("Stunde"),
                            inhalt.getString("Vertreter"),
                            inhalt.getString("Raum"),
                            inhalt.getString("Vertretungs-Text"),
                            inhalt.getString("Tag"),
                            inhalt.getString("Datum"));
                }else {
                    loadPersonalVPlan(year,
                            inhalt.getString("Fach"),
                            inhalt.getString("Stunde"),
                            inhalt.getString("Vertreter"),
                            inhalt.getString("Raum"),
                            inhalt.getString("Vertretungs-Text"),
                            inhalt.getString("Tag"),
                            inhalt.getString("Datum"));
                }

                //String tag = stufe.getJSONObject("tag")
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return output;
    }

    private void loadPersonalVPlan(int year, String fach, String stunde, String lehrer, String raum, String vertretungsText, String tag, String datum){
        String rawKurskrzl = readFromFile("kurskrzl");
        if(rawKurskrzl != null) {
            String[] aryKurskrzl = rawKurskrzl.split("#");
            for (int i = 0; i < aryKurskrzl.length; i++) {
                String kurskrzl = aryKurskrzl[i];
                if (kurskrzl.toLowerCase().equals(fach.toLowerCase())) {

                    TableRow row = new TableRow(this);


                    TextView lesson = new TextView(this);
                    lesson.setBackgroundResource(R.drawable.cell_shape);
                    lesson.setPadding(5, 15, 15, 15);
                    TextView time = new TextView(this);
                    time.setBackgroundResource(R.drawable.cell_shape);
                    time.setPadding(15, 15, 15, 15);
                    TextView tutor = new TextView(this);
                    tutor.setBackgroundResource(R.drawable.cell_shape);
                    tutor.setPadding(15, 15, 15, 15);
                    TextView room = new TextView(this);
                    room.setBackgroundResource(R.drawable.cell_shape);
                    room.setPadding(15, 15, 15, 15);
                    TextView extra = new TextView(this);
                    extra.setBackgroundResource(R.drawable.cell_shape);
                    extra.setPadding(15, 15, 5, 15);
                    TextView tests = new TextView(this);
                    tests.setBackgroundResource(R.drawable.cell_shape);
                    tests.setPadding(15, 15, 5, 15);

                    TextView day = new TextView(this);
                    //day.setBackgroundResource(R.drawable.cell_shape);
                    day.setPadding(15, 15, 5, 15);
                    day.setTextSize(20);

                    TextView date = new TextView(this);
                    //date.setBackgroundResource(R.drawable.cell_shape);
                    date.setPadding(15, 15, 5, 15);
                    date.setTextSize(20);


                    android.widget.TableRow.LayoutParams trparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            android.widget.TableRow.LayoutParams.WRAP_CONTENT);

                    TableRow.LayoutParams oneColParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            android.widget.TableRow.LayoutParams.WRAP_CONTENT);
                    oneColParam.span = 1;
                    TableRow.LayoutParams twoColParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            android.widget.TableRow.LayoutParams.WRAP_CONTENT);
                    twoColParam.span = 2;
                    TableRow.LayoutParams threeColParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            android.widget.TableRow.LayoutParams.WRAP_CONTENT);
                    threeColParam.span = 3;

                    lesson.setLayoutParams(trparams);
                    time.setLayoutParams(trparams);
                    tutor.setLayoutParams(trparams);
                    room.setLayoutParams(trparams);
                    extra.setLayoutParams(trparams);
                    day.setLayoutParams(twoColParam);
                    date.setLayoutParams(threeColParam);
                    tests.setLayoutParams(oneColParam);

                    lesson.setText(fach);
                    time.setText(stunde);
                    tutor.setText(lehrer);
                    room.setText(raum);
                    extra.setText(vertretungsText);
                    day.setText(tag);
                    date.setText(datum);
                    tests.setText(fach);
//        TableLayout layoutINNER = new TableLayout(this);
//        layoutINNER.setLayoutParams(params);
//        TableRow tr = new TableRow(this);

//        tr.setLayoutParams(params);
                    if (oldDatum.equals(datum)) {
                        row.addView(lesson);
                        row.addView(time);
                        row.addView(tutor);
                        row.addView(room);
                        row.addView(extra);
                    } else if (!tag.equals("Tag")) {
                        row.addView(day);
                        row.addView(date);
                        tableLayout.addView(row);
                        row = new TableRow(this);
                        row.addView(lesson);
                        row.addView(time);
                        row.addView(tutor);
                        row.addView(room);
                        row.addView(extra);
                    }

                    tableLayout.addView(row);
                    oldDatum = datum;
                }

            }
        }

    }

    private void macheEineZeile(String text){
        TableRow row = new TableRow(this);


        TextView klausuren = new TextView(this);
        klausuren.setTextSize(20);
        klausuren.setPaintFlags(klausuren.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        klausuren.setTextColor(getResources().getColor(R.color.colorAccent));
        klausuren.setPadding(5, 15, 15, 15);



        TableRow.LayoutParams oneColParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                android.widget.TableRow.LayoutParams.WRAP_CONTENT);
        oneColParam.span = 5;

        klausuren.setLayoutParams(oneColParam);

        klausuren.setText(text);

        row.addView(klausuren);
        tableLayout.addView(row);
    }

    protected String readFromFile(String filename) {
        StringBuffer readData = new StringBuffer("");
        try {
            FileInputStream inputStream = openFileInput(filename);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String readString = bufferedReader.readLine();
            while (readString != null) {
                readData.append(readString);
                readString = bufferedReader.readLine();
            }
            streamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return readData.toString();
    }

    protected void appendToFile(String filename, String data){
        String oldFile = readFromFile(filename);
        if(oldFile==null){
            writeToFile(filename, data);
        }else {
            writeToFile(filename, oldFile + "#" + data);
        }
    }

    protected void deleteStorageFile(String filename){
        File dir = getFilesDir();
        File file = new File(dir, filename);
        boolean deleted = file.delete();
    }

    protected void setAlarmSchedule(){
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("prefTurnOfMsg", true)) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, NotifyReceiver.class);
            alarmIntent.putExtra("notification", true);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            int hourOfDay = sharedPref.getInt("prefNotifyHourOfDay", 7);
            int minute = sharedPref.getInt("prefNotifyMinute", 00);

            Calendar calendar = Calendar.getInstance();
            Calendar now = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 00);

            if (calendar.before(now)) {
                calendar.add(Calendar.DATE, 1);
            }
            mEverydayPendingIntent = pendingIntent;
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mEverydayPendingIntent);
        }

    }

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }
        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
            return;
        } else if (savedVersionCode == DOESNT_EXIST) {
            //This is a new install
            Intent firstIntent = new Intent(this, FirstStartActivity.class);
            startActivity(firstIntent);
        } else if (currentVersionCode > savedVersionCode) {
            // TODO This is an upgrade
        }
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();
    }

    private boolean isDefaultYearAvailable() {
        if (readFromFile("defaultyear") != null) {
            System.out.println(readFromFile("defaultyear"));
            return true;
        } else {
            return false;
        }
    }

    public static Context getAppContext(){
        return MainActivity.getAppContext();
    }
    public int getDefaultYear(){return defaultYear;}

}
