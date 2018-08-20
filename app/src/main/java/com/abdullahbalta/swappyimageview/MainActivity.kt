package com.abdullahbalta.swappyimageview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.abdullahbalta.swappy.OnSwappyListener
import com.abdullahbalta.swappy.SwappyImageView

class MainActivity : AppCompatActivity(), OnSwappyListener {

    override fun onSwappedImages(targetTag: String, draggedTag: String) {
        Toast.makeText(this, "Changed $targetTag image to $draggedTag", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val swappy = findViewById<SwappyImageView>(R.id.swappy_view)
        swappy.setOnSwappyListener(this)
    }

}
