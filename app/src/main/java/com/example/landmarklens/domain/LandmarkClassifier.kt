package com.example.landmarklens.domain

import android.graphics.Bitmap


interface LandmarkClassifier {
    fun classifyCurrentFrame(bitmap: Bitmap, rotation: Int): List<Classification>
}