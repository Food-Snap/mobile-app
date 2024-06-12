package com.foodsnap.app.ui.image

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.foodsnap.app.R
import com.foodsnap.app.data.Result
import com.foodsnap.app.databinding.ActivityImageBinding
import com.foodsnap.app.ui.ViewModelFactory
import com.foodsnap.app.ui.detail.FoodDetailActivity
import com.foodsnap.app.ui.detail.FoodDetailActivity.Companion.EXTRA_PREDICT
import com.foodsnap.app.utils.ToastManager

class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding
    private val viewModel: ImageViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val uri by lazy { intent.getStringExtra(EXTRA_IMAGE) }
    private val title by lazy { intent.getStringExtra(EXTRA_TITLE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFullScreenSize()
        setView()
        setListener()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setView() {
        binding.apply {
            Glide.with(this@ImageActivity)
                .load(uri)
                .into(ivPhoto)
            title?.let {
                toolbar.visibility = View.VISIBLE
                tvTitle.text = title
            } ?: run {
                llAction.visibility = View.VISIBLE
            }
        }
    }

    private fun setListener() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnClose.setOnClickListener { finish() }
            btnCheck.setOnClickListener {
                uri?.toUri()?.let { uri ->
                    viewModel.predictFood(this@ImageActivity, uri)
                        .observe(this@ImageActivity) { result ->
                            when (result) {
                                is Result.Loading -> {
                                    progressOverlay.visibility = View.VISIBLE
                                }

                                is Result.Error -> {
                                    progressOverlay.visibility = View.GONE
                                    ToastManager.showToast(this@ImageActivity, result.error)
                                }

                                is Result.Success -> {
                                    progressOverlay.visibility = View.GONE
                                    val intent = Intent(this@ImageActivity, FoodDetailActivity::class.java)
                                    intent.putExtra(EXTRA_PREDICT, result.data)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                }
            }
        }
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_TITLE = "extra_title"
    }

}

