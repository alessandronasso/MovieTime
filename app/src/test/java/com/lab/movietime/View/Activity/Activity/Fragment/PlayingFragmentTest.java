package com.lab.movietime.View.Activity.Activity.Fragment;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.movietime.Model.MovieModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

public class PlayingFragmentTest {
    @Mock
    RecyclerView recyclerView;
    @Mock
    TextView tvNoData;
    @Mock
    List<MovieModel> movies;
    @Mock
    HashMap<Integer, String> trailerMap;
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
    PlayingFragment playingFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreate() throws Exception {
        playingFragment.onCreate(null);
    }

    @Test
    public void testOnResume() throws Exception {
        playingFragment.onResume();
    }

    @Test
    public void testOnCreateView() throws Exception {
        View result = playingFragment.onCreateView(null, null, null);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testIsNetworkAvailable() throws Exception {
        boolean result = playingFragment.isNetworkAvailable();
        Assert.assertEquals(true, result);
    }
}