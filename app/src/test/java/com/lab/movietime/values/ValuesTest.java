package com.lab.movietime.values;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

public class ValuesTest {
    @Mock
    HashMap<String, Integer> INVERSE_MAP;
    @Mock
    HashMap<Integer, String> MAP_NAME;
    @InjectMocks
    Values values;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetBaseURL() throws Exception {
        String result = values.getBaseURL();
        Assert.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    public void testSetBaseURL() throws Exception {
        values.setBaseURL("baseURL");
    }

    @Test
    public void testGetGENRE_BY_NAME() throws Exception {
        String[] result = values.getGENRE_BY_NAME();
        Assert.assertArrayEquals(new String[]{"replaceMeWithExpectedResult"}, result);
    }

    @Test
    public void testSetGENRE_BY_NAME() throws Exception {
        values.setGENRE_BY_NAME(new String[]{"GENRE_BY_NAME"});
    }
}
