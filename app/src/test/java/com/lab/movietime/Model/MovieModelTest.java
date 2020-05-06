package com.lab.movietime.Model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class MovieModelTest {
    @Mock
    List<Integer> genreIds;
    @InjectMocks
    MovieModel movieModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetGenreIds() throws Exception {
        List<Integer> result = movieModel.getGenreIds();
        Assert.assertEquals(Arrays.<Integer>asList(Integer.valueOf(0)), result);
    }

    @Test
    public void testSetGenreIds() throws Exception {
        movieModel.setGenreIds(Arrays.<Integer>asList(Integer.valueOf(0)));
    }

    @Test
    public void testGetId() throws Exception {
        int result = movieModel.getId();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetId() throws Exception {
        movieModel.setId(0);
    }

    @Test
    public void testGetOriginalTitle() throws Exception {
        String result = movieModel.getOriginalTitle();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetOriginalTitle() throws Exception {
        movieModel.setOriginalTitle("originalTitle");
    }

    @Test
    public void testGetOriginalLanguage() throws Exception {
        String result = movieModel.getOriginalLanguage();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetOriginalLanguage() throws Exception {
        movieModel.setOriginalLanguage("originalLanguage");
    }

    @Test
    public void testGetTitle() throws Exception {
        String result = movieModel.getTitle();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetTitle() throws Exception {
        movieModel.setTitle("title");
    }

    @Test
    public void testGetBackdropPath() throws Exception {
        String result = movieModel.getBackdropPath();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetBackdropPath() throws Exception {
        movieModel.setBackdropPath("backdropPath");
    }

    @Test
    public void testGetPopularity() throws Exception {
        double result = movieModel.getPopularity();
        Assert.assertEquals(0d, result);
    }

    @Test
    public void testSetPopularity() throws Exception {
        movieModel.setPopularity(0d);
    }

    @Test
    public void testGetVoteCount() throws Exception {
        int result = movieModel.getVoteCount();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetVoteCount() throws Exception {
        movieModel.setVoteCount(0);
    }

    @Test
    public void testGetVideo() throws Exception {
        boolean result = movieModel.getVideo();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testSetVideo() throws Exception {
        movieModel.setVideo(true);
    }

    @Test
    public void testGetLinkTrailer() throws Exception {
        String result = movieModel.getLinkTrailer();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetLinkTrailer() throws Exception {
        movieModel.setLinkTrailer("linkTrailer");
    }

    @Test
    public void testGetVoteAverage() throws Exception {
        double result = movieModel.getVoteAverage();
        Assert.assertEquals(0d, result);
    }

    @Test
    public void testSetVoteAverage() throws Exception {
        movieModel.setVoteAverage(0d);
    }

    @Test
    public void testGetGenre() throws Exception {
        String result = movieModel.getGenre();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testGetPosterPath() throws Exception {
        String result = movieModel.getPosterPath();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetPosterPath() throws Exception {
        movieModel.setPosterPath("posterPath");
    }

    @Test
    public void testIsAdult() throws Exception {
        boolean result = movieModel.isAdult();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testSetAdult() throws Exception {
        movieModel.setAdult(true);
    }

    @Test
    public void testGetOverview() throws Exception {
        String result = movieModel.getOverview();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetOverview() throws Exception {
        movieModel.setOverview("overview");
    }

    @Test
    public void testGetReleaseDate() throws Exception {
        String result = movieModel.getReleaseDate();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetReleaseDate() throws Exception {
        movieModel.setReleaseDate("releaseDate");
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme