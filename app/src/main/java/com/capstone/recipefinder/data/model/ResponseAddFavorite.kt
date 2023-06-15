package com.capstone.recipefinder.data.model

import com.google.gson.annotations.SerializedName

data class ResponseAddFavorite(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("insertId")
	val insertId: Int
)
