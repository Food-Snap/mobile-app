package com.foodsnap.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.foodsnap.app.R
import com.foodsnap.app.databinding.ActivityAuthBinding
import com.foodsnap.app.ui.login.LoginActivity
import com.foodsnap.app.ui.register.RegisterActivity
import com.foodsnap.app.utils.dp

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenSize()
        setListeners()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.llContainer.setPadding(30.dp, 30.dp, 30.dp, systemBars.bottom + 30.dp)
            insets
        }
    }

    private fun setListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                startActivity(Intent(this@AuthActivity, LoginActivity::class.java))
                finish()
            }

            btnSignup.setOnClickListener {
                startActivity(Intent(this@AuthActivity, RegisterActivity::class.java))
                finish()
            }
        }
    }
}