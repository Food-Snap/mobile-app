package com.foodsnap.app.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.foodsnap.app.databinding.ActivitySplashScreenBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.auth.AuthActivity
import com.foodsnap.app.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: SplashScreenViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.getSession().observe(this) {
            runBlocking {
                delay(3000L)
                val intent = if (it.isLogin) {
                    Intent(this@SplashScreenActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashScreenActivity, AuthActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}