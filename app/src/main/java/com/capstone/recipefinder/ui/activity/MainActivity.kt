package com.capstone.recipefinder.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.recipefinder.R
import com.capstone.recipefinder.adapter.FoodAdapter
import com.capstone.recipefinder.data.preference.LoginPreference
import com.capstone.recipefinder.databinding.ActivityMainBinding
import com.capstone.recipefinder.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FoodAdapter
    private lateinit var PreferenceLogin: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        adapter = FoodAdapter()
        PreferenceLogin = LoginPreference(this)

        setupViewModel()
        setRecyclerView()
        intent()


    }


    override fun onResume() {
        super.onResume()
        setupViewModel()
    }
    private fun setRecyclerView() {
        binding.apply {
            rvMain.layoutManager = LinearLayoutManager(this@MainActivity)
            rvMain.setHasFixedSize(true)
            rvMain.adapter = adapter

        }
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.getAllFood(PreferenceLogin.getUser().token)
        Log.d("makanan123", PreferenceLogin.getUser().token)
        viewModel.listFood.observe(this) {

                Log.d("tes123",it.toString())
                adapter.setFood(it)

        }

    }

    private fun intent() {
        binding.ivCamera.setOnClickListener {
            val intent = Intent(this, ScanFoodActivity::class.java)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle()
            )
        }
    }
}