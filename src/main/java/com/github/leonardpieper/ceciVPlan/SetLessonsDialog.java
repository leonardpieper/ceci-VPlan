package com.github.leonardpieper.ceciVPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leonard.ceciVPlan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SetLessonsDialog extends DialogFragment{


    public class WTag {
        public EditText et1;
        public EditText et2;
        public EditText et3;
        public EditText etRoom;
    };

    public HashMap<Integer, WTag> tagDict;

    private ArrayList daysHours;



    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        this.tagDict = new HashMap<Integer, WTag>();

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



        builder.setView(scrollView)
                .setTitle(R.string.sld_title)
                //.setMessage(getIndexofSLDD())
                .setPositiveButton("Fertig", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Clicked finish
                        giveValuesfromDialog();

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String lesson = sharedPref.getString("prefLesson", "");
                        String shortcut = sharedPref.getString("prefShortcut", "");
                        String id = sharedPref.getString("prefId", "");
                        String teacher = sharedPref.getString("prefTeacher", "");

                        String color = sharedPref.getString("prefColor", "");

                        //Delete sharedpref (Entries in the Settingsscreen)
                        sharedPref.edit().remove("prefLesson").apply();
                        sharedPref.edit().remove("prefShortcut").apply();
                        sharedPref.edit().remove("prefId").apply();
                        sharedPref.edit().remove("prefTeacher").apply();
                        sharedPref.edit().remove("prefColor").apply();

//                        ArrayList days = SetLessonsDayDialog.mSelectedItems;
//                        System.out.println(daysHours.toString());
                        AddNewLesson.theActivity.addTheLesson(lesson, shortcut, id, teacher, daysHours, color);
                        Toast toast = Toast.makeText(getActivity(), "Fach hinzugefügt", Toast.LENGTH_SHORT);
                        toast.show();
                        PrefDayActivity.theActivity.finish();

                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetLessonsDialog.this.getDialog().cancel();
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

        EditText et1;
        EditText et2;
        EditText et3;
        EditText etRoom;

        InputFilter[] fArray2 = new InputFilter[1];
        fArray2[0] = new InputFilter.LengthFilter(1);
        InputFilter[] fArray4 = new InputFilter[1];
        fArray4[0] = new InputFilter.LengthFilter(4);

        ArrayList selectedItems = new ArrayList();
        selectedItems = SetLessonsDayDialog.mSelectedItems;

        List dialogValues = new ArrayList();

        for(int i = 0;i<=selectedItems.size()-1; i++){
            int index = (int) selectedItems.get(i);
            WTag wt = new WTag();
            tagDict.put(index, wt);

            TextView tvDay = new TextView(getActivity());
            tvDay.setText(convertIndextoDay(index));
            tvDay.setPadding(40, 40, 40, 40);
            tvDay.setTextSize(20);
            tvDay.setTextColor(getResources().getColor(R.color.colorAccent));


            TextView tvHour = new TextView(getActivity());
            tvHour.setText("Stunden:");
            tvHour.setPadding(40, 0, 40, 40);
            tvHour.setTextSize(16);

            et1 = new EditText(getActivity());
            et1.setInputType(InputType.TYPE_CLASS_NUMBER);
            et1.setFilters(fArray2);
            et1.setMinWidth(50);
            et1.setId(1 + (index * 10));
            et1.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            et1.setNextFocusForwardId(2 + (index * 10));

            wt.et1 = et1;

            et2 = new EditText(getActivity());
            et2.setInputType(InputType.TYPE_CLASS_NUMBER);
            et2.setFilters(fArray2);
            et2.setMinWidth(50);
            et2.setId(2 + (index * 10));
            et2.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            et2.setNextFocusForwardId(3+ (index*10));
            wt.et2 = et2;

            et3 = new EditText(getActivity());
            et3.setInputType(InputType.TYPE_CLASS_NUMBER);
            et3.setFilters(fArray2);
            et3.setMinWidth(50);
            et3.setId(3 + (index * 10));
            et3.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            et3.setNextFocusForwardId( 4 + (index*10));
            wt.et3 = et3;

            TextView tvRoom = new TextView(getActivity());
            tvRoom.setText("Raum:");
            tvRoom.setPadding(40, 0, 90, 40);
            tvRoom.setTextSize(16);


            etRoom  = new EditText(getActivity());
//            etRoom.setPadding(0, 0, 0, 0);
//            etRoom.setEms(3);
            etRoom.setFilters(fArray4);
            etRoom.setSingleLine(true);
            etRoom.setMinWidth(150);
            etRoom.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            etRoom.setId(4 + (index * 10));
            etRoom.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            etRoom.setNextFocusForwardId(1 + ((index + 1) *10));
            wt.etRoom = etRoom;

            LinearLayout row = new LinearLayout(getActivity());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.addView(tvHour);
            row.addView(et1);
            row.addView(et2);
            row.addView(et3);

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

    private void giveValuesfromDialog(){
        daysHours = new ArrayList();
        for(int i=0; i<5;i++){
            EditText fet1 = new EditText(getActivity());

            if (tagDict.containsKey(i)){
                WTag wt = tagDict.get(i);
                daysHours.add(i);
                daysHours.add(wt.et1.getText());
                daysHours.add(wt.et2.getText());
                daysHours.add(wt.et3.getText());
                daysHours.add(wt.etRoom.getText());
            }
            else{
                System.out.println("Error");
            }
        }
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
