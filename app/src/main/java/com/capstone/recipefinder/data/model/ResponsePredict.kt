package com.capstone.recipefinder.data.model

import com.google.gson.annotations.SerializedName


data class ResponsePredictItem(

	@field:SerializedName("img")
	val img: String,

	@field:SerializedName("karbohidrat")
	val karbohidrat: String,

	@field:SerializedName("protein")
	val protein: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("kkal")
	val kkal: String,

	@field:SerializedName("kategori")
	val kategori: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: Int,

	@field:SerializedName("idx")
	val idx: String,

	@field:SerializedName("lemak")
	val lemak: String
)
