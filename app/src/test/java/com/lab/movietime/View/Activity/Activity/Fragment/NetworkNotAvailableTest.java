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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NetworkNotAvailableTest {
    @Mock
    SwipeRefreshLayout mPullToRefresh;
    @Mock
    Object USE_DEFAULT_TRANSITION;
    //Field mSavedFragmentState of type Bundle - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    SparseArray<Parcelable> mSavedViewState;
    //Field mArguments of type Bundle - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    Fragment mTarget;
    //Field mFragmentManager of type FragmentManagerImpl - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    FragmentHostCallback mHost;
    //Field mChildFragmentManager of type FragmentManagerImpl - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
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
    //Field mSavedStateRegistryController of type SavedStateRegistryController - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @InjectMocks
    NetworkNotAvailable networkNotAvailable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetMPullToRefresh() throws Exception {
        SwipeRefreshLayout result = networkNotAvailable.getMPullToRefresh();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSetMPullToRefresh() throws Exception {
        networkNotAvailable.setMPullToRefresh(null);
    }

    @Test
    public void testOnCreate() throws Exception {
        networkNotAvailable.onCreate(null);
    }

    @Test
    public void testOnCreateView() throws Exception {
        View result = networkNotAvailable.onCreateView(null, null, null);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testIsNetworkAvailable() throws Exception {
        boolean result = networkNotAvailable.isNetworkAvailable();
        Assert.assertEquals(false, result);
    }
}