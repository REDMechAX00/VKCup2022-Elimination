package com.redmechax00.vkcup2022elimination.views.matching.view_items

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewMatchingButtonBinding

class MatchingButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ICustomizableMatchingButton {

    companion object {
        const val ANIMATION_DURATION = 400L
    }

    private val textView: TextView
    private val strokeDrawable: GradientDrawable
    private val backgroundDrawable: GradientDrawable

    private var buttonIsSelected = false

    private val colorNormal = ContextCompat.getColor(context, R.color.matching_color_button_normal)
    private val colorSelected =
        ContextCompat.getColor(context, R.color.matching_color_button_selected)
    private val colorStrokeNormal =
        ContextCompat.getColor(context, R.color.matching_color_button_stroke)

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewMatchingButtonBinding.inflate(inflater, this, true)

        textView = binding.matchingButtonText
        val backgroundView = binding.matchingButtonBackground

        strokeDrawable =
            (backgroundView.background as LayerDrawable).findDrawableByLayerId(R.id.matching_button_background_stroke)
                    as GradientDrawable
        backgroundDrawable =
            (backgroundView.background as LayerDrawable).findDrawableByLayerId(R.id.matching_button_background)
                    as GradientDrawable
        setStrokeColor(false)
        setBackgroundButtonColor(colorNormal)
    }

    override fun setText(text: String) {
        textView.text = text
    }

    override fun getText(): String = textView.text.toString()

    override fun setTextSize(sp: Int) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(sp))
    }

    override fun getTextSize(): Int = px2sp(textView.textSize)

    override fun setSelected(isSelected: Boolean) {
        buttonIsSelected = isSelected
        setBackgroundColorAnimated(isSelected)
        setStrokeColor(isSelected)
    }

    override fun cleanSelected() {
        buttonIsSelected = false
        setBackgroundButtonColor(colorNormal)
        setStrokeColor(false)
    }

    private fun setBackgroundColorAnimated(isSelected: Boolean) {
        val currentColor = backgroundDrawable.color?.defaultColor ?: colorNormal
        val newColor = if (isSelected) colorSelected else colorNormal
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), currentColor, newColor)
        colorAnimation.duration = ANIMATION_DURATION
        colorAnimation.addUpdateListener { animator ->
            setBackgroundButtonColor(animator.animatedValue as Int)
        }
        colorAnimation.start()
    }

    private fun setStrokeColor(isSelected: Boolean) {
        val color = if (isSelected) colorSelected else colorStrokeNormal
        strokeDrawable.setTintList(ColorStateList.valueOf(color))
    }

    private fun setBackgroundButtonColor(color: Int) {
        backgroundDrawable.setTintList(ColorStateList.valueOf(color))
    }

    override fun getButtonIsSelected(): Boolean = buttonIsSelected

    private fun sp2px(sp: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )

    private fun px2sp(px: Float) = (px / context.resources.displayMetrics.scaledDensity).toInt()
}