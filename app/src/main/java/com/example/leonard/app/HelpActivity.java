package com.example.leonard.app;

import android.app.ActionBar;
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

        TextView textView = (TextView)findViewById(R.id.textViewLizenzen);
        textView.setText("Die Vertretungsplan App, des Ceciliengymnasiums (die App) wurde von \n Leonard Pieper\nentwickelt und steht unter der GNU GPL Lizenz."+
                "\n"+
                "\n"+
                " This program is free software: you can redistribute it and/or modify\n" +
                        "    it under the terms of the GNU General Public License as published by\n" +
                        "    the Free Software Foundation, either version 3 of the License, or\n" +
                        "    (at your option) any later version.\n" +
                        "\n" +
                        "    This program is distributed in the hope that it will be useful,\n" +
                        "    but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                        "    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                        "    GNU General Public License for more details.\n" +
                        "\n" +
                        "    You should have received a copy of the GNU General Public License\n" +
                        "    along with this program.  If not, see <http://www.gnu.org/licenses/>.\n" +
                        "\n" +
                        "    Dieses Programm ist Freie Software: Sie können es unter den Bedingungen\n" +
                        "    der GNU General Public License, wie von der Free Software Foundation,\n" +
                        "    Version 3 der Lizenz oder (nach Ihrer Wahl) jeder neueren\n" +
                        "    veröffentlichten Version, weiterverbreiten und/oder modifizieren.\n" +
                        "\n" +
                        "    Dieses Programm wird in der Hoffnung, dass es nützlich sein wird, aber\n" +
                        "    OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite\n" +
                        "    Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.\n" +
                        "    Siehe die GNU General Public License für weitere Details.\n" +
                        "\n" +
                        "    Sie sollten eine Kopie der GNU General Public License zusammen mit diesem\n" +
                        "    Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>."+
                "\n"+
                "\n"+
                "---------\n\n"+
                "Es wurden Bestandteile aus Open-Source/freier Software verwendet, \n" +
                        "der FloatingActionButton steht unter der Apache 2.0 Lizenz\n\n"+
                "Copyright 2015 futuresimple\n" +
                "\n" +
                "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                "you may not use this file except in compliance with the License.\n" +
                "You may obtain a copy of the License at\n" +
                "\n" +
                "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                "\n" +
                "Unless required by applicable law or agreed to in writing, software\n" +
                "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                "See the License for the specific language governing permissions and\n" +
                "limitations under the License.");
        
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
