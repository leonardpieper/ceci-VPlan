package com.github.leonardpieper.ceciVPlan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.leonard.ceciVPlan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangeTTActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_tt);
        setTitle("Fach auswählen");

        linearLayout = (LinearLayout)findViewById(R.id.lineartt);
        getFach();
    }

    private void getFach(){
        String rawData = new String();
        if(MainActivity.theActivity.readFromFile("facher.json")!=null){
            rawData = MainActivity.theActivity.readFromFile("facher.json");
        }else {rawData= null;}

        if(rawData != null){
            try {
                JSONObject root = new JSONObject(rawData);
                JSONArray jaFacher = root.getJSONArray("Faecher");

                for(int i = 0; i<jaFacher.length(); i++){
                    final JSONObject joFach = jaFacher.getJSONObject(i);
                    Button btn = new Button(this);
                    btn.setBackgroundResource(R.drawable.stroke);
                    final String fach = joFach.getString("Fach");
                    btn.setText(fach);
//                    btn.setBackgroundResource(android.R.style.Widget_DeviceDefault_Button_Borderless);
//                    btn.setTag(joFach.getString("Fach"));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent updateIntent = new Intent(getApplicationContext(), UpdateTTActivity.class);
                            updateIntent.putExtra("Fach", fach);
                            startActivity(updateIntent);
                        }
                    });
                    linearLayout.addView(btn);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
