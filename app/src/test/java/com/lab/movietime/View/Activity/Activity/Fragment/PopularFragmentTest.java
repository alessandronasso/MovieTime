package com.lab.movietime.View.Activity.Activity.Fragment;

import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lab.movietime.Model.MovieModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

public class PopularFragmentTest {
    @Mock
    AutoCompleteTextView mSearchField;
    @Mock
    ImageButton mSearchBtn;
    @Mock
    HashMap<Integer, String> trailerMap;
    @Mock
    List<MovieModel> movies;
    @Mock
    List<MovieModel> moviesCopy;
    @Mock
    RecyclerView recyclerView;
    @Mock
    FloatingActionButton fab;
    @Mock
    FloatingActionButton fabitem1;
    @Mock
    FloatingActionButton fabitem2;
    @Mock
    FloatingActionButton fabitem3;
    @Mock
    CheckBox checkbox;
    @Mock
    LinearLayout hiddenTab;
    @Mock
    Spinner spinnerGenre;
    @Mock
    Spinner spinnerVote;
    @Mock
    Spinner spinnerOperator;
    @Mock
    EditText releaseYear;
    @Mock
    TextView tvNoData;
    @Mock
    Animation fab_open;
    @Mock
    Animation fab_close;
    @Mock
    Animation rotate_forward;
    @Mock
    Animation rotate_backward;
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
    @Mock
    LifecycleRegistry mLifecycleRegistry;
    @Mock
    MutableLiveData<LifecycleOwner> mViewLifecycleOwnerLiveData;
    //Field mSavedStateRegistryController of type SavedStateRegistryController - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @InjectMocks
    PopularFragment popularFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetFab_open() throws Exception {
        Animation result = popularFragment.getFab_open();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSetFab_open() throws Exception {
        popularFragment.setFab_open(null);
    }

    @Test
    public void testGetFab_close() throws Exception {
        Animation result = popularFragment.getFab_close();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSetFab_close() throws Exception {
        popularFragment.setFab_close(null);
    }

    @Test
    public void testGetRotate_forward() throws Exception {
        Animation result = popularFragment.getRotate_forward();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSetRotate_forward() throws Exception {
        popularFragment.setRotate_forward(null);
    }

    @Test
    public void testGetRotate_backward() throws Exception {
        Animation result = popularFragment.getRotate_backward();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testSetRotate_backward() throws Exception {
        popularFragment.setRotate_backward(null);
    }

    @Test
    public void testGetNoMoviesAvailable() throws Exception {
        boolean result = popularFragment.getNoMoviesAvailable();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testSetNoMoviesAvailable() throws Exception {
        popularFragment.setNoMoviesAvailable(true);
    }

    @Test
    public void testOnCreateView() throws Exception {
        View result = popularFragment.onCreateView(null, null, null);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testIsNetworkAvailable() throws Exception {
        boolean result = popularFragment.isNetworkAvailable();
        Assert.assertEquals(true, result);
    }
}
