package com.example.landmarklens.presentation

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.landmarklens.domain.Classification
import com.example.landmarklens.domain.LandmarkClassifier
import com.example.landmarklens.utils.centerCrop

class LandmarkImageAnalyzer(
    private val classifier: LandmarkClassifier,
    private val onClassificationResults: (List<Classification>) -> Unit
): ImageAnalysis.Analyzer {
    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        // Analyze only 1 frame per-second
        if (frameSkipCounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image
                .toBitmap()
                .centerCrop(321, 321)

            // Calling Domain layer classification method
            val classificationResults = classifier.classifyCurrentFrame(
                bitmap,
                rotationDegrees
            )
            onClassificationResults(classificationResults)
        }
        frameSkipCounter++

        // Fully processed the frame
        image.close()
    }

}