package com.lab.movietime.View.Activity.Activity.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lab.movietime.Adapter.MoviesAdapter;
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
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.lab.movietime.values.Values.GENRE;
import static com.lab.movietime.values.Values.MAP_NAME;

public class HomeFragment extends Fragment {

    private int NUM_ROWS = 3;

    private RecyclerView[] recyclerView = new RecyclerView[NUM_ROWS];

    private Call[] call = new retrofit2.Call[NUM_ROWS];

    private TextView[] genre = new TextView[NUM_ROWS];;

    private View view;

    private HashMap<Integer, String> trailerMap = new HashMap<>();

    SwipeRefreshLayout mPullToRefresh;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.main_fragment, container, false);
        genre[0] = view.findViewById(R.id.MovieGenre);
        genre[1] = view.findViewById(R.id.MovieGenre2);
        genre[2] = view.findViewById(R.id.MovieGenre3);
        recyclerView[0] = view.findViewById(R.id.rc_view);
        recyclerView[1] = view.findViewById(R.id.rc_view2);
        recyclerView[2] = view.findViewById(R.id.rc_view3);

        mPullToRefresh = view.findViewById(R.id.swipe_list);

        loadMovie();

        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadMovie();
                for (int i=0; i<NUM_ROWS; i++) recyclerView[i].getAdapter().notifyDataSetChanged();
                mPullToRefresh.setRefreshing(false);
            }
        });
        return view;
    }


    private void loadMovie() {
        Random rand = new Random(System.currentTimeMillis());
        ApiService apiService = ApiBuilder.getClient(getContext()).create(ApiService.class);
        List<Integer> randomGenre = new ArrayList<>();
        for (int j = 0; j < NUM_ROWS; j++) {
            //GENERATING THE EXPLORE SECTION
            int rand_tmp = rand.nextInt(GENRE.length);
            while (randomGenre.contains(rand_tmp)) rand_tmp = rand.nextInt(GENRE.length);
            randomGenre.add(rand_tmp);
            genre[j].setText(MAP_NAME.get(GENRE[rand_tmp]));
            //SETTING THE LAYOUT
            recyclerView[j].setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
            //SETTING THE CALL
            call[j] = apiService.getDiscover(BuildConfig.API_KEY, Values.LANGUAGE, Values.SORT_BY[0], Values.ADULT, GENRE[randomGenre.get(j)], Values.PAGE[rand.nextInt(5)]);
        }

        for (int j = 0; j < NUM_ROWS; j++) {
            int finalJ = j;
            call[j].enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    final List<MovieModel> movies = response.body().getResults();
                    for (int i = 0; i < movies.size(); i++) {
                        Call<MovieTrailerResponse> callT = apiService.getMovieTrailer(movies.get(i).getId(), BuildConfig.API_KEY);
                        callT.enqueue(new Callback<MovieTrailerResponse>() {
                            @Override
                            public void onResponse(Call<MovieTrailerResponse> call2, Response<MovieTrailerResponse> response2) {
                                List<MovieTrailer> mt = response2.body().getResults();
                                Integer mtID = response2.body().getId();
                                if (mt.isEmpty()) trailerMap.put(mtID, "S0Q4gqBUs7c");
                                else trailerMap.put(mtID, mt.get(0).getKey());
                            }
                            @Override
                            public void onFailure(Call<MovieTrailerResponse> call2, Throwable t) { }
                        });
                    }
                    recyclerView[finalJ].setAdapter(new MoviesAdapter(movies, R.layout.content_main, getContext()));
                    recyclerView[finalJ].addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                        GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                            public boolean onSingleTapUp(MotionEvent e) {
                                return true;
                            }
                        });

                        @Override
                        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                            View child = rv.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && gestureDetector.onTouchEvent(e)) {
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
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) { }
            });
        }
    }

    private void reloadMovie () {
        Random rand = new Random(System.currentTimeMillis());
        ApiService apiService = ApiBuilder.getClient(getContext()).create(ApiService.class);
        List<Integer> randomGenre = new ArrayList<>();
        for (int j = 0; j < NUM_ROWS; j++) {
            //GENERATING THE EXPLORE SECTION
            int rand_tmp = rand.nextInt(GENRE.length);
            while (randomGenre.contains(rand_tmp)) rand_tmp = rand.nextInt(GENRE.length);
            randomGenre.add(rand_tmp);
            genre[j].setText(MAP_NAME.get(GENRE[rand_tmp]));
            //SETTING THE LAYOUT
            recyclerView[j].setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));
            //SETTING THE CALL
            call[j] = apiService.getDiscover(BuildConfig.API_KEY, Values.LANGUAGE, Values.SORT_BY[0], Values.ADULT, GENRE[randomGenre.get(j)], Values.PAGE[rand.nextInt(5)]);
        }

        for (int j = 0; j < NUM_ROWS; j++) {
            int finalJ = j;
            call[j].enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    final List<MovieModel> movies = response.body().getResults();
                    for (int i = 0; i < movies.size(); i++) {
                        Call<MovieTrailerResponse> callT = apiService.getMovieTrailer(movies.get(i).getId(), BuildConfig.API_KEY);
                        callT.enqueue(new Callback<MovieTrailerResponse>() {
                            @Override
                            public void onResponse(Call<MovieTrailerResponse> call2, Response<MovieTrailerResponse> response2) {
                                List<MovieTrailer> mt = response2.body().getResults();
                                Integer mtID = response2.body().getId();
                                if (mt.isEmpty()) trailerMap.put(mtID, "S0Q4gqBUs7c");
                                else trailerMap.put(mtID, mt.get(0).getKey());
                            }

                            @Override
                            public void onFailure(Call<MovieTrailerResponse> call2, Throwable t) {
                            }
                        });
                    }
                    recyclerView[finalJ].setAdapter(new MoviesAdapter(movies, R.layout.content_main, getContext()));
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                }
            });
        }
    }
}