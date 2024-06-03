package com.foodsnap.app.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.databinding.ItemFoodRowBinding

class FoodAdapter :
    ListAdapter<Food, FoodAdapter.MyViewHolder>(DIFF_CALLBACK) {
    var onFoodClick: ((View, Food) -> Unit)? = null

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

                tvDate.text = food.date
                tvTitle.text = food.name
                tvCal.text = StringBuilder("${food.cal} kcal")

                cardLayout.setOnClickListener {
                    onFoodClick?.invoke(root, food)
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