package com.github.leonardpieper.ceciVPlan;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.leonard.ceciVPlan.R;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static SettingsActivity theActivity;

    public SettingsActivity() {
        theActivity = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setTitle("Einstellungen");

//        setContentView(R.layout.pref_actionbar);

//        Toolbar toolbar = (Toolbar)findViewById(R.id.preftoolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }



    public void prefLogin(){
        String uName = PreferenceManager.getDefaultSharedPreferences(this).getString("prefUname", null);
        String pwd = PreferenceManager.getDefaultSharedPreferences(this).getString("prefPwd", null);
        MainActivity.theActivity.loginPost(uName, pwd);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Wenn der Standard-Jahrgang geändert wir
        if(key.equals("prefYear")) {
            String defaultYear = SettingsFragment.theFragment.getValueFromList("prefYear");
            switch (defaultYear) {
                case "EF":
                    MainActivity.theActivity.writeToFile("defaultyear", "0");
                    break;
                case "Q1":
                    MainActivity.theActivity.writeToFile("defaultyear", "1");
                    break;
                case "Q2":
                    MainActivity.theActivity.writeToFile("defaultyear", "2");
                    break;
            }

        }else if(key.equals("prefTurnOfMsg")){
            System.out.println(key);
            System.out.println(sharedPreferences.getBoolean(key, true));
            MainActivity.theActivity.setAlarmSchedule();
        }
    }



//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//
//        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
//        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
//        root.addView(bar, 0); // insert at top
//        bar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }


}
