package com.capstone.recipefinder.data.model

import com.google.gson.annotations.SerializedName

data class ResponseScanFood(

	@field:SerializedName("predicted_class")
	val predictedClass: String
)
