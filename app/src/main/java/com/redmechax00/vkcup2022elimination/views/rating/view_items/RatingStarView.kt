package com.redmechax00.vkcup2022elimination.views.rating.view_items

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewRatingStarBinding

class RatingStarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ICustomizableRatingStar {

    private val starView: ImageView

    private var starIsSelected = false

    private val backgroundOutline = R.drawable.rating_ic_star_outline
    private val backgroundFilled = R.drawable.rating_ic_star_filled

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewRatingStarBinding.inflate(inflater, this)

        starView = binding.ratingStarImageView
        starView.setBackgroundResource(backgroundOutline)
    }

    override fun setStarIsSelected(isSelected: Boolean) {
        when (isSelected) {
            true -> {
                starIsSelected = true
                starView.setBackgroundResource(backgroundFilled)
            }
            false -> {
                starIsSelected = false
                starView.setBackgroundResource(backgroundOutline)
            }
        }
    }

    override fun getIsSelected(): Boolean = starIsSelected

    fun setLayoutParams(params: LayoutParams) {
        starView.layoutParams = params
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}