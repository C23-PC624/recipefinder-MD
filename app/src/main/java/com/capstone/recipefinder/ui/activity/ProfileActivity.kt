package com.capstone.recipefinder.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import com.capstone.recipefinder.data.preference.LoginPreference
import com.capstone.recipefinder.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var PreferenceLogin: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        PreferenceLogin = LoginPreference(this)

        binding.ivBack.setOnClickListener{ finish()}

        action()
    }

    private fun action() {
        binding.btnLogout.setOnClickListener {
            PreferenceLogin.logout()
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@ProfileActivity as Activity).toBundle()
            )
        }
    }
}