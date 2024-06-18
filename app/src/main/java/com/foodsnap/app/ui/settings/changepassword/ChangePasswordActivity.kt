package com.foodsnap.app.ui.settings.changepassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.foodsnap.app.R
import com.foodsnap.app.data.Result
import com.foodsnap.app.databinding.ActivityChangePasswordBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.utils.ToastManager.showToast
import com.foodsnap.app.utils.isConnectedToInternet

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnSave.setOnClickListener {
                if (tilOldPassword.isErrorEnabled || edOldPassword.text.toString().isEmpty()) {
                    showToast(
                        this@ChangePasswordActivity,
                        getString(R.string.error_password)
                    )
                    return@setOnClickListener
                } else if (tilNewPassword.isErrorEnabled || edNewPassword.text.toString()
                        .isEmpty()
                ) {
                    showToast(
                        this@ChangePasswordActivity,
                        getString(R.string.error_password)
                    )
                    return@setOnClickListener
                } else if (tilPasswordConfirmation.isErrorEnabled || edPasswordConfirmation.text.toString()
                        .isEmpty()
                ) {
                    showToast(
                        this@ChangePasswordActivity,
                        getString(R.string.error_confirmation)
                    )
                    return@setOnClickListener
                }

                val oldPassword = edOldPassword.text.toString()
                val newPassword = edNewPassword.text.toString()

                if (isConnectedToInternet(this@ChangePasswordActivity)) {
                    observeChangePassword(oldPassword, newPassword)
                } else {
                    showToast(this@ChangePasswordActivity, "Please check your network connection")
                }
            }

            edPasswordConfirmation.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != edNewPassword.text.toString()) {
                        tilPasswordConfirmation.isErrorEnabled = true
                        tilPasswordConfirmation.error = getString(R.string.error_confirmation)
                    } else {
                        tilPasswordConfirmation.isErrorEnabled = false
                        tilPasswordConfirmation.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun observeChangePassword(oldPassword: String, newPassword: String) {
        binding.apply {
            viewModel.changePassword(oldPassword, newPassword)
                .observe(this@ChangePasswordActivity) { result ->
                    when (result) {
                        is Result.Loading -> {
                            progressOverlay.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            progressOverlay.visibility = View.GONE
                            showToast(this@ChangePasswordActivity, result.error)
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
}