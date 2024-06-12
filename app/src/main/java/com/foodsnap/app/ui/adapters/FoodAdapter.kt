package com.foodsnap.app.ui.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.databinding.ItemFoodRowBinding
import com.foodsnap.app.ui.detail.FoodDetailActivity
import com.foodsnap.app.utils.roundToString
import com.foodsnap.app.utils.toLocalDateFormat

class FoodAdapter :
    ListAdapter<Food, FoodAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        ItemFoodRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MyViewHolder(private val binding: ItemFoodRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.apply {
                Glide.with(root)
                    .load(food.imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivFood)

                tvDate.text = food.date.toLocalDateFormat()
                tvTitle.text = food.name
                tvCal.text = StringBuilder("${food.calories.roundToString()} cal")

                itemView.setOnClickListener {
                    val iDetail =
                        Intent(itemView.context as Activity, FoodDetailActivity::class.java)
                    iDetail.putExtra(FoodDetailActivity.EXTRA_FOOD, food)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(cardLayout, "layoutCard"),
                        )

                    itemView.context.startActivity(iDetail, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Food>() {
            override fun areItemsTheSame(
                oldItem: Food,
                newItem: Food
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Food,
                newItem: Food
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}