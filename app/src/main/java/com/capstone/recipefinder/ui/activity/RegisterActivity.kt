package com.capstone.recipefinder.ui.activity


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.recipefinder.databinding.ActivityRegisterBinding
import com.capstone.recipefinder.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setViewModel()
        intent()


    }

    private fun intent() {
        binding.btnRegister.setOnClickListener {
            registerAccount()
        }
        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity as Activity).toBundle()
            )
        }

    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        viewModel.isLoading.observe(this) { showLoading(it) }
    }

    private fun registerAccount() {
        val name = binding.cetName.text.toString().trim()
        val email = binding.ceRegister.text.toString().trim()
        val password = binding.cpRegister.text.toString().trim()
        viewModel.registerAccountUser(name, email, password)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity as Activity).toBundle())
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}