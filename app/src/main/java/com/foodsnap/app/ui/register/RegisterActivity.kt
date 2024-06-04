package com.foodsnap.app.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.foodsnap.app.R
import com.foodsnap.app.databinding.ActivityRegisterBinding
import com.foodsnap.app.ui.login.LoginActivity
import com.foodsnap.app.utils.ToastManager

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
            btnRegister.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                ToastManager.showToast(applicationContext, "User Created")
                finish()
            }

            btnLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}