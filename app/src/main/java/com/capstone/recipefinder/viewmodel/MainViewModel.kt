package com.capstone.recipefinder.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.capstone.recipefinder.data.model.ResponseFoodItem
import com.capstone.recipefinder.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel(){

    private val _listFood = MutableLiveData<List<ResponseFoodItem>>()
    val listFood: LiveData<List<ResponseFoodItem>> = _listFood

    private val _isLoading = MutableLiveData<Boolean>()


    fun getAllFood(auth: String) {
        _isLoading.value = true
        ApiConfig().getApiService().listFood(auth)
            .enqueue(object : Callback<List<ResponseFoodItem>> {
                override fun onResponse(
                    call: Call<List<ResponseFoodItem>>,
                    response: Response<List<ResponseFoodItem>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        Log.d("sss222",response.body().toString())
                        _listFood.postValue(response.body())
                    }

                }

                override fun onFailure(call: Call<List<ResponseFoodItem>>, t: Throwable) {
                    _isLoading.value = false
                }

            })
    }


}



