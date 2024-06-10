package com.foodsnap.app.ui.main.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foodsnap.app.databinding.FragmentAccountBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.auth.AuthActivity
import com.foodsnap.app.ui.main.MainViewModel
import com.foodsnap.app.ui.settings.editprofile.EditProfileActivity

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
        val root: View = binding.root
        setListeners()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarTextColor(true)
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

            btnLogOut.setOnClickListener {
                viewModel.logout()
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }

            viewModel.getUser().observe(requireActivity()) {
                Log.d(TAG, "setListeners: $it")
                tvUsername.text = it.name
                tvEmail.text = it.email
                if (it.bmi < 1) {
                    tvCal.visibility = View.GONE
                }
            }
        }
    }

    private fun setStatusBarTextColor(isDark: Boolean) {
        activity?.window?.let { window ->
            val decorView = window.decorView
            WindowInsetsControllerCompat(window, decorView).isAppearanceLightStatusBars = isDark
        }
    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}