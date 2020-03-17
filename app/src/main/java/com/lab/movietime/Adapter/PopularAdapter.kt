package com.lab.movietime.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.lab.movietime.BuildConfig
import com.lab.movietime.Model.MovieModel
import com.lab.movietime.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PopularAdapter(private val movies: List<MovieModel>, private val rowLayout: Int, private val context: Context) : RecyclerView.Adapter<PopularAdapter.MovieViewHolder>() {

    class MovieViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        @JvmField
        @BindView(R.id.movieTitle)
        var movieTitle: TextView? = null

        @JvmField
        @BindView(R.id.date)
        var date: TextView? = null

        @JvmField
        @BindView(R.id.popularityTextView)
        var popularity: TextView? = null

        @JvmField
        @BindView(R.id.gambar)
        var backbg: ImageView? = null

        init {
            ButterKnife.bind(this, v!!)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.row_popular, viewGroup, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.movieTitle!!.text = movies[position].title
        val time = movies[position].releaseDate
        val parser = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = null
        try {
            date = parser.parse(time)
            val formatter = SimpleDateFormat("EEEE, MMM d, yyyy")
            val formattedDate = formatter.format(date)
            holder.date!!.text = formattedDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.popularity!!.text = movies[position].getVoteAverage().toString()
        Glide.with(context).load(BuildConfig.URL_BACKBG + movies[position].backdropPath)
                .into(holder.backbg)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}