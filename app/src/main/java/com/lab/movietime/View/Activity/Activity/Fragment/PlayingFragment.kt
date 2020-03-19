package com.lab.movietime.View.Activity.Activity.Fragment

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Pair
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import butterknife.BindView
import com.lab.movietime.Adapter.PopularAdapter
import com.lab.movietime.BuildConfig
import com.lab.movietime.Interface.ApiBuilder.getClient
import com.lab.movietime.Interface.ApiService
import com.lab.movietime.Interface.DBHandler
import com.lab.movietime.Model.MovieModel
import com.lab.movietime.Model.MovieResponse
import com.lab.movietime.Model.MovieTrailer
import com.lab.movietime.Model.MovieTrailerResponse
import com.lab.movietime.R
import com.lab.movietime.View.Activity.Activity.Activity.DetailActivity
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PlayingFragment : Fragment() {
    @JvmField
    @BindView(R.id.rc_view)
    var recyclerView: RecyclerView? = null
    private var movies: MutableList<MovieModel> = ArrayList()
    private val trailerMap = HashMap<Int, String?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (recyclerView!!.adapter != null) {
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PlayingFragment())
                    .commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.playing_fragment, container, false)
        recyclerView = view.findViewById(R.id.playing_rc_view)
        movies = ArrayList()
        loadMovie()
        return view
    }

    private fun loadMovie() {
        val apiService = getClient(context)!!.create(ApiService::class.java)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val db = DBHandler(context!!)
        db.open()
        val c = db.movies
        val listExtracted: List<Pair<String, String>> = ArrayList()
        if (c.moveToFirst()) {
            do {
                val itemToSearch = c.getString(1)
                val IDitemToSearch = c.getString(0)
                val call = apiService.getItemSearch(itemToSearch)
                call!!.enqueue(object : Callback<MovieResponse?> {
                    override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                        val tmp = response.body()!!.results
                        for (i in tmp!!.indices) {
                            if (itemToSearch == tmp[i].title && IDitemToSearch == Integer.toString(tmp[i].id)) movies.add(tmp[i])
                        }
                        for (i in movies.indices) {
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
                        recyclerView!!.adapter = PopularAdapter(movies, R.layout.content_main, context!!)
                    }

                    override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
                })
            } while (c.moveToNext())
        }
        recyclerView!!.addOnItemTouchListener(object : OnItemTouchListener {
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
                        context!!.startActivity(i)
                    }
                } else DynamicToast.makeError(activity!!.applicationContext, "Missing internet connection!", 2000).show();
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
        db.close()
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): PlayingFragment {
            val fragment = PlayingFragment()
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