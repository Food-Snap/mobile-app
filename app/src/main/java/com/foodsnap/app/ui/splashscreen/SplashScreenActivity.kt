package com.foodsnap.app.ui.splashscreen

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.foodsnap.app.databinding.ActivitySplashScreenBinding
import com.foodsnap.app.ui.auth.AuthActivity


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, AuthActivity::class.java)
            val anim = ActivityOptions.makeCustomAnimation(
                application,
                android.R.anim.fade_in, android.R.anim.fade_out
            ).toBundle()
            startActivity(intent, anim)
            finish()
        }, 3000L)
    }

    private fun startAnimation() {
        val scaleDownX = ObjectAnimator.ofFloat(binding.ivLogoBack, View.SCALE_X, 0.5f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(binding.ivLogoBack, View.SCALE_Y, 0.5f, 1f)
        scaleDownX.duration = 1000
        scaleDownY.duration = 1000
        scaleDownX.interpolator = AccelerateDecelerateInterpolator()
        scaleDownY.interpolator = AccelerateDecelerateInterpolator()
        scaleDownX.start()
        scaleDownY.start()
    }
}