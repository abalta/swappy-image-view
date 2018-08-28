package com.abdullahbalta.swappyimageview

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.abdullahbalta.swappy.OnSwappyListener
import com.abdullahbalta.swappy.SwappyImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class AddImageUrlActivity : AppCompatActivity(), OnSwappyListener {

    private lateinit var edtUrl: EditText
    private lateinit var swappy: SwappyImageView

    override fun onSwappedImages(targetTag: String, draggedTag: String) {
        Toast.makeText(this, "Changed $targetTag image to $draggedTag", Toast.LENGTH_SHORT).show()
    }

    override fun onAddingImage(imageView: ImageView) {
        Glide.with(this).load(edtUrl.text.toString()).listener(object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                swappy.setImageAddedFailed()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                swappy.setImageAddedSuccess()
                return false
            }

        }).into(imageView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image_url)

        edtUrl = findViewById(R.id.editTextUrl)

        swappy = findViewById(R.id.swappy_view)
        swappy.setOnSwappyListener(this)
    }
}
