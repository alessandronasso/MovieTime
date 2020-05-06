package com.lab.movietime.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class MovieResponseTest {
    @Mock
    List<MovieModel> results;
    @InjectMocks
    MovieResponse movieResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPage() throws Exception {
        int result = movieResponse.getPage();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetPage() throws Exception {
        movieResponse.setPage(0);
    }

    @Test
    public void testGetResults() throws Exception {
        List<MovieModel> result = movieResponse.getResults();
        Assert.assertEquals(Arrays.<MovieModel>asList(new MovieModel("posterPath", true, "overview", "releaseDate", Arrays.<Integer>asList(Integer.valueOf(0)), 0, "originalTitle", "originalLanguage", "title", "backdropPath", 0d, 0, true, 0d)), result);
    }

    @Test
    public void testSetResults() throws Exception {
        movieResponse.setResults(Arrays.<MovieModel>asList(new MovieModel("posterPath", true, "overview", "releaseDate", Arrays.<Integer>asList(Integer.valueOf(0)), 0, "originalTitle", "originalLanguage", "title", "backdropPath", 0d, 0, true, 0d)));
    }

    @Test
    public void testGetTotalResults() throws Exception {
        int result = movieResponse.getTotalResults();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetTotalResults() throws Exception {
        movieResponse.setTotalResults(0);
    }

    @Test
    public void testGetTotalPages() throws Exception {
        int result = movieResponse.getTotalPages();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetTotalPages() throws Exception {
        movieResponse.setTotalPages(0);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme