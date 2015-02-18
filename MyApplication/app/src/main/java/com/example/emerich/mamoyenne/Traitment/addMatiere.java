package com.example.emerich.mamoyenne.Traitment;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.emerich.mamoyenne.BddPack.MyBddClass;
import com.example.emerich.mamoyenne.R;

import java.util.ArrayList;

public class addMatiere extends ActionBarActivity implements View.OnClickListener{

    MyBddClass maBdd;
    Spinner mySpinner ;
    ArrayList<String> maListe = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_matiere);

        // on set la bdd
        maBdd = new MyBddClass(this);
        maBdd.openDatabase();


        Button one = (Button) findViewById(R.id.insertBtn);
        one.setOnClickListener(this); // calling onClick() method
        Button two = (Button) findViewById(R.id.delete);
        two.setOnClickListener(this);

        Button four = (Button) findViewById(R.id.retour);
        four.setOnClickListener(this);
        Button five = (Button) findViewById(R.id.upMatiere);
        five.setOnClickListener(this);
        loadSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.insertBtn:
                EditText mat = (EditText) findViewById(R.id.matiereText);
                EditText coef = (EditText) findViewById(R.id.coefText);
                maBdd.insert_matiere(mat.getText().toString(), coef.getText().toString());
                mat.setText("");
                coef.setText("");
                loadSpinner();
                break;

            case R.id.delete:
                maBdd.deleteMatiere(mySpinner.getSelectedItem().toString());
                mySpinner.setAdapter(null);
                loadSpinner();
                break;

            case R.id.upMatiere:
                EditText note = (EditText) findViewById(R.id.newMatiere);
                maBdd.updateMatiere(note.getText().toString(), mySpinner.getSelectedItem().toString());
                note.setText("");
                loadSpinner();
                break;

            case R.id.retour:
                    finish();
                break;
        }

    }

    private void loadSpinner(){
        mySpinner = (Spinner) findViewById(R.id.spinner);
        maListe = maBdd.selectMatiere();

        if(maListe.size() > 0)
            maListe.add("Liste vide");

        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, maListe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        maBdd.closeDatabase();
    }
}
