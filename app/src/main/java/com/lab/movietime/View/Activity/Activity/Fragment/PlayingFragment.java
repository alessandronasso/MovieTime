package com.lab.movietime.View.Activity.Activity.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.movietime.Adapter.PopularAdapter;
import com.lab.movietime.BuildConfig;
import com.lab.movietime.Interface.ApiBuilder;
import com.lab.movietime.Interface.ApiService;
import com.lab.movietime.Model.MovieModel;
import com.lab.movietime.Model.MovieResponse;
import com.lab.movietime.R;
import com.lab.movietime.View.Activity.Activity.Activity.DetailActivity;
import com.lab.movietime.values.Values;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayingFragment extends Fragment {

    @BindView(R.id.rc_view)
    RecyclerView recyclerView;

    public PlayingFragment() {
        // Required empty public constructor
    }

    public static PlayingFragment newInstance(String param1, String param2) {
        PlayingFragment fragment = new PlayingFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.playing_fragment, container, false);
        recyclerView = view.findViewById(R.id.playing_rc_view);

        loadMovie();

        return view;
    }


    private void loadMovie() {
        ApiService apiService = ApiBuilder.getClient(getContext()).create(ApiService.class);
//        final RecyclerView recyclerView = findViewById(R.id.rc_view);
//        final RecyclerView recyclerView2 = findViewById(R.id.rc_view2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        Call<MovieResponse> call = apiService.getPopular(Values.CATEGORY[1], BuildConfig.API_KEY,Values.LANGUAGE,Values.PAGE[0]);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                final List<MovieModel> movies = response.body().getResults();
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
                            Intent i = new Intent(getContext(), DetailActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra(DetailActivity.EXTRA_TITLE, movies.get(position).getTitle());
                            i.putExtra(DetailActivity.EXTRA_OVERVIEW, movies.get(position).getOverview());
                            i.putExtra(DetailActivity.EXTRA_TIME, movies.get(position).getReleaseDate());
                            i.putExtra(DetailActivity.EXTRA_POSTER, movies.get(position).getPosterPath());
                            i.putExtra(DetailActivity.EXTRA_LANGUAGE, movies.get(position).getOriginalLanguage());
                            try{
                                List<Integer> genr = movies.get(position).getGenreIds();
                                i.putExtra(DetailActivity.EXTRA_GENRES, genr.toString());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            getContext().startActivity(i);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });
                if (movies != null ){

                    MovieModel firstMovie = movies.get(0);
                    if(firstMovie != null) {
                        Log.i("TAG","#Log "+firstMovie.getTitle());
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse>call, Throwable t) {
                // Log error here since request failed

                Log.i("TAG","#Log "+t);

            }
        });
    }

}
