package com.abdullahbalta.swappy

import android.content.ClipData
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.constraint.ConstraintSet.MATCH_CONSTRAINT
import android.support.constraint.ConstraintSet.VERTICAL
import android.support.constraint.Guideline
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast

class SwappyImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnLongClickListener, View.OnDragListener {

    /**
     * Bottomify params knows about how the item will render
     */
    private val params = SwappyParams()
    /**
     * This holds for main image tag
     */
    private var id1 = "0"
    /**
     * This holds for first image tag
     */
    private var id2 = "1"
    /**
     * This holds for second image tag
     */
    private var id3 = "2"
    /**
     * This holds for third image tag
     */
    private var id4 = "3"
    /**
     * The listener that triggered when active navigation item changed
     */
    private var swappyListener: OnSwappyListener? = null
    /**
     * This holds for margin of the views
     */
    private val FULL_MARGIN = params.itemPadding.toInt()
    private val HALF_MARGIN = params.itemPadding.toInt() / 2
    private val ZERO_MARGIN = 0
    /**
     * ImageView item references
     */
    private val mainImageView = LayoutInflater.from(context).inflate(R.layout.swappy_image_item, this, false)
    private val firstImageView = LayoutInflater.from(context).inflate(R.layout.swappy_image_item, this, false)
    private val secondImageView = LayoutInflater.from(context).inflate(R.layout.swappy_image_item, this, false)
    private val thirdImageView = LayoutInflater.from(context).inflate(R.layout.swappy_image_item, this, false)
    /**
     * Guideline reference
     */
    private val guideLine = Guideline(context)
    /**
     * ImageView references
     */
    private lateinit var mainImage: ImageView
    private lateinit var firstImage: ImageView
    private lateinit var secondImage: ImageView
    private lateinit var thirdImage: ImageView
    /**
     * Add/Remove Button references
     */
    private lateinit var addRemoveBtnMain: ImageButton
    private lateinit var addRemoveBtnFirst: ImageButton
    private lateinit var addRemoveBtnSecond: ImageButton
    private lateinit var addRemoveBtnThird: ImageButton


    private var whichImageView = 0
    private var resultDraw: Drawable? = null

    companion object {
        private var mainImageDrawable: Drawable? = null
        private var firstImageDrawable: Drawable? = null
        private var secondImageDrawable: Drawable? = null
        private var thirdImageDrawable: Drawable? = null
    }

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.SwappyImageView)

        if (a.hasValue(R.styleable.SwappyImageView_add_icon)) {
            val addIcon = a.getResourceId(R.styleable.SwappyImageView_add_icon, R.drawable.ic_add_circle)
            params.addIcon = addIcon
        }

        if (a.hasValue(R.styleable.SwappyImageView_remove_icon)) {
            val removeIcon = a.getResourceId(R.styleable.SwappyImageView_remove_icon, R.drawable.ic_remove_circle)
            params.removeIcon = removeIcon
        }

        if (a.hasValue(R.styleable.SwappyImageView_item_padding)) {
            val px = a.getDimension(R.styleable.SwappyImageView_item_padding, 0f)
            params.itemPadding = px
        }

        if (a.hasValue(R.styleable.SwappyImageView_main_drawable)) {
            val mainDrawable = a.getResourceId(R.styleable.SwappyImageView_main_drawable, 0)
            params.mainImage = mainDrawable
        }

        if (a.hasValue(R.styleable.SwappyImageView_first_drawable)) {
            val firstDrawable = a.getResourceId(R.styleable.SwappyImageView_first_drawable, 0)
            params.firstImage = firstDrawable
        }

        if (a.hasValue(R.styleable.SwappyImageView_second_drawable)) {
            val secondDrawable = a.getResourceId(R.styleable.SwappyImageView_second_drawable, 0)
            params.secondImage = secondDrawable
        }

        if (a.hasValue(R.styleable.SwappyImageView_third_drawable)) {
            val thirdDrawable = a.getResourceId(R.styleable.SwappyImageView_third_drawable, 0)
            params.thirdImage = thirdDrawable
        }

        if (a.hasValue(R.styleable.SwappyImageView_placeholder)) {
            val placeholder = a.getResourceId(R.styleable.SwappyImageView_placeholder, R.drawable.ic_image_empty)
            params.placeholder = placeholder
        }

        a.recycle()
        prepareView()
    }

    /**
     * For setting OnNavigationItemChangeListener
     */
    fun setOnSwappyListener(listener: OnSwappyListener) {
        this.swappyListener = listener
    }

    private fun prepareView() {
        addViews()
        setLayoutParams()
        setConstraints()
        setListeners()

    }

    private fun addViews() {
        guideLine.id = R.id.guideline_id
        addView(guideLine)

        addRemoveBtnMain = mainImageView.findViewById(R.id.btn_add_remove)
        addRemoveBtnMain.id = R.id.main_add_remove_id
        addRemoveBtnFirst = firstImageView.findViewById(R.id.btn_add_remove)
        addRemoveBtnFirst.id = R.id.first_add_remove_id
        addRemoveBtnSecond = secondImageView.findViewById(R.id.btn_add_remove)
        addRemoveBtnSecond.id = R.id.second_add_remove_id
        addRemoveBtnThird = thirdImageView.findViewById(R.id.btn_add_remove)
        addRemoveBtnThird.id = R.id.third_add_remove_id

        mainImageView.id = R.id.main_image_view_id
        mainImageView.tag = id1
        addView(mainImageView)

        firstImageView.id = R.id.first_image_view_id
        firstImageView.tag = id2
        addView(firstImageView)

        secondImageView.id = R.id.second_image_view_id
        secondImageView.tag = id3
        addView(secondImageView)

        thirdImageView.id = R.id.third_image_view_id
        thirdImageView.tag = id4
        addView(thirdImageView)
    }

    private fun setLayoutParams() {
        val guidelineParams = guideLine.layoutParams as ConstraintLayout.LayoutParams
        guidelineParams.orientation = VERTICAL
        guidelineParams.guidePercent = 0.75f
        guideLine.layoutParams = guidelineParams

        val mainImageViewParams = mainImageView.layoutParams as ConstraintLayout.LayoutParams
        mainImageViewParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        mainImageViewParams.width = MATCH_CONSTRAINT
        mainImageView.layoutParams = mainImageViewParams

        val firstImageViewParams = firstImageView.layoutParams as ConstraintLayout.LayoutParams
        firstImageViewParams.height = MATCH_CONSTRAINT
        firstImageViewParams.width = MATCH_CONSTRAINT
        firstImageView.layoutParams = firstImageViewParams

        val secondImageViewParams = secondImageView.layoutParams as ConstraintLayout.LayoutParams
        secondImageViewParams.height = MATCH_CONSTRAINT
        secondImageViewParams.width = MATCH_CONSTRAINT
        secondImageView.layoutParams = secondImageViewParams

        val thirdImageViewParams = thirdImageView.layoutParams as ConstraintLayout.LayoutParams
        thirdImageViewParams.height = MATCH_CONSTRAINT
        thirdImageViewParams.width = MATCH_CONSTRAINT
        thirdImageView.layoutParams = thirdImageViewParams
    }

    private fun setConstraints() {
        val constraintSet = ConstraintSet()

        constraintSet.clone(this)

        constraintSet.connect(mainImageView.id, ConstraintSet.START, this.id, ConstraintSet.START, FULL_MARGIN)
        constraintSet.connect(mainImageView.id, ConstraintSet.TOP, this.id, ConstraintSet.TOP, FULL_MARGIN)
        constraintSet.connect(mainImageView.id, ConstraintSet.END, guideLine.id, ConstraintSet.START, HALF_MARGIN)
        constraintSet.connect(mainImageView.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.BOTTOM, FULL_MARGIN)

        constraintSet.connect(firstImageView.id, ConstraintSet.END, this.id, ConstraintSet.END, FULL_MARGIN)
        constraintSet.connect(firstImageView.id, ConstraintSet.TOP, mainImageView.id, ConstraintSet.TOP, ZERO_MARGIN)
        constraintSet.connect(firstImageView.id, ConstraintSet.START, guideLine.id, ConstraintSet.END, HALF_MARGIN)
        constraintSet.connect(firstImageView.id, ConstraintSet.BOTTOM, secondImageView.id, ConstraintSet.TOP, FULL_MARGIN)

        constraintSet.connect(secondImageView.id, ConstraintSet.END, this.id, ConstraintSet.END, FULL_MARGIN)
        constraintSet.connect(secondImageView.id, ConstraintSet.TOP, firstImageView.id, ConstraintSet.BOTTOM, HALF_MARGIN)
        constraintSet.connect(secondImageView.id, ConstraintSet.START, guideLine.id, ConstraintSet.END, HALF_MARGIN)
        constraintSet.connect(secondImageView.id, ConstraintSet.BOTTOM, thirdImageView.id, ConstraintSet.TOP, FULL_MARGIN)

        constraintSet.connect(thirdImageView.id, ConstraintSet.END, this.id, ConstraintSet.END, FULL_MARGIN)
        constraintSet.connect(thirdImageView.id, ConstraintSet.TOP, secondImageView.id, ConstraintSet.BOTTOM, HALF_MARGIN)
        constraintSet.connect(thirdImageView.id, ConstraintSet.START, guideLine.id, ConstraintSet.END, HALF_MARGIN)
        constraintSet.connect(thirdImageView.id, ConstraintSet.BOTTOM, mainImageView.id, ConstraintSet.BOTTOM, ZERO_MARGIN)

        constraintSet.applyTo(this)
    }

    private fun setListeners() {

        mainImage = mainImageView.findViewById(R.id.imageView)
        mainImage.setImageResource(params.mainImage)
        if (params.mainImage != 0) {
            mainImage.setBackgroundResource(0)
            addRemoveBtnMain.setBackgroundResource(params.removeIcon)
        } else {
            addRemoveBtnMain.setBackgroundResource(params.addIcon)
            mainImage.setBackgroundResource(params.placeholder)
        }

        firstImage = firstImageView.findViewById(R.id.imageView)
        firstImage.setImageResource(params.firstImage)
        if (params.firstImage != 0) {
            firstImage.setBackgroundResource(0)
            addRemoveBtnFirst.setBackgroundResource(params.removeIcon)
        } else {
            addRemoveBtnFirst.setBackgroundResource(params.addIcon)
            firstImage.setBackgroundResource(params.placeholder)
        }

        secondImage = secondImageView.findViewById(R.id.imageView)
        secondImage.setImageResource(params.secondImage)
        if (params.secondImage != 0) {
            secondImage.setBackgroundResource(0)
            addRemoveBtnSecond.setBackgroundResource(params.removeIcon)
        } else {
            addRemoveBtnSecond.setBackgroundResource(params.addIcon)
            secondImage.setBackgroundResource(params.placeholder)
        }

        thirdImage = thirdImageView.findViewById(R.id.imageView)
        thirdImage.setImageResource(params.thirdImage)
        if (params.thirdImage != 0) {
            thirdImage.setBackgroundResource(0)
            addRemoveBtnThird.setBackgroundResource(params.removeIcon)
        } else {
            addRemoveBtnThird.setBackgroundResource(params.addIcon)
            thirdImage.setBackgroundResource(params.placeholder)
        }

        mainImage.setOnLongClickListener(this)
        firstImage.setOnLongClickListener(this)
        secondImage.setOnLongClickListener(this)
        thirdImage.setOnLongClickListener(this)

        mainImage.setOnDragListener(this)
        firstImage.setOnDragListener(this)
        secondImage.setOnDragListener(this)
        thirdImage.setOnDragListener(this)

        /*mainImage.viewTreeObserver.addOnGlobalLayoutListener {
            checkImageAdded(mainImage)
        }

        firstImage.viewTreeObserver.addOnGlobalLayoutListener {
            checkImageAdded(firstImage)
        }

        secondImage.viewTreeObserver.addOnGlobalLayoutListener {
            checkImageAdded(secondImage)
        }

        thirdImage.viewTreeObserver.addOnGlobalLayoutListener {
            checkImageAdded(thirdImage)
        }*/

        /*viewTreeObserver.addOnDrawListener {
            checkImageAdded(mainImage)
            checkImageAdded(firstImage)
            checkImageAdded(secondImage)
            checkImageAdded(thirdImage)
        }*/

        addRemoveBtnMain.setOnClickListener {
            whichImageView = 0
            checkRemoveOrAddImage(mainImage.drawable)
        }
        addRemoveBtnFirst.setOnClickListener {
            whichImageView = 1
            checkRemoveOrAddImage(firstImage.drawable)
        }
        addRemoveBtnSecond.setOnClickListener {
            whichImageView = 2
            checkRemoveOrAddImage(secondImage.drawable)
        }
        addRemoveBtnThird.setOnClickListener {
            whichImageView = 3
            checkRemoveOrAddImage(thirdImage.drawable)
        }
    }

    /**
     * Swapping imageviews via dragging
     * If target and dragged views have drawable
     */
    override fun onDrag(v: View?, dragEvent: DragEvent?): Boolean {
        val event = dragEvent?.action
        val dragged = dragEvent?.localState as AppCompatImageView
        val target = v as AppCompatImageView
        val targetDrawable = target.drawable
        val draggedDrawable = dragged.drawable
        when (event) {
            DragEvent.ACTION_DRAG_ENTERED -> {
            }
            DragEvent.ACTION_DRAG_EXITED -> {
            }
            DragEvent.ACTION_DROP -> if (targetDrawable != null && draggedDrawable != null) {
                target.setImageDrawable(draggedDrawable)
                dragged.setImageDrawable(targetDrawable)
                swappyListener?.onSwappedImages(target.id.toString(), dragged.id.toString())
            }
        }
        return true
    }

    /**
     * Starts dragging images via long click
     */
    override fun onLongClick(v: View?): Boolean {
        val data = ClipData.newPlainText("", "")
        val dragShadowBuilder = View.DragShadowBuilder(v)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            v?.startDragAndDrop(data, dragShadowBuilder, v, 0)
        } else {
            v?.startDrag(data, dragShadowBuilder, v, 0)
        }
        return true
    }

    /**
     * Check item has image or null
     * If image is not null remove image
     * If image is null add image
     */
    private fun checkRemoveOrAddImage(image: Drawable?) {
        if (image != null) {
            if (whichImageView == 0) {
                if (resultDraw == null) {
                    isSmallImageFilled(0)
                    if (resultDraw == null) {
                        isSmallImageFilled(1)
                        if (resultDraw == null) {
                            isSmallImageFilled(2)
                            if (resultDraw == null) {
                                isSmallImageFilled(3)
                                if (resultDraw == null) {
                                    Toast.makeText(context, context.getString(R.string.no_images_left), Toast.LENGTH_SHORT).show()
                                    mainImage.setImageDrawable(resultDraw)
                                    mainImage.setBackgroundResource(params.placeholder)
                                    addRemoveBtnMain.setBackgroundResource(params.addIcon)
                                } else
                                    mainImage.setImageDrawable(resultDraw)
                            } else
                                mainImage.setImageDrawable(resultDraw)
                        } else
                            mainImage.setImageDrawable(resultDraw)
                    } else
                        mainImage.setImageDrawable(resultDraw)
                }
            } else if (whichImageView == 1) {
                isSmallImageFilled(1)
            } else if (whichImageView == 2) {
                isSmallImageFilled(2)
            } else if (whichImageView == 3) {
                isSmallImageFilled(3)
            }
        } else {
            when (whichImageView) {
                0 -> {
                    swappyListener?.onAddingImage(mainImage)
                }
                1 -> {
                    swappyListener?.onAddingImage(firstImage)
                }
                2 -> {
                    swappyListener?.onAddingImage(secondImage)
                }
                3 -> {
                    swappyListener?.onAddingImage(thirdImage)
                }
            }

        }
        resultDraw = null
    }

    /**
     * Check if small imageviews are filled
     */
    private fun isSmallImageFilled(pos: Int) {
        resultDraw = null
        if (pos == 0) {
            resultDraw = firstImage.drawable
            if (secondImage.drawable != null) {
                addImage(firstImage, addRemoveBtnFirst, secondImage.drawable)
                if (thirdImage.drawable != null) {
                    addImage(secondImage, addRemoveBtnSecond, thirdImage.drawable)
                    removeImage(thirdImage, addRemoveBtnThird)
                } else {
                    removeImage(secondImage, addRemoveBtnSecond)
                }
            } else if (thirdImage.drawable != null) {
                addImage(firstImage, addRemoveBtnFirst, thirdImage.drawable)
                removeImage(thirdImage, addRemoveBtnThird)
            } else {
                removeImage(firstImage, addRemoveBtnFirst)
            }
        } else if (pos == 1 && firstImage.drawable != null) {
            resultDraw = firstImage.drawable
            if (secondImage.drawable != null) {
                addImage(firstImage, addRemoveBtnFirst, secondImage.drawable)
                removeImage(secondImage, addRemoveBtnSecond)
                if (thirdImage.drawable != null) {
                    addImage(secondImage, addRemoveBtnSecond, thirdImage.drawable)
                    removeImage(thirdImage, addRemoveBtnThird)
                } else {
                    removeImage(secondImage, addRemoveBtnSecond)
                }
            } else if (thirdImage.drawable != null) {
                addImage(firstImage, addRemoveBtnFirst, thirdImage.drawable)
                removeImage(thirdImage, addRemoveBtnThird)
            } else {
                removeImage(firstImage, addRemoveBtnFirst)
            }
        } else if (pos == 2 && secondImage.drawable != null) {
            resultDraw = secondImage.drawable
            if (thirdImage.drawable != null) {
                addImage(secondImage, addRemoveBtnSecond, thirdImage.drawable)
                removeImage(thirdImage, addRemoveBtnThird)
            } else {
                removeImage(secondImage, addRemoveBtnSecond)
            }
        } else if (pos == 3 && thirdImage.drawable != null) {
            resultDraw = thirdImage.drawable
            removeImage(thirdImage, addRemoveBtnThird)
        }
    }

    /**
     * Check if image successfully added
     */
    fun setImageAddedSuccess() {
        when (whichImageView) {
            0 -> {
                addRemoveBtnMain.setBackgroundResource(params.removeIcon)
                mainImage.setBackgroundResource(0)
            }
            1 -> {
                addRemoveBtnFirst.setBackgroundResource(params.removeIcon)
                firstImage.setBackgroundResource(0)
            }
            2 -> {
                addRemoveBtnSecond.setBackgroundResource(params.removeIcon)
                secondImage.setBackgroundResource(0)
            }
            3 -> {
                addRemoveBtnThird.setBackgroundResource(params.removeIcon)
                thirdImage.setBackgroundResource(0)
            }
        }
    }

    /**
     * Check id image adding failed
     */
    fun setImageAddedFailed() {
        when (whichImageView) {
            0 -> { removeImage(mainImage, addRemoveBtnMain) }
            1 -> { removeImage(firstImage, addRemoveBtnFirst) }
            2 -> { removeImage(secondImage, addRemoveBtnSecond) }
            3 -> { removeImage(thirdImage, addRemoveBtnThird) }
        }
    }

    /**
     * Remove image
     */
    private fun removeImage(imageView: ImageView, imageButton: ImageButton) {
        imageView.setImageDrawable(null)
        imageView.setBackgroundResource(params.placeholder)
        imageButton.setBackgroundResource(params.addIcon)
    }

    /**
     * Add image
     */
    private fun addImage(imageView: ImageView, imageButton: ImageButton, drawable: Drawable?) {
        if (drawable != null) {
            imageButton.setBackgroundResource(params.removeIcon)
            imageView.setBackgroundResource(0)
        } else {
            imageButton.setBackgroundResource(params.addIcon)
            imageView.setBackgroundResource(params.placeholder)
        }
        imageView.setImageDrawable(drawable)
    }

    fun addImage(bitmap: Bitmap) {
        addImageDrawableBitmap(null, bitmap)
    }

    fun addImage(drawable: Drawable?) {
        addImageDrawableBitmap(drawable, null)
    }

    private fun setImage(imageView: ImageView, drawable: Drawable?, bitmap: Bitmap?) {
        if (drawable != null)
            imageView.setImageDrawable(drawable)
        else
            imageView.setImageBitmap(bitmap)
    }

    private fun addImageDrawableBitmap(drawable: Drawable?, bitmap: Bitmap?) {
        when (whichImageView) {
            0 -> {
                setImage(mainImage, drawable, bitmap)
                addRemoveBtnMain.setBackgroundResource(params.removeIcon)
                mainImage.setBackgroundResource(0)
            }
            1 -> {
                setImage(firstImage, drawable, bitmap)
                addRemoveBtnFirst.setBackgroundResource(params.removeIcon)
                firstImage.setBackgroundResource(0)
            }
            2 -> {
                setImage(secondImage, drawable, bitmap)
                addRemoveBtnSecond.setBackgroundResource(params.removeIcon)
                secondImage.setBackgroundResource(0)
            }
            3 -> {
                setImage(thirdImage, drawable, bitmap)
                addRemoveBtnThird.setBackgroundResource(params.removeIcon)
                thirdImage.setBackgroundResource(0)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        mainImageDrawable = mainImage.drawable
        firstImageDrawable = firstImage.drawable
        secondImageDrawable = secondImage.drawable
        thirdImageDrawable = thirdImage.drawable
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        addImage(mainImage, addRemoveBtnMain, mainImageDrawable)
        addImage(firstImage, addRemoveBtnFirst, firstImageDrawable)
        addImage(secondImage, addRemoveBtnSecond, secondImageDrawable)
        addImage(thirdImage, addRemoveBtnThird, thirdImageDrawable)
    }

}