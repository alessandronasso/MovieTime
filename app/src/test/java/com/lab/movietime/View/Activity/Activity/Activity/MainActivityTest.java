package com.lab.movietime.View.Activity.Activity.Activity;

import android.content.BroadcastReceiver;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.SimpleArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.FragmentController;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lab.movietime.View.Activity.Activity.Fragment.HomeFragment;
import com.lab.movietime.View.Activity.Activity.Fragment.NetworkNotAvailable;
import com.lab.movietime.View.Activity.Activity.Fragment.PlayingFragment;
import com.lab.movietime.View.Activity.Activity.Fragment.PopularFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MainActivityTest {
    @Mock
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    @Mock
    BroadcastReceiver broadcastReceiver;
    @Mock
    AppCompatDelegate mDelegate;
    @Mock
    Resources mResources;
    @Mock
    FragmentController mFragments;
    @Mock
    LifecycleRegistry mFragmentLifecycleRegistry;
    @Mock
    SparseArrayCompat<String> mPendingFragmentActivityResults;
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    //Field mSavedStateRegistryController of type SavedStateRegistryController - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    ViewModelStore mViewModelStore;
    //Field mOnBackPressedDispatcher of type OnBackPressedDispatcher - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    SimpleArrayMap<Class<? extends ComponentActivity.ExtraData>, ComponentActivity.ExtraData> mExtraDataMap;
    @InjectMocks
    MainActivity mainActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetHomeFragment() throws Exception {
        HomeFragment result = mainActivity.getHomeFragment();
        Assert.assertEquals(new HomeFragment(), result);
    }

    @Test
    public void testSetHomeFragment() throws Exception {
        mainActivity.setHomeFragment(new HomeFragment());
    }

    @Test
    public void testGetPopularFragment() throws Exception {
        PopularFragment result = mainActivity.getPopularFragment();
        Assert.assertEquals(new PopularFragment(), result);
    }

    @Test
    public void testSetPopularFragment() throws Exception {
        mainActivity.setPopularFragment(new PopularFragment());
    }

    @Test
    public void testGetPlayingFragment() throws Exception {
        PlayingFragment result = mainActivity.getPlayingFragment();
        Assert.assertEquals(new PlayingFragment(), result);
    }

    @Test
    public void testSetPlayingFragment() throws Exception {
        mainActivity.setPlayingFragment(new PlayingFragment());
    }

    @Test
    public void testGetNetworkFragment() throws Exception {
        NetworkNotAvailable result = mainActivity.getNetworkFragment();
        Assert.assertEquals(new NetworkNotAvailable(), result);
    }

    @Test
    public void testSetNetworkFragment() throws Exception {
        mainActivity.setNetworkFragment(new NetworkNotAvailable());
    }

    @Test
    public void testOnCreate() throws Exception {
        mainActivity.onCreate(null);
    }

    @Test
    public void testOnWindowFocusChanged() throws Exception {
        mainActivity.onWindowFocusChanged(true);
    }

}