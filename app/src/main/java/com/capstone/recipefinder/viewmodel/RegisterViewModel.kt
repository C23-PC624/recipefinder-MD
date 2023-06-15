package com.capstone.recipefinder.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.recipefinder.data.model.ResponseRegister
import com.capstone.recipefinder.data.remote.ApiConfig
import com.capstone.recipefinder.utils.RETROFIT_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerAccountUser(name: String, username: String,password: String) {
        _isLoading.value = true
        ApiConfig().getApiService().registerUser(name, username, password)
            .enqueue(object : Callback<ResponseRegister> {
                override fun onResponse(
                    call: Call<ResponseRegister>,
                    response: Response<ResponseRegister>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        Log.d(RETROFIT_TAG, response.body()?.message.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                    _isLoading.value = false
                    Log.d(RETROFIT_TAG, t.message.toString())
                }

            })
    }

}