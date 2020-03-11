package com.lab.movietime.View.Activity.Activity.Activity;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lab.movietime.Interface.DBHandler;
import com.lab.movietime.Listener.OnSwipeTouchListener;
import com.lab.movietime.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lab.movietime.BuildConfig.URL_POSTER;

public class DetailActivity extends AppCompatActivity {
    public static String EXTRA_ID = "extra_id";
    public static String EXTRA_TITLE = "extra_title";
    public static String EXTRA_OVERVIEW = "extra_overview";
    public static String EXTRA_TIME = "extra_time";
    public static String EXTRA_POSTER = "extra_poster";
    public static String EXTRA_LANGUAGE = "extra_language";
    public static String EXTRA_GENRES = "extra_genres";
    public static String EXTRA_VOTE = "extra_vote";
    public static String EXTRA_YTLINK = "extra_ytlink";
    public static String IS_FAVORITE = "is_favorite";

    private int currentApiVersion;


    @BindView(R.id.movieTitle) TextView tvTitle;
    @BindView(R.id.overviewTextView) TextView tvOverview;
    //@BindView(R.id.durationTextView) TextView tvTime;
    //@BindView(R.id.languageTextView) TextView tvLanguage;
    @BindView(R.id.genresTextView) TextView tvGenres;
    @BindView(R.id.posterImg) ImageView imgPoster;
    @BindView(R.id.favButton) Button btnFav;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.listitemrating) RatingBar ratingBar;
    @BindView(R.id.youtube_player_view) YouTubePlayerView youTubePlayerView;

    DBHandler db = new DBHandler(this);


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        addGestures();
        hideNavigationBar();

        ButterKnife.bind(this);
        int id = getIntent().getIntExtra(EXTRA_ID, 0);
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        String overview = getIntent().getStringExtra(EXTRA_OVERVIEW);
        String time = getIntent().getStringExtra(EXTRA_TIME);
        String poster = getIntent().getStringExtra(EXTRA_POSTER);
        String language = getIntent().getStringExtra(EXTRA_LANGUAGE);
        String genres = getIntent().getStringExtra(EXTRA_GENRES);
        double vote = getIntent().getDoubleExtra(EXTRA_VOTE, 0);
        final boolean[] isFav = {false};

        db.open();
        Cursor c = db.getMovies();
        if (c.moveToFirst()) {
            do {
                if (c.getString(0).equals(Integer.toString(id))) {
                    isFav[0] = true;
                    btnFav.setText("Remove from watchlist");
                }
            } while (c.moveToNext());
        }
        db.close();

        tvTitle.setText(title);
        tvOverview.setText(overview);
        //tvLanguage.setText(language);
        tvGenres.setText(genres);
        //ab.setTitle(title);
        setRatingBar(vote);

        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(getIntent().getStringExtra(EXTRA_YTLINK), 0);
                youTubePlayer.pause();
            }
        });

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = parser.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM, yyyy");
            String formattedDate = formatter.format(date);
            //tvTime.setText(formattedDate);
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

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                if (!isFav[0]){
                    db.insertMovie(Integer.toString(id), title);
                    isFav[0] = true;
                    btnFav.setText("Remove from watchlist");
                } else {
                    db.removeMovie(Integer.toString(id));
                    isFav[0] = false;
                    btnFav.setText("add to watchlist");
                }
                db.close();
            }
        });
    }

    private void addGestures () {
        View myView = findViewById(R.id.detail_activity);
        myView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                //Called when swiping top on this view
            }
            public void onSwipeRight() {
                onBackPressed();
            }
            public void onSwipeLeft() {
                //Called when swiping left on this view
            }
            public void onSwipeBottom() {
                onBackPressed();
            }

        });
    }

    private void hideNavigationBar() {
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
    }

    private void setRatingBar (double vote) {
        ratingBar.setStepSize((float) 0.25);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating((float) vote);
    }
}


