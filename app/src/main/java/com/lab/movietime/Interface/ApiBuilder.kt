package com.lab.movietime.Interface

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiBuilder {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    private var retrofit: Retrofit? = null
    @JvmStatic
    fun getClient(context: Context?): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }
        return retrofit
    }
}