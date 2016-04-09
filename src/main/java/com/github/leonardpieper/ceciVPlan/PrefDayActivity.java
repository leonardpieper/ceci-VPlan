package com.github.leonardpieper.ceciVPlan;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.leonard.ceciVPlan.R;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

public class PrefDayActivity extends AppCompatActivity {

    public static PrefDayActivity theActivity;
    public PrefDayActivity(){theActivity=this;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref_day);
        setTitle("Fach hinzufügen");

        final EditText fach = (EditText)findViewById(R.id.prefFachname);
        final EditText abk = (EditText)findViewById(R.id.prefFachkurzel);
        final EditText krzl = (EditText)findViewById(R.id.prefKurskurzel);
        final EditText lehrer = (EditText)findViewById(R.id.prefLehrer);


        Button colorBtn = (Button)findViewById(R.id.prefColorButton);
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new ColorDialog();
                dialogFragment.show(getFragmentManager(), "ColorDialog");
            }
        });

        Button tage = (Button)findViewById(R.id.prefTageButton);
        tage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplication());
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("prefLesson", fach.getText().toString());
                editor.putString("prefShortcut", abk.getText().toString());
                editor.putString("prefId", krzl.getText().toString());
                editor.putString("prefTeacher", lehrer.getText().toString());
                editor.apply();

                DialogFragment dialogFragment = new SetLessonsDayDialog();
                dialogFragment.show(getFragmentManager(), "SetLessonsDayDialog");
            }
        });
    }

}
