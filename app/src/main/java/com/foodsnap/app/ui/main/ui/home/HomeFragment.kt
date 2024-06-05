package com.foodsnap.app.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodsnap.app.R
import com.foodsnap.app.databinding.FragmentHomeBinding
import com.foodsnap.app.ui.adapters.FoodAdapter
import com.foodsnap.app.ui.detail.FoodDetailActivity
import com.foodsnap.app.ui.history.HistoryActivity
import com.foodsnap.app.utils.FakeData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val foodAdapter = FoodAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {
        setDate()
        setChartCard()
        setRecyclerViews()
        setListeners()
    }

    private fun setDate() {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM")
        binding.tvDateNow.text = StringBuilder("Today, ${formatter.format(currentDate)}")
    }

    private fun setChartCard() {
        binding.apply {
            tvCalory.text = "1067"
            donutChart.setProgress(80)

            tvCarbs.text = StringBuilder("36g")
            tvProteins.text = StringBuilder("26g")
            tvFats.text = StringBuilder("3g")
        }
    }

    private fun setRecyclerViews() {
        binding.rvFood.apply {
            foodAdapter.submitList(FakeData.generateFood())
            layoutManager = LinearLayoutManager(context)
            adapter = foodAdapter
        }
    }

    private fun setListeners() {
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }
    }
}