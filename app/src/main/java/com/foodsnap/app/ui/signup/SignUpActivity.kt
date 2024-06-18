package com.foodsnap.app.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.foodsnap.app.R
import com.foodsnap.app.data.Result
import com.foodsnap.app.databinding.ActivitySignUpBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.login.LoginActivity
import com.foodsnap.app.utils.ToastManager.showToast
import com.foodsnap.app.utils.isConnectedToInternet

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenSize()
        setListeners()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setListeners() {
        binding.apply {
            btnSignup.setOnClickListener {
                if (tilName.isErrorEnabled || edName.text.toString().isEmpty()) {
                    showToast(this@SignUpActivity, getString(R.string.error_name))
                    return@setOnClickListener
                } else if (tilEmail.isErrorEnabled || edEmail.text.toString().isEmpty()) {
                    showToast(this@SignUpActivity, getString(R.string.error_email))
                    return@setOnClickListener
                } else if (tilPassword.isErrorEnabled || edPassword.text.toString().isEmpty()) {
                    showToast(this@SignUpActivity, getString(R.string.error_password))
                    return@setOnClickListener
                } else if (tilPasswordConfirmation.isErrorEnabled || edPasswordConfirmation.text.toString()
                        .isEmpty()
                ) {
                    showToast(this@SignUpActivity, getString(R.string.error_confirmation))
                    return@setOnClickListener
                }

                val name = edName.text.toString()
                val email = edEmail.text.toString()
                val password = edPassword.text.toString()

                if (isConnectedToInternet(this@SignUpActivity)) {
                    observeSignUp(email, password, name)
                } else {
                    showToast(this@SignUpActivity, "Please check your network connection")
                }

            }

            btnLogin.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                finish()
            }

            edName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        tilName.isErrorEnabled = true
                        tilName.error = getString(R.string.error_name)
                    } else {
                        tilName.isErrorEnabled = false
                        tilName.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edPasswordConfirmation.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString() != edPassword.text.toString()) {
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

    private fun observeSignUp(email: String, password: String, name: String) {
        binding.apply {
            viewModel.signup(email, password, name).observe(this@SignUpActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        progressIndicator.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        progressIndicator.visibility = View.GONE
                        showToast(this@SignUpActivity, result.error)
                    }

                    is Result.Success -> {
                        progressIndicator.visibility = View.GONE
                        showToast(applicationContext, result.data)
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

}