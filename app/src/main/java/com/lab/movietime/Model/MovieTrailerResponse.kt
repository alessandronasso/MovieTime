package com.lab.movietime.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class MovieTrailerResponse : Serializable {
    @SerializedName("id")
    var id = 0

    @SerializedName("results")
    var results: ArrayList<MovieTrailer>? = null
        private set

    fun setTrailerResult(trailerResult: ArrayList<MovieTrailer>?) {
        results = trailerResult
    }
}