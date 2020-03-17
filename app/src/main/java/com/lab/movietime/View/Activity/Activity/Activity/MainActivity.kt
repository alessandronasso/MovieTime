package com.lab.movietime.View.Activity.Activity.Activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lab.movietime.Adapter.MoviesAdapter
import com.lab.movietime.BuildConfig
import com.lab.movietime.Interface.ApiBuilder.getClient
import com.lab.movietime.Interface.ApiService
import com.lab.movietime.Model.MovieResponse
import com.lab.movietime.R
import com.lab.movietime.View.Activity.Activity.Activity.MainActivity
import com.lab.movietime.View.Activity.Activity.Fragment.HomeFragment
import com.lab.movietime.View.Activity.Activity.Fragment.PlayingFragment
import com.lab.movietime.View.Activity.Activity.Fragment.PopularFragment
import com.lab.movietime.values.Values
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var counter = 0
    private var currentApiVersion = 0
    var homeFragment: HomeFragment? = null
    var popularFragment: PopularFragment? = null
    var playingFragment: PlayingFragment? = null
    private fun loadFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragment_container, fragment)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideNavigationBar()
        setContentView(R.layout.activity_main)
        if (homeFragment == null) {
            homeFragment = HomeFragment()
        }
        loadFragment(homeFragment!!)
        val navigation = findViewById<View>(R.id.BottomNavigation) as BottomNavigationView
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
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

    @SuppressLint("NewApi")
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bbn_item1 -> {
                currentIndex = 0
                if (homeFragment == null) homeFragment = HomeFragment()
                loadFragment(homeFragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.bbn_item2 -> {
                currentIndex = 1
                if (popularFragment == null) popularFragment = PopularFragment()
                loadFragment(popularFragment!!)
                return@OnNavigationItemSelectedListener true
            }
            R.id.bbn_item3 -> {
                currentIndex = 2
                if (playingFragment == null) playingFragment = PlayingFragment()
                loadFragment(playingFragment!!)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        var apiService = getClient(this@MainActivity)!!.create(ApiService::class.java)
        override fun onReceive(context: Context, intent: Intent) {
            counter += 1
            val call = apiService.getPopular(Values.CATEGORY[0], BuildConfig.API_KEY, Values.LANGUAGE, counter)
            call!!.enqueue(object : Callback<MovieResponse?> {
                override fun onResponse(call: Call<MovieResponse?>, response: Response<MovieResponse?>) {
                    val movies = response.body()!!.results
                    val recyclerView = findViewById<RecyclerView>(R.id.rc_view)
                    if (recyclerView != null) {
                        recyclerView.adapter = MoviesAdapter(movies!!, R.layout.content_main, applicationContext)
                    }
                }

                override fun onFailure(call: Call<MovieResponse?>, t: Throwable) {}
            })
        }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.lab.movietime.Service")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.lab.movietime.Service")
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
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

    override fun onBackPressed() {}

    companion object {
        var currentIndex = 0
    }
}