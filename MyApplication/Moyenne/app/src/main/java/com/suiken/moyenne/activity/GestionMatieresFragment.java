package com.suiken.moyenne.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;


public class GestionMatieresFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    private OnFragmentInteractionListener mListener;

    public static GestionMatieresFragment newInstance(String param1, String param2) {
        GestionMatieresFragment fragment = new GestionMatieresFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GestionMatieresFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  null;
        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE
           && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            view = inflater.inflate(R.layout.matiere_xlarge, container, false);

        }

        else
            view = inflater.inflate(R.layout.fragment_gestion_matieres, container, false);

        matiereDAO = new MatiereDAO(getActivity().getBaseContext());

        lv = (ListView) view.findViewById(R.id.listMatiere);
        array = matiereDAO.getMatieresBySemestre(1);

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        lv.setAdapter(arrayAdapter);
        addOnClickListenerListView();

        btnAjouter = (Button) view.findViewById(R.id.button_ajouter_matiere);
        btnAjouter.setOnClickListener(this);

        btnModifier = (Button) view.findViewById(R.id.button_modifier_matiere);
        btnModifier.setOnClickListener(this);
        btnModifier.setVisibility(View.INVISIBLE);

        btnSupprimer = (Button) view.findViewById(R.id.button_supprimer_matiere);
        btnSupprimer.setOnClickListener(this);
        btnSupprimer.setVisibility(View.INVISIBLE);

        btnEffacer = (Button) view.findViewById(R.id.button_effacer_matiere);
        btnEffacer.setOnClickListener(this);
        btnEffacer.setVisibility(View.INVISIBLE);

        radioGroup = (RadioGroup) view.findViewById(R.id.semestres);

        rbPremierSemestre = (RadioButton) view.findViewById(R.id.premierSemestre);
        rbPremierSemestre.setOnClickListener(this);

        rbDeuxiemeSemestre = (RadioButton) view.findViewById(R.id.deuxiemeSemestre);
        rbDeuxiemeSemestre.setOnClickListener(this);

        txtMatiere = (EditText) view.findViewById(R.id.text_matiere);
        txtMatiere.setText("");
        txtCoeff = (EditText) view.findViewById(R.id.text_coefficient);
        txtCoeff.setText("");

        btnRetour = (Button) view.findViewById(R.id.button_retour_matiere);
        btnRetour.setOnClickListener(this);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
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
                        toast = Toast.makeText(getActivity().getApplicationContext(), "Vous devez saisir choisir un semestre", Toast.LENGTH_SHORT);
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
                //Intent gestionDonnees = new Intent(this, GestionDonneesActivity.class);
                //startActivity(gestionDonnees);
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
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        lv.setAdapter(arrayAdapter);
    }

    public void reloadListView(ArrayList<Matiere> array){
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
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
            toast = Toast.makeText(getActivity().getApplicationContext(), "Vous devez saisir une matiere", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (txtCoeff.getText().toString().equals("")) {
            toast = Toast.makeText(getActivity().getApplicationContext(), "Vous devez saisir un coefficient", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }else if(Integer.parseInt(txtCoeff.getText().toString()) == 0){
            toast = Toast.makeText(getActivity().getApplicationContext(), "Le coefficient doit être supérieur à 0", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }
}
