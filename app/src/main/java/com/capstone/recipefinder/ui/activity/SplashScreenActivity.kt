package com.capstone.recipefinder.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.capstone.recipefinder.R
import com.capstone.recipefinder.data.preference.LoginPreference
import com.capstone.recipefinder.utils.SHARED_PREFERENCES
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var preference: SharedPreferences
    lateinit var userpreference: LoginPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        setPreference()

        lifecycleScope.launch {
            delay(3000)

            if (userpreference.getUser().token.isEmpty()){

                startActivity(Intent(this@SplashScreenActivity, GetStartedActivity::class.java))
                finish()
            }else {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onPause() {
        lifecycleScope.cancel()
        super.onPause()
    }
    private fun setPreference() {
        preference = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userpreference = LoginPreference(this)
    }
}