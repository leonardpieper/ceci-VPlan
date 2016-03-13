package com.github.leonardpieper.ceciVPlan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.leonard.ceciVPlan.R;
import com.google.android.gms.analytics.Tracker;

public class FirstStartActivity extends AppCompatActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);
        setTitle("Vertretungsplan");

//        AnalyticsApplication.initialize(getApplication());
//        mTracker = AnalyticsApplication.getInstance().get(AnalyticsApplication.Target.APP);

        Button btn = (Button) findViewById(R.id.firstStartBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(mainIntent);
                finish();

            }
        });
    }
}
