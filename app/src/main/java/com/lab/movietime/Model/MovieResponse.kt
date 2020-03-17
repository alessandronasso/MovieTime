package com.lab.movietime.Model

import com.google.gson.annotations.SerializedName

class MovieResponse {
    @SerializedName("page")
    var page = 0

    @SerializedName("results")
    var results: List<MovieModel>? = null

    @SerializedName("total_results")
    var totalResults = 0

    @SerializedName("total_pages")
    var totalPages = 0

}