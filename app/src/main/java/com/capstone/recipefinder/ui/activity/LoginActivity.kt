package com.capstone.recipefinder.ui.activity


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.capstone.recipefinder.data.model.Result
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.capstone.recipefinder.data.preference.LoginPreference
import com.capstone.recipefinder.data.user.UserSessions
import com.capstone.recipefinder.databinding.ActivityLoginBinding
import com.capstone.recipefinder.utils.SHARED_PREFERENCES
import com.capstone.recipefinder.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModelViewModel: LoginViewModel
    private lateinit var preference: SharedPreferences
    lateinit var userpreference: LoginPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setPreference()
        setViewModel()
        intent()

    }

    private fun setPreference() {
        preference = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        userpreference = LoginPreference(this)
    }

    private fun setViewModel() {
        viewModelViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModelViewModel.isLoading.observe(this) { showLoading(it) }
        viewModelViewModel.toastMessage.observe(this) { toast(it) }
    }

    private fun intent() {
        binding.btnLogin.setOnClickListener {
            login()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity).toBundle()
            )
        }
    }

    private fun login() {
        val email = binding.ceLogin.text.toString().trim()
        val password = binding.cpLogin.text.toString().trim()
        when {
            email.isEmpty() -> {
                binding.inputEmail.error = "Masukkan email"
            }
            password.isEmpty() -> {
                binding.inputPw.error = "Masukkan password"
            }
            else -> {
                viewModelViewModel.loginUser(email, password).observe(this) {
                    binding.pbLogin.visibility = View.VISIBLE
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> {
                                Log.d("L0Ading", "Loading....")
                            }
                            is Result.Success -> {
                                val data = it.data
                                saveSession(UserSessions(data.data.name, data.token, data.data.id.toString(), true))
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                            is Result.Error -> {
                                Toast.makeText(
                                    this,
                                    "ERRORRRRR",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    }

                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveSession(user: UserSessions) {
        userpreference.setUser(user)
    }


}