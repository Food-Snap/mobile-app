package com.foodsnap.app.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
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

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val foodAdapter = FoodAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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
        binding.apply {
            btnHistory.setOnClickListener {
                startActivity(Intent(requireActivity(), HistoryActivity::class.java))
            }

            foodAdapter.onFoodClick = { view, food ->
                val layoutCard = view.findViewById<CardView>(R.id.card_layout)

                val iDetail = Intent(requireActivity(), FoodDetailActivity::class.java)
                iDetail.putExtra(FoodDetailActivity.EXTRA_FOOD, food)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        androidx.core.util.Pair(layoutCard, "layoutCard"),
                    )

                startActivity(iDetail, optionsCompat.toBundle())
            }
        }
    }
}