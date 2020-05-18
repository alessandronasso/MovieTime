package com.lab.movietime.View.Activity.Activity.Activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lab.movietime.Adapter.PopularAdapter
import com.lab.movietime.BuildConfig
import com.lab.movietime.Interface.ApiBuilder.getClient
import com.lab.movietime.Interface.ApiService
import com.lab.movietime.Interface.DBHandler
import com.lab.movietime.Listener.OnSwipeTouchListener
import com.lab.movietime.Model.*
import com.lab.movietime.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import kotlinx.android.synthetic.main.detail_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private var currentApiVersion = 0
    private val mapLang = HashMap<String, String?>()
    private var movies: List<MovieModel?>? = ArrayList()
    private val trailerMap = HashMap<Int, String?>()
    val apiService = getClient(this@DetailActivity)!!.create(ApiService::class.java)

    @JvmField
    @BindView(R.id.rcRelated_view)
    var recyclerView: RecyclerView? = null

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
    @BindView(R.id.runtimeTextView)
    var tvLength: TextView? = null

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
        detail_activity.setOnTouchListener(object: OnSwipeTouchListener(this@DetailActivity) {
            override fun onSwipeLeft() {
                onBackPressed()
            }
            override fun onSwipeRight() {
                onBackPressed()
            }
        })
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
                val runtime = intent.getIntExtra(EXTRA_RUNTIME, 0)
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
                val random = Random()
                val startTime: String = "00:00";
                val minutes: Int = random.nextInt(90) + 100;
                val h: Int = minutes / 60 + Integer.parseInt(startTime.substring(0,1));
                val m: Int = minutes % 60 + Integer.parseInt(startTime.substring(3,4));
                val newtime = h.toString()+"h "+m+"m"
                tvLength!!.text = newtime
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

        //RELATED MOVES
        recyclerView = findViewById(R.id.rcRelated_view)
        recyclerView!!.layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
        val callRelated = apiService.getItemSearch((intent.getStringExtra(EXTRA_TITLE)+" ").split(" ")[0])
        callRelated!!.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                movies = response.body()!!.results
                removeAlreadySelectedMovie()
                for (i in movies!!.indices) {
                    val callT = apiService.getMovieTrailer(movies!![i]!!.id, BuildConfig.API_KEY)
                    callT!!.enqueue(object : Callback<MovieTrailerResponse?> {
                        override fun onResponse(call2: Call<MovieTrailerResponse?>, response2: Response<MovieTrailerResponse?>) {
                            val mt: List<MovieTrailer>? = response2.body()!!.results
                            val mtID = response2.body()!!.id
                            if (mt!!.isEmpty()) trailerMap[mtID] = "S0Q4gqBUs7c" else trailerMap[mtID] = mt[0].key
                        }

                        override fun onFailure(call2: Call<MovieTrailerResponse?>, t: Throwable) {}
                    })
                }
                recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, this@DetailActivity)
                recyclerView!!.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    var gestureDetector = GestureDetector(this@DetailActivity, object : GestureDetector.SimpleOnGestureListener() {
                        override fun onSingleTapUp(e: MotionEvent): Boolean {
                            return true
                        }
                    })

                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        if (isNetworkAvailable) {
                            val child = rv.findChildViewUnder(e.x, e.y)
                            if (child != null && gestureDetector.onTouchEvent(e)) {
                                val position = rv.getChildAdapterPosition(child)
                                val i = Intent(this@DetailActivity, DetailActivity::class.java)
                                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                i.putExtra(DetailActivity.EXTRA_ID, movies!![position]!!.id)
                                i.putExtra(DetailActivity.EXTRA_TITLE, movies!![position]!!.title)
                                i.putExtra(DetailActivity.EXTRA_OVERVIEW, movies!![position]!!.overview)
                                i.putExtra(DetailActivity.EXTRA_TIME, movies!![position]!!.releaseDate)
                                i.putExtra(DetailActivity.EXTRA_POSTER, movies!![position]!!.posterPath)
                                i.putExtra(DetailActivity.EXTRA_LANGUAGE, movies!![position]!!.originalLanguage)
                                i.putExtra(DetailActivity.EXTRA_GENRES, movies!![position]!!.genre)
                                i.putExtra(DetailActivity.EXTRA_VOTE, movies!![position]!!.getVoteAverage())
                                i.putExtra(DetailActivity.EXTRA_YTLINK, trailerMap[movies!![position]!!.id])
                                i.putExtra(DetailActivity.EXTRA_RUNTIME, movies!![position]!!.runtime)
                                this@DetailActivity.startActivity(i)
                            }
                        } else DynamicToast.makeError(this@DetailActivity, "Missing internet connection!", 2000).show();
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                })
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
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

    private fun removeAlreadySelectedMovie() {
        var selectedSeries: MutableList<MovieModel?>? = ArrayList()
        for (i in movies!!.indices) {
            if (!(movies!!.get(i)!!.id == intent.getIntExtra(EXTRA_ID, 0)))
                selectedSeries!!.add(movies!!.get(i))
        }
        movies = null
        movies = selectedSeries as List<MovieModel?>
    }

    private fun initializeMapLang() {
        val call = apiService.getLanguages(BuildConfig.API_KEY)
        call!!.enqueue(object : Callback<List<LanguageModel?>?> {
            override fun onResponse(call: Call<List<LanguageModel?>?>, response: Response<List<LanguageModel?>?>) {
                val lM = response.body()!!
                for (i in lM.indices) mapLang[lM[i]?.id!!.toLowerCase()] = lM[i]?.originalLanguage
            }

            override fun onFailure(call: Call<List<LanguageModel?>?>, t: Throwable) {}
        })
    }

    val isNetworkAvailable: Boolean
        get() = try {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo: NetworkInfo? = null
            if (manager != null) {
                networkInfo = manager.activeNetworkInfo
            }
            networkInfo != null && networkInfo.isConnected
        } catch (e: NullPointerException) {
            false
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
        var EXTRA_RUNTIME = "extra_runtime"
    }
}