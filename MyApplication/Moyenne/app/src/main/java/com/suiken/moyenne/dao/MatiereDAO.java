package com.suiken.moyenne.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suiken.moyenne.model.Matiere;
import com.suiken.moyenne.model.Note;

import java.util.ArrayList;

/**
 * Created by Suiken on 23/02/15.
 */
public class MatiereDAO extends DatabaseDAO{

//    Matiere
    public static final String LIBELLE = "libelle";
    public static final String COEFFICIENT = "coefficient";
    public static final String SEMESTRE = "semestre";

//    Notes
    public static final String NOTE = "note";
    public static final String MATIERE = "id_matiere";


    public MatiereDAO(Context pContext) {
        super(pContext);
    }

    /**
     * @param m le métier à ajouter à la base
     */
    public void addMatiere(Matiere m) {
        SQLiteDatabase datab = super.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LIBELLE, m.getLibelle());
        contentValues.put(COEFFICIENT, m.getCoefficient());
        contentValues.put(SEMESTRE, m.getSemestre());

        datab.insert(super.database.TABLE_MATIERE, null, contentValues);

        /*super.database.getWritableDatabase().execSQL(
                "Insert into " + super.database.TABLE_MATIERE + "(libelle, coefficient, semestre) " +
                "values (" + m.getLibelle() + ", " + m.getCoefficient() + ", " + m.getSemestre() + ");"
        );*/
        datab.close();
    }

    /**
     * @param id l'identifiant du métier à supprimer
     */
    public void deleteMatiereById(long id) {
        SQLiteDatabase datab = super.open();

        datab.delete(super.database.TABLE_MATIERE, "id = ?", new String[]{String.valueOf(id)});
        datab.delete(super.database.TABLE_NOTE, "id_matiere = ?", new String[]{String.valueOf(id)});

        /*super.database.getWritableDatabase().execSQL(
                "delete from " + super.database.TABLE_MATIERE + " where id = " + id + ";"
        );*/
        datab.close();
    }

    /**
     * @param m le métier modifié
     */
    public void updateMatiere(Matiere m) {
        SQLiteDatabase datab = super.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LIBELLE, m.getLibelle());
        contentValues.put(COEFFICIENT, m.getCoefficient());
        contentValues.put(SEMESTRE, m.getSemestre());

        datab.update(super.database.TABLE_MATIERE, contentValues, "id = ?", new String[]{String.valueOf(m.getId())});

        /*super.database.getWritableDatabase().execSQL(
                "update " + super.database.TABLE_MATIERE + "set " +
                "libelle = " + m.getLibelle() + ", " +
                "note = " + m.getNote() + ", " +
                "cofficient = " + m.getCoefficient() + ", " +
                "semestre = " + m.getSemestre() + ", " +
                "where id = " + m.getId() + ";"
        );*/

        datab.close();
    }

    /**
     * @param id l'identifiant du métier à récupérer
     */
    public Matiere getMatiereById(long id) throws Exception {
        /*super.database.getReadableDatabase().execSQL(
                "select * " +
                "from " + super.database.TABLE_MATIERE + " " +
                "where id = " + id + ";"
        );
        return null;*/

        SQLiteDatabase datab = super.open();

        Cursor c = datab.rawQuery("select * from " + super.database.TABLE_MATIERE + " where id = ?", new String[]{String.valueOf(id)});

        datab.close();

        if(c.getCount() > 1){
            throw new Exception("La requete renvoie plus d'un résultat");
        }else if(c.getCount() < 1){
            throw new Exception("La requete ne renvoie rien");
        }else {
            c.moveToFirst();
            return new Matiere(c.getLong(0), c.getString(1), c.getInt(2), c.getInt(3));
        }
    }

    public ArrayList<Matiere> getMatieresBySemestre(int semestre){
        ArrayList<Matiere> result = new ArrayList<>();

        SQLiteDatabase datab = super.open();

        Cursor c = datab.rawQuery("select * from " + super.database.TABLE_MATIERE + " where semestre = ?", new String[]{String.valueOf(semestre)});


        while(c.moveToNext()){
            result.add(new Matiere(c.getLong(0), c.getString(1), c.getInt(2), c.getInt(3)));
        }

        datab.close();
        return result;
    }

    public ArrayList<Matiere> getMatieres(){
        ArrayList<Matiere> result = new ArrayList<>();

        SQLiteDatabase datab = super.open();

        Cursor c = datab.rawQuery("select * from " + super.database.TABLE_MATIERE, new String[]{});



        while(c.moveToNext()){
            result.add(new Matiere(c.getLong(0), c.getString(1), c.getInt(2), c.getInt(2)));
        }

        datab.close();
        return result;
    }

    public void updateNote(Note n){
        SQLiteDatabase datab = super.open();
//
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE, n.getNote());

        datab.update(super.database.TABLE_NOTE, contentValues, "id = ?", new String[]{String.valueOf(n.getId())});

//        String query="UPDATE notes SET " +
//                "note = " + n.getNote() + " " +
//                "where id = " + String.valueOf(n.getId());
//        datab.execSQL(query);


        datab.close();
    }

    public ArrayList<Matiere> getMatieresBySemestreWithNotes(int semestre){
        ArrayList<Matiere> result = new ArrayList<>();
        ArrayList<Note> notes;
        Matiere matiere;
        Note note;
        SQLiteDatabase datab = super.open();
        Cursor cNotes;

        Cursor c = datab.rawQuery("select * from " + super.database.TABLE_MATIERE + " where semestre = ?", new String[]{String.valueOf(semestre)});



        while(c.moveToNext()){
            notes = new ArrayList<>();
            matiere = new Matiere(c.getLong(0), c.getString(1), c.getInt(2), c.getInt(3));

            cNotes = datab.rawQuery("select * from " + super.database.TABLE_NOTE + " where " + MATIERE + " = ?", new String[]{ String.valueOf(matiere.getId()) });

            while(cNotes.moveToNext()){
                note = new Note(cNotes.getLong(0), cNotes.getFloat(1), new Matiere(cNotes.getLong(2)));
                notes.add(note);
            }

            matiere.setNotes(notes);
            result.add(matiere);
            cNotes.close();
        }

        datab.close();
        return result;

    }

    public void addNote(Note note){
        SQLiteDatabase datab = super.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTE, note.getNote());
        contentValues.put(MATIERE, note.getMatiere().getId());

        datab.insert(super.database.TABLE_NOTE, null, contentValues);

        /*super.database.getWritableDatabase().execSQL(
                "Insert into " + super.database.TABLE_MATIERE + "(libelle, coefficient, semestre) " +
                "values (" + m.getLibelle() + ", " + m.getCoefficient() + ", " + m.getSemestre() + ");"
        );*/
        datab.close();
    }

    public void deleteNoteById(long id){
        SQLiteDatabase datab = super.open();

        datab.delete(super.database.TABLE_NOTE, "id = ?", new String[]{String.valueOf(id)});

        /*super.database.getWritableDatabase().execSQL(
                "delete from " + super.database.TABLE_MATIERE + " where id = " + id + ";"
        );*/
        datab.close();
    }

    public ArrayList<Note> getNotes(){
        ArrayList<Note> result = new ArrayList<>();

        SQLiteDatabase datab = super.open();

        Cursor c = datab.rawQuery("select * from " + super.database.TABLE_NOTE, new String[]{});

        while(c.moveToNext()){
            result.add(new Note(c.getLong(0), c.getFloat(1)));
        }

        datab.close();
        return result;
    }
}
