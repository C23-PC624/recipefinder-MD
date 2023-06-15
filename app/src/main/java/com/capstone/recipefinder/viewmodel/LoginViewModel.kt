package com.capstone.recipefinder.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.recipefinder.data.model.ResponseLogin
import com.capstone.recipefinder.data.remote.ApiConfig
import com.capstone.recipefinder.utils.RETROFIT_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.capstone.recipefinder.data.model.Result

class LoginViewModel : ViewModel() {
    private val AccountLogin = MutableLiveData<ResponseLogin>()
    val userLogin: LiveData<ResponseLogin> = AccountLogin

    private val ResponseAccount = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = ResponseAccount

    private val tm = MutableLiveData<String>()
    val toastMessage: LiveData<String> = tm

    fun loginUser(name: String, password: String): LiveData<Result<ResponseLogin>> {
        val result = MediatorLiveData<Result<ResponseLogin>>()
        result.postValue(Result.Loading)
        ResponseAccount.value = true
        ApiConfig().getApiService().loginUser(name, password)
            .enqueue(object : Callback<ResponseLogin> {
                override fun onResponse(
                    call: Call<ResponseLogin>,
                    response: Response<ResponseLogin>
                ) {
                    ResponseAccount.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        Log.d("ADABWANG", responseBody.toString())
                        if (responseBody != null) {
                            result.value = Result.Success(responseBody)
                        }

                    }
                    if (!response.isSuccessful) {
                        tm.value = response.message()
                    }
                }

                override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                    tm.value = t.message
                    ResponseAccount.value = false
                    Log.d(RETROFIT_TAG, t.message.toString())
                }

            })
        return result
    }
}