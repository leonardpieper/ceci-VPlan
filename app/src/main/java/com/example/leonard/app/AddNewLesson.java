package com.example.leonard.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddNewLesson {
    public static AddNewLesson theActivity;
    public AddNewLesson(){
        theActivity = this;
    }

    public static void addTheLesson(String fach, String abk, String fachkrzl, String lehrer, ArrayList detaills){
        try {
            JSONObject jsoMain = new JSONObject();
            JSONObject jsoFach;
            JSONArray jsaFaecher = new JSONArray();
            JSONObject jsoTag = null;
            JSONArray jsaTage;
            JSONArray jsaStunden = null;

            // Start Fach
            jsoFach = new JSONObject();

            jsoFach.put("Fach", fach);
            jsoFach.put("Abk", abk);
            jsoFach.put("Fachkrzl", fachkrzl);
            jsoFach.put("Lehrer", lehrer);

            jsaTage = new JSONArray();

            // Start Tag;

            for(int i=0;i<detaills.size();i++){

                int rechner = i % 5;
                if (rechner == 0){
                    jsoTag = new JSONObject();
                    jsaStunden = new JSONArray();
                }
                //case 0 ist der Wochentag
                //case 1-3 die Stunden
                //case 4 der Raum
                switch (rechner){
                    case 0:
                        jsoTag.put("Wochentag", detaills.get(i));
                        break;
                    case 1:
                    case 2:
                        jsaStunden.put(detaills.get(i));
                        break;
                    case 3:
                        jsaStunden.put(detaills.get(i));
                        jsoTag.put("Stunden", jsaStunden);
                        break;
                    case 4:
                        jsoTag.put("Raum", detaills.get(i));
                        break;
                }

                if (rechner == 4) {
                    jsaTage.put(jsoTag);
                }
            }
            // Ende Tag

            jsoFach.put("Tage",jsaTage);


            jsaFaecher.put(jsoFach);
            // Ende Fach

            // Das kommt nur einmal ganz am Schluss wenn alle Fächer aufgebaut sind:
            // Prüft, ob die Datei bereits vorhanden ist
            if(MainActivity.theActivity.readFromFile("personlicheFacher.json") == null){
                jsoMain.put("Fächer", jsaFaecher);
                String jsonData = new String();
                jsonData = jsoMain.toString();
                MainActivity.theActivity.writeToFile("personlicheFacher.json", jsonData);
            } else{
                appendToJson(jsaFaecher);
            }

//            System.out.println(jsoMain);

            //write String to file


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* TODO: */
    private static void appendToJson(JSONArray jsaFaecher){
        String rawData = MainActivity.theActivity.readFromFile("personlicheFacher.json");
        if(rawData != null){
            try {
                JSONObject facher = new JSONObject(rawData);
                facher.put("Fächer",jsaFaecher);
                String jsonData = new String();
                jsonData = facher.toString();
                MainActivity.theActivity.writeToFile("personlicheFacher.json", jsonData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}