package com.example.leonard.app;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    public static SettingsActivity theActivity;

    public SettingsActivity() {
        theActivity = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //Display Fragment as main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
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
        if(key.equals("prefYear")){
            String defaultYear = SettingsFragment.theFragment.getValueFromList("prefYear");
            switch (defaultYear){
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
        }
    }


}
