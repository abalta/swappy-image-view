package com.abdullahbalta.swappyimageview

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.abdullahbalta.swappy.OnSwappyListener
import com.abdullahbalta.swappy.SwappyImageView
import net.alhazmy13.mediapicker.Image.ImagePicker
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.Toast


class AddImageGalleryCameraActivity : AppCompatActivity(), OnSwappyListener {

    private lateinit var swappy: SwappyImageView

    override fun onSwappedImages(targetTag: String, draggedTag: String) {
        Toast.makeText(this, "Changed $targetTag image to $draggedTag", Toast.LENGTH_SHORT).show()
    }

    override fun onAddingImage(imageView: ImageView) {
        ImagePicker.Builder(this@AddImageGalleryCameraActivity)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image_gallery_camera)

        swappy = findViewById(R.id.swappy_view)
        swappy.setOnSwappyListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val mPaths = data!!.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH) as List<String>
            swappy.addImage(BitmapFactory.decodeFile(mPaths[0]))
        }
    }

}
