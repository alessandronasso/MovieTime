package com.lab.movietime.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieTrailerResponseTest {
    @Mock
    ArrayList<MovieTrailer> results;
    @InjectMocks
    MovieTrailerResponse movieTrailerResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetId() throws Exception {
        int result = movieTrailerResponse.getId();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetId() throws Exception {
        movieTrailerResponse.setId(0);
    }

    @Test
    public void testGetResults() throws Exception {
        ArrayList<MovieTrailer> result = movieTrailerResponse.getResults();
        Assert.assertEquals(new ArrayList<MovieTrailer>(Arrays.asList(new MovieTrailer())), result);
    }

    @Test
    public void testSetTrailerResult() throws Exception {
        movieTrailerResponse.setTrailerResult(new ArrayList<MovieTrailer>(Arrays.asList(new MovieTrailer())));
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme