package com.github.leonardpieper.ceciVPlan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.leonard.ceciVPlan.R;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

public class ColorDialog extends DialogFragment{
    private LobsterPicker lobsterPicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_color, null);

        builder.setView(view)
                .setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int  i = lobsterPicker.getColor();
                        String hex = Integer.toHexString(i);

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("prefOldColor", i).apply();
                        editor.putString("prefColor", hex).apply();
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ColorDialog.this.getDialog().cancel();
                    }
                });

        lobsterPicker = (LobsterPicker)view.findViewById(R.id.lobsterpicker);
        LobsterShadeSlider shadeSlider = (LobsterShadeSlider)view.findViewById(R.id.shadeslider);
        lobsterPicker.addDecorator(shadeSlider);
        lobsterPicker.setColorHistoryEnabled(true);

        SharedPreferences sharedPref1= PreferenceManager.getDefaultSharedPreferences(getActivity());
        int oldColor= sharedPref1.getInt("prefOldColor", 0);
        lobsterPicker.setHistory(oldColor);
        lobsterPicker.setColor(oldColor);

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.dialog_color, container, false);
//        lobsterPicker = (LobsterPicker)view.findViewById(R.id.lobsterpicker);
//        lobsterPicker.addOnColorListener(new OnColorListener() {
//            @Override
//            public void onColorChanged(@ColorInt int color) {
//                System.out.println("1" + color);
//            }
//
//            @Override
//            public void onColorSelected(@ColorInt int color) {
//                System.out.println("2" + color);
//            }
//        });
//        LobsterShadeSlider shadeSlider = (LobsterShadeSlider)view.findViewById(R.id.shadeslider);

//        lobsterPicker.addDecorator(shadeSlider);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
