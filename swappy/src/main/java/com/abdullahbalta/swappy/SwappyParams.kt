package com.abdullahbalta.swappy

import android.support.annotation.DrawableRes

class SwappyParams {
    @DrawableRes var addIcon: Int = R.drawable.ic_add_circle
    @DrawableRes var removeIcon: Int = R.drawable.ic_remove_circle
    @DrawableRes var placeholder: Int = R.drawable.ic_image_empty
    var itemPadding: Float = 8f
    @DrawableRes var mainImage: Int = 0
    @DrawableRes var firstImage: Int = 0
    @DrawableRes var secondImage: Int = 0
    @DrawableRes var thirdImage: Int = 0
}