package com.capstone.recipefinder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.recipefinder.databinding.ActivityDetailFoodBinding
import com.capstone.recipefinder.utils.*

class DetailPredictActivity : AppCompatActivity() {
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
        val name = intent.getStringExtra("name1")
        val category = intent.getStringExtra("kategori1")
        val kkal = intent.getStringExtra("kkal1")
        val lemak = intent.getStringExtra("lemak1")
        val protein = intent.getStringExtra("protein1")
        val karbo = intent.getStringExtra("karbohidrat1")
        val description = intent.getStringExtra("description1")
        val ingredient = intent.getStringExtra("ingredients1")
        val image = intent.getStringExtra("imgage1")


        binding.apply {
            tvDetailFullname.text = name
            idCategory.text = category
            tvKkal.text = kkal
            tvKarbohidrat.text = karbo
            tvProtein.text = protein
            tvLemak.text = lemak
            tvDescription.text = description
            tvIngredient.text = ingredient
            Glide.with(this@DetailPredictActivity)
                .load(image)
                .into(ivDetail)
        }
    }
}