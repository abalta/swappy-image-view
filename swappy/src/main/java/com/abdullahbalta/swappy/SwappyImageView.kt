package com.abdullahbalta.swappy

import android.content.ClipData
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
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
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView

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

    private var whichImageView = 0
    private var resultDraw: Drawable? = null


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

        val addRemoveBtnMain = mainImageView.findViewById<ImageButton>(R.id.btn_add_remove)
        val addRemoveBtnFirst = firstImageView.findViewById<ImageButton>(R.id.btn_add_remove)
        val addRemoveBtnSecond = secondImageView.findViewById<ImageButton>(R.id.btn_add_remove)
        val addRemoveBtnThird = thirdImageView.findViewById<ImageButton>(R.id.btn_add_remove)

        val mainImage = mainImageView.findViewById<ImageView>(R.id.imageView)
        mainImage.tag = id1
        val firstImage = firstImageView.findViewById<ImageView>(R.id.imageView)
        firstImage.tag = id2
        val secondImage = secondImageView.findViewById<ImageView>(R.id.imageView)
        secondImage.tag = id3
        val thirdImage = thirdImageView.findViewById<ImageView>(R.id.imageView)
        thirdImage.tag = id4

        addRemoveBtnMain.setBackgroundResource(params.addIcon)
        addRemoveBtnFirst.setBackgroundResource(params.addIcon)
        addRemoveBtnSecond.setBackgroundResource(params.addIcon)
        addRemoveBtnThird.setBackgroundResource(params.addIcon)

        mainImage.setOnLongClickListener(this)
        firstImage.setOnLongClickListener(this)
        secondImage.setOnLongClickListener(this)
        thirdImage.setOnLongClickListener(this)

        mainImage.setOnDragListener(this)
        firstImage.setOnDragListener(this)
        secondImage.setOnDragListener(this)
        thirdImage.setOnDragListener(this)

        addRemoveBtnMain.setOnClickListener {
            whichImageView = 0
            checkRemoveOrAddImage(mainImage.drawable)
        }
        addRemoveBtnFirst.setOnClickListener{
            whichImageView = 1
            checkRemoveOrAddImage(firstImage.drawable)
        }
        addRemoveBtnSecond.setOnClickListener{
            whichImageView = 2
            checkRemoveOrAddImage(secondImage.drawable)
        }
        addRemoveBtnThird.setOnClickListener{
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
                swappyListener?.onSwappedImages(target.tag.toString(), dragged.tag.toString())
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
    private fun checkRemoveOrAddImage(image: Drawable) {
        /*if (image != null) {
            if (whichImageView == 0)
                if (resultDraw == null)

        } else {

        }*/
    }

    /**
     * Check if small imageviews are filled
     */
    private fun isSmallImageFilled(pos: Int) {
        resultDraw = null

    }
}