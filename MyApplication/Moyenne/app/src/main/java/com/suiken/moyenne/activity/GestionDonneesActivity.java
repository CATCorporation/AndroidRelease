package com.suiken.moyenne.activity;

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


public class GestionDonneesActivity extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_donnees);

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
            default:
                toast = Toast.makeText(getApplicationContext(), "En construction", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gestion_donnees, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
