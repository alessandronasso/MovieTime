package com.lab.movietime.View.Activity.Activity.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import butterknife.BindView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lab.movietime.Adapter.PopularAdapter
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
import com.lab.movietime.values.Values.INVERSE_MAP
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class PopularFragment : Fragment() {
    private var mSearchField: EditText? = null
    private var mSearchBtn: ImageButton? = null
    private val trailerMap = HashMap<Int, String?>()
    private var movies: List<MovieModel?>? = ArrayList()
    private var moviesCopy: List<MovieModel?>? = ArrayList()
    private var isFabOpen: Boolean = false

    @JvmField
    @BindView(R.id.rc_view)
    var recyclerView: RecyclerView? = null

    @JvmField
    @BindView(R.id.fab)
    var fab: FloatingActionButton? = null

    @JvmField
    @BindView(R.id.fab1)
    var fabitem1: FloatingActionButton? = null

    @JvmField
    @BindView(R.id.fab2)
    var fabitem2: FloatingActionButton? = null

    @JvmField
    @BindView(R.id.fab3)
    var fabitem3: FloatingActionButton? = null

    @JvmField
    @BindView(R.id.checkbox_adv)
    var checkbox: CheckBox? = null

    @JvmField
    @BindView(R.id.hiddenTab)
    var hiddenTab: LinearLayout? = null

    @JvmField
    @BindView(R.id.spinnerGenre)
    var spinnerGenre: Spinner? = null

    @JvmField
    @BindView(R.id.spinnerVote)
    var spinnerVote: Spinner? = null

    @JvmField
    @BindView(R.id.spinnerOperator)
    var spinnerOperator: Spinner? = null

    @JvmField
    @BindView(R.id.releaseYear)
    var releaseYear: EditText? = null

    lateinit var fab_open: Animation
    lateinit var fab_close: Animation
    lateinit var rotate_forward: Animation
    lateinit var rotate_backward: Animation

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.popular_fragment, container, false)
        recyclerView = view.findViewById(R.id.popular_rc_view) as RecyclerView
        mSearchField = view.findViewById<View>(R.id.search_field) as EditText
        mSearchBtn = view.findViewById<View>(R.id.search_btn) as ImageButton
        checkbox = view.findViewById(R.id.checkbox_adv) as CheckBox
        hiddenTab = view.findViewById(R.id.hiddenTab) as LinearLayout
        spinnerGenre = view.findViewById(R.id.spinnerGenre) as Spinner
        spinnerVote = view.findViewById(R.id.spinnerVote) as Spinner
        spinnerOperator = view.findViewById(R.id.spinnerOperator) as Spinner
        releaseYear = view.findViewById(R.id.releaseYear) as EditText

        checkbox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) hiddenTab!!.visibility = View.VISIBLE
            else hiddenTab!!.visibility = View.GONE
        }

        mSearchBtn!!.setOnClickListener {
            if (mSearchField!!.text.toString() != "") {
                searchMovie(mSearchField!!.text.toString())

            } else loadMovie()
        }

        fab_open = AnimationUtils.loadAnimation(context, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(context,R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(context,R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(context,R.anim.rotate_backward);


        fab = view.findViewById(R.id.fab)
        fabitem1 = view.findViewById(R.id.fab1)
        fabitem2 = view.findViewById(R.id.fab2)
        fabitem3 = view.findViewById(R.id.fab3)

        fab!!.setOnClickListener { view ->
            animateFAB();
        }

        fabitem1!!.setOnClickListener { view ->
            filterByMostPopular()
            closeAnimation()
        }

        fabitem2!!.setOnClickListener { view ->
           filterByMostRated()
            closeAnimation()
        }

        fabitem3!!.setOnClickListener { view ->
            filterByMostRecent()
            closeAnimation()
        }

        if (movies!!.isEmpty()) loadMovie() else refreshList()
        return view
    }

    private fun removeGenres(genreSelected: String) {
        var selectedSeries: MutableList<MovieModel?>? = ArrayList()
        for (i in movies!!.indices) {
            System.out.println(movies!!.get(i)!!.genreIds.contains(INVERSE_MAP.get(genreSelected)))
            if (movies!!.get(i)!!.genreIds.contains(INVERSE_MAP.get(genreSelected)))
                selectedSeries!!.add(movies!!.get(i))
        }
        movies = null
        movies = selectedSeries as List<MovieModel?>
    }

    private fun removeYear(year: Int) {
        var selectedSeries: MutableList<MovieModel?>? = ArrayList()
        for (i in movies!!.indices) {
            var s = movies!!.get(i)!!.releaseDate.substring(0,4)
            if (s.toInt()==year)
                selectedSeries!!.add(movies!!.get(i))
        }
        movies = null
        movies = selectedSeries as List<MovieModel?>
    }

    private fun removeVote(vote: Int, operator: String) {
        var selectedSeries: MutableList<MovieModel?>? = ArrayList()
        for (i in movies!!.indices) {
            if (operator.equals(">")) {
                if (movies!!.get(i)!!.getVoteAverage() >= vote)
                    selectedSeries!!.add(movies!!.get(i))
            } else if (operator.equals("<")) {
                if (movies!!.get(i)!!.getVoteAverage() < vote)
                    selectedSeries!!.add(movies!!.get(i))
            } else if (operator.equals("=")) {
                if (movies!!.get(i)!!.getVoteAverage() == vote.toDouble())
                    selectedSeries!!.add(movies!!.get(i))
            }
        }
        movies = null
        movies = selectedSeries as List<MovieModel?>
    }

    private fun animateFAB() {
        if (!isFabOpen) openAnimation()
        else closeAnimation()
    }

    @SuppressLint("RestrictedApi")
    private fun openAnimation() {
        fab!!.startAnimation(rotate_forward);
        fabitem1!!.startAnimation(fab_open);
        fabitem2!!.startAnimation(fab_open);
        fabitem3!!.startAnimation(fab_open);
        fabitem1!!.visibility = View.VISIBLE
        fabitem2!!.visibility = View.VISIBLE
        fabitem3!!.visibility = View.VISIBLE
        fabitem1!!.isClickable = true;
        fabitem2!!.isClickable = true;
        fabitem3!!.isClickable = true;
        isFabOpen = true;
    }

    @SuppressLint("RestrictedApi")
    private fun closeAnimation() {
        fab!!.startAnimation(rotate_backward);
        fabitem1!!.startAnimation(fab_close);
        fabitem2!!.startAnimation(fab_close);
        fabitem3!!.startAnimation(fab_close);
        fabitem1!!.visibility = View.INVISIBLE
        fabitem2!!.visibility = View.INVISIBLE
        fabitem3!!.visibility = View.INVISIBLE
        fabitem1!!.isClickable = false;
        fabitem2!!.isClickable = false;
        fabitem3!!.isClickable = false;
        isFabOpen = false;
    }

    private fun loadMovie() {
        val apiService = getClient(context)!!.create(ApiService::class.java)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val call = apiService.getPopular(Values.CATEGORY[1], BuildConfig.API_KEY, Values.LANGUAGE, Values.PAGE[0])
        call!!.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                movies = response.body()!!.results
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
                recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, context!!)
                recyclerView!!.addOnItemTouchListener(object : OnItemTouchListener {
                    var gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                        override fun onSingleTapUp(e: MotionEvent): Boolean {
                            return true
                        }
                    })

                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        val child = rv.findChildViewUnder(e.x, e.y)
                        if (child != null && gestureDetector.onTouchEvent(e)) {
                            val position = rv.getChildAdapterPosition(child)
                            val i = Intent(context, DetailActivity::class.java)
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
                            context!!.startActivity(i)
                        }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
                })
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
        })
    }

    private fun searchMovie(movieName: String) {
        val apiService = getClient(context)!!.create(ApiService::class.java)
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val call = apiService.getItemSearch(movieName)
        call!!.enqueue(object : Callback<MovieResponse?> {
            override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                movies = response.body()!!.results
                moviesCopy = movies
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

                if (checkbox!!.isChecked) {
                    if (spinnerGenre!!.selectedItem.toString()!="---")
                        removeGenres(spinnerGenre!!.selectedItem.toString())
                    if (releaseYear!!.text.toString().trim().length>0) {
                        var numeric = true
                        var num = 0
                        try { num = parseInt(releaseYear!!.text.toString())
                        } catch (e: NumberFormatException) { numeric = false }
                        if (numeric && num>1850 && num<2030)
                            removeYear(num)
                    }
                    if (spinnerVote!!.selectedItem.toString()!="---")
                        removeVote(spinnerVote!!.selectedItem.toString().toInt(), spinnerOperator!!.selectedItem.toString())
                }

                recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, context!!)
                recyclerView!!.adapter!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
        })
    }

    private fun filterByMostRecent() {
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        moviesCopy = movies
        Collections.sort(movies) { o1, o2 -> o1!!.releaseDate.compareTo(o2!!.releaseDate) }
        Collections.reverse(movies)
        recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, context!!)
        recyclerView!!.adapter!!.notifyDataSetChanged()
    }

    private fun filterByMostPopular() {
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        Collections.sort(movies) { o1, o2 -> o1!!.popularity.compareTo(o2!!.popularity) }
        Collections.reverse(movies)
        recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, context!!)
        recyclerView!!.adapter!!.notifyDataSetChanged()
    }

    private fun filterByMostRated() {
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        Collections.sort(movies) { o1, o2 -> o1!!.voteCount.compareTo(o2!!.voteCount) }
        Collections.reverse(movies)
        recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, context!!)
        recyclerView!!.adapter!!.notifyDataSetChanged()
    }

    private fun undoFilter() {
        movies = moviesCopy
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, context!!)
        recyclerView!!.adapter!!.notifyDataSetChanged()
    }

    private fun refreshList() {
        recyclerView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.adapter = PopularAdapter(movies as List<MovieModel>, R.layout.content_main, context!!)
        recyclerView!!.addOnItemTouchListener(object : OnItemTouchListener {
            var gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }
            })

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val child = rv.findChildViewUnder(e.x, e.y)
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    val position = rv.getChildAdapterPosition(child)
                    val i = Intent(context, DetailActivity::class.java)
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
                    context!!.startActivity(i)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    companion object {
        fun newInstance(param1: String?, param2: String?): PopularFragment {
            val fragment = PopularFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}