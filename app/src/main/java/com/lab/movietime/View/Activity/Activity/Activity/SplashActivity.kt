package com.lab.movietime.View.Activity.Activity.Activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lab.movietime.R

class SplashActivity : AppCompatActivity() {
    private var currentApiVersion = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)
        hideNavigationBar();
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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
}