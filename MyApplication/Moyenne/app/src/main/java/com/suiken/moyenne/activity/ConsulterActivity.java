package com.suiken.moyenne.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.suiken.moyenne.R;
import com.suiken.moyenne.dao.MatiereDAO;
import com.suiken.moyenne.model.Matiere;
import com.suiken.moyenne.model.Note;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;


public class ConsulterActivity extends ActionBarActivity implements View.OnClickListener{

    private RadioGroup rgSemestres;
    private RadioButton rdSemestreUn;
    private RadioButton rdSemestreDeux;
    private TableLayout tableNotes;

    private MatiereDAO matiereDAO;
    private ArrayList<Matiere> matieres;
    private ArrayList<Note> notes;

    private String properties[] = {"Matière", "Coefficient", "Moyenne"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_consulter));

        matiereDAO = new MatiereDAO(getApplicationContext());

        rgSemestres = (RadioGroup) findViewById(R.id.semestres);

        rdSemestreUn = (RadioButton) findViewById(R.id.premierSemestre);
        rdSemestreUn.setOnClickListener(this);

        rdSemestreDeux = (RadioButton) findViewById(R.id.deuxiemeSemestre);
        rdSemestreDeux.setOnClickListener(this);

        tableNotes = (TableLayout) findViewById(R.id.tableNotes);

        matieres = matiereDAO.getMatieresBySemestreWithNotes(1);

        displayTableLayout(getMoyennes());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Configuration config = getResources().getConfiguration();
        getResources().updateConfiguration(config, null);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Configuration config = new Configuration();

        if (id == R.id.action_fr) {
            config.locale = Locale.FRENCH;
            getResources().updateConfiguration(config, null);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        if (id == R.id.action_en) {
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.premierSemestre:
                matieres = matiereDAO.getMatieresBySemestreWithNotes(1);

                displayTableLayout(getMoyennes());
                break;
            case R.id.deuxiemeSemestre:
                matieres = matiereDAO.getMatieresBySemestreWithNotes(2);

                displayTableLayout(getMoyennes());
                break;
        }
    }

    public LinkedHashMap<Matiere, Note> getMoyennes(){
        LinkedHashMap<Matiere, Note> moyennes = new LinkedHashMap<>();

        int totalCoeff = 0, nbNotes = 0;
        float moyenne = 0.0f, moyenneGenerale = 0.0f;
        boolean noteFound = false;

        DecimalFormat df = new DecimalFormat("##.##");


        for(Matiere m : matieres){
            if(m.getNotes().size() > 0) {
                if(!noteFound) noteFound = true;
                for (Note n : m.getNotes()) {
                    moyenne += n.getNote();
                    nbNotes++;
                }
                moyenne /= nbNotes;

                moyenne = Float.valueOf(df.format(moyenne).replace(",", "."));

                moyennes.put(m, new Note(moyenne));

                moyenneGenerale += moyenne * m.getCoefficient();
                totalCoeff += m.getCoefficient();

                moyenne = 0.0f;
                nbNotes = 0;
            }
        }

        if(noteFound) {
            moyenneGenerale /= totalCoeff;
            moyenneGenerale = Float.valueOf(df.format(moyenneGenerale).replace(",", "."));
            moyennes.put(new Matiere("Moyenne Générale"), new Note(moyenneGenerale));
        }

        return moyennes;
    }

    public void displayTableLayout(LinkedHashMap<Matiere, Note> moyennes) {

        tableNotes.removeAllViews();

        if (!moyennes.isEmpty()) {

//        ligne header
            TableRow rowHeader = new TableRow(this);

            TableLayout.LayoutParams lpHeader = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT);
            rowHeader.setLayoutParams(lpHeader);

            rowHeader.setBackgroundColor(Color.parseColor("#68A9D3"));


            for (int i = 0; i < properties.length; i++) {
                TextView column = new TextView(this);

                column.setGravity(Gravity.CENTER);

                column.setText(properties[i]);
                column.setTextSize(12.0f);
                column.setPadding(15, 0, 15, 0);
                column.setTextColor(Color.parseColor("#FFFFFF"));
                column.setTypeface(null, Typeface.BOLD);

                rowHeader.addView(column);
            }

            tableNotes.addView(rowHeader);

//        lignes contenant les données

            LinkedHashMap<Matiere, Note> selects = moyennes;
            for (Map.Entry<Matiere, Note> entry : selects.entrySet()) {

                TableRow row = new TableRow(this);

                TableLayout.LayoutParams lpColumn = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lpColumn);

                row.setPadding(15, 3, 15, 3);

                row.setBackgroundColor(Color.parseColor("#83B2D6"));

                for (int i = 0; i < 3; i++) {

                    TextView Header = new TextView(this);

                    Header.setGravity(Gravity.CENTER);

                    String property = new String();
                    switch (i) {
                        case 0:
                            property = entry.getKey().getLibelle();
                            break;
                        case 1:
                            if (entry.getKey().getCoefficient() == 0) {
                                property = "";
                            } else {
                                property = String.valueOf(entry.getKey().getCoefficient());
                            }
                            break;
                        case 2:
                            property = String.valueOf(entry.getValue().getNote());
                            break;
                    }
                    Header.setText(property);
                    Header.setTextSize(12.0f);
                    Header.setPadding(15, 0, 15, 0);
                    Header.setTextColor(Color.parseColor("#FFFFFF"));
                    Header.setTypeface(null, Typeface.BOLD);

                    row.addView(Header);
                }

                tableNotes.addView(row);
            }

        }
    }
}
