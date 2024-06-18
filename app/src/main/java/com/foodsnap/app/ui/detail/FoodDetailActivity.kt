package com.foodsnap.app.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodsnap.app.data.Result
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.data.model.response.PredictResponse
import com.foodsnap.app.databinding.ActivityFoodDetailBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.image.ImageActivity
import com.foodsnap.app.ui.image.ImageActivity.Companion.EXTRA_IMAGE
import com.foodsnap.app.ui.image.ImageActivity.Companion.EXTRA_TITLE
import com.foodsnap.app.utils.ToastManager.showToast
import com.foodsnap.app.utils.isConnectedToInternet
import com.foodsnap.app.utils.roundToString
import com.foodsnap.app.utils.toLocalDateFormat
import com.foodsnap.app.utils.toTitleCase

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetailBinding
    private val food by lazy { intent.getParcelableExtra<Food>(EXTRA_FOOD) }
    private val prediction by lazy { intent.getParcelableExtra<PredictResponse>(EXTRA_PREDICT) }

    private val viewModel: FoodDetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        food?.let { setViews(it) }
        prediction?.let {
            setViews(it.toFood())
            setTrackButton(it)
        }
        setFullScreenSize()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
    }

    private fun setViews(food: Food) {
        binding.apply {
            btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

            Glide.with(root)
                .load(food.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivFood)

            tvTitle.text = food.name.toTitleCase()
            tvDate.text = food.date.toLocalDateFormat()

            tvCalories.text = StringBuilder("${food.calories.roundToString()} cal")
            tvCarbs.text = StringBuilder("${food.carbs.roundToString()} g")
            tvProteins.text = StringBuilder("${food.protein.roundToString()} g")
            tvFats.text = StringBuilder("${food.fats.roundToString()} g")

            ivFood.setOnClickListener {
                val intent = Intent(this@FoodDetailActivity, ImageActivity::class.java)
                intent.putExtra(EXTRA_IMAGE, food.imageUrl)
                intent.putExtra(EXTRA_TITLE, food.name)
                startActivity(intent)
            }
        }
    }

    private fun setTrackButton(prediction: PredictResponse) {
        binding.apply {
            btnTrack.visibility = View.VISIBLE
            tvDate.visibility = View.GONE
            btnTrack.setOnClickListener {
                if (isConnectedToInternet(this@FoodDetailActivity)) {
                    observeSaveFood(prediction)
                } else {
                    showToast(this@FoodDetailActivity, "Please check your network connection")
                }
            }
        }
    }

    private fun observeSaveFood(prediction: PredictResponse) {
        binding.apply {
            viewModel.saveFood(prediction.predictedFood.foodId, prediction.imageUrl)
                .observe(this@FoodDetailActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            progressOverlay.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            progressOverlay.visibility = View.GONE
                            showToast(this@FoodDetailActivity, result.error)
                        }

                        is Result.Success -> {
                            progressOverlay.visibility = View.GONE
                            showToast(applicationContext, result.data)
                            finish()
                        }
                    }
                }
        }
    }

    companion object {
        const val EXTRA_FOOD = "extra_food"
        const val EXTRA_PREDICT = "extra_predict"
    }
}