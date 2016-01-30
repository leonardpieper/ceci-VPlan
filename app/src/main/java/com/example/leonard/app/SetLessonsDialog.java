package com.example.leonard.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class SetLessonsDialog extends DialogFragment{
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //For custom Layout
        //For scrollability
        ScrollView scrollView = new ScrollView(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        setLayoutView(layout);

        //Join 'layout' into scrollview
        scrollView.addView(layout);


        //builder.setView(layout);


        builder.setView(scrollView)
                .setTitle(R.string.sld_title)
                //.setMessage(getIndexofSLDD())
                .setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Clicked finish
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Clicked Abbrechen
                    }
                });

        return builder.create();
    }

    /**
     * TEST to see if dialog in dialog works --> seems working
     * But it reminds the order of check --> means if I click 'Donnerstag' before 'Monatg' it shows '[3,0]'
     * @return returns checked items from SLDD
     */
    private String getIndexofSLDD(){
        String s1 = new String();
        s1 = SetLessonsDayDialog.mSelectedItems.toString();
        SetLessonsDayDialog.mSelectedItems.clear();
        return s1;
    }

    private void setLayoutView(LinearLayout layout1){
        ArrayList selectedItems = new ArrayList();
        selectedItems = SetLessonsDayDialog.mSelectedItems;

        for(int i = 0;i<=selectedItems.size()-1; i++){
            int index = (int) selectedItems.get(i);

            TextView tvDay = new TextView(getActivity());
            tvDay.setText(convertIndextoDay(index));
            tvDay.setPadding(40, 40, 40, 40);
            tvDay.setTextSize(20);
            tvDay.setTextColor(getResources().getColor(R.color.colorAccent));


            TextView tvHour = new TextView(getActivity());
            tvHour.setText("Stunden:");
            tvHour.setPadding(40, 0, 40, 40);
            tvHour.setTextSize(16);

            EditText et1 = new EditText(getActivity());
            et1.setInputType(InputType.TYPE_CLASS_NUMBER);

            EditText et2 = new EditText(getActivity());
            et2.setInputType(InputType.TYPE_CLASS_NUMBER);

            EditText et3 = new EditText(getActivity());
            et3.setInputType(InputType.TYPE_CLASS_NUMBER);

            EditText et4 = new EditText(getActivity());
            et4.setInputType(InputType.TYPE_CLASS_NUMBER);


            TextView tvRoom = new TextView(getActivity());
            tvRoom.setText("Raum:");
            tvRoom.setPadding(40, 0, 90, 40);
            tvRoom.setTextSize(16);

            EditText etRoom = new EditText(getActivity());
            etRoom.setPadding(40,0,40,40);
            etRoom.setEms(4);
            etRoom.setSingleLine(true);

            LinearLayout row = new LinearLayout(getActivity());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.addView(tvHour);
            row.addView(et1);
            row.addView(et2);
            row.addView(et3);
            row.addView(et4);

            LinearLayout row1 = new LinearLayout(getActivity());
            row1.setOrientation(LinearLayout.HORIZONTAL);
            row1.addView(tvRoom);
            row1.addView(etRoom);


            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout1.addView(tvDay, tvParams);
            layout1.addView(row);
            layout1.addView(row1);
        }
        SetLessonsDayDialog.mSelectedItems.clear();

    }

    /**
     * converts index to a day string
     * @param index index which comes from SetLessonsDayDialog
     * @return
     */
    private String convertIndextoDay(int index){
        String day = new String();
        switch (index){
            case 0:
                day = "Montag";
                break;
            case 1:
                day = "Dienstag";
                break;
            case 2:
                day = "Mittwoch";
                break;
            case 3:
                day = "Donnerstag";
                break;
            case 4:
                day = "Freitag";
                break;
        }

        return day;
    }

}
