package com.foodsnap.app.ui.history

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodsnap.app.databinding.ActivityHistoryBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.adapters.FoodAdapter

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
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
            getFood().observe(this@HistoryActivity) {
                Log.d("History Activity", "observeViewModel: $it")
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
                    viewModel.getFood()
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
        binding.btnBack.setOnClickListener { finish() }
    }
}