package com.suiken.moyenne.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.suiken.moyenne.R;


public class AProposActivity extends Activity implements View.OnClickListener{

    private Button btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_propos);

        btnRetour = (Button) findViewById(R.id.button_retour_a_propos);
        btnRetour.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_retour_a_propos:
                Intent gestionNotes = new Intent(this, MainActivity.class);
                startActivity(gestionNotes);
                break;
        }
    }
}
