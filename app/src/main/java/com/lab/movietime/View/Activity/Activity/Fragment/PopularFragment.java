package com.lab.movietime.View.Activity.Activity.Fragment;


import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
import java.util.Collections;
import java.util.Comparator;
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
    private List<MovieModel> moviesCopy = new ArrayList<>();
    private Chip[] chipTopRow = new Chip[3];

    @BindView(R.id.rc_view)
    RecyclerView recyclerView;

    public PopularFragment() { }

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

        chipTopRow[0] = (Chip) view.findViewById(R.id.chipPopular);
        chipTopRow[1] = (Chip) view.findViewById(R.id.chipRecent);
        chipTopRow[2] = (Chip) view.findViewById(R.id.chipRated);

        ChipGroup chipGroup = (ChipGroup) view.findViewById(R.id.chipGroup);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip chip = chipGroup.findViewById(i);
                if (chip!=null) {
                    if (chip.isCloseIconVisible()) {
                        chip.setCloseIconVisible(false);
                        undoFilter();
                    } else {
                        deselectAllChips();
                        filterBy(chip.getId());
                        chip.setCloseIconVisible(true);
                    }
                } else  {
                    deselectAllChips();
                    undoFilter();
                }
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mSearchField.getText().toString().equals("")) {
                    deselectAllChips();
                    searchMovie(mSearchField.getText().toString());
                    moviesCopy = new ArrayList<>(movies);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });

        loadMovie();
        return view;
    }

    public void deselectAllChips () {
        for (int i=0; i < 3; i++) chipTopRow[i].setCloseIconVisible(false);
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
                moviesCopy = new ArrayList<>(movies);
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

    private void filterBy(int chipID) {
        movies = new ArrayList<>(moviesCopy);
        switch (chipID) {
            case R.id.chipPopular:
                filterByMostPopular();
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.chipRecent:
                filterByMostRecent();
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.chipRated:
                filterByMostRated();
                recyclerView.getAdapter().notifyDataSetChanged();
                break;
        }
    }

    private void filterByMostRecent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        List<MovieModel> moviesCopy = new ArrayList<MovieModel>(movies);
        Collections.sort(movies, new Comparator<MovieModel>() {
            public int compare(MovieModel o1, MovieModel o2) {
                return o1.getReleaseDate().compareTo(o2.getReleaseDate());
            }
        });
        Collections.reverse(movies);
        recyclerView.setAdapter(new PopularAdapter(movies, R.layout.content_main, getContext()));
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void filterByMostPopular() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        Collections.sort(movies, new Comparator<MovieModel>() {
            public int compare(MovieModel o1, MovieModel o2) {
                return o1.getPopularity().compareTo(o2.getPopularity());
            }
        });
        Collections.reverse(movies);
        recyclerView.setAdapter(new PopularAdapter(movies, R.layout.content_main, getContext()));
    }

    private void filterByMostRated() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        Collections.sort(movies, new Comparator<MovieModel>() {
            public int compare(MovieModel o1, MovieModel o2) {
                return o1.getVoteCount().compareTo(o2.getVoteCount());
            }
        });
        Collections.reverse(movies);
        recyclerView.setAdapter(new PopularAdapter(movies, R.layout.content_main, getContext()));
    }

    private void undoFilter() {
        movies = new ArrayList<>(moviesCopy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new PopularAdapter(movies, R.layout.content_main, getContext()));
    }
}