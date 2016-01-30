package com.example.leonard.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class YearChoiceDialog extends DialogFragment {
    final CharSequence[] yearArray = {"EF", "Q1", "Q2"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Wähle deinen Jahrgang")
                .setItems(yearArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                MainActivity.theActivity.writeToFile("defaultyear", "0");
                                break;
                            case 1:
                                MainActivity.theActivity.writeToFile("defaultyear", "1");
                                break;
                            case 2:
                                MainActivity.theActivity.writeToFile("defaultyear", "2");
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
