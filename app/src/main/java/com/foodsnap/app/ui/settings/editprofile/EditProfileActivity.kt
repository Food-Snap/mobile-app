package com.foodsnap.app.ui.settings.editprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.foodsnap.app.R
import com.foodsnap.app.data.Result
import com.foodsnap.app.databinding.ActivityEditProfileBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.utils.ToastManager
import com.foodsnap.app.utils.roundToString

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnSave.setOnClickListener {
                if (tilName.isErrorEnabled || edName.text.toString().isEmpty()) {
                    ToastManager.showToast(this@EditProfileActivity, getString(R.string.error_name))
                    return@setOnClickListener
                } else if (tilEmail.isErrorEnabled || edEmail.text.toString().isEmpty()) {
                    ToastManager.showToast(
                        this@EditProfileActivity,
                        getString(R.string.error_email)
                    )
                    return@setOnClickListener
                } else if (tilAge.isErrorEnabled || edAge.text.toString().isEmpty()) {
                    ToastManager.showToast(this@EditProfileActivity, getString(R.string.error_age))
                    return@setOnClickListener
                } else if (tilHeight.isErrorEnabled || edHeight.text.toString().isEmpty()) {
                    ToastManager.showToast(
                        this@EditProfileActivity,
                        getString(R.string.error_height)
                    )
                    return@setOnClickListener
                } else if (tilWeight.isErrorEnabled || edWeight.text.toString().isEmpty()) {
                    ToastManager.showToast(
                        this@EditProfileActivity,
                        getString(R.string.error_weight)
                    )
                    return@setOnClickListener
                }

                val name = edName.text.toString()
                val email = edEmail.text.toString()
                val age = edAge.text.toString().toInt()
                val height = edHeight.text.toString().toFloat()
                val weight = edWeight.text.toString().toFloat()
                val gender = if (rbFemale.isChecked) "Female" else "Male"

                viewModel.editProfile(name, email, weight, height, gender, age)
                    .observe(this@EditProfileActivity) { result ->
                        when(result) {
                            is Result.Loading -> {
                                progressOverlay.visibility = View.VISIBLE
                            }
                            is Result.Error -> {
                                progressOverlay.visibility = View.GONE
                                ToastManager.showToast(this@EditProfileActivity, result.error)
                            }
                            is Result.Success -> {
                                progressOverlay.visibility = View.GONE
                                ToastManager.showToast(applicationContext, result.data.message)
                                finish()
                            }
                        }
                    }
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
                        tilName.error = getString(R.string.error_required)
                    } else {
                        tilName.isErrorEnabled = false
                        tilName.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edAge.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        tilAge.isErrorEnabled = true
                        tilAge.error = getString(R.string.error_required)
                    } else {
                        tilAge.isErrorEnabled = false
                        tilAge.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edHeight.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        tilHeight.isErrorEnabled = true
                        tilHeight.error = getString(R.string.error_required)
                    } else {
                        tilHeight.isErrorEnabled = false
                        tilHeight.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            edWeight.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        tilWeight.isErrorEnabled = true
                        tilWeight.error = getString(R.string.error_required)
                    } else {
                        tilWeight.isErrorEnabled = false
                        tilWeight.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun setView() {
        viewModel.getUser().observe(this) {
            binding.apply {
                edName.setText(it.name)
                edEmail.setText(it.email)
                if (it.age > 0)  edAge.setText(it.age.toString())
                if (it.weight > 0) edWeight.setText(it.weight.roundToString())
                if (it.height > 0) edHeight.setText(it.height.roundToString())
                if (it.gender == "Female") {
                    rbFemale.isChecked = true
                    Glide
                        .with(this@EditProfileActivity)
                        .load(R.drawable.img_avatar_female)
                        .into(ivAvatar)
                } else {
                    rbMale.isChecked = true
                    Glide
                        .with(this@EditProfileActivity)
                        .load(R.drawable.img_avatar_male)
                        .into(ivAvatar)
                }
            }
        }
    }
}