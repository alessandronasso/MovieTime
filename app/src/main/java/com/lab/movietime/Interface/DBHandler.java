package com.lab.movietime.Interface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler {

    static final String KEY_FILMID = "id_movie";
    static final String KEY_FILMNAME = "movie_name";
    static final String FILM_TABLE = "movies";
    static final String DATABASE_NAME = "TestDB";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATION = "CREATE TABLE movies (id integer primary key autoincrement, id_movie text not null, movie_name text not null);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBHandler (Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREATION);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            onCreate(db);
        }

    }

    public void deleteDB () {
        context.deleteDatabase(DATABASE_NAME);
    }


    public DBHandler open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        DBHelper.close();
    }


    public long insertMovie(String id, String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FILMID, id);
        initialValues.put(KEY_FILMNAME, name);
        return db.insert(FILM_TABLE, null, initialValues);
    }


    public Cursor getMovies()  {
        return db.query(FILM_TABLE, new String[] {KEY_FILMID, KEY_FILMNAME}, null, null, null, null, null);
    }

    public void removeMovie(String id) {
        String sql = "DELETE FROM movies WHERE id_movie="+id;
        db.execSQL(sql);
    }
}
