package com.capstone.recipefinder.data.remote

import com.capstone.recipefinder.data.model.ResponseFoodItem
import com.capstone.recipefinder.data.model.ResponseLogin
import com.capstone.recipefinder.data.model.ResponseRegister
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("users/register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<ResponseRegister>

    @FormUrlEncoded
    @POST("users/login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password")password: String
    ): Call<ResponseLogin>


    @GET("food")
    fun listFood(
        @Header("Authentication") auth: String
    ): Call<List<ResponseFoodItem>>

}