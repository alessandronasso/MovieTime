package com.lab.movietime.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lab.movietime.Model.MovieModel;
import com.lab.movietime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lab.movietime.BuildConfig.URL_BACKBG;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<MovieModel> movies;
    private int rowLayout;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movieTitle) TextView movieTitle;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.popularityTextView) TextView popularity;
        @BindView(R.id.gambar) ImageView backbg;
        public MovieViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public MoviesAdapter(List<MovieModel> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.row, viewGroup,false);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        holder.movieTitle.setText(movies.get(position).getTitle());

        String time = movies.get(position).getReleaseDate();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = parser.parse(time);
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy");
            String formattedDate = formatter.format(date);
            holder.date.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.popularity.setText(movies.get(position).getVoteAverage().toString());
        Glide.with(context).load(URL_BACKBG + movies.get(position).getBackdropPath())
                .into(holder.backbg);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
