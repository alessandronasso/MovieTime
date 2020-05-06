package com.lab.movietime.Model;

import org.junit.Assert;
import org.junit.Test;

public class MovieTrailerTest {
    MovieTrailer movieTrailer = new MovieTrailer();

    @Test
    public void testGetId() throws Exception {
        String result = movieTrailer.getId();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetId() throws Exception {
        movieTrailer.setId("id");
    }

    @Test
    public void testGetOriginalLanguage() throws Exception {
        String result = movieTrailer.getOriginalLanguage();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetOriginalLanguage() throws Exception {
        movieTrailer.setOriginalLanguage("originalLanguage");
    }

    @Test
    public void testGetCountryOfOrigin() throws Exception {
        String result = movieTrailer.getCountryOfOrigin();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetCountryOfOrigin() throws Exception {
        movieTrailer.setCountryOfOrigin("countryOfOrigin");
    }

    @Test
    public void testGetKey() throws Exception {
        String result = movieTrailer.getKey();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetKey() throws Exception {
        movieTrailer.setKey("key");
    }

    @Test
    public void testGetName() throws Exception {
        String result = movieTrailer.getName();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetName() throws Exception {
        movieTrailer.setName("name");
    }

    @Test
    public void testGetSite() throws Exception {
        String result = movieTrailer.getSite();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetSite() throws Exception {
        movieTrailer.setSite("site");
    }

    @Test
    public void testGetSize() throws Exception {
        int result = movieTrailer.getSize();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSetSize() throws Exception {
        movieTrailer.setSize(0);
    }

    @Test
    public void testGetType() throws Exception {
        String result = movieTrailer.getType();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetType() throws Exception {
        movieTrailer.setType("type");
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme