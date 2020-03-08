package com.lab.movietime.View.Activity.Activity.Activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lab.movietime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lab.movietime.BuildConfig.URL_POSTER;

public class DetailActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    public static String EXTRA_ID = "extra_id";
    public static String EXTRA_TITLE = "extra_title";
    public static String EXTRA_OVERVIEW = "extra_overview";
    public static String EXTRA_TIME = "extra_time";
    public static String EXTRA_POSTER = "extra_poster";
    public static String EXTRA_LANGUAGE = "extra_language";
    public static String EXTRA_GENRES = "extra_genres";
    public static String IS_FAVORITE = "is_favorite";

    private int currentApiVersion;
    private GestureDetectorCompat mDetector;
    private final int SWIPE_MIN_DISTANCE = 120;
    private final int SWIPE_THRESHOLD_VELOCITY = 200;


    @BindView(R.id.movieTitle) TextView tvTitle;
    @BindView(R.id.overviewTextView) TextView tvOverview;
    @BindView(R.id.durationTextView) TextView tvTime;
    @BindView(R.id.languageTextView) TextView tvLanguage;
    @BindView(R.id.genresTextView) TextView tvGenres;
    @BindView(R.id.posterImg) ImageView imgPoster;
    @BindView(R.id.bookButton) Button btnBook;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    Context context;
//    private FavoriteHelper favoriteHelper;
//    private boolean isFavorite = false;
//    private int favorite;


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        final View view = getWindow().getDecorView().getRootView();

        mDetector = new GestureDetectorCompat(this,this);
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);

        //////// HIDE NAVIGATION BAR ///////////
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
        ////////////////

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        ActionBar ab = getSupportActionBar();
//        assert ab != null;
//        ab.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String overview = getIntent().getStringExtra(EXTRA_OVERVIEW);
        String time = getIntent().getStringExtra(EXTRA_TIME);
        String poster = getIntent().getStringExtra(EXTRA_POSTER);
        String language = getIntent().getStringExtra(EXTRA_LANGUAGE);
        String genres = getIntent().getStringExtra(EXTRA_GENRES);
        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvLanguage.setText(language);
        tvGenres.setText(genres);
//        ab.setTitle(title);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = parser.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM, yyyy");
            String formattedDate = formatter.format(date);
            tvTime.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Glide.with(this).load(URL_POSTER+poster)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(imgPoster);

//        favoriteHelper = new FavoriteHelper(this);
//        favoriteHelper.open();

//        favorite = getIntent().getIntExtra(IS_FAVORITE, 0);
//        if (favorite == 1){
//            isFavorite = true;
//            btnFovorite.setText("Imposta come preferito");
//        }

//        btnFovorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isFavorite){
//                    addFavorit();
//                    Toast.makeText(DetailActivity.this, "Aggiunto ai preferiti!", Toast.LENGTH_LONG).show();
//                }else {
//                    deleteFavorite();
//                    Toast.makeText(DetailActivity.this, "Rimosso dai preferiti!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });


    }

//    @Override
//    public boolean onNavigateUp(){
//        finish();
//        return true;
//    }


//    private void addFavorit(){
//        Favorite favorites = new Favorite();
//        favorites.setTitle(getIntent().getStringExtra(EXTRA_TITLE));
//        favorites.setOverview(getIntent().getStringExtra(EXTRA_OVERVIEW));
//        favorites.setRelease_date(getIntent().getStringExtra(EXTRA_TIME));
//        favorites.setPoster(getIntent().getStringExtra(EXTRA_POSTER));
//        favoriteHelper.insert(favorites);
//
//
//        int widgetIDs[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), ImageBannerWidget.class));
//        for (int id : widgetIDs)
//            AppWidgetManager.getInstance(getApplication()).notifyAppWidgetViewDataChanged(id, R.id.stack_view);
//    }

//    private void deleteFavorite(){
//        favoriteHelper.delete(getIntent().getIntExtra(EXTRA_ID, 0));
//
//        int widgetIDs[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), ImageBannerWidget.class));
//        for (int id : widgetIDs)
//            AppWidgetManager.getInstance(getApplication()).notifyAppWidgetViewDataChanged(id, R.id.stack_view);
//        finish();
//    }
@Override
public boolean onTouchEvent(MotionEvent event){
    if (this.mDetector.onTouchEvent(event)) {
        return true;
    }
    return super.onTouchEvent(event);
}

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        if ((int)event2.getX() - (int)event1.getX() > SWIPE_MIN_DISTANCE &&     Math.abs(velocityX) >  SWIPE_THRESHOLD_VELOCITY) {
            onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        return true;
    }
}


