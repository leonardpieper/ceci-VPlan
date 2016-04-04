package com.github.leonardpieper.ceciVPlan;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonard.ceciVPlan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateTTActivity extends AppCompatActivity {

    private EditText etFach;
    private EditText etAbk;
    private EditText etKrzl;
    private EditText etLehrer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tt);
        setTitle("Fach bearbeiten");

        Intent intent = getIntent();
        final String fach = intent.getStringExtra("Fach");

        etFach = (EditText)findViewById(R.id.updateFachname);
        etAbk = (EditText)findViewById(R.id.updateFachkurzel);
        etKrzl = (EditText)findViewById(R.id.updateKurskurzel);
        etLehrer = (EditText)findViewById(R.id.updateLehrer);


        Button colorBtn = (Button)findViewById(R.id.updateColorButton);
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new ColorDialog();
                dialogFragment.show(getFragmentManager(), "ColorDialog");
            }
        });

        Button submitbtn = (Button)findViewById(R.id.updateTageButton);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("prefLesson", etFach.getText().toString());
                editor.putString("prefShortcut", etAbk.getText().toString());
                editor.putString("prefId", etKrzl.getText().toString());
                editor.putString("prefTeacher", etLehrer.getText().toString());
                editor.apply();

                removeJSONObject(fach);

                DialogFragment dialogFragment = new SetLessonsDayDialog();
                dialogFragment.show(getFragmentManager(), "SetLessonsDayDialog");


            }
        });

        Button delBtn = (Button)findViewById(R.id.removeButton);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeJSONObject(fach);
                Toast toast = Toast.makeText(getApplicationContext(), fach + " gelöscht", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        showMask(fach);
    }

    private void showMask(String fach){
        String rawData = new String();
        if(MainActivity.theActivity.readFromFile("facher.json")!=null){
            rawData = MainActivity.theActivity.readFromFile("facher.json");
        }else {rawData= null;}

        if(rawData != null){
            try {
                JSONObject root = new JSONObject(rawData);
                JSONArray jaFacher = root.getJSONArray("Faecher");
                for(int i=0; i<jaFacher.length(); i++){
                    JSONObject joFach = jaFacher.getJSONObject(i);
                    String valueFach = joFach.getString("Fach");
                    if(valueFach.equals(fach)){
                        String valueAbk = joFach.getString("Abk");
                        String valueFachkrzl = joFach.getString("Fachkrzl");
                        String valueLehrer = joFach.getString("Lehrer");
//                        String valueColor = joFach.getString("Farbe");
                        etFach.setText(valueFach);
                        etAbk.setText(valueAbk);
                        etKrzl.setText(valueFachkrzl);
                        etLehrer.setText(valueLehrer);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void removeJSONObject(String fach) {
        String rawData = new String();
        if (MainActivity.theActivity.readFromFile("facher.json") != null) {
            rawData = MainActivity.theActivity.readFromFile("facher.json");
        } else {
            rawData = null;
        }

        if (rawData != null) {
            try {
                JSONObject root = new JSONObject(rawData);
                JSONArray jaFacher = root.getJSONArray("Faecher");
                for (int i = 0; i < jaFacher.length(); i++) {
                    JSONObject joFach = jaFacher.getJSONObject(i);
                    String valueFach = joFach.getString("Fach");
                    if (valueFach.equals(fach)) {
                        //TODO: Only API-19
                        jaFacher.remove(i);
                    }
                }
                String data = root.toString();
                MainActivity.theActivity.writeToFile("facher.json", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
