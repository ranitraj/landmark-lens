package com.example.landmarklens.data

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import com.example.landmarklens.domain.Classification
import com.example.landmarklens.domain.LandmarkClassifier
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class TfLiteLandmarkClassifier(
    private val context: Context,
    private val confidenceThreshold: Float = 0.75f,
    private val maxResults: Int = 3
): LandmarkClassifier {
    private var classifier: ImageClassifier? = null

    override fun classifyCurrentFrame(bitmap: Bitmap, rotation: Int): List<Classification> {
        if (classifier == null) {
            initializeClassifier()
        }

        val tfLiteImageProcessingOptions = initializeImageProcessingOptions(rotation)
        val tfLiteImageProcessor: ImageProcessor = ImageProcessor.Builder().build()
        val tensorImage: TensorImage = tfLiteImageProcessor.process(
            TensorImage.fromBitmap(bitmap)
        )

        val classificationResults = classifier?.classify(
            tensorImage,
            tfLiteImageProcessingOptions
        )

        // Obtain each category from the result, map them to 'Classification' & lastly remove duplicates using 'distinctBy'
        return classificationResults?.flatMap { curClassification ->
            curClassification.categories.map { curCategory ->
                Classification(
                    detectedObjectName = curCategory.displayName,
                    confidenceScore = curCategory.score
                )
            }
        }?.distinctBy { it.detectedObjectName } ?: emptyList()
    }

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

    /**
     * Builds and returns the TfLite ImageProcessingOptions
     *
     * @param rotation
     * @return ImageProcessingOptions
     */
    private fun initializeImageProcessingOptions(rotation: Int): ImageProcessingOptions {
        return ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()
    }

    /**
     * Returns the orientation of image from the rotation using the
     * TfLite ImageProcessingOptions.Orientation used to internally make use of image axes as an Image metadata
     *
     * @param rotation
     * @return ImageProcessingOptions.Orientation
     */
    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when(rotation) {
            Surface.ROTATION_0 -> ImageProcessingOptions.Orientation.RIGHT_TOP
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
        }
    }
}