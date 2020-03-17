package com.lab.movietime.Interface

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(val context: Context) {
    var DBHelper: DatabaseHelper
    var db: SQLiteDatabase? = null

    class DatabaseHelper internal constructor(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            try {
                db.execSQL(DATABASE_CREATION)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onCreate(db)
        }
    }

    fun deleteDB() {
        context.deleteDatabase(DATABASE_NAME)
    }

    @Throws(SQLException::class)
    fun open(): DBHandler {
        db = DBHelper.writableDatabase
        return this
    }

    fun close() {
        DBHelper.close()
    }

    fun insertMovie(id: String?, name: String?): Long {
        val initialValues = ContentValues()
        initialValues.put(KEY_FILMID, id)
        initialValues.put(KEY_FILMNAME, name)
        return db!!.insert(FILM_TABLE, null, initialValues)
    }

    val movies: Cursor
        get() = db!!.query(FILM_TABLE, arrayOf(KEY_FILMID, KEY_FILMNAME), null, null, null, null, null)

    fun removeMovie(id: String) {
        val sql = "DELETE FROM movies WHERE id_movie=$id"
        db!!.execSQL(sql)
    }

    companion object {
        const val KEY_FILMID = "id_movie"
        const val KEY_FILMNAME = "movie_name"
        const val FILM_TABLE = "movies"
        const val DATABASE_NAME = "TestDB"
        const val DATABASE_VERSION = 1
        const val DATABASE_CREATION = "CREATE TABLE movies (id integer primary key autoincrement, id_movie text not null, movie_name text not null);"
    }

    init {
        DBHelper = DatabaseHelper(context)
    }
}