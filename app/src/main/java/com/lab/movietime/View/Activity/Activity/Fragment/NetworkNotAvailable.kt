package com.lab.movietime.View.Activity.Activity.Fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lab.movietime.R
import com.pranavpandey.android.dynamic.toasts.DynamicToast

class NetworkNotAvailable : Fragment() {

    var mPullToRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPause()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.network_not_available, container, false)
        mPullToRefresh = view?.findViewById(R.id.swipe_list2)
        this.mPullToRefresh?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            mPullToRefresh?.run { setRefreshing(false) }
            if (isNetworkAvailable) {
                activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
            } else DynamicToast.makeError(activity!!.applicationContext, "Missing internet connection!", 2000).show();
        })
        return view
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