package com.foodsnap.app.ui.camera

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.foodsnap.app.databinding.ActivityCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null
    private var cameraPosition: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFullScreenSize()
        startCameraService()
        setListeners()
    }

    private fun setFullScreenSize() {
        enableEdgeToEdge()
    }

    private fun setListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener { finish() }
            btnSwitch.setOnClickListener {
                cameraPosition =
                    if (cameraPosition == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                startCameraService()
            }
            btnShutter.setOnClickListener {
                takePhoto()
            }
        }
    }

    private fun startCameraService() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(720, 1280))
                .build()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().setTargetResolution(Size(720, 1280)).build()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraPosition,
                    preview,
                    imageCapture, imageAnalysis
                )
            } catch (e: Exception) {
                showToast("Error : ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val capturedImageFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(capturedImageFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    showToast("Error : ${e.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()

                    intent.putExtra(EXTRA_IMAGE, capturedImageFile)
                    intent.putExtra(
                        EXTRA_CAMERA_MODE,
                        cameraPosition == CameraSelector.DEFAULT_BACK_CAMERA
                    )

                    setResult(RESULT_OK, intent)
                    this@CameraActivity.finish()
                }
            }
        )
    }

    private fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, "image").apply { mkdirs() }
        }

        return File(
            if (
                mediaDir != null && mediaDir.exists()
            ) mediaDir else application.filesDir, "${
                SimpleDateFormat(
                    "ddMMyySSSSS",
                    Locale.getDefault()
                ).format(System.currentTimeMillis())
            }.jpg"
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_CAMERA_MODE = "extra_camera_mode"
    }
}