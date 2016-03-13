package com.github.leonardpieper.ceciVPlan;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.leonard.ceciVPlan.R;

public class SettingsActivityOld extends AppCompatActivity {
    private EditText usernameField,passwordField;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingsold);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        setTitle("Einstellungen");

        loginButton = (Button) findViewById(R.id.button);

        usernameField = (EditText) findViewById(R.id.usernameEditText);
        passwordField = (EditText) findViewById(R.id.passwordEditText);

        //Teil für Enter bei Tastartur
//        passwordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_GO) {
//                    loginButton.performClick();
//                    return true;
//                }
//                return false;
//            }
//        });

    }

    public void preLogin(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

//        mainActivity.loginPost(view, username, password);
        MainActivity.theActivity.loginPost(username, password);
        this.finish();

    }

    public void showYearChoiceDialog(View view){
        DialogFragment dialogFragment = new YearChoiceDialog();
        dialogFragment.show(getFragmentManager(), "YearChoiceDialog");
    }

    public void showSetLessonsDayDialog(View view){
        DialogFragment dialogFragment = new SetLessonsDayDialog();
        dialogFragment.show(getFragmentManager(), "SetLessonsDayDialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
