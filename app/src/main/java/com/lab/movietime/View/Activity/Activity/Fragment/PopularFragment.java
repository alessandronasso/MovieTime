package com.lab.movietime.View.Activity.Activity.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.movietime.Adapter.PopularAdapter;
import com.lab.movietime.BuildConfig;
import com.lab.movietime.Interface.ApiBuilder;
import com.lab.movietime.Interface.ApiService;
import com.lab.movietime.Model.MovieModel;
import com.lab.movietime.Model.MovieResponse;
import com.lab.movietime.Model.MovieTrailer;
import com.lab.movietime.Model.MovieTrailerResponse;
import com.lab.movietime.R;
import com.lab.movietime.View.Activity.Activity.Activity.DetailActivity;
import com.lab.movietime.values.Values;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularFragment extends Fragment {

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private HashMap<Integer, String> trailerMap = new HashMap<>();
    private List<MovieModel> movies = new ArrayList<>();

    @BindView(R.id.rc_view)
    RecyclerView recyclerView;

    public PopularFragment() {
        // Required empty public constructor
    }

    public static PopularFragment newInstance(String param1, String param2) {
        PopularFragment fragment = new PopularFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popular_fragment, container, false);
        recyclerView = view.findViewById(R.id.popular_rc_view);

        mSearchField = (EditText) view.findViewById(R.id.search_field);
        mSearchBtn = (ImageButton) view.findViewById(R.id.search_btn);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMovie(mSearchField.getText().toString());
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        loadMovie();
        return view;
    }




    @Override
    public void onPause() {
        super.onPause();
    }

    private void loadMovie() {
        ApiService apiService = ApiBuilder.getClient(getContext()).create(ApiService.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        Call<MovieResponse> call = apiService.getPopular(Values.CATEGORY[1], BuildConfig.API_KEY,Values.LANGUAGE,Values.PAGE[0]);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                movies = response.body().getResults();
                for (int i = 0; i<movies.size(); i++) {
                    Call<MovieTrailerResponse> callT = apiService.getMovieTrailer(movies.get(i).getId(), BuildConfig.API_KEY);
                    callT.enqueue(new Callback<MovieTrailerResponse>() {
                        @Override
                        public void onResponse(Call<MovieTrailerResponse>call2, Response<MovieTrailerResponse> response2) {
                            List<MovieTrailer> mt = response2.body().getResults();
                            Integer mtID = response2.body().getId();
                            if (mt.isEmpty()) trailerMap.put(mtID, "S0Q4gqBUs7c");
                            else trailerMap.put(mtID, mt.get(0).getKey());
                        }

                        @Override
                        public void onFailure(Call<MovieTrailerResponse>call2, Throwable t) { }
                    });

                }
                recyclerView.setAdapter(new PopularAdapter(movies, R.layout.content_main, getContext()));
                recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                        public boolean onSingleTapUp(MotionEvent e){
                            return true;
                        }
                    });

                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        View child = rv.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && gestureDetector.onTouchEvent(e)){
                            int position = rv.getChildAdapterPosition(child);
                            Log.i("AH","POSITION: "+position+" MOVIE: "+movies.get(position).getTitle());
                            Intent i = new Intent(getContext(), DetailActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra(DetailActivity.EXTRA_TITLE, movies.get(position).getTitle());
                            i.putExtra(DetailActivity.EXTRA_OVERVIEW, movies.get(position).getOverview());
                            i.putExtra(DetailActivity.EXTRA_TIME, movies.get(position).getReleaseDate());
                            i.putExtra(DetailActivity.EXTRA_POSTER, movies.get(position).getPosterPath());
                            i.putExtra(DetailActivity.EXTRA_LANGUAGE, movies.get(position).getOriginalLanguage());
                            i.putExtra(DetailActivity.EXTRA_GENRES, movies.get(position).getGenre());
                            i.putExtra(DetailActivity.EXTRA_VOTE, movies.get(position).getVoteAverage());
                            i.putExtra(DetailActivity.EXTRA_YTLINK, trailerMap.get(movies.get(position).getId()));
                            getContext().startActivity(i);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) { }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) { }
                });
            }

            @Override
            public void onFailure(Call<MovieResponse>call, Throwable t) { }
        });
    }

    private void searchMovie(String movieName) {
        ApiService apiService = ApiBuilder.getClient(getContext()).create(ApiService.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        Call<MovieResponse> call = apiService.getItemSearch(movieName);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                movies = response.body().getResults();
                for (int i = 0; i<movies.size(); i++) {
                    Call<MovieTrailerResponse> callT = apiService.getMovieTrailer(movies.get(i).getId(), BuildConfig.API_KEY);
                    callT.enqueue(new Callback<MovieTrailerResponse>() {
                        @Override
                        public void onResponse(Call<MovieTrailerResponse>call2, Response<MovieTrailerResponse> response2) {
                            List<MovieTrailer> mt = response2.body().getResults();
                            Integer mtID = response2.body().getId();
                            if (mt.isEmpty()) trailerMap.put(mtID, "S0Q4gqBUs7c");
                            else trailerMap.put(mtID, mt.get(0).getKey());
                        }

                        @Override
                        public void onFailure(Call<MovieTrailerResponse>call2, Throwable t) { }
                    });

                }
                recyclerView.setAdapter(new PopularAdapter(movies, R.layout.content_main, getContext()));
            }

            @Override
            public void onFailure(Call<MovieResponse>call, Throwable t) { }
        });
    }
}
