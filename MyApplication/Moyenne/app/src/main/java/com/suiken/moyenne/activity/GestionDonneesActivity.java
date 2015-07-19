package com.suiken.moyenne.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.suiken.moyenne.R;
import com.suiken.moyenne.dao.MatiereDAO;
import com.suiken.moyenne.model.Matiere;

import java.util.ArrayList;
import java.util.Locale;


public class GestionDonneesActivity extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_donnees);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_gestion_donnees));

        Button gestionMatieres = (Button) findViewById(R.id.button_gestion_matieres);
        gestionMatieres.setOnClickListener(this);

        Button gestionNotes = (Button) findViewById(R.id.button_gestion_notes);
        gestionNotes.setOnClickListener(this);
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
    public void onClick(View v){
        Toast toast;
        switch(v.getId()){
            case R.id.button_gestion_matieres:
                Intent gestionMatieres = new Intent(this, GestionMatieresActivity.class);
                startActivity(gestionMatieres);
                break;
            case R.id.button_gestion_notes:
                MatiereDAO matiereDAO = new MatiereDAO(getBaseContext());
                ArrayList<Matiere> matieres = matiereDAO.getMatieres();

                if(!matieres.isEmpty()) {
                    Intent gestionNotes = new Intent(this, GestionNotesActivity.class);
                    startActivity(gestionNotes);
                }else{
                    toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.erreur_saisie_note_avant_matiere), Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
        }
    }
}
