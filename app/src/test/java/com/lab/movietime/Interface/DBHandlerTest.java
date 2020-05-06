package com.lab.movietime.Interface;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DBHandlerTest {
    @Mock
    Context context;
    @InjectMocks
    DBHandler dBHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDBHelper() throws Exception {
        DBHandler.DatabaseHelper result = dBHandler.getDBHelper();
        Assert.assertEquals(new DBHandler.DatabaseHelper(null), result);
    }

    @Test
    public void testSetDBHelper() throws Exception {
        dBHandler.setDBHelper(new DBHandler.DatabaseHelper(null));
    }

    @Test
    public void testGetDb() throws Exception {
        SQLiteDatabase result = dBHandler.getDb();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSetDb() throws Exception {
        dBHandler.setDb(null);
    }

    @Test
    public void testDeleteDB() throws Exception {
        dBHandler.deleteDB();
    }

    @Test
    public void testOpen() throws Exception {
        DBHandler result = dBHandler.open();
        Assert.assertEquals(new DBHandler(null), result);
    }

    @Test
    public void testClose() throws Exception {
        dBHandler.close();
    }

    @Test
    public void testInsertMovie() throws Exception {
        long result = dBHandler.insertMovie("id", "name");
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetMovies() throws Exception {
        Cursor result = dBHandler.getMovies();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testRemoveMovie() throws Exception {
        dBHandler.removeMovie("id");
    }

    @Test
    public void testGetContext() throws Exception {
        Context result = dBHandler.getContext();
        Assert.assertEquals(null, result);
    }
}