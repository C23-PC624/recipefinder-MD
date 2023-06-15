package com.capstone.recipefinder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.recipefinder.data.model.ResponseFoodItem
import com.capstone.recipefinder.databinding.ActivityDetailFoodBinding
import com.capstone.recipefinder.utils.*

class DetailFoodActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE)
    {
        ActivityDetailFoodBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        setDetail()

        binding.ivBack.setOnClickListener{ finish()}

    }

    private fun setDetail() {
        val name = intent.getStringExtra(FOOD_NAME)
        val category = intent.getStringExtra(ID_CATEGORY)
        val kkal = intent.getStringExtra(KALORI)
        val lemak = intent.getStringExtra(LEMAK)
        val protein = intent.getStringExtra(PROTEIN)
        val karbo = intent.getStringExtra(KARBOHIDRAT)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val ingredient = intent.getStringExtra(EXTRA_INGREDIENT)
        val image = intent.getStringExtra(EXTRA_IMAGE)

        binding.apply {
            tvDetailFullname.text = name
            idCategory.text = category
            tvKkal.text = kkal
            tvKarbohidrat.text = karbo
            tvProtein.text = protein
            tvLemak.text = lemak
            tvDescription.text = description
            tvIngredient.text = ingredient
            Glide.with(this@DetailFoodActivity)
                .load(image)
                .into(ivDetail)
        }
    }
}