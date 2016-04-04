package com.github.leonardpieper.ceciVPlan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinginActivity extends AsyncTask<String, Void, String> {
    private Context context;
    private String sessionID;
    private String tokenID;
    private String cookie;
//    private ProgressDialog progressDialog;
    private MainActivity mainActivity;

    private List _listeners = new ArrayList();

    public synchronized void addEventListener(SigninFinishListener listener) {
        _listeners.add(listener);
    }

    public synchronized void removeEventListener(SigninFinishListener listener) {
        _listeners.remove(listener);
    }


    public String html;


    public SinginActivity(Context context) {
        this.context = context;
        sessionID = "";
        tokenID = "";
        cookie = "";

//        progressDialog = new ProgressDialog(context);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Vertretungsplan wird geladen...");
//        progressDialog.setTitle("Bitte warten");
//        progressDialog.setIndeterminate(true);
        //this.textView2 = textView2;
    }


//    protected void onPreExecute() {
//        progressDialog.show();
//    }


    @Override
    protected String doInBackground(String... arg0) {

        try {
            /**
             * Öffnet URL
             */
            URL u = new URL("http://ceciliengymnasium.de/index.php/vertretungsplan");
            HttpURLConnection ucon = (HttpURLConnection) u.openConnection();
            /*System.setProperty("http.agent", "Chrome");
            ucon.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");*/
            Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
            Matcher m = p.matcher(ucon.getContentType());
            //Matcher m = p.matcher("application/x-www-form-urlencoded");

            int status = ucon.getResponseCode();
            System.err.println(status);

            String charset = m.matches() ? m.group(1) : "UTF-8";
            Reader r = new InputStreamReader(ucon.getInputStream(), charset);
            StringBuilder buf = new StringBuilder();
            while (true) {
                int ch = r.read();
                if (ch < 0) {
                    break;
                }
                buf.append((char) ch);
            }

            /**
             * Sucht und speichert nach der SessionID und der TokenID im Quellcode der Webseite
             * Vermutung:
             * SessionID ist Hardware/Geräteabhängig
             * TokenID wird nach neuladen erneuert
             */
            String searchPattern1 = "<input type=\"hidden\" name=\"return\" value=\"";
            String searchPattern2 = "<input type=\"hidden\" name=\"";
            String str1 = buf.toString();
            sessionID = str1.substring(str1.indexOf(searchPattern1) + searchPattern1.length());
            sessionID = sessionID.substring(0, sessionID.indexOf("\""));

            String str2 = buf.toString();
            tokenID = str2.substring(str2.indexOf(searchPattern1) + searchPattern1.length());
            tokenID = tokenID.substring(tokenID.indexOf(searchPattern2) + searchPattern2.length());
            tokenID = tokenID.substring(0, tokenID.indexOf("\""));

            /**
             * Sucht und speichert den Cookie
             */
            String headerName = null;
            for (int i = 1; (headerName = ucon.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals("Set-Cookie")) {
                    cookie = ucon.getHeaderField(i);
                    cookie = cookie.substring(0, cookie.indexOf(";"));
                    System.out.println(cookie);
                }
            }


            System.out.println(sessionID);
            System.out.println(tokenID);

        } catch (Exception e1) {
            return new String("Exception: " + e1.getMessage());
        }


        /**
         * -----------------------------------------------------------------------------------------------------------------
         */
        try {


            String username = (String) arg0[0];
            String password = (String) arg0[1];

            /**
             * Sammelt alle Daten für das HTML-Form
             */
            String host = "http://ceciliengymnasium.de/index.php/vertretungsplan";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            //data += "&" + URLEncoder.encode("checkbox", "UTF-8") + "=" + URLEncoder.encode("yes", "UTF-8");
            data += "&" + URLEncoder.encode("option", "UTF-8") + "=" + URLEncoder.encode("com_users", "UTF-8");
            data += "&" + URLEncoder.encode("task", "UTF-8") + "=" + URLEncoder.encode("user.login", "UTF-8");
            data += "&" + URLEncoder.encode("return", "UTF-8") + "=" + URLEncoder.encode(sessionID, "UTF-8");
            data += "&" + URLEncoder.encode(tokenID, "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

            URL url = new URL(host);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Cookie", cookie);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)");
            conn.connect();

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            /**
             * Liest die Antwort des Servers aus (Quellcode)
             */
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            this.html = sb.toString();
            loadFinishEvent();

            return html;


            //return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    private synchronized void loadFinishEvent() {
        SigninFinishEvent event = new SigninFinishEvent(this);
        Iterator i = _listeners.iterator();
        while (i.hasNext()) {
            ((SigninFinishListener) i.next()).handleSigninFinishEvent(event);
        }
    }


    protected void onPostExecute(String result) {
//        if(progressDialog != null && progressDialog.isShowing()){
//            progressDialog.dismiss();
//        }

        /** TODO maybe preference to default year */

        MainActivity.theActivity.swipeRefreshChanger(false);


        boolean personal = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("prefPersonal", true);
        MainActivity.theActivity.clearTableLayout();
        MainActivity.theActivity.loadVPlan(MainActivity.theActivity.getDefaultYear(), personal);
        MainActivity.theActivity.loadVPlan(MainActivity.theActivity.getDefaultYear() + 3, personal);

        super.onPostExecute(result);

    }

}
