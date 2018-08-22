package com.abdullahbalta.swappyimageview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import com.abdullahbalta.swappy.OnSwappyListener
import com.abdullahbalta.swappy.SwappyImageView
import com.evernote.android.state.State
import com.evernote.android.state.StateSaver

class MainActivity : AppCompatActivity(), OnSwappyListener {

    @State
    var mValue: Int = 0

    override fun onAddingImage(imageView: ImageView) {
    }

    override fun onSwappedImages(targetTag: String, draggedTag: String) {
        Toast.makeText(this, "Changed $targetTag image to $draggedTag", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StateSaver.restoreInstanceState(this, savedInstanceState);

        val swappy = findViewById<SwappyImageView>(R.id.swappy_view)
        swappy.setOnSwappyListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        StateSaver.saveInstanceState(this, outState)
    }

}
