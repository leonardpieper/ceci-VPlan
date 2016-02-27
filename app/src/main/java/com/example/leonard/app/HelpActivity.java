package com.example.leonard.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("Hilfe");

        Intent intent = getIntent();

        TextView textViewUber = (TextView)findViewById(R.id.textViewUber);
        textViewUber.setText("Diese App wurde von Leonard Pieper entwickelt.\nVervielfältigung, Verbreitung, Veränderung nur unter den unten gegebenen Lizenzen!");

        TextView textViewBedienung = (TextView)findViewById(R.id.textViewBedienung);
        textViewBedienung.setText("Um dich anmelden zu können benötigst du die Logindaten des Online-Vertretungsplans.\n" +
                "Wenn du den Vertretungsplan aktualisieren möchtest streiche einmal im Hauptbildschirm von oben nach unten.\n" +
                "Die App ist nach dem aktualisieren auf dem stand des Online-Vertretungsplans.");

        TextView textView = (TextView)findViewById(R.id.textViewLizenz);
        textView.setText(new StringBuilder().append("Die Vertretungsplan App, des Ceciliengymnasiums (die App) wurde von \n")
                .append("Leonard Pieper\n")
                .append("entwickelt und steht unter der GNU GPL Lizenz.")
                .append("\n")
                .append("\n")
                .append(" This program is free software: you can redistribute it and/or modify\n")
                .append("it under the terms of the GNU General Public License as published by\n")
                .append("the Free Software Foundation, either version 3 of the License, or\n")
                .append("(at your option) any later version.\n")
                .append("\n")
                .append("This program is distributed in the hope that it will be useful,\n")
                .append("but WITHOUT ANY WARRANTY; without even the implied warranty of\n")
                .append("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n")
                .append("GNU General Public License for more details.\n")
                .append("\n")
                .append("You should have received a copy of the GNU General Public License\n")
                .append("along with this program.  If not, see <http://www.gnu.org/licenses/>.\n")
                .append("\n")
                .append("Dieses Programm ist Freie Software: Sie können es unter den Bedingungen\n")
                .append("der GNU General Public License, wie von der Free Software Foundation,\n")
                .append("Version 3 der Lizenz oder (nach Ihrer Wahl) jeder neueren\n")
                .append("veröffentlichten Version, weiterverbreiten und/oder modifizieren.\n")
                .append("\n")
                .append("Dieses Programm wird in der Hoffnung, dass es nützlich sein wird, aber\n")
                .append("OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite\n")
                .append("Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.\n")
                .append("Siehe die GNU General Public License für weitere Details.\n").append("\n")
                .append("Sie sollten eine Kopie der GNU General Public License zusammen mit diesem\n")
                .append("Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.")
                .append("\n")
                .append("\n")
                .append("---------\n\n")
                .append("Es wurden Bestandteile aus Open-Source/freier Software verwendet, \n")
                .append("der FloatingActionButton steht unter der Apache 2.0 Lizenz\n\n")
                .append("Copyright 2015 futuresimple\n").append("\n")
                .append("Licensed under the Apache License, Version 2.0 (the \"License\");\n")
                .append("you may not use this file except in compliance with the License.\n")
                .append("You may obtain a copy of the License at\n").append("\n")
                .append("http://www.apache.org/licenses/LICENSE-2.0\n")
                .append("\n")
                .append("Unless required by applicable law or agreed to in writing, software\n")
                .append("distributed under the License is distributed on an \"AS IS\" BASIS,\n")
                .append("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n")
                .append("See the License for the specific language governing permissions and\n")
                .append("limitations under the License.")
                .append("\n")
                .append("\n")
                .append("\n")
                .append("jsoup steht unter der MIT Lizenz" +
                        "Copyright © 2009 - 2013 Jonathan Hedley (jonathan@hedley.net)\n")
                .append("\n")
                .append("Permission is hereby granted, free of charge, ")
                .append("to any person obtaining a copy of this software and associated documentation files ")
                .append("(the \"Software\"), to deal in the Software without restriction, ")
                .append("including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,")
                .append(" and/or sell copies of the Software, ")
                .append("and to permit persons to whom the Software is furnished to do so, ")
                .append("subject to the following conditions")
                .append("The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\n")
                .append("\n")
                .append("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,")
                .append(" EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, ")
                .append("FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. ")
                .append("IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,")
                .append(" DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,")
                .append("TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE")
                .append(" OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.")
                .toString());
        
    }

    private void showLicence(){
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(this);
        alertdialogBuilder.setTitle("Lizenzen")
                .setMessage("");
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
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
