package com.abdullahbalta.swappy

interface OnSwappyListener {
    fun onSwappedImages(targetTag: String, draggedTag: String)
}