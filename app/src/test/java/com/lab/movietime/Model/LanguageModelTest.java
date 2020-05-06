package com.lab.movietime.Model;

import org.junit.Assert;
import org.junit.Test;

public class LanguageModelTest {
    LanguageModel languageModel = new LanguageModel();

    @Test
    public void testGetId() throws Exception {
        String result = languageModel.getId();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetId() throws Exception {
        languageModel.setId("id");
    }

    @Test
    public void testGetOriginalLanguage() throws Exception {
        String result = languageModel.getOriginalLanguage();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetOriginalLanguage() throws Exception {
        languageModel.setOriginalLanguage("originalLanguage");
    }
}