package com.example.landmarklens.data

import android.content.Context
import android.graphics.Bitmap
import com.example.landmarklens.domain.Classification
import com.example.landmarklens.domain.LandmarkClassifier

class TfLiteLandmarkClassifier(
    private val context: Context,
    private val confidenceThreshold: Float = 0.75f,
    private val maxResults: Int = 1
): LandmarkClassifier {

    override fun classifyCurrentFrame(bitmap: Bitmap, rotation: Int): List<Classification> {
        TODO("Not yet implemented")
    }

}