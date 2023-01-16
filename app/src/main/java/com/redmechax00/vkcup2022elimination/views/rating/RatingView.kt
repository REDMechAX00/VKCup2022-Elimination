package com.redmechax00.vkcup2022elimination.views.rating

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewRatingBinding
import com.redmechax00.vkcup2022elimination.views.rating.view_items.RatingStarView

class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IEditableRating {

    interface RatingListener {
        fun onDone(rating: Int)
    }

    private var ratingListener: RatingListener? = null

    fun setAnswerListener(ratingListener: RatingListener?) {
        this.ratingListener = ratingListener
    }

    private val ratingContainer: FlexboxLayout
    private val listOfStars = mutableListOf<RatingStarView>()

    private val starSize: Int
    private var countOfStars: Int

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewRatingBinding.inflate(inflater, this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RatingView, 0, 0)
        try {
            starSize = attributes.getDimensionPixelSize(
                R.styleable.RatingView_star_size,
                context.resources.getDimensionPixelSize(R.dimen.rating_star_default_size)
            )
            countOfStars =
                attributes.getInt(
                    R.styleable.RatingView_count_of_stars,
                    5
                )

        } finally {
            attributes.recycle()
        }

        ratingContainer = binding.ratingContainer

        createView()
    }

    private fun createView() {
        clearView()
        val customParams = LayoutParams(starSize, starSize)
        for (i in 0 until countOfStars) {
            val starView = RatingStarView(context)
            if (starSize != 0) starView.setLayoutParams(customParams)
            starView.setOnTouchListener(starTouchListener)
            listOfStars.add(starView)
            ratingContainer.addView(starView)
        }
    }

    private val starTouchListener = (OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.disallowScrollInAllParents(true)
                setCurrentRating(listOfStars.indexOf(v) + 1)
            }
            MotionEvent.ACTION_UP -> {
                v.performClick()
                v.disallowScrollInAllParents(false)
                ratingListener?.onDone(getCurrentRating())
            }
            MotionEvent.ACTION_CANCEL ->{
                v.performClick()
                v.disallowScrollInAllParents(false)
                ratingListener?.onDone(getCurrentRating())
            }
            else -> {

            }
        }
        true
    })

    private fun View.disallowScrollInAllParents(disallow: Boolean){
        var rootParent = this.parent
        while (rootParent.parent != null) {
            rootParent = rootParent.parent
            if(rootParent is RecyclerView){
                rootParent.requestDisallowInterceptTouchEvent(disallow)
            }
            if(rootParent is ScrollView){
                rootParent.requestDisallowInterceptTouchEvent(disallow)
            }
        }
    }

    override fun setCountOfStars(count: Int) {
        countOfStars = count
        createView()
    }

    override fun getCountOfStars(): Int = countOfStars

    override fun setCurrentRating(rating: Int) {
        listOfStars.forEach { it.setStarIsSelected(false) }
        for (i in 0 until rating) {
            listOfStars[i].setStarIsSelected(true)
        }
    }

    override fun getCurrentRating(): Int {
        var rating = 0
        listOfStars.forEach {
            if (it.getIsSelected()) rating++
        }
        return rating
    }

    private fun clearView() {
        listOfStars.forEach { it.setOnTouchListener(null) }
        listOfStars.clear()
        ratingContainer.removeAllViews()
    }
}