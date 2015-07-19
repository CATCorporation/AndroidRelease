package com.suiken.moyenne.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.suiken.moyenne.R;
import com.suiken.moyenne.dao.MatiereDAO;
import com.suiken.moyenne.model.Matiere;
import com.suiken.moyenne.model.Note;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class GestionNoteFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText txtNote;
    private Spinner matieresSpinner;
    private Button btnModifier;
    private Button btnSupprimer;
    private Button btnAjouter;
    private Button btnEffacer;
    private RadioButton radioPremierSemestre;
    private RadioButton radioDeuxiemeSemestre;
    private ListView listNotes;
    private RadioGroup rgSemestres;
    private Button btnRetour;

    private MatiereDAO matiereDAO;
    private ArrayList<Matiere> matieres;
    private ArrayAdapter<Matiere> arrayAdapterMatieres;
    private ArrayList<Note> notes;
    private ArrayAdapter<Note> arrayAdapterNotes;
    private Matiere selectedCell;
    private Note selectedNote;

    private OnFragmentInteractionListener mListener;

    public static GestionNoteFragment newInstance(String param1, String param2) {
        GestionNoteFragment fragment = new GestionNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public GestionNoteFragment() {
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
        View view =  null;

        if((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE
                && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            view = inflater.inflate(R.layout.note_xlarge, container, false);

        }

        else
            view = inflater.inflate(R.layout.fragment_gestion_note, container, false);


        matiereDAO = new MatiereDAO(getActivity().getApplicationContext());
        matieres = matiereDAO.getMatieresBySemestreWithNotes(1);
        arrayAdapterMatieres = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, matieres);

        matieresSpinner = (Spinner) view.findViewById(R.id.matieres);
        matieresSpinner.setAdapter(arrayAdapterMatieres);
        addOnclickListenerSpinner();

        selectedCell = matieres.get(0);

        txtNote = (EditText) view.findViewById(R.id.text_note);

        btnAjouter = (Button) view.findViewById(R.id.button_ajouter_note);
        btnAjouter.setOnClickListener(this);

        btnModifier = (Button) view.findViewById(R.id.button_modifier_note);
        btnModifier.setOnClickListener(this);
        btnModifier.setVisibility(View.INVISIBLE);


        btnSupprimer = (Button) view.findViewById(R.id.button_supprimer_note);
        btnSupprimer.setOnClickListener(this);
        btnSupprimer.setVisibility(View.INVISIBLE);


        btnEffacer = (Button) view.findViewById(R.id.button_effacer_note);
        btnEffacer.setOnClickListener(this);
        btnEffacer.setVisibility(View.INVISIBLE);


        radioPremierSemestre = (RadioButton) view.findViewById(R.id.premierSemestre);
        radioPremierSemestre.setOnClickListener(this);

        radioDeuxiemeSemestre = (RadioButton) view.findViewById(R.id.deuxiemeSemestre);
        radioDeuxiemeSemestre.setOnClickListener(this);

        listNotes = (ListView) view.findViewById(R.id.notes);
        notes = matieres.get(0).getNotes();
        arrayAdapterNotes = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notes);
        listNotes.setAdapter(arrayAdapterNotes);
        addOnClickListenerListView();

        rgSemestres = (RadioGroup) view.findViewById(R.id.semestres);


        return view;
    }


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
            throw new ClassCastException(activity.toString()
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


    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Configuration config = new Configuration();

        if (id == R.id.action_fr) {
            config.locale = Locale.FRENCH;
            getResources().updateConfiguration(config, null);
            Fragment currentFragment = getFragmentManager().findFragmentByTag("FragmentNotes");
            FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
            return true;
        }
        if (id == R.id.action_en) {
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, null);
            Fragment currentFragment = getFragmentManager().findFragmentByTag("FragmentNotes");
            FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Toast toast;
        DecimalFormat df = new DecimalFormat("##.##");
        switch(v.getId()){
            case R.id.button_modifier_note:
                if(verifSaisie()) {

                    float note = Float.parseFloat(txtNote.getText().toString());
                    selectedNote.setNote(Float.parseFloat(df.format(note).replace(",", ".")));
                    matiereDAO.updateNote(selectedNote);
                    displayListBySemestreAndMatiere();
                    modifEffaceSuppr();
                }
                break;
            case R.id.button_ajouter_note:
                if(verifSaisie()) {
                    float note = Float.parseFloat(txtNote.getText().toString());
                    Note newNote = new Note(Float.parseFloat(df.format(note).replace(",", ".")), selectedCell);
                    if(newNote.getNote() <= 20 && newNote.getNote() >= 0){
                        matiereDAO.addNote(newNote);
                        notes.add(newNote);
                        reloadListNotes(notes);
                        txtNote.setText("");
                    }
                }
                break;
            case R.id.button_supprimer_note:
                matiereDAO.deleteNoteById(selectedNote.getId());
                notes.remove(selectedNote);
                displayListBySemestreAndMatiere();
                modifEffaceSuppr();
                break;
            case R.id.button_effacer_note:
                selectedNote = null;
                txtNote.setText("");
                btnModifier.setVisibility(View.INVISIBLE);
                btnAjouter.setVisibility(View.VISIBLE);
                btnEffacer.setVisibility(View.INVISIBLE);
                btnSupprimer.setVisibility(View.INVISIBLE);
                break;
            case R.id.premierSemestre:
                displayListBySemestre(1);
                break;
            case R.id.deuxiemeSemestre:
                displayListBySemestre(2);
                break;
        }
    }

    public void addOnclickListenerSpinner(){
        matieresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                selectedCell = new Matiere(matieres.get(matieresSpinner.getSelectedItemPosition()));

                notes = selectedCell.getNotes();
                reloadListNotes(notes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addOnClickListenerListView(){
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNote = new Note(notes.get(position));
                txtNote.setText(String.valueOf(selectedNote.getNote()));

                btnAjouter.setVisibility(View.INVISIBLE);
                btnModifier.setVisibility(View.VISIBLE);
                btnSupprimer.setVisibility(View.VISIBLE);
                btnEffacer.setVisibility(View.VISIBLE);

            }
        });
    }

    public void displayListBySemestre(int semestre){
        matieres = matiereDAO.getMatieresBySemestreWithNotes(semestre);
        arrayAdapterMatieres = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, matieres);
        matieresSpinner.setAdapter(arrayAdapterMatieres);
        Toast toast;
        if(!matieres.isEmpty()) {
            selectedCell = matieres.get(0);
            notes = selectedCell.getNotes();
            reloadListNotes(notes);
            matieresSpinner.setEnabled(true);
            txtNote.setEnabled(true);
            txtNote.setText("");
            btnAjouter.setVisibility(View.VISIBLE);
            btnModifier.setVisibility(View.INVISIBLE);
            btnEffacer.setVisibility(View.INVISIBLE);
            btnSupprimer.setVisibility(View.INVISIBLE);
        }else{
            selectedCell = null;
            notes = new ArrayList<Note>();
            reloadListNotes(notes);
            txtNote.setText("");
            txtNote.setEnabled(false);
            btnModifier.setVisibility(View.INVISIBLE);
            btnAjouter.setVisibility(View.INVISIBLE);
            btnEffacer.setVisibility(View.INVISIBLE);
            btnSupprimer.setVisibility(View.INVISIBLE);
            matieresSpinner.setEnabled(false);
        }
    }

    public void reloadListNotes(ArrayList<Note> notes){
        arrayAdapterNotes = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notes);
        listNotes.setAdapter(arrayAdapterNotes);
    }

    public boolean verifSaisie(){
        Toast toast;
        if(txtNote.getText().toString().equals("")){
            toast = Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.erreur_saisie_note), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }else if(Float.valueOf(txtNote.getText().toString()) < 0 || Float.valueOf(txtNote.getText().toString()) > 20){
            toast = Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.erreur_note), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    public void modifEffaceSuppr(){
        selectedNote = null;
        txtNote.setText("");
        btnModifier.setVisibility(View.INVISIBLE);
        btnAjouter.setVisibility(View.VISIBLE);
        btnEffacer.setVisibility(View.INVISIBLE);
        btnSupprimer.setVisibility(View.INVISIBLE);
    }

    public void displayListBySemestreAndMatiere(){
        if(rgSemestres.getCheckedRadioButtonId() == R.id.premierSemestre){
            displayListByMatiere(1);
        }else if(rgSemestres.getCheckedRadioButtonId() == R.id.deuxiemeSemestre) {
            displayListByMatiere(2);
        }
    }

    public void displayListByMatiere(int semestre){
        matieres = matiereDAO.getMatieresBySemestreWithNotes(semestre);
        notes = new ArrayList<>(matieres.get(matieresSpinner.getSelectedItemPosition()).getNotes());
        arrayAdapterNotes = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, notes);
        listNotes.setAdapter(arrayAdapterNotes);
    }


}
