package com.abdullahbalta.swappyimageview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.abdullahbalta.swappy.OnSwappyListener
import com.abdullahbalta.swappy.SwappyImageView

class MainActivity : AppCompatActivity(), OnSwappyListener {

    override fun onAddingImage(imageView: ImageView) {
        Toast.makeText(this, "Add an image from gallery/camera/url", Toast.LENGTH_SHORT).show()
    }

    override fun onSwappedImages(targetTag: String, draggedTag: String) {
        Toast.makeText(this, "Changed $targetTag image to $draggedTag", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val swappy = findViewById<SwappyImageView>(R.id.swappy_view)
        swappy.setOnSwappyListener(this)

        val btnShowCameraActivity: Button = findViewById(R.id.btn_show_gallery_camera)
        btnShowCameraActivity.setOnClickListener { startActivity(Intent(this, AddImageGalleryCameraActivity::class.java)) }

        val btnShowUrlActivity: Button = findViewById(R.id.btn_show_image_url)
        btnShowUrlActivity.setOnClickListener { startActivity(Intent(this, AddImageUrlActivity::class.java)) }
    }

}
