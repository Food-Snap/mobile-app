package com.foodsnap.app.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.databinding.ActivityFoodDetailBinding

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private val food by lazy { intent.getParcelableExtra<Food>(EXTRA_FOOD) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenSize()
        setViews()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
    }

    private fun setViews() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            food?.let {
                Glide.with(root)
                    .load(it.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivFood)

                tvTitle.text = it.name
                tvDate.text = it.date

                tvCalories.text = StringBuilder("${it.cal} kcal")
                tvCarbs.text = StringBuilder("${it.carbs} g")
                tvProteins.text = StringBuilder("${it.protein} g")
                tvFats.text = StringBuilder("${it.fats} g")
                tvCalcium.text = StringBuilder("${it.calcium} g")
            }
        }
    }

    companion object {
        const val EXTRA_FOOD = "extra_food"
    }
}