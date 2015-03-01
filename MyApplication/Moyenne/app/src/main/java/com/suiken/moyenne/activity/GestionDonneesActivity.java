package com.suiken.moyenne.activity;

import android.app.Activity;
import android.content.Intent;
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


public class GestionDonneesActivity extends Activity implements View.OnClickListener{

    private Button btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_donnees);

        btnRetour = (Button) findViewById(R.id.button_retour_gestion_donnees);
        btnRetour.setOnClickListener(this);

        Button gestionMatieres = (Button) findViewById(R.id.button_gestion_matieres);
        gestionMatieres.setOnClickListener(this);

        Button gestionNotes = (Button) findViewById(R.id.button_gestion_notes);
        gestionNotes.setOnClickListener(this);
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
                    toast = Toast.makeText(getApplicationContext(), "Vous devez saisir des matières avant de pouvoir saisir des notes", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.button_retour_gestion_donnees:
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
                break;
        }
    }


}
