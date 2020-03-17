package com.lab.movietime.values

import java.util.*

object Values {
    var baseURL = "https://api.myjson.com/bins/"
    @JvmField
    var CATEGORY = arrayOf("popular", "now_playing", "upcoming")
    @JvmField
    var LANGUAGE = "en-US"
    @JvmField
    var PAGE = intArrayOf(1, 2, 3, 4, 5)
    @JvmField
    var GENRE = intArrayOf(28, 12, 16, 35, 80, 99, 18, 10751, 14, 36, 27, 10402, 9648, 10749, 878, 10770, 53, 10752, 37)
    var GENRE_BY_NAME = arrayOf("Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
            "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western")
    @JvmField
    var ADULT = false
    @JvmField
    var SORT_BY = arrayOf("popularity.desc", "popularity.asc", "Fantasy")
    @JvmField
    var MAP_NAME: HashMap<Int, String> = initializeMap()
    private fun initializeMap(): HashMap<Int, String> {
        val tmp = HashMap<Int, String>()
        for (i in GENRE.indices) tmp[GENRE[i]] = GENRE_BY_NAME[i]
        return tmp
    }
}