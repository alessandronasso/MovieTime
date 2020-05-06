package com.lab.movietime.View.Activity.Activity.Activity;

import android.content.res.Resources;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.collection.SimpleArrayMap;
import androidx.collection.SparseArrayCompat;
import androidx.core.app.ComponentActivity;
import androidx.fragment.app.FragmentController;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.ViewModelStore;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.movietime.Interface.ApiService;
import com.lab.movietime.Interface.DBHandler;
import com.lab.movietime.Model.MovieModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;

public class DetailActivityTest {
    @Mock
    HashMap<String, String> mapLang;
    @Mock
    List<MovieModel> movies;
    @Mock
    HashMap<Integer, String> trailerMap;
    @Mock
    ApiService apiService;
    @Mock
    RecyclerView recyclerView;
    @Mock
    TextView tvTitle;
    @Mock
    TextView tvOverview;
    @Mock
    TextView tvLanguage;
    @Mock
    TextView tvReleaseDate;
    @Mock
    TextView tvGenres;
    @Mock
    ImageView imgPoster;
    @Mock
    Button btnFav;
    @Mock
    ProgressBar progressBar;
    @Mock
    RatingBar ratingBar;
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
    @Mock
    SimpleArrayMap<Class<? extends ComponentActivity.ExtraData>, ComponentActivity.ExtraData> mExtraDataMap;

    @InjectMocks
    DetailActivity detailActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetApiService() throws Exception {
        ApiService result = detailActivity.getApiService();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testGetDb() throws Exception {
        DBHandler result = detailActivity.getDb();
        Assert.assertEquals(new DBHandler(null), result);
    }

    @Test
    public void testSetDb() throws Exception {
        detailActivity.setDb(new DBHandler(null));
    }

    @Test
    public void testOnBackPressed() throws Exception {
        detailActivity.onBackPressed();
    }

    @Test
    public void testOnCreate() throws Exception {
        detailActivity.onCreate(null);
    }

    @Test
    public void testIsNetworkAvailable() throws Exception {
        boolean result = detailActivity.isNetworkAvailable();
        Assert.assertEquals(true, result);
    }
}