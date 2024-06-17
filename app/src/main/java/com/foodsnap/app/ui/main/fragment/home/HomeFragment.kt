package com.foodsnap.app.ui.main.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodsnap.app.R
import com.foodsnap.app.data.model.Food
import com.foodsnap.app.databinding.FragmentHomeBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.adapters.FoodAdapter
import com.foodsnap.app.ui.history.HistoryActivity
import com.foodsnap.app.ui.main.MainViewModel
import com.foodsnap.app.ui.settings.editprofile.EditProfileActivity
import com.foodsnap.app.utils.dp
import com.foodsnap.app.utils.roundToString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val foodAdapter = FoodAdapter()
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

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
        setRecyclerViews()
        setListeners()
    }

    private fun setDate() {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        binding.tvDateNow.text = StringBuilder("Today, ${formatter.format(currentDate)}")
    }

    private fun setChartCard(listFood: List<Food>) {
        binding.apply {
            viewModel.getUser().observe(viewLifecycleOwner) { user ->
                if (user.bmi < 1) {
                    llInformation.visibility = View.VISIBLE
                    llProgress.visibility = View.GONE
                    btnEditProfile.setOnClickListener {
                        startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
                    }
                } else {
                    llProgress.visibility = View.VISIBLE
                    llInformation.visibility = View.GONE
                    val bmr = user.calculateDailyCaloricNeeds()
                    val calories = listFood.sumOf { it.calories.toDouble() }
                    val carbs = listFood.sumOf { it.carbs.toDouble() }
                    val proteins = listFood.sumOf { it.protein.toDouble() }
                    val fats = listFood.sumOf { it.fats.toDouble() }
                    val progress = (calories / bmr * 100).coerceIn(0.0, 100.0)
                    donutChart.setProgress(progress.toInt())
                    tvCarbs.text = getString(R.string.grams_info, carbs.toFloat().roundToString())
                    tvCalory.text = getString(R.string.grams_info, calories.toFloat().roundToString())
                    tvFats.text = getString(R.string.grams_info, fats.toFloat().roundToString())
                    tvProteins.text = getString(R.string.grams_info, proteins.toFloat().roundToString())
                }
            }
        }
    }

    private fun setRecyclerViews() {
        binding.rvFood.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foodAdapter
            viewModel.getFood().observe(viewLifecycleOwner) {
                val todayFood = filterTodayFood(it)
                setChartCard(todayFood)
                if (todayFood.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    binding.emptyView.visibility = View.GONE
                    foodAdapter.submitList(todayFood)
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(requireActivity(), HistoryActivity::class.java))
        }

        binding.main.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 75.dp) {
                setStatusBarTextColor(true)
            } else {
                setStatusBarTextColor(false)
            }
        }
    }

    private fun setStatusBarTextColor(isDark: Boolean) {
        activity?.window?.let { window ->
            val decorView = window.decorView
            WindowInsetsControllerCompat(window, decorView).isAppearanceLightStatusBars = isDark
        }
    }

    private fun filterTodayFood(foodList: List<Food>): List<Food> {
        val todayCalendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return foodList.filter { food ->
            val foodDate =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }.parse(food.date)?.let { foodDate ->
                    Calendar.getInstance().apply {
                        time = foodDate
                    }
                }

            foodDate?.let { foodCalendar ->
                foodCalendar[Calendar.YEAR] == todayCalendar[Calendar.YEAR] &&
                        foodCalendar[Calendar.MONTH] == todayCalendar[Calendar.MONTH] &&
                        foodCalendar[Calendar.DAY_OF_MONTH] == todayCalendar[Calendar.DAY_OF_MONTH]
            } ?: false
        }
    }
}