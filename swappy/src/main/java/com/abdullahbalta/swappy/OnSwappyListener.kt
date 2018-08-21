package com.abdullahbalta.swappy

import android.widget.ImageView

interface OnSwappyListener {
    fun onSwappedImages(targetTag: String, draggedTag: String)
    fun onAddingImage(imageView: ImageView)
}