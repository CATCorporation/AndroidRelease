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

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private MatiereDAO matiereDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        matiereDAO = new MatiereDAO(getApplicationContext());

        Button aPropos = (Button) findViewById(R.id.button_a_propos);
        aPropos.setOnClickListener(this);

        Button consulter = (Button) findViewById(R.id.button_consulter);
        consulter.setOnClickListener(this);

        Button gestionDonnees = (Button) findViewById(R.id.button_gestion_donnees);
        gestionDonnees.setOnClickListener(this);

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
            case R.id.button_a_propos:
                Intent aPropos = new Intent(this, AProposActivity.class);
                startActivity(aPropos);
                break;
            case R.id.button_consulter:
                if(hasNotes()) {
                    Intent consulter = new Intent(this, ConsulterActivity.class);
                    startActivity(consulter);
                }else{
                    toast = Toast.makeText(getApplicationContext(), "Vous devez saisir des notes afin de pouvoir consulter vos moyennes", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            case R.id.button_gestion_donnees:
                Intent gestionDonnees = new Intent(this, GestionDonneesActivity.class);
                startActivity(gestionDonnees);
                break;
            default:
                toast = Toast.makeText(getApplicationContext(), "En construction", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    public boolean hasNotes(){
        if(matiereDAO.getNotes().isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
