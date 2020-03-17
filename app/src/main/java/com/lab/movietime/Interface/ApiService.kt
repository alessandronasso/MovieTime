package com.lab.movietime.Interface

import com.lab.movietime.BuildConfig
import com.lab.movietime.Model.LanguageModel
import com.lab.movietime.Model.MovieResponse
import com.lab.movietime.Model.MovieTrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie?api_key=" + BuildConfig.API_KEY)
    fun getItemSearch(
            @Query("query") movie_name: String?): Call<MovieResponse?>?

    @GET("movie/{category}")
    fun getPopular(
            @Path("category") category: String?,
            @Query("api_key") apiKey: String?,
            @Query("language") language: String?,
            @Query("page") page: Int): Call<MovieResponse?>?

    @GET("discover/movie")
    fun getDiscover(
            @Query("api_key") apiKey: String?,
            @Query("language") language: String?,
            @Query("sort_by") sort_by: String?,
            @Query("include_adult") adult: Boolean?,
            @Query("with_genres") genre: Int,
            @Query("page") page: Int): Call<MovieResponse?>?

    @GET("configuration/languages")
    fun getLanguages(
            @Query("api_key") apiKey: String?): Call<List<LanguageModel?>?>?

    @GET("movie/upcoming")
    fun getUpcoming(
            @Query("api_key") apiKey: String?): Call<MovieResponse?>?

    @GET("movie/{id}/videos")
    fun getMovieTrailer(
            @Path("id") id: Int,
            @Query("api_key") apiKey: String?): Call<MovieTrailerResponse?>? //    @GET("movie/top_rated")
    //    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);
}