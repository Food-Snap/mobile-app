package com.foodsnap.app.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.foodsnap.app.R
import com.foodsnap.app.databinding.ActivityMainBinding
import com.foodsnap.app.ui.camera.CameraActivity
import com.foodsnap.app.ui.main.ui.account.AccountFragment
import com.foodsnap.app.ui.main.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenSize()
        setBottomNavView()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
    }

    private fun setBottomNavView() {
        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_camera -> {
                    setCameraListener()
                    return@setOnItemSelectedListener false
                }

                R.id.navigation_account -> {
                    replaceFragment(AccountFragment())
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun setCameraListener() {
        if (checkImagePermission()) {
            val iCamera = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(iCamera)
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                REQUIRED_CAMERA_PERMISSION,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun checkImagePermission() = REQUIRED_CAMERA_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (checkImagePermission()) {
                val iCamera = Intent(this@MainActivity, CameraActivity::class.java)
                startActivity(iCamera)
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        binding.bottomNav.selectedItemId = R.id.navigation_home
    }

    companion object {
        private val REQUIRED_CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 101
    }
}