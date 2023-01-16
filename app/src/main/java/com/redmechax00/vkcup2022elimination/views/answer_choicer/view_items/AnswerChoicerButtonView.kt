package com.redmechax00.vkcup2022elimination.views.answer_choicer.view_items

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewAnswerChoicerButtonBinding

class AnswerChoicerButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    ICustomizableAnswerChoicerButton {

    companion object {
        const val ANIMATION_DURATION = 1000L
    }

    //Title
    private val textAnswer: TextView

    //Circle View
    private val radioImage: ImageView

    //Percent Value Info
    private val answerInfoContainer: View
    private val textPercent: TextView
    private val imageAnswerRight: ImageView
    private val imageAnswerWrong: ImageView

    //Percent Visualization
    private val progressBarContainer: View
    private val progressBar: ProgressBar

    private val answerRightContainer: View

    //Separator
    private val itemSeparator: View

    private var countOfClicks: Int = 0

    private val colorNormal =
        ContextCompat.getColor(context, R.color.answer_choicer_color_selected_normal)
    private val colorRight =
        ContextCompat.getColor(context, R.color.answer_choicer_color_selected_right)
    private val colorWrong =
        ContextCompat.getColor(context, R.color.answer_choicer_color_selected_wrong)

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewAnswerChoicerButtonBinding.inflate(inflater, this)

        textAnswer = binding.answerChoicerButtonText
        radioImage = binding.answerChoicerButtonRadioImage
        answerInfoContainer = binding.answerChoicerButtonAnswerInfoContainer
        textPercent = binding.answerChoicerButtonTextPercent
        imageAnswerRight = binding.answerChoicerButtonImageAnswerRight
        imageAnswerWrong = binding.answerChoicerButtonImageAnswerWrong
        progressBarContainer = binding.answerChoicerButtonProgressContainer
        progressBar = binding.answerChoicerButtonProgress
        answerRightContainer = binding.answerChoicerButtonRightContainer
        itemSeparator = binding.answerChoicerButtonItemSeparator
    }

    private fun setMinHeight() {
        answerRightContainer.post {
            answerRightContainer.minimumHeight =
                textAnswer.height +
                        getDimensionInIntFromRes(R.dimen.answer_choicer_button_image_answer_size)
        }
    }

    override fun setText(text: String) {
        textAnswer.text = text
        setMinHeight()
    }

    override fun getText(): String = textAnswer.text.toString()

    override fun setTextSize(sp: Int) {
        textAnswer.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(sp))
    }

    override fun getTextSize(): Int = px2sp(textAnswer.textSize)

    override fun setCountOfClicks(count: Int) {
        countOfClicks = count
    }

    override fun getCountOfClicks(): Int = countOfClicks

    override fun increaseCountOfClicks() {
        countOfClicks++
    }

    fun showSeparator() {
        itemSeparator.visibility = View.VISIBLE
    }

    fun hideSeparator() {
        itemSeparator.visibility = View.INVISIBLE
    }

    private fun showAnswerPreparation() {
        hideSeparator()
        progressBarContainer.visibility = View.VISIBLE
        itemSeparator.visibility = View.INVISIBLE
        radioImage.visibility = View.GONE
        answerInfoContainer.visibility = View.VISIBLE
    }

    fun showNormalAnswer(roundedPercent: Int) {
        showAnswerPreparation()
        imageAnswerRight.visibility = View.GONE
        imageAnswerWrong.visibility = View.GONE
        showPercentOfClicksAnimated(roundedPercent, colorNormal)
        setProgressOfClicksAnimated(roundedPercent, colorNormal)
    }

    fun showRightAnswer(roundedPercent: Int) {
        showAnswerPreparation()
        imageAnswerRight.visibility = View.VISIBLE
        imageAnswerWrong.visibility = View.GONE
        showPercentOfClicksAnimated(roundedPercent, colorRight)
        setProgressOfClicksAnimated(roundedPercent, colorRight)
    }

    fun showWrongAnswer(roundedPercent: Int) {
        showAnswerPreparation()
        imageAnswerRight.visibility = View.GONE
        imageAnswerWrong.visibility = View.VISIBLE
        showPercentOfClicksAnimated(roundedPercent, colorWrong)
        setProgressOfClicksAnimated(roundedPercent, colorWrong)
    }

    fun showNotSelectedRightAnswer(roundedPercent: Int) {
        showAnswerPreparation()
        imageAnswerRight.visibility = View.VISIBLE
        imageAnswerWrong.visibility = View.GONE
        showPercentOfClicksAnimated(roundedPercent, colorNormal)
        setProgressOfClicksAnimated(roundedPercent, colorNormal)
    }

    private fun showPercentOfClicksAnimated(percent: Int, answerColor: Int) {
        imageAnswerRight.imageTintList = ColorStateList.valueOf(answerColor)
        val anim = ValueAnimator.ofInt(0, percent)
        anim.addUpdateListener {
            textPercent.text =
                context.resources.getString(
                    R.string.answer_choicer_button_text_percent,
                    (it.animatedValue as Int)
                )
        }
        anim.duration = ANIMATION_DURATION
        anim.start()
    }

    private fun setProgressOfClicksAnimated(percent: Int, answerColor: Int) {
        progressBar.progressTintList = ColorStateList.valueOf(answerColor)
        val scaledPercent = (percent * 100)
        progressBar.max = 10000
        val anim = ValueAnimator.ofInt(0, scaledPercent)
        anim.addUpdateListener { progressBar.progress = (it.animatedValue as Int) }
        anim.duration = ANIMATION_DURATION
        anim.start()
    }

    private fun getDimensionInIntFromRes(dimensionRes: Int) =
        context.resources.getDimensionPixelSize(dimensionRes)

    private fun sp2px(sp: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )

    private fun px2sp(px: Float) = (px / context.resources.displayMetrics.scaledDensity).toInt()
}