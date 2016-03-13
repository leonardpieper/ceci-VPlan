package com.github.leonardpieper.ceciVPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.ArrayList;

public class SetLessonsDayDialog extends DialogFragment{

    final CharSequence[] daysArray = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag"};
    static final ArrayList mSelectedItems = new ArrayList();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("Tage")
                .setMultiChoiceItems(daysArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            //The user checked the item
                            mSelectedItems.add(which);
                        } else if(mSelectedItems.contains(which)){
                            //If Item is already checkes --> remove it
                            mSelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                })
                .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogFragment dialogFragment = new SetLessonsDialog();
                        dialogFragment.show(getFragmentManager(), "SetLessonsDialog");
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //User clicked 'Abbrechen'
                    }
                });

        return builder.create();
    }
}
