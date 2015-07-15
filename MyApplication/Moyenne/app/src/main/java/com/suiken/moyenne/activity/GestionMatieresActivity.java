package com.suiken.moyenne.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.suiken.moyenne.R;

import java.util.Locale;


public class GestionMatieresActivity extends ActionBarActivity implements GestionMatieresFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_matieres);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_gestion_matieres));

        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new GestionMatieresFragment();
        fragmentTransaction.add(R.id.Matieres, fragment,"FragmentMatiere");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
    public void onFragmentInteraction(Uri uri) {

    }
}
