package com.lab.movietime.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LanguageModel : Serializable {
    @SerializedName("iso_639_1")
    var id: String? = null

    @SerializedName("english_name")
    var originalLanguage: String? = null

    @SerializedName("name")
    private val name: String? = null

}