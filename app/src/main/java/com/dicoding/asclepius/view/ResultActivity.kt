package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasi
        //  l gambar, prediksi, dan confidence score.
        val imageUri: Uri? = intent.getParcelableExtra("IMAGE_URI")
        val resultText: String? = intent.getStringExtra("RESULT_TEXT")
        val inferenceTime: Long = intent.getLongExtra("INFERENCE", 0L)

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }

        resultText?.let {
            binding.resultText.text = it
        }

        inferenceTime.let {
            binding.inference.text = "inference time : $it ms"
        }
    }


}