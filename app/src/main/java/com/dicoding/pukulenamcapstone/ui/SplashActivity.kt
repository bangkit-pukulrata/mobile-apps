package com.dicoding.pukulenamcapstone.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pukulenamcapstone.R
import com.dicoding.pukulenamcapstone.ui.auth.WelcomeActivity
import com.google.firebase.FirebaseApp


class SplashActivity : AppCompatActivity() {

    companion object {
        const val SPLASH_SCREEN_TIME_OUT: Long = 3000 // 3 seconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        FirebaseApp.initializeApp(this)

        Handler().postDelayed({
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }, SPLASH_SCREEN_TIME_OUT)
    }
}