package com.foodsnap.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.foodsnap.app.R
import com.foodsnap.app.data.Result
import com.foodsnap.app.databinding.ActivityLoginBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.main.MainActivity
import com.foodsnap.app.ui.signup.SignUpActivity
import com.foodsnap.app.utils.ToastManager.showToast
import com.foodsnap.app.utils.isConnectedToInternet

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
            btnLogin.setOnClickListener {
                if (tilEmail.isErrorEnabled || edEmail.text.toString().isEmpty()) {
                    return@setOnClickListener
                } else if (tilPassword.isErrorEnabled || edPassword.text.toString().isEmpty()) {
                    return@setOnClickListener
                }

                val email = edEmail.text.toString()
                val password = edPassword.text.toString()
                if (isConnectedToInternet(this@LoginActivity)) {
                    observeLogin(email, password)
                } else {
                    showToast(this@LoginActivity, "Please check your network connection")
                }

            }

            btnSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
                finish()
            }
        }
    }

    private fun observeLogin(email: String, password: String) {
        binding.apply {
            viewModel.login(email, password).observe(this@LoginActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        progressIndicator.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        progressIndicator.visibility = View.GONE
                        showToast(this@LoginActivity, result.error)
                    }

                    is Result.Success -> {
                        progressIndicator.visibility = View.GONE
                        showToast(applicationContext, result.data)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}