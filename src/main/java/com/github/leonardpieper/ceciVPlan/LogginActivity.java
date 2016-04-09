package com.github.leonardpieper.ceciVPlan;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonard.ceciVPlan.R;

public class LogginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        loginToCeci();
    }

    private void loginToCeci(){
        final TextView uNameTV = (TextView)findViewById(R.id.loggUser);
        final TextView pwdTV = (TextView)findViewById(R.id.loggPwd);
        final Spinner yearSpinner = (Spinner)findViewById(R.id.loggYear);
        Button subBtn = (Button)findViewById(R.id.loggSub);

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LogginActivity.this);
                SharedPreferences.Editor editor = sharedPref.edit();

                String user = uNameTV.getText().toString();
                String password = pwdTV.getText().toString();

                editor.putString("prefUname", user).apply();
                editor.putString("prefPwd", password).apply();

                MainActivity.theActivity.loginPost(user, password);


                switch (yearSpinner.getSelectedItem().toString()){
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

                Toast toast = Toast.makeText(getApplicationContext(), "Daten gespeichert", Toast.LENGTH_SHORT);
                toast.show();
                finish();


            }
        });
    }
}
