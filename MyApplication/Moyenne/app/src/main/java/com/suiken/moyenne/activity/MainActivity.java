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


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private MatiereDAO matiereDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matiereDAO = new MatiereDAO(getApplicationContext());

        Button aPropos = (Button) findViewById(R.id.button_a_propos);
        aPropos.setOnClickListener(this);

        Button consulter = (Button) findViewById(R.id.button_consulter);
        consulter.setOnClickListener(this);

        Button gestionDonnees = (Button) findViewById(R.id.button_gestion_donnees);
        gestionDonnees.setOnClickListener(this);

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
                Intent consulter = new Intent(this, ConsulterActivity.class);
                startActivity(consulter);
                break;
            case R.id.button_gestion_donnees:
                Intent gestionDonnees = new Intent(this, GestionDonneesActivity.class);
                startActivity(gestionDonnees);
                break;
            case R.id.button_a_propose:
                Intent aPropose = new Intent(this, AProposActivity.class);
                startActivity(aPropose);
            default:
                toast = Toast.makeText(getApplicationContext(), "En construction", Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.button_a_propose) {
            Intent aPropos = new Intent(this, AProposActivity.class);
            startActivity(aPropos);
        }

        return super.onOptionsItemSelected(item);
    }
}
