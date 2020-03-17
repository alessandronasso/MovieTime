package com.lab.movietime.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieTrailer : Serializable {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("iso_639_1")
    var originalLanguage: String? = null

    @SerializedName("iso_3166_1")
    var countryOfOrigin: String? = null

    @SerializedName("key")
    var key: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("site")
    var site: String? = null

    @SerializedName("size")
    var size = 0

    @SerializedName("type")
    var type: String? = null

}