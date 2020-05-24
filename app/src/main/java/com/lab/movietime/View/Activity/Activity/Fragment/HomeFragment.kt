package com.lab.movietime.View.Activity.Activity.Fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.lab.movietime.Adapter.MoviesAdapter
import com.lab.movietime.BuildConfig
import com.lab.movietime.Interface.ApiBuilder.getClient
import com.lab.movietime.Interface.ApiService
import com.lab.movietime.Model.MovieModel
import com.lab.movietime.Model.MovieResponse
import com.lab.movietime.Model.MovieTrailer
import com.lab.movietime.Model.MovieTrailerResponse
import com.lab.movietime.R
import com.lab.movietime.View.Activity.Activity.Activity.DetailActivity
import com.lab.movietime.values.Values
import com.lab.movietime.values.Values.GENRE
import com.lab.movietime.values.Values.MAP_NAME
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment() {
    private val NUM_ROWS = 3
    private val recyclerView = arrayOfNulls<RecyclerView>(NUM_ROWS)
    private val call = arrayOfNulls<Call<MovieResponse?>?>(NUM_ROWS)
    private val genre = arrayOfNulls<TextView>(NUM_ROWS)
    private val trailerMap = HashMap<Int, String?>()
    var mPullToRefresh: SwipeRefreshLayout? = null
    private val movieList: MutableList<List<MovieModel>> = ArrayList()
    private var randomGenre: MutableList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        genre[0] = view?.findViewById<TextView?>(R.id.MovieGenre)
        genre[1] = view?.findViewById<TextView?>(R.id.MovieGenre2)
        genre[2] = view?.findViewById(R.id.MovieGenre3)
        recyclerView[0] = view?.findViewById(R.id.rc_view)
        recyclerView[1] = view?.findViewById(R.id.rc_view2)
        recyclerView[2] = view?.findViewById(R.id.rc_view3)
        mPullToRefresh = view?.findViewById(R.id.swipe_list)
        if (movieList.size == 0) loadMovie() else refreshList()
        this.mPullToRefresh?.setOnRefreshListener(OnRefreshListener {
            if (isNetworkAvailable) {
                reloadMovie()
                for (i in 0 until NUM_ROWS) recyclerView[i]?.getAdapter()!!.notifyDataSetChanged()
                mPullToRefresh?.run { setRefreshing(false) }
            } else {
                mPullToRefresh?.run { setRefreshing(false) }
                DynamicToast.makeError(activity!!.applicationContext, "Missing internet connection!", 2000).show();
            }
        })
        return view
    }

    private fun loadMovie() {
        val rand = Random(System.currentTimeMillis())
        val apiService = getClient(context)!!.create(ApiService::class.java)
        randomGenre = ArrayList()
        for (j in 0 until NUM_ROWS) {
            var byRelevance = rand.nextInt(GENRE.size)
            while (randomGenre.contains(byRelevance)) byRelevance = rand.nextInt(GENRE.size)
            randomGenre.add(byRelevance)
            genre[j]?.text = MAP_NAME.get(GENRE.get(byRelevance))
            recyclerView[j]!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            call[j] = apiService.getDiscover(BuildConfig.API_KEY, Values.LANGUAGE, Values.SORT_BY[0], Values.ADULT, GENRE.get(randomGenre[j]), Values.PAGE[rand.nextInt(5)])
        }
        for (j in 0 until NUM_ROWS) {
            call[j]!!.enqueue(object : Callback<MovieResponse?> {
                override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                    var movies = response.body()!!.results
                    movieList.add(movies!!)
                    movies = removeEmptyMovies(movies)
                    for (i in movies!!.indices) {
                        val callT = apiService.getMovieTrailer(movies[i].id, BuildConfig.API_KEY)
                        callT!!.enqueue(object : Callback<MovieTrailerResponse?> {
                            override fun onResponse(call2: Call<MovieTrailerResponse?>, response2: Response<MovieTrailerResponse?>) {
                                val mt: List<MovieTrailer>? = response2.body()!!.results
                                val mtID = response2.body()!!.id
                                if (mt!!.isEmpty()) trailerMap[mtID] = "S0Q4gqBUs7c" else trailerMap[mtID] = mt[0].key
                            }

                            override fun onFailure(call2: Call<MovieTrailerResponse?>, t: Throwable) {}
                        })
                    }
                    recyclerView[j]!!.adapter = MoviesAdapter(movies, R.layout.content_main, context!!)
                    recyclerView[j]!!.addOnItemTouchListener(object : OnItemTouchListener {
                        var gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                            override fun onSingleTapUp(e: MotionEvent): Boolean {
                                return true
                            }
                        })

                        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                            if (isNetworkAvailable) {
                                val child = rv.findChildViewUnder(e.x, e.y)
                                if (child != null && gestureDetector.onTouchEvent(e)) {
                                    val position = rv.getChildAdapterPosition(child)
                                    val i = Intent(context, DetailActivity::class.java)
                                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    i.putExtra(DetailActivity.EXTRA_ID, movies[position].id)
                                    i.putExtra(DetailActivity.EXTRA_TITLE, movies[position].title)
                                    i.putExtra(DetailActivity.EXTRA_OVERVIEW, movies[position].overview)
                                    i.putExtra(DetailActivity.EXTRA_TIME, movies[position].releaseDate)
                                    i.putExtra(DetailActivity.EXTRA_POSTER, movies[position].posterPath)
                                    i.putExtra(DetailActivity.EXTRA_LANGUAGE, movies[position].originalLanguage)
                                    i.putExtra(DetailActivity.EXTRA_GENRES, movies[position].genre)
                                    i.putExtra(DetailActivity.EXTRA_VOTE, movies[position].getVoteAverage())
                                    i.putExtra(DetailActivity.EXTRA_YTLINK, trailerMap[movies[position].id])
                                    i.putExtra(DetailActivity.EXTRA_RUNTIME, movies!![position]!!.runtime)
                                    context!!.startActivity(i)
                                }
                            } else DynamicToast.makeError(activity!!.applicationContext, "Missing internet connection!", 2000).show();
                            return false
                        }

                        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                    })
                }

                override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
            })
        }
    }

    private fun reloadMovie() {
        val rand = Random(System.currentTimeMillis())
        val apiService = getClient(context)!!.create(ApiService::class.java)
        randomGenre = ArrayList()
        for (j in 0 until NUM_ROWS) {
            var rand_tmp = rand.nextInt(GENRE.size)
            while (randomGenre.contains(rand_tmp)) rand_tmp = rand.nextInt(GENRE.size)
            randomGenre.add(rand_tmp)
            genre[j]?.setText(MAP_NAME.get(GENRE.get(rand_tmp)))
            recyclerView[j]!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            call[j] = apiService.getDiscover(BuildConfig.API_KEY, Values.LANGUAGE, Values.SORT_BY[0], Values.ADULT, GENRE.get(randomGenre[j]), Values.PAGE[rand.nextInt(5)])
        }
        for (j in 0 until NUM_ROWS) {
            call[j]!!.enqueue(object : Callback<MovieResponse?> {
                override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                    val movies = response.body()!!.results
                    movieList[j] = movies!!
                    for (i in movies!!.indices) {
                        val callT = apiService.getMovieTrailer(movies[i].id, BuildConfig.API_KEY)
                        callT!!.enqueue(object : Callback<MovieTrailerResponse?> {
                            override fun onResponse(call2: Call<MovieTrailerResponse?>, response2: Response<MovieTrailerResponse?>) {
                                val mt: List<MovieTrailer>? = response2.body()!!.results
                                val mtID = response2.body()!!.id
                                if (mt!!.isEmpty()) trailerMap[mtID] = "S0Q4gqBUs7c" else trailerMap[mtID] = mt[0].key
                            }

                            override fun onFailure(call2: Call<MovieTrailerResponse?>, t: Throwable) {}
                        })
                    }
                    recyclerView[j]!!.adapter = MoviesAdapter(movies, R.layout.content_main, context!!)
                }

                override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
            })
        }
    }

    private fun refreshList() {
        for (j in 0 until NUM_ROWS) {
            genre[j]?.setText(MAP_NAME.get(GENRE.get(randomGenre[j])))
            recyclerView[j]!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView[j]!!.adapter = MoviesAdapter(movieList[j]!!, R.layout.content_main, context!!)
            recyclerView[j]!!.addOnItemTouchListener(object : OnItemTouchListener {
                var gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }
                })

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (isNetworkAvailable) {
                        val child = rv.findChildViewUnder(e.x, e.y)
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            val position = rv.getChildAdapterPosition(child)
                            val i = Intent(context, DetailActivity::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            i.putExtra(DetailActivity.EXTRA_ID, movieList[j]!![position].id)
                            i.putExtra(DetailActivity.EXTRA_TITLE, movieList[j]!![position].title)
                            i.putExtra(DetailActivity.EXTRA_OVERVIEW, movieList[j]!![position].overview)
                            i.putExtra(DetailActivity.EXTRA_TIME, movieList[j]!![position].releaseDate)
                            i.putExtra(DetailActivity.EXTRA_POSTER, movieList[j]!![position].posterPath)
                            i.putExtra(DetailActivity.EXTRA_LANGUAGE, movieList[j]!![position].originalLanguage)
                            i.putExtra(DetailActivity.EXTRA_GENRES, movieList[j]!![position].genre)
                            i.putExtra(DetailActivity.EXTRA_VOTE, movieList[j]!![position].getVoteAverage())
                            i.putExtra(DetailActivity.EXTRA_YTLINK, trailerMap[movieList[j]!![position].id])
                            i.putExtra(DetailActivity.EXTRA_RUNTIME, movieList[j]!![position].runtime)
                            context!!.startActivity(i)
                        }
                    } else DynamicToast.makeError(activity!!.applicationContext, "Missing internet connection!", 2000).show();
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }

    private fun removeEmptyMovies (movies: List<MovieModel>?): List<MovieModel>? {
        val selectedSeries: MutableList<MovieModel?>? = ArrayList()
        for (i in movies!!.indices) {
            if (!(movies!!.get(i)!!.backdropPath==null || movies!!.get(i)!!.posterPath==null))
                selectedSeries!!.add(movies!!.get(i))
        }
        return selectedSeries as List<MovieModel>?
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    val isNetworkAvailable: Boolean
        get() = try {
            val manager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo: NetworkInfo? = null
            if (manager != null) {
                networkInfo = manager.activeNetworkInfo
            }
            networkInfo != null && networkInfo.isConnected
        } catch (e: NullPointerException) {
            false
        }
}