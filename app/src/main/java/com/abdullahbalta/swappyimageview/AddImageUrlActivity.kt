package com.abdullahbalta.swappyimageview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.abdullahbalta.swappy.OnSwappyListener
import com.abdullahbalta.swappy.SwappyImageView
import com.bumptech.glide.Glide

class AddImageUrlActivity : AppCompatActivity(), OnSwappyListener {

    private lateinit var edtUrl: EditText

    override fun onSwappedImages(targetTag: String, draggedTag: String) {
        Toast.makeText(this, "Changed $targetTag image to $draggedTag", Toast.LENGTH_SHORT).show()
    }

    override fun onAddingImage(imageView: ImageView) {
        Glide.with(this).load(edtUrl.text.toString()).into(imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image_url)

        edtUrl = findViewById(R.id.editTextUrl)

        val swappy = findViewById<SwappyImageView>(R.id.swappy_view)
        swappy.setOnSwappyListener(this)
    }
}
