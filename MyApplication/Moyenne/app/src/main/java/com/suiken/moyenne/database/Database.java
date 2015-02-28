package com.suiken.moyenne.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suiken on 22/02/15.
 */
public class Database extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "moyenne.db";
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_MATIERE = "matieres";
    public static final String TABLE_NOTE = "notes";

//    Création de la base de données
    public static final String CREATION_TABLE_MATIERE =
            "Create table " + TABLE_MATIERE + "(" +
            "id integer primary key autoincrement, " +
            "libelle text not null, " +
            "coefficient integer not null, " +
            "semestre integer not null );";
    public static final String CREATION_TABLE_NOTE =
            "Create table " + TABLE_NOTE + "(" +
            "id integer primary key autoincrement, " +
            "note real, " +
            "id_matiere integer not null, " +
            "foreign key(id_matiere) references matieres(id));";

//    Destruction de la base de données
    public static final String DROP_DATABASE =
            "Drop table id exists " + TABLE_NOTE + ";" +
            "Drop table if exists " + TABLE_MATIERE + ";";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATION_TABLE_MATIERE);
        db.execSQL(CREATION_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_DATABASE);
        onCreate(db);
    }
}
