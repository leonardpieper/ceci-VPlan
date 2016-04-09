package com.github.leonardpieper.ceciVPlan;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddNewLesson{
    public static AddNewLesson theActivity;

    public AddNewLesson() {
        theActivity = this;
    }

    public static void addTheLesson(String fach, String abk, String fachkrzl, String lehrer, ArrayList detaills, String color) {
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
            MainActivity.theActivity.appendToFile("kurskrzl", fachkrzl);
            jsoFach.put("Lehrer", lehrer);
            jsoFach.put("Farbe", color);

            jsaTage = new JSONArray();

            // Start Tag;

            for (int i = 0; i < detaills.size(); i++) {

                int rechner = i % 5;
                if (rechner == 0) {
                    jsoTag = new JSONObject();
                    jsaStunden = new JSONArray();
                }
                //case 0 ist der Wochentag
                //case 1-3 die Stunden
                //case 4 der Raum
                switch (rechner) {
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

            jsoFach.put("Tage", jsaTage);


            jsaFaecher.put(jsoFach);
            // Ende Fach

            // Das kommt nur einmal ganz am Schluss wenn alle Fächer aufgebaut sind:
            // Prüft, ob die Datei bereits vorhanden ist
            if (MainActivity.theActivity.readFromFile("facher.json") == null) {
                jsoMain.put("Faecher", jsaFaecher);
                String jsonData = new String();
                jsonData = jsoMain.toString();
                MainActivity.theActivity.writeToFile("facher.json", jsonData);
            } else {
                jsoMain.put("Faecher", appendToJson(jsaFaecher));
                String jsonData = new String();
                jsonData = jsoMain.toString();
                MainActivity.theActivity.writeToFile("facher.json", jsonData);
            }


//            System.out.println(jsoMain);

            //write String to file


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /* TODO: Doesn't work */
    /* TODO: Works! */
    private static JSONArray appendToJson(JSONArray jsaFaecher) {

        try {

            JSONObject rawData = new JSONObject(MainActivity.theActivity.readFromFile("facher.json"));
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = rawData.optJSONArray("Faecher");
            JSONArray newJsonArray = new JSONArray();
            newJsonArray = concatArray(jsonArray, jsaFaecher);
            return newJsonArray;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }

}