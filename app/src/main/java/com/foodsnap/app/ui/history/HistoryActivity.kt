package com.foodsnap.app.ui.history

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodsnap.app.R
import com.foodsnap.app.databinding.ActivityHistoryBinding
import com.foodsnap.app.ui.adapters.FoodAdapter
import com.foodsnap.app.ui.detail.FoodDetailActivity
import com.foodsnap.app.utils.FakeData

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        HistoryViewModel.ViewModelInjector()
    }

    private val foodAdapter = FoodAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()
        setSearchBar()
        setRecyclerView()
        setListeners()
    }

    private fun observeViewModel() {
        viewModel.apply {
            searchedFood.observe(this@HistoryActivity) {
                binding.emptyView.isVisible = it.isEmpty()
                foodAdapter.submitList(it)
            }
        }
    }

    private fun setSearchBar() {
        binding.svAccounts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.searchedFood.postValue(FakeData.generateFood())
                } else {
                    viewModel.searchFood(newText)
                }
                return false
            }
        })
    }

    private fun setRecyclerView() {
        binding.rvFood.apply {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = foodAdapter
        }
    }

    private fun setListeners() {
        binding.apply {
            foodAdapter.onFoodClick = { view, food ->
                val layoutCard = view.findViewById<CardView>(R.id.card_layout)

                val iDetail = Intent(this@HistoryActivity, FoodDetailActivity::class.java)
                iDetail.putExtra(FoodDetailActivity.EXTRA_FOOD, food)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@HistoryActivity,
                        androidx.core.util.Pair(layoutCard, "layoutCard"),
                    )

                startActivity(iDetail, optionsCompat.toBundle())
            }

            btnBack.setOnClickListener { finish() }
        }
    }
}