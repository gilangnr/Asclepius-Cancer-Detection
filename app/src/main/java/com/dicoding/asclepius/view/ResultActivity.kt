package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.CancerEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.history.HistoryFactory
import com.dicoding.asclepius.view.history.HistoryViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = HistoryFactory.getInstance(this)
        historyViewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        // TODO: Menampilkan hasi
        //  l gambar, prediksi, dan confidence score.
        val imageUri: Uri? = intent.getParcelableExtra("IMAGE_URI")
        val resultText: String? = intent.getStringExtra("RESULT_TEXT")

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }

        resultText?.let {
            binding.resultText.text = it
        }

        binding.btnSave.setOnClickListener {
            imageUri?.let { uri ->
                resultText?.let { result ->
                    val cancerEntity = CancerEntity(
                        image = uri.toString(),
                        result = result
                    )

                    historyViewModel.insertCancers(listOf(cancerEntity))
                }
            }
        }

    }


}