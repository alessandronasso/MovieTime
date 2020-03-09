package com.lab.movietime.Interface;

import com.lab.movietime.BuildConfig;
import com.lab.movietime.Model.MovieResponse;
import com.lab.movietime.Model.MovieTrailerResponse;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("search/movie?api_key=" + BuildConfig.API_KEY)
    Call<MovieResponse> getItemSearch(
            @Query("query") String movie_name);

    @GET("movie/{category}")
    Call<MovieResponse> getPopular(
            @Path("category") String category,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);

    @GET("discover/movie")
    Call<MovieResponse> getDiscover(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("sort_by") String sort_by,
            @Query("include_adult") Boolean adult,
            @Query("with_genres") int genre,
            @Query("page") int page);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcoming(
            @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerResponse> getMovieTrailer(
            @Path("id") int id,
            @Query("api_key") String apiKey);

//    @GET("movie/top_rated")
//    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

}