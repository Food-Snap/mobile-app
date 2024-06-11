package com.foodsnap.app.ui.camera

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Size
import android.view.OrientationEventListener
import android.view.Surface
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
import com.foodsnap.app.ui.image.ImageActivity
import com.foodsnap.app.ui.image.ImageActivity.Companion.EXTRA_IMAGE
import com.foodsnap.app.utils.ToastManager.showToast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
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
                showToast(this,"Error : ${e.message}")
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
                    showToast(this@CameraActivity,"Error : ${e.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent(this@CameraActivity, ImageActivity::class.java)
                    intent.putExtra(EXTRA_IMAGE, capturedImageFile.toURI().toString())
                    startActivity(intent)
                    finish()
                }
            }
        )
    }

    private fun createFile(context: Context): File {
        val storageDir = File("/storage/emulated/0/Pictures/FoodSnap")

        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_$timeStamp.jpg"
        return File(storageDir, imageFileName)
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }
    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }
}