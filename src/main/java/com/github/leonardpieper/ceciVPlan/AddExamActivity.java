package com.github.leonardpieper.ceciVPlan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.leonard.ceciVPlan.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddExamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);
        addToLayout();
    }

    private void addToLayout() {
        addLessonSpinner();

        String[] typeContent = getResources().getStringArray(R.array.examTypes);
        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeContent);
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.addExamLayout);

        RelativeLayout.LayoutParams typeParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        typeParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        typeParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        typeParam.addRule(RelativeLayout.BELOW, R.id.fachSpinner);
        typeParam.setMargins(0, 20, 0, 0);

        RelativeLayout.LayoutParams etParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        etParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        etParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        etParam.addRule(RelativeLayout.BELOW, R.id.typeSpinner);
        etParam.setMargins(0, 20, 0, 0);

        RelativeLayout.LayoutParams switchParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switchParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        switchParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        switchParam.addRule(RelativeLayout.BELOW, R.id.descriptionET);
        switchParam.setMargins(0, 20, 0, 0);

        RelativeLayout.LayoutParams btn1Param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn1Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btn1Param.addRule(RelativeLayout.ALIGN_PARENT_START);
        btn1Param.addRule(RelativeLayout.BELOW, R.id.notifySwitch);
        btn1Param.setMargins(0, 20, 0, 0);

        RelativeLayout.LayoutParams btn2Param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn2Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btn2Param.addRule(RelativeLayout.ALIGN_PARENT_START);
        btn2Param.addRule(RelativeLayout.BELOW, R.id.dateBtn);
        btn2Param.setMargins(0, 20, 0, 0);

        RelativeLayout.LayoutParams btn3Param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btn3Param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btn3Param.addRule(RelativeLayout.ALIGN_PARENT_START);
        btn3Param.addRule(RelativeLayout.BELOW, R.id.timeBtn);
        btn3Param.setMargins(0, 20, 0, 0);


        Spinner typeSpinner = new Spinner(this);
        typeSpinner.setLayoutParams(typeParam);
        typeSpinner.setAdapter(typeArrayAdapter);
        typeSpinner.setId(R.id.typeSpinner);


        EditText descriptionET = new EditText(this);
        descriptionET.setLayoutParams(etParam);
        descriptionET.setId(R.id.descriptionET);
        descriptionET.setHint("Beschreibung");


        Switch notifySwitch = new Switch(this);
        notifySwitch.setLayoutParams(switchParam);
        notifySwitch.setText("Erinnerung  ");
        notifySwitch.setTextColor(getResources().getColor(R.color.colorBlack));
        notifySwitch.setTextSize(16);
        notifySwitch.setId(R.id.notifySwitch);
        notifySwitch.setChecked(false);


        final Calendar changedCal = Calendar.getInstance();


        Button dateBtn = new Button(this);
        dateBtn.setLayoutParams(btn1Param);
        dateBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        dateBtn.setBackgroundResource(R.drawable.stroke);
        dateBtn.setText("Tag");
        dateBtn.setId(R.id.dateBtn);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddExamActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        changedCal.set(Calendar.YEAR, year);
                        changedCal.set(Calendar.MONTH, monthOfYear);
                        changedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                    }
                }, changedCal.get(Calendar.YEAR), changedCal.get(Calendar.MONTH), changedCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button timeBtn = new Button(this);
        timeBtn.setLayoutParams(btn2Param);
        timeBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        timeBtn.setBackgroundResource(R.drawable.stroke);
        timeBtn.setText("Uhrzeit");
        timeBtn.setId(R.id.timeBtn);
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddExamActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        changedCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        changedCal.set(Calendar.MINUTE, minute);
                    }
                }, changedCal.get(Calendar.HOUR_OF_DAY), changedCal.get(Calendar.MINUTE), true).show();
            }
        });


        Button submitBtn = new Button(this);
        submitBtn.setLayoutParams(btn3Param);
        submitBtn.setBackgroundResource(R.drawable.stroke);
        submitBtn.setId(R.id.submitBtn);
        submitBtn.setText("Speichern");
        submitBtn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        submitBtn.setTextColor(getResources().getColor(R.color.colorAccent));



        rLayout.addView(typeSpinner);
        rLayout.addView(descriptionET);
        rLayout.addView(notifySwitch);
        rLayout.addView(dateBtn);
        rLayout.addView(timeBtn);
        rLayout.addView(submitBtn);

    }

    private void addLessonSpinner() {
        List<String> spinnerContent = new ArrayList<>();
        RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.addExamLayout);

        RelativeLayout.LayoutParams firstParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        firstParam.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        firstParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        firstParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        Spinner lessonSpinner = new Spinner(this);

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
                    final JSONObject joFach = jaFacher.getJSONObject(i);

                    final String fach = joFach.getString("Fach");
                    spinnerContent.add(fach);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        lessonSpinner.setLayoutParams(firstParam);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerContent);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lessonSpinner.setAdapter(spinnerArrayAdapter);
        lessonSpinner.setId(R.id.fachSpinner);

        rLayout.addView(lessonSpinner);

    }
}
