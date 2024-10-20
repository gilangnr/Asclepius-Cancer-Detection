package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media Selected")
            showToast("No media selected")
        }
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        currentImageUri?.let { uri ->
            // Inisialisasi ImageClassifierHelper di sini
            val imageClassifierHelper = ImageClassifierHelper(context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        showToast("Error: $error")
                    }

                    override fun onResult(results: List<Classifications>?, inferenceTime: Long) {
                        val resultText = results?.joinToString("\n") { classification ->
                            classification.categories.joinToString(", ") {
                                "${it.label} ${"%.2f".format(it.score * 100)}%"
                            }
                        }
                        resultText?.let {
                            moveToResult(uri, it, inferenceTime)
                        } ?: showToast("Tidak ada hasil yang ditemukan")
                    }
                })

            // Klasifikasi gambar
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: run {
            showToast("Pilih gambar terlebih dahulu")
        }
    }

    private fun moveToResult(imageUri: Uri, resultText: String, inferenceTime : Long) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("IMAGE_URI", imageUri)
        intent.putExtra("RESULT_TEXT", resultText)
        intent.putExtra("INFERENCE", inferenceTime)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}