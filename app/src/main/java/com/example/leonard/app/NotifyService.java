package com.example.leonard.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class NotifyService extends Service {
    private NotificationManager mNM;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        getMsgForToday();
        refreshData();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void getMsgForToday(){
//        System.out.println("Starte getMsgForToday");
        String rawKurskrzl = readFromFile("kurskrzl");
        String rawVertretung = readFromFile("vertretungsplan.json");

        ArrayList<String> data = new ArrayList<String>();
//        System.out.println("readFromFile ok");
        if(rawVertretung != null) {
            try {
//                System.out.println("Vor JSONObject root");
                JSONObject root = new JSONObject(rawVertretung);
                String jahrgang = new String();
                switch (MainActivity.theActivity.getDefaultYear()) {
                    case 0:
                        jahrgang = "EF";
                        break;
                    case 1:
                        jahrgang = "Q1";
                        break;
                    case 2:
                        jahrgang = "Q2";
                        break;
                    case 3:
                        jahrgang = "Klausur EF";
                        break;
                    case 4:
                        jahrgang = "Klausur Q1";
                        break;
                    case 5:
                        jahrgang = "Klausur Q2";
                        break;
                }
                JSONArray stufe = root.getJSONArray(jahrgang);
//                System.out.println(stufe);
                for (int i = 0; i < stufe.length(); i++){
//                    System.out.println("for Schleife i=" + i);
                    try {
                        JSONObject inhalt = stufe.getJSONObject(i);
                        String fach = inhalt.getString("Fach");
                        String raum = inhalt.getString("Raum");
                        String lehrer = inhalt.getString("Vertreter");
                        String datum = inhalt.getString("Datum");

                        datum = datum.substring(0, datum.length()-1);
//                       System.out.println(datum);

                        String[] splitDatum = datum.split(Pattern.quote("."));

                        int day = Integer.parseInt(splitDatum[0]);
                        int month = Integer.parseInt(splitDatum[1]);
                        System.out.println("Datum " + day + "/" + month);
//                        System.out.println(day);

                        Calendar calendar = Calendar.getInstance();
                        int calMonth = calendar.get(Calendar.MONTH) + 1; //Calendar.Month is Zero-Based i.g. January is 0..
                        int calDay = calendar.get(Calendar.DAY_OF_MONTH);

//                        System.out.println(calMonth);
//                        System.out.println(calDay);

                        if (day == calDay && month == calMonth) {
//                            System.out.println(fach);
                            if(rawKurskrzl != null) {
                                String[] aryKurskrzl = rawKurskrzl.split("#");
                                for (int k = 0; k < aryKurskrzl.length; k++) {
                                    String kurskrzl = aryKurskrzl[k];
                                    if (kurskrzl.toLowerCase().equals(fach.toLowerCase())) {
                                        if(lehrer.equals("+")) {
                                            data.add(fach + " entfällt heute");
                                        }else {
                                            data.add(fach + " heute in " + raum + " mit " + lehrer);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    catch(Exception e) {
//                        System.err.println(e.getMessage());
                        continue;
                    }
                }
                if(data.size()>0) {
                    newMsg(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void newMsg(ArrayList<String> data){
        int requestID = (int) System.currentTimeMillis();
//        NotificationManager mNotificationManager  = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(getApplicationContext(), MainActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setGroup("Vertretungen")
                .setContentTitle("Vertretungsplan")
                .setContentText("Vertretungen heute")
                .setContentIntent(contentIntent);
        inboxStyle.setBigContentTitle("Vertretungsplan");
        for (String s : data){
            inboxStyle.addLine(s);
        }
        notificationBuilder.setStyle(inboxStyle);

        mNM.notify(requestID, notificationBuilder.build());
    }

    private String readFromFile(String filename){
        StringBuffer readData = new StringBuffer("");
        try {
            FileInputStream inputStream = openFileInput(filename);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String readString = bufferedReader.readLine();
            while (readString != null) {
                readData.append(readString);
                readString = bufferedReader.readLine();
            }
            streamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return readData.toString();
    }

    /**
     * Lädt neue Daten, wenn das Gerät W-Lan hat
     */
    private void refreshData(){
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        boolean isWifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        if(isWifi){
            MainActivity.theActivity.refreshData();
        }
    }
}
