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

public class PopularAdapterTest {
    @Mock
    List<MovieModel> movies;
    @Mock
    Context context;
    @InjectMocks
    PopularAdapter popularAdapter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreateViewHolder() throws Exception {
        PopularAdapter.MovieViewHolder result = popularAdapter.onCreateViewHolder(null, 0);
        Assert.assertEquals(new PopularAdapter.MovieViewHolder(null), result);
    }

    @Test
    public void testOnBindViewHolder() throws Exception {
        popularAdapter.onBindViewHolder(new PopularAdapter.MovieViewHolder(null), 0);
    }

    @Test
    public void testGetItemId() throws Exception {
        long result = popularAdapter.getItemId(0);
        Assert.assertEquals(0L, result);
    }

    @Test
    public void testGetItemViewType() throws Exception {
        int result = popularAdapter.getItemViewType(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetItemCount() throws Exception {
        int result = popularAdapter.getItemCount();
        Assert.assertEquals(0, result);
    }
}