package com.lab.movietime.View.Activity.Activity.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
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
import com.lab.movietime.Interface.DBHandler;
import com.lab.movietime.Model.MovieModel;
import com.lab.movietime.Model.MovieResponse;
import com.lab.movietime.Model.MovieTrailer;
import com.lab.movietime.Model.MovieTrailerResponse;
import com.lab.movietime.R;
import com.lab.movietime.View.Activity.Activity.Activity.DetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayingFragment extends Fragment {

    @BindView(R.id.rc_view)
    RecyclerView recyclerView;

    private List<MovieModel> movies = new ArrayList<>();
    private HashMap<Integer, String> trailerMap = new HashMap<>();

    public PlayingFragment() { }

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
    public void onResume() {
        super.onResume();
        if (movies.size()!=0) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PlayingFragment())
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playing_fragment, container, false);
        recyclerView = view.findViewById(R.id.playing_rc_view);
        loadMovie();
        return view;
    }


    private void loadMovie() {
        ApiService apiService = ApiBuilder.getClient(getContext()).create(ApiService.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        DBHandler db = new DBHandler(getContext());
        db.open();
        Cursor c = db.getMovies();
        List<Pair<String, String>> listExtracted = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                String itemToSearch = c.getString(1);
                String IDitemToSearch = c.getString(0);
                Call<MovieResponse> call = apiService.getItemSearch(itemToSearch);
                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse>call, Response<MovieResponse> response) {
                        List<MovieModel> tmp = response.body().getResults();
                        for (int i = 0; i < tmp.size(); i++) {
                            if (itemToSearch.equals(tmp.get(i).getTitle()) && IDitemToSearch.equals(Integer.toString(tmp.get(i).getId())))
                                movies.add(tmp.get(i));
                        }
                        for (int i = 0; i < movies.size(); i++) {
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
                    public void onFailure(Call<MovieResponse>call, Throwable t) {}
                });
            } while (c.moveToNext());
        }
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
                    i.putExtra(DetailActivity.EXTRA_ID, movies.get(position).getId());
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
        db.close();
    }

}
