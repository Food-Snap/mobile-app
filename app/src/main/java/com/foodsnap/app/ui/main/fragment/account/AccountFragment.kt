package com.foodsnap.app.ui.main.fragment.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.foodsnap.app.R
import com.foodsnap.app.databinding.FragmentAccountBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.auth.AuthActivity
import com.foodsnap.app.ui.main.MainViewModel
import com.foodsnap.app.ui.settings.changepassword.ChangePasswordActivity
import com.foodsnap.app.ui.settings.editprofile.EditProfileActivity
import com.foodsnap.app.utils.roundToString

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarTextColor(true)
        setListeners()
        setView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setStatusBarTextColor(false)
    }

    private fun setListeners() {
        binding.apply {
            btnEditProfile.setOnClickListener {
                startActivity(Intent(requireActivity(), EditProfileActivity::class.java))
            }

            btnChangePass.setOnClickListener {
                startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
            }

            btnLogOut.setOnClickListener {
                viewModel.logout()
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private fun setView() {
        binding.apply {
            viewModel.getUser().observe(viewLifecycleOwner) {
                Log.d(TAG, "setListeners: $it")
                tvUsername.text = it.name
                tvEmail.text = it.email
                if (it.gender == "Female") {
                    Glide
                        .with(this@AccountFragment)
                        .load(R.drawable.img_avatar_female)
                        .into(ivAvatar)
                } else {
                    Glide
                        .with(this@AccountFragment)
                        .load(R.drawable.img_avatar_male)
                        .into(ivAvatar)
                }

                val bmiCategory = getBmiCategory(it.bmi)
                when (bmiCategory) {
                    "Underweight" -> {
                        binding.cvCal.setCardBackgroundColor(
                            getColor(
                                requireActivity(),
                                R.color.blue
                            )
                        )
                        Log.d(TAG, "setListeners: $bmiCategory")
                    }

                    "Normal" -> {
                        binding.cvCal.setCardBackgroundColor(
                            getColor(
                                requireActivity(),
                                R.color.green
                            )
                        )
                        Log.d(TAG, "setListeners: $bmiCategory")
                    }

                    "Overweight" -> {
                        binding.cvCal.setCardBackgroundColor(
                            getColor(
                                requireActivity(),
                                R.color.orange
                            )
                        )
                        Log.d(TAG, "setListeners: $bmiCategory")
                    }

                    "Obese" -> {
                        binding.cvCal.setCardBackgroundColor(
                            getColor(
                                requireActivity(),
                                R.color.red
                            )
                        )
                        Log.d(TAG, "setListeners: $bmiCategory")
                    }
                }
                Log.d(TAG, "bmi category: $bmiCategory")
                val stringBmi = it.bmi.roundToString()
                binding.tvCal.text = "BMI: $stringBmi ($bmiCategory)"
            }
        }
    }

    private fun setStatusBarTextColor(isDark: Boolean) {
        activity?.window?.let { window ->
            val decorView = window.decorView
            WindowInsetsControllerCompat(window, decorView).isAppearanceLightStatusBars = isDark
        }
    }

    private fun getBmiCategory(bmi: Float): String {
        return when {
            bmi in 0.1..18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal"
            bmi in 25.0..29.9 -> "Overweight"
            bmi >= 30.0 -> "Obese"
            else -> "Invalid BMI"
        }
    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}