package com.lab.movietime.Model

import com.google.gson.annotations.SerializedName
import com.lab.movietime.values.Values
import java.util.*

class MovieModel(@field:SerializedName("poster_path") var posterPath: String, @field:SerializedName("adult") var isAdult: Boolean, @field:SerializedName("overview") var overview: String, @field:SerializedName("release_date") var releaseDate: String, genreIds: List<Int>, id: Int,
                 originalTitle: String, originalLanguage: String, title: String, backdropPath: String, popularity: Double,
                 voteCount: Int, video: Boolean, voteAverage: Double, runtime: Int) {

    @SerializedName("genre_ids")
    var genreIds: List<Int> = ArrayList()

    @SerializedName("id")
    var id: Int

    @SerializedName("original_title")
    var originalTitle: String

    @SerializedName("original_language")
    var originalLanguage: String

    @SerializedName("title")
    var title: String

    @SerializedName("backdrop_path")
    var backdropPath: String

    @SerializedName("popularity")
    var popularity: Double

    @SerializedName("vote_count")
    var voteCount: Int

    @SerializedName("video")
    var video: Boolean

    @SerializedName("runtime")
    var runtime: Int

    @SerializedName("vote_average")
    private var voteAverage: Double

    var linkTrailer: String? = null

    fun getVoteAverage(): Double {
        return voteAverage / 2
    }

    fun setVoteAverage(voteAverage: Double) {
        this.voteAverage = voteAverage
    }

    val genre: String
        get() {
            var tmp = ""
            for (i in genreIds.indices) tmp += Values.MAP_NAME[genreIds[i]].toString() + ", "
            return if (tmp == "") "Unknown" else tmp.substring(0, tmp.length - 2)
        }

    init {
        this.genreIds = genreIds
        this.id = id
        this.originalTitle = originalTitle
        this.originalLanguage = originalLanguage
        this.title = title
        this.backdropPath = backdropPath
        this.popularity = popularity
        this.voteCount = voteCount
        this.video = video
        this.voteAverage = voteAverage
        this.runtime = runtime
    }
}