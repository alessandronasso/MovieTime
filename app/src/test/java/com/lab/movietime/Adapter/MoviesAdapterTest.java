package com.lab.movietime.Adapter;

import android.content.Context;

import com.lab.movietime.Model.MovieModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class MoviesAdapterTest {
    @Mock
    List<MovieModel> movies;
    @Mock
    Context context;
    @InjectMocks
    MoviesAdapter moviesAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreateViewHolder() throws Exception {
        MoviesAdapter.MovieViewHolder result = moviesAdapter.onCreateViewHolder(null, 0);
        Assert.assertEquals(new MoviesAdapter.MovieViewHolder(null), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        moviesAdapter.onBindViewHolder(new MoviesAdapter.MovieViewHolder(null), 0);
    }

    @Test
    public void testGetItemCount() throws Exception {
        int result = moviesAdapter.getItemCount();
        Assert.assertEquals(0, result);
    }
}
