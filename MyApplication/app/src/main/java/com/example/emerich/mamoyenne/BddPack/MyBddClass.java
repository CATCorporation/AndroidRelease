package com.example.emerich.mamoyenne.BddPack;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Emerich on 26/01/2015.
 */
public class MyBddClass extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "moyenne.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_MATS = "matiere";
    private static final String TABLE_NAME_NOTE = "notes";

    public SQLiteDatabase db;

    private SQLiteStatement insertMatiere;
    private SQLiteStatement insertNote;

    private static final String INSERTM = "insert into " + TABLE_NAME_MATS + " (name,coef) values (?,?)";

    private static final String INSERTN= "insert into " + TABLE_NAME_NOTE + " (note,semestre, id_mat) values (?,?,?)";

    public MyBddClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public long insert_matiere(String name, String coef) {
        this.insertMatiere.bindString(1, name);
        this.insertMatiere.bindString(2, coef);
        return this.insertMatiere.executeInsert();
    }


    public long insert_note(String note, String semestre, String id_mat) {
        this.insertNote.bindString(1, note);
        this.insertNote.bindString(2, semestre);
        this.insertNote.bindString(3, id_mat);
        return this.insertNote.executeInsert();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TABLE_NAME_MATS + " (_id INTEGER PRIMARY KEY, name TEXT, coef INTEGER, UNIQUE(name) )");
            db.execSQL("CREATE TABLE " + TABLE_NAME_NOTE + " (_id INTEGER PRIMARY KEY, note REAL, semestre INTEGER, " +
                    "id_mat INTEGER NOT NULL CONSTRAINT fk_id_mat REFERENCES "+ TABLE_NAME_MATS + " (_id) );");
            db.execSQL("CREATE VIEW Moyenne as select ma.name,  ma.coef, " +
                       "(select sum(note)/count(note) from "+TABLE_NAME_NOTE+" as no where no.id_mat=ma._id and no.semestre=1) as Moyenne1," +
                       "(select sum(note)/count(note) from "+TABLE_NAME_NOTE+" as no where no.id_mat=ma._id and no.semestre=2) as Moyenne2 " +
                        "from "+TABLE_NAME_MATS+" as ma;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    public void openDatabase() {
        try {
            db = this.getWritableDatabase();
            this.db = this.getWritableDatabase();
            this.insertMatiere = this.db.compileStatement(INSERTM);
            this.insertNote = this.db.compileStatement(INSERTN);
        }catch(SQLiteException e){}
    }

    public void closeDatabase() {
        db.close();
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            checkDB = null;
        }
        return checkDB != null ? true : false;
    }

    public ArrayList<String> selectMatiere (){
        ArrayList<String> liste = new ArrayList<>();
        try {
            Cursor c = db.rawQuery("SELECT name FROM " + TABLE_NAME_MATS, null);
            if (c.moveToFirst()) {
                do {
                    liste.add(c.getString(0));
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
        }
        return liste;
    }

    public ArrayList<String> selectNote (String matiere,String semestre){
        String _id = getMatiereId(matiere);
        ArrayList<String> liste = new ArrayList<>();
        try {
            Cursor c = db.rawQuery("SELECT note FROM " + TABLE_NAME_NOTE +" where id_mat="+_id+" and semestre="+semestre, null);
            if (c.moveToFirst()) {
                do {
                    liste.add(c.getString(0));
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
        }
        return liste;
    }

    public ArrayList<String> selectMoyenne (String semestre){
        ArrayList<String> liste = new ArrayList<>();
        try {
            Cursor c = db.rawQuery("SELECT name,coef,Moyenne"+semestre+" FROM Moyenne", null);
            if (c.moveToFirst()) {
                do {
                    liste.add(c.getString(0)+"/"+c.getString(1)+"/"+c.getString(2));
                } while (c.moveToNext());
            }
            c.close();
        } catch (SQLiteException e) {
        }
        return liste;
    }
    public String getMoyGenerale(String semestre){

        String moyG ="";
        try {
            Cursor c = db.rawQuery("SELECT sum(moyenne"+semestre+"*coef)/sum(coef) FROM Moyenne", null);
            if(c.moveToFirst()){
                moyG = String.valueOf(c.getDouble(0));
            }
            c.close();
        } catch (SQLiteException e) {
        }
        return moyG;
    }

    public String getMatiereId(String matiere){

        String _id ="";
        try {
            Cursor c = db.rawQuery("SELECT _id FROM "+TABLE_NAME_MATS+ " where name ='"+matiere+"'", null);
            if(c.moveToFirst()){
                    _id = String.valueOf(c.getLong (0));
            }
        c.close();
        } catch (SQLiteException e) {
        }
        return _id;
    }

    public void updateNote(String Note, String oldNote,String matiere){

        String _id = getMatiereId(matiere);

        try {
            Cursor c = db.rawQuery("UPDATE "+TABLE_NAME_NOTE+ " SET note='"+oldNote+"' where id_mat ='"+_id+"' and note='"+Note+"'", null);
            if(c.moveToFirst()){
                _id = String.valueOf(c.getLong (0));
            }
            c.close();
        } catch (SQLiteException e) {
        }
    }

    public void updateMatiere(String oldMatiere,String matiere){

        String _id = getMatiereId(matiere);

        try {
            Cursor c = db.rawQuery("UPDATE "+TABLE_NAME_MATS+ " SET name='"+oldMatiere+"' where _id ='"+_id+"'", null);
            if(c.moveToFirst()){
                _id = String.valueOf(c.getLong (0));
            }
            c.close();
        } catch (SQLiteException e) {
        }
    }

    public boolean  deleteMatiere(String matiere){

        String _id = getMatiereId(matiere);
        try {
            db.delete(TABLE_NAME_NOTE," id_mat ="+_id,null);
            return db.delete(TABLE_NAME_MATS," name ='"+matiere+"'",null)>0 ;
        } catch (SQLiteException e) {
            Log.v("TAG", e.getMessage());
        }
        return false;
    }

    public boolean  deleteNote(String note,String matiere){
        String _id = getMatiereId(matiere);
        try {
            return db.delete(TABLE_NAME_NOTE," id_mat ='"+_id+"' and note='"+note+"'",null)>0;
        } catch (SQLiteException e) {
        }
    return false;
    }
 }
