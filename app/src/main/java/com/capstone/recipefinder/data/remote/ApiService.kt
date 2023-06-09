package com.capstone.recipefinder.data.remote

import com.capstone.recipefinder.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("predict")
    fun uploadImage(
        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody
    ): Call<ResponsePredictItem>
}