package com.suiken.moyenne.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.suiken.moyenne.database.Database;

/**
 * Created by Suiken on 23/02/15.
 */
public abstract class DatabaseDAO {

    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut
    protected final static int VERSION = 1;

    // Le nom du fichier qui représente ma base
    protected final static String NOM = "database.db";

    protected SQLiteDatabase databaseSqlite = null;
    protected Database database = null;

    public DatabaseDAO(Context pContext) {
        this.database = new Database(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        databaseSqlite = database.getWritableDatabase();
        return databaseSqlite;
    }

    public void close() {
        databaseSqlite.close();
    }

    public SQLiteDatabase getDb() {
        return databaseSqlite;
    }
}