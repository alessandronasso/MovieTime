package com.lab.movietime.View.Activity.Activity.Fragment;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lab.movietime.Model.MovieModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

public class HomeFragmentTest {
    @Mock
    HashMap<Integer, String> trailerMap;
    @Mock
    SwipeRefreshLayout mPullToRefresh;
    @Mock
    List<List<MovieModel>> movieList;
    @Mock
    List<Integer> randomGenre;
    @Mock
    Object USE_DEFAULT_TRANSITION;
    @Mock
    SparseArray<Parcelable> mSavedViewState;
    @Mock
    Fragment mTarget;
    @Mock
    FragmentHostCallback mHost;
    @Mock
    Fragment mParentFragment;
    @Mock
    ViewGroup mContainer;
    @Mock
    View mView;
    @Mock
    View mInnerView;
    @Mock
    Runnable mPostponedDurationRunnable;
    @Mock
    LayoutInflater mLayoutInflater;
    //Field mMaxState of type State - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    @Mock
    MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData;
    @InjectMocks
    HomeFragment homeFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMPullToRefresh() throws Exception {
        SwipeRefreshLayout result = homeFragment.getMPullToRefresh();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSetMPullToRefresh() throws Exception {
        homeFragment.setMPullToRefresh(null);
    }

    @Test
    public void testOnCreate() throws Exception {
        homeFragment.onCreate(null);
    }

    @Test
    public void testOnCreateView() throws Exception {
        View result = homeFragment.onCreateView(null, null, null);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testIsNetworkAvailable() throws Exception {
        boolean result = homeFragment.isNetworkAvailable();
        Assert.assertEquals(true, result);
    }
}