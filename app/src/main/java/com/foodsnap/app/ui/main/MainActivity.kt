package com.foodsnap.app.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.foodsnap.app.R
import com.foodsnap.app.databinding.ActivityMainBinding
import com.foodsnap.app.ui.camera.CameraActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenSize()
        setBottomNavView()
        setCameraListener()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
    }

    private fun setBottomNavView() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    private fun setCameraListener() {
        binding.btnCamera.setOnClickListener {
            if (checkImagePermission()) {
                val iCamera = Intent(this@MainActivity, CameraActivity::class.java)
                startActivity(iCamera)
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    REQUIRED_CAMERA_PERMISSION,
                    REQUEST_CODE_PERMISSIONS
                )

                if (checkImagePermission()) {
                    val iCamera = Intent(this@MainActivity, CameraActivity::class.java)
                    startActivity(iCamera)
                }
            }
        }
    }

    private fun checkImagePermission() = REQUIRED_CAMERA_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 101
    }
}