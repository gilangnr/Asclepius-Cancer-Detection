package com.dicoding.asclepius.view.analyze

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentAnalyzeBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.view.ResultActivity
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class AnalyzeFragment : Fragment() {

    private var _binding : FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!
//    private var currentImageUri: Uri? = null

    private val viewModel: AnalyzeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showImage()

        binding.galleryButton.setOnClickListener {
            startGallery()
        }
        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            startCrop(uri)
        } else {
            Log.d("Photo Picker", getString(R.string.no_media_selected))
            showToast(getString(R.string.no_media_selected))
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        viewModel.currentImageUri?.let { uri ->
            val imageClassifierHelper = ImageClassifierHelper(
                context = requireContext(),
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
                            moveToResult(uri, it)
                        } ?: showToast(getString(R.string.tidak_ada_hasil_yang_ditemukan))
                    }
                }
            )

            // Klasifikasi gambar
            imageClassifierHelper.classifyStaticImage(uri)
        } ?: run {
            showToast(getString(R.string.pilih_gambar_terlebih_dahulu))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun moveToResult(imageUri: Uri, resultText: String) {
        val intent = Intent(requireContext(), ResultActivity::class.java).apply {
            putExtra("IMAGE_URI", imageUri)
            putExtra("RESULT_TEXT", resultText)
        }
        startActivity(intent)
    }

    private val cropActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val resultUri = data?.let { UCrop.getOutput(it) }
            if (resultUri != null) {
                viewModel.currentImageUri = resultUri
                showImage()
            } else {
                showToast(getString(R.string.error_no_image_returned_after_cropping))
            }
        } else {
            if (result.resultCode == Activity.RESULT_CANCELED) {
                Log.d("uCrop", "Crop cancelled by user")
            } else {
                val cropError = UCrop.getError(result.data!!)
                cropError?.let {
                    showToast("Crop failed: ${it.message}")
                }
            }
        }
    }

    private fun startCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_${System.currentTimeMillis()}.jpg"))
        val uCrop = UCrop.of(uri, destinationUri)
        uCrop.withAspectRatio(1f, 1f)
        uCrop.withMaxResultSize(1000, 1000)

        val intent = uCrop.getIntent(requireContext())
        cropActivityResultLauncher.launch(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}