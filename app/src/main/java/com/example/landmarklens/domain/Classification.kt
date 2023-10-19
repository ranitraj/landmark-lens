package com.example.landmarklens.domain

/**
 * Data class to receive the results which comprise of landmark-name &
 * the confidence-score from the Tensorflow Lite model.
 */
data class Classification(
    val detectedObjectName: String,
    val confidenceScore: Float
)
