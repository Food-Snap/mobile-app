package com.foodsnap.app.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.databinding.ActivityFoodDetailBinding
import com.foodsnap.app.ui.image.ImageActivity
import com.foodsnap.app.ui.image.ImageActivity.Companion.EXTRA_IMAGE
import com.foodsnap.app.ui.image.ImageActivity.Companion.EXTRA_TITLE

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private val food by lazy { intent.getParcelableExtra<Food>(EXTRA_FOOD) }
    private val prediction by lazy { intent.getParcelableExtra<Food>(EXTRA_PREDICT) }

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
            btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            food?.let { food ->
                Glide.with(root)
                    .load(food.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivFood)

                tvTitle.text = food.name
                tvDate.text = food.date

                tvCalories.text = StringBuilder("${food.calories} kcal")
                tvCarbs.text = StringBuilder("${food.carbs} g")
                tvProteins.text = StringBuilder("${food.protein} g")
                tvFats.text = StringBuilder("${food.fats} g")


                ivFood.setOnClickListener {
                    val intent = Intent(this@FoodDetailActivity, ImageActivity::class.java)
                    intent.putExtra(EXTRA_IMAGE, food.imageUrl)
                    intent.putExtra(EXTRA_TITLE, food.name)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        const val EXTRA_FOOD = "extra_food"
        const val EXTRA_PREDICT = "extra_predict"
    }
}