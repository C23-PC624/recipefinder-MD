package com.capstone.recipefinder.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.recipefinder.data.model.ResponseFoodItem
import com.capstone.recipefinder.databinding.FoodItemBinding
import com.capstone.recipefinder.ui.activity.DetailFoodActivity
import com.capstone.recipefinder.utils.*


class FoodAdapter: RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    private val listFood = ArrayList<ResponseFoodItem>()

    class ViewHolder(private val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: ResponseFoodItem) {
            Log.d("cek123", food.img)
            binding.apply {
                Glide.with(itemView)
                    .load(food.img)
                    .into(ivFood)

                tvNameFood.text = food.name
                tvIngredient.text = food.ingredients

                cvFood.setOnClickListener {

                    val intent = Intent(itemView.context, DetailFoodActivity::class.java)

                    intent.putExtra(EXTRA_IMAGE, food.img)
                    intent.putExtra(FOOD_NAME, food.name)
                    intent.putExtra(ID_CATEGORY, food.kategori)
                    intent.putExtra(KALORI, food.kkal)
                    intent.putExtra(KARBOHIDRAT, food.karbohidrat)
                    intent.putExtra(PROTEIN, food.protein)
                    intent.putExtra(LEMAK, food.lemak)
                    intent.putExtra(EXTRA_DESCRIPTION, food.description)
                    intent.putExtra(EXTRA_INGREDIENT, food.ingredients)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivFood, "photo"),
                            Pair(tvNameFood, "name"),
                            Pair(tvIngredient, "ingredients"),
                        )

                    itemView.context.startActivity(intent,optionsCompat.toBundle())
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFood[position])
    }

    override fun getItemCount(): Int = listFood.size

    @SuppressLint("NotifyDataSetChanged")
    fun setFood(food: List<ResponseFoodItem>) {
        listFood.clear()
        listFood.addAll(food)
        notifyDataSetChanged()
    }

}