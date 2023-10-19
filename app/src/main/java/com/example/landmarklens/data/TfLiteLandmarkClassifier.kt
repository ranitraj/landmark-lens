package com.example.landmarklens.data

import android.content.Context
import android.graphics.Bitmap
import com.example.landmarklens.domain.Classification
import com.example.landmarklens.domain.LandmarkClassifier
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class TfLiteLandmarkClassifier(
    private val context: Context,
    private val confidenceThreshold: Float = 0.75f,
    private val maxResults: Int = 1
): LandmarkClassifier {
    private var classifier: ImageClassifier? = null

    /**
     * Initializes the TfLite Image Classifier
     */
    private fun initializeClassifier() {
        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .useGpu()
            .build()

        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResults)
            .setScoreThreshold(confidenceThreshold)
            .build()

        try {
            // Create the classifier from the TfLite File
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "assets/landmark_classifier.tflite",
                options
            )
        } catch (exception: IllegalStateException) {
            exception.printStackTrace()
        }
    }

    override fun classifyCurrentFrame(bitmap: Bitmap, rotation: Int): List<Classification> {
        TODO("Not yet implemented")
    }

}