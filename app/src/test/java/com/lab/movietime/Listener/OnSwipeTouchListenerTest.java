package com.lab.movietime.Listener;

import android.view.GestureDetector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OnSwipeTouchListenerTest {
    @Mock
    GestureDetector gestureDetector;
    @InjectMocks
    OnSwipeTouchListener onSwipeTouchListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnTouch() throws Exception {
        boolean result = onSwipeTouchListener.onTouch(null, null);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testOnSwipeRight() throws Exception {
        onSwipeTouchListener.onSwipeRight();
    }

    @Test
    public void testOnSwipeLeft() throws Exception {
        onSwipeTouchListener.onSwipeLeft();
    }

    @Test
    public void testOnSwipeTop() throws Exception {
        onSwipeTouchListener.onSwipeTop();
    }

    @Test
    public void testOnSwipeBottom() throws Exception {
        onSwipeTouchListener.onSwipeBottom();
    }
}