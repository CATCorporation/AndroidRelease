package com.example.emerich.mamoyenne.Traitment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emerich.mamoyenne.BddPack.MyBddClass;
import com.example.emerich.mamoyenne.R;

import java.util.ArrayList;

public class addNote extends ActionBarActivity implements View.OnClickListener{

    MyBddClass maBdd;
    Spinner mySpinner1 ;
    Spinner mySpinner2 ;
    ArrayList<String> maListe = new ArrayList<String>();
    String Semestre = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // on set la bdd
        maBdd = new MyBddClass(this);
        maBdd.openDatabase();

        Button three = (Button) findViewById(R.id.addNote);
        three.setOnClickListener(this);
        Button four = (Button) findViewById(R.id.delNote);
        four.setOnClickListener(this);
        Button two = (Button) findViewById(R.id.retour);
        two.setOnClickListener(this);
        Button six = (Button) findViewById(R.id.upNote);
        six.setOnClickListener(this);

        loadSpinner2();

        mySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                loadSpinner3();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        RadioGroup rg = (RadioGroup) findViewById(R.id.radiogroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.sm1:
                        Semestre = "1";
                        loadSpinner3();
                        break;

                    case R.id.sm2:
                        Semestre = "2";
                        loadSpinner3();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addNote:
                EditText note = (EditText) findViewById(R.id.noteText);
                try{
                    int valeur = Integer.parseInt(note.getText().toString());
                    if(valeur <= 20) {
                        String matiere = mySpinner1.getSelectedItem().toString();
                        if (!matiere.equals("Vide"))
                            maBdd.insert_note(note.getText().toString(), Semestre, maBdd.getMatiereId(matiere));
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Vous devez d'abord ajouter une matière", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Erreur la note doit être comprise entre 0 et 20!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (NumberFormatException e) {}

                note.setText("");
                loadSpinner3();
            break;

            case R.id.delNote:
                deleteNote();
            break;

            case R.id.upNote:
                note = (EditText) findViewById(R.id.newNote);
                try{
                    int valeur = Integer.parseInt(note.getText().toString());
                    if(valeur <= 20) {
                        String matiere = mySpinner1.getSelectedItem().toString();
                        if (!matiere.equals("Vide"))
                            maBdd.updateNote(mySpinner2.getSelectedItem().toString(),note.getText().toString(),mySpinner1.getSelectedItem().toString());
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Vous devez d'abord sélectionner une matière pour effectuer la misee à jour", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Erreur la note doit être comprise entre 0 et 20!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (NumberFormatException e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Vous devez d'abord saisir une note pour effectuer la mise à jour", Toast.LENGTH_SHORT);
                    toast.show();
                }
                note.setText("");
                loadSpinner3();
            break;

            case R.id.retour:
                finish();
            break;
        }

    }

    private void loadSpinner2(){
        maListe = maBdd.selectMatiere();
        if(maListe.size() <= 0)
            maListe.add("Vide");
        mySpinner1 = (Spinner) findViewById(R.id.matiereSpin);

        mySpinner2 = (Spinner) findViewById(R.id.matiereSpin);
        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, maListe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(adapter);
    }
    private void loadSpinner3(){
        ArrayList<String> maListe = new ArrayList<>();
        maListe = maBdd.selectNote(mySpinner1.getSelectedItem().toString(),Semestre);
        if(maListe.size() <= 0)
            maListe.add("Vide");
        mySpinner2 = (Spinner) findViewById(R.id.noteSpin);

        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, maListe);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(adapter);
    }

    private void deleteNote() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        AlertDialog.Builder builder = alertDialogBuilder
                .setMessage("Etes-vous sure de supprimer la note " + mySpinner2.getSelectedItem().toString() +
                        " pour la matière " + mySpinner1.getSelectedItem().toString() + "!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        maBdd.deleteNote(mySpinner2.getSelectedItem().toString(), mySpinner1.getSelectedItem().toString());
                        mySpinner1.setAdapter(null);
                        mySpinner2.setAdapter(null);
                        loadSpinner2();
                        loadSpinner3();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        maBdd.closeDatabase();
    }
}
