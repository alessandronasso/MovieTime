package com.lab.movietime.View.Activity.Activity.Activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lab.movietime.BuildConfig
import com.lab.movietime.Interface.ApiBuilder.getClient
import com.lab.movietime.Interface.ApiService
import com.lab.movietime.Interface.DBHandler
import com.lab.movietime.Model.LanguageModel
import com.lab.movietime.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private var currentApiVersion = 0
    private val mapLang = HashMap<String, String?>()

    @JvmField
    @BindView(R.id.movieTitle)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.overviewTextView)
    var tvOverview: TextView? = null

    @JvmField
    @BindView(R.id.countryTextView)
    var tvLanguage: TextView? = null

    @JvmField
    @BindView(R.id.releaseDateTextView)
    var tvReleaseDate: TextView? = null

    @JvmField
    @BindView(R.id.genresTextView)
    var tvGenres: TextView? = null

    @JvmField
    @BindView(R.id.posterImg)
    var imgPoster: ImageView? = null

    @JvmField
    @BindView(R.id.favButton)
    var btnFav: Button? = null

    @JvmField
    @BindView(R.id.progressBar)
    var progressBar: ProgressBar? = null

    @JvmField
    @BindView(R.id.listitemrating)
    var ratingBar: RatingBar? = null

    @JvmField
    @BindView(R.id.youtube_player_view)
    var youTubePlayerView: YouTubePlayerView? = null

    var db = DBHandler(this)

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        //addGestures()
        hideNavigationBar()
        initializeMapLang()
        val apiService = getClient(this@DetailActivity)!!.create(ApiService::class.java)
        val call = apiService.getLanguages(BuildConfig.API_KEY)
        call!!.enqueue(object : Callback<List<LanguageModel?>?> {
            override fun onResponse(call: Call<List<LanguageModel?>?>, response: Response<List<LanguageModel?>?>) {
                val lM = response.body()!!
                for (i in lM.indices) {
                    mapLang[lM[i]!!.id!!.toLowerCase()] = lM[i]!!.originalLanguage
                }
                ButterKnife.bind(this@DetailActivity)
                val id = intent.getIntExtra(EXTRA_ID, 0)
                val title = intent.getStringExtra(EXTRA_TITLE)
                val overview = intent.getStringExtra(EXTRA_OVERVIEW)
                val time = intent.getStringExtra(EXTRA_TIME)
                val poster = intent.getStringExtra(EXTRA_POSTER)
                val language = intent.getStringExtra(EXTRA_LANGUAGE)
                val genres = intent.getStringExtra(EXTRA_GENRES)
                val vote = intent.getDoubleExtra(EXTRA_VOTE, 0.0)
                val isFav = booleanArrayOf(false)
                db.open()
                val c = db.movies
                if (c.moveToFirst()) {
                    do {
                        if (c.getString(0) == Integer.toString(id)) {
                            isFav[0] = true
                            btnFav!!.text = "Remove from watchlist"
                        }
                    } while (c.moveToNext())
                }
                db.close()
                tvTitle!!.text = title
                tvLanguage!!.text = mapLang[language.toLowerCase()]
                tvOverview!!.text = overview
                tvGenres!!.text = genres
                setRatingBar(vote)
                youTubePlayerView?.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(intent.getStringExtra(EXTRA_YTLINK), 0f)
                        youTubePlayer.pause()
                    }
                })
                val parser = SimpleDateFormat("yyyy-MM-dd")
                var date: Date? = null
                try {
                    date = parser.parse(time)
                    val formatter = SimpleDateFormat("dd MMM yyyy")
                    val formattedDate = formatter.format(date)
                    tvReleaseDate!!.text = formattedDate
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                Glide.with(this@DetailActivity).load(BuildConfig.URL_POSTER + poster)
                        .listener(object : RequestListener<String?, GlideDrawable?> {
                            override fun onException(e: Exception, model: String?, target: Target<GlideDrawable?>, isFirstResource: Boolean): Boolean {
                                progressBar!!.visibility = View.INVISIBLE
                                return false
                            }

                            override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable?>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                progressBar!!.visibility = View.INVISIBLE
                                return false
                            }
                        })
                        .into(imgPoster)
                btnFav!!.setOnClickListener {
                    db.open()
                    if (!isFav[0]) {
                        db.insertMovie(Integer.toString(id), title)
                        isFav[0] = true
                        btnFav!!.text = "Remove from watchlist"
                    } else {
                        db.removeMovie(Integer.toString(id))
                        isFav[0] = false
                        btnFav!!.text = "add to watchlist"
                    }
                    db.close()
                }
            }

            override fun onFailure(call: Call<List<LanguageModel?>?>, t: Throwable) {}
        })
    }

    private fun hideNavigationBar() {
        currentApiVersion = Build.VERSION.SDK_INT
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            val decorView = window.decorView
            decorView
                    .setOnSystemUiVisibilityChangeListener { visibility ->
                        if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                            decorView.systemUiVisibility = flags
                        }
                    }
        }
    }

    private fun setRatingBar(vote: Double) {
        ratingBar!!.stepSize = 0.25.toFloat()
        ratingBar!!.setIsIndicator(true)
        ratingBar!!.rating = vote.toFloat()
    }

    private fun initializeMapLang() {
        val apiService = getClient(this@DetailActivity)!!.create(ApiService::class.java)
        val call = apiService.getLanguages(BuildConfig.API_KEY)
        call!!.enqueue(object : Callback<List<LanguageModel?>?> {
            override fun onResponse(call: Call<List<LanguageModel?>?>, response: Response<List<LanguageModel?>?>) {
                val lM = response.body()!!
                for (i in lM.indices) mapLang[lM[i]?.id!!.toLowerCase()] = lM[i]?.originalLanguage
            }

            override fun onFailure(call: Call<List<LanguageModel?>?>, t: Throwable) {}
        })
    }

    companion object {
        var EXTRA_ID = "extra_id"
        var EXTRA_TITLE = "extra_title"
        var EXTRA_OVERVIEW = "extra_overview"
        var EXTRA_TIME = "extra_time"
        var EXTRA_POSTER = "extra_poster"
        var EXTRA_LANGUAGE = "extra_language"
        var EXTRA_GENRES = "extra_genres"
        var EXTRA_VOTE = "extra_vote"
        var EXTRA_YTLINK = "extra_ytlink"
    }
}