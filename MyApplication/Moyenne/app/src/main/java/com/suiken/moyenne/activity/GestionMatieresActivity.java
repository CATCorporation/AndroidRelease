package com.suiken.moyenne.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.suiken.moyenne.R;
import com.suiken.moyenne.dao.MatiereDAO;
import com.suiken.moyenne.model.Matiere;

import java.io.Console;
import java.util.ArrayList;


public class GestionMatieresActivity extends Activity implements View.OnClickListener{

//    Composants de la vue
    private Button btnAjouter;
    private Button btnSupprimer;
    private Button btnModifier;
    private Button btnEffacer;
    private ListView lv;
    private RadioButton rbPremierSemestre;
    private RadioButton rbDeuxiemeSemestre;
    private EditText txtMatiere;
    private EditText txtCoeff;
    private RadioGroup radioGroup;
    private Button btnRetour;

    private MatiereDAO matiereDAO;
    private ArrayAdapter<Matiere> arrayAdapter;
    private ArrayList<Matiere> array;
    private Matiere selectedMatiere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_matieres);

        matiereDAO = new MatiereDAO(getBaseContext());

        lv = (ListView) findViewById(R.id.listMatiere);
        array = matiereDAO.getMatieresBySemestre(1);

        /*array.add("Français");
        array.add("Maths");
        array.add("EPS");
        array.add("Anglais");
        array.add("Allemand");
        array.add("Chinois");
        array.add("Chimie");
        array.add("Physiques");
        array.add("SVT");*/

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
        lv.setAdapter(arrayAdapter);
        addOnClickListenerListView();

        btnAjouter = (Button) findViewById(R.id.button_ajouter_matiere);
        btnAjouter.setOnClickListener(this);

        btnModifier = (Button) findViewById(R.id.button_modifier_matiere);
        btnModifier.setOnClickListener(this);
        btnModifier.setVisibility(View.INVISIBLE);

        btnSupprimer = (Button) findViewById(R.id.button_supprimer_matiere);
        btnSupprimer.setOnClickListener(this);
        btnSupprimer.setVisibility(View.INVISIBLE);

        btnEffacer = (Button) findViewById(R.id.button_effacer_matiere);
        btnEffacer.setOnClickListener(this);
        btnEffacer.setVisibility(View.INVISIBLE);

        radioGroup = (RadioGroup) findViewById(R.id.semestres);

        rbPremierSemestre = (RadioButton) findViewById(R.id.premierSemestre);
        rbPremierSemestre.setOnClickListener(this);

        rbDeuxiemeSemestre = (RadioButton) findViewById(R.id.deuxiemeSemestre);
        rbDeuxiemeSemestre.setOnClickListener(this);

        txtMatiere = (EditText) findViewById(R.id.text_matiere);
        txtMatiere.setText("");
        txtCoeff = (EditText) findViewById(R.id.text_coefficient);
        txtCoeff.setText("");

        btnRetour = (Button) findViewById(R.id.button_retour_matiere);
        btnRetour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast toast;
        switch(v.getId()){
            case R.id.button_ajouter_matiere:
                if(verifSaisie()){
                    Matiere matiere;
                    if(radioGroup.getCheckedRadioButtonId() == R.id.premierSemestre){
                        matiere = new Matiere(txtMatiere.getText().toString(), Integer.parseInt(txtCoeff.getText().toString()), 1);
                        updateArray(matiere);
                    }else if(radioGroup.getCheckedRadioButtonId() == R.id.deuxiemeSemestre){
                        matiere = new Matiere(txtMatiere.getText().toString(), Integer.parseInt(txtCoeff.getText().toString()), 2);
                        updateArray(matiere);
                    }else{
                        toast = Toast.makeText(getApplicationContext(), "Vous devez saisir choisir un semestre", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                break;
            case R.id.button_modifier_matiere:
                if(verifSaisie()) {
                    selectedMatiere.setCoefficient(Integer.parseInt(txtCoeff.getText().toString()));
                    selectedMatiere.setLibelle(txtMatiere.getText().toString());
                    matiereDAO.updateMatiere(selectedMatiere);
                    modifEffaceSuppr();
                    updateList();
                }
                break;
            case R.id.button_supprimer_matiere:
                matiereDAO.deleteMatiereById(selectedMatiere.getId());
                modifEffaceSuppr();
                updateList();
                break;
            case R.id.button_effacer_matiere:
                modifEffaceSuppr();
                break;
            case R.id.premierSemestre:
                displayListBySemestre(1);
                break;
            case R.id.deuxiemeSemestre:
                displayListBySemestre(2);
                break;
            case R.id.button_retour_matiere:
                Intent gestionDonnees = new Intent(this, GestionDonneesActivity.class);
                startActivity(gestionDonnees);
                break;
        }
    }

    public void updateList(){
        if(radioGroup.getCheckedRadioButtonId() == R.id.premierSemestre){
            displayListBySemestre(1);
        }else if(radioGroup.getCheckedRadioButtonId() == R.id.deuxiemeSemestre) {
            displayListBySemestre(2);
        }
    }

    public void displayListBySemestre(int semestre){
        array = matiereDAO.getMatieresBySemestre(semestre);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
        lv.setAdapter(arrayAdapter);
    }

    public void reloadListView(ArrayList<Matiere> array){
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array);
        lv.setAdapter(arrayAdapter);

        txtMatiere.setText("");
        txtCoeff.setText("");
    }

    public void updateArray(Matiere m){
        matiereDAO.addMatiere(m);
        array.add(m);
        reloadListView(array);
    }

    public void addOnClickListenerListView(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMatiere = new Matiere(array.get(position));
                txtMatiere.setText(selectedMatiere.getLibelle());
                txtCoeff.setText(String.valueOf(selectedMatiere.getCoefficient()));

                btnAjouter.setVisibility(View.INVISIBLE);
                btnModifier.setVisibility(View.VISIBLE);
                btnSupprimer.setVisibility(View.VISIBLE);
                btnEffacer.setVisibility(View.VISIBLE);

            }
        });
    }

    public void modifEffaceSuppr(){
        selectedMatiere = null;

        txtMatiere.setText("");
        txtCoeff.setText("");

        btnAjouter.setVisibility(View.VISIBLE);
        btnEffacer.setVisibility(View.INVISIBLE);
        btnModifier.setVisibility(View.INVISIBLE);
        btnSupprimer.setVisibility(View.INVISIBLE);
    }

    public boolean verifSaisie() {
        Toast toast;
        if (txtMatiere.getText().toString().equals("")) {
            toast = Toast.makeText(getApplicationContext(), "Vous devez saisir une matiere", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (txtCoeff.getText().toString().equals("")) {
            toast = Toast.makeText(getApplicationContext(), "Vous devez saisir un coefficient", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }else if(Integer.parseInt(txtCoeff.getText().toString()) == 0){
            toast = Toast.makeText(getApplicationContext(), "Le coefficient doit être supérieur à 0", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }


}
