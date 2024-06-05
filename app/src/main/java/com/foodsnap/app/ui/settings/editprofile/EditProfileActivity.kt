package com.foodsnap.app.ui.settings.editprofile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.foodsnap.app.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnSave.setOnClickListener { finish() }
        }
    }
}