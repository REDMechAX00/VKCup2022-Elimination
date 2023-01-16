package com.redmechax00.vkcup2022elimination.views.answer_choicer

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewAnswerChoicerBinding
import com.redmechax00.vkcup2022elimination.views.IPopulatable
import com.redmechax00.vkcup2022elimination.views.answer_choicer.view_items.AnswerChoicerButtonView
import kotlin.math.roundToInt

class AnswerChoicerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    IPopulatable<AnswerChoicerViewModel>, IEditableAnswerChoicer,
    ICustomizableAnswerChoicer {

    interface AnswerListener {
        fun onDone(isRight: Boolean)
    }

    private var answerListener: AnswerListener? = null

    fun setAnswerListener(answerListener: AnswerListener?) {
        this.answerListener = answerListener
    }

    private var questionTitleText: TextView
    private var countOfAnswersText: TextView
    private var buttonsContainer: LinearLayout

    private var positionOfRightAnswer: Int = 0

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewAnswerChoicerBinding.inflate(inflater, this, true)

        questionTitleText = binding.answerChoicerTextQuestionTitle
        countOfAnswersText = binding.answerChoicerTextCountOfAnswers
        buttonsContainer = binding.answerChoicerButtonsContainer

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.AnswerChoicerView, 0, 0)
        try {
            questionTitleText.text =
                attributes.getString(R.styleable.AnswerChoicerView_android_text) ?: ""
            questionTitleText.setTextSize(
                TypedValue.COMPLEX_UNIT_PX, attributes.getDimension(
                    R.styleable.AnswerChoicerView_android_textSize,
                    getDimensionFromRes(R.dimen.answer_choicer_text_big)
                )
            )
        } finally {
            attributes.recycle()
        }
    }

    override fun populate(model: AnswerChoicerViewModel) {
        questionTitleText.text = model.questionTitle
        positionOfRightAnswer = model.positionOfRightAnswer

        var countOfAnswers = 0
        model.answerButtonsData.forEach { countOfAnswers += it.second }
        countOfAnswersText.setCountOfAnswers(countOfAnswers)

        if (buttonsContainer.children.toList().isEmpty()) {
            initButtons(model.answerButtonsData)
        } else {
            applyToAllButtons { position, answerButton ->
                answerButton.setText(model.answerButtonsData[position].first)
                answerButton.setCountOfClicks(model.answerButtonsData[position].second)
            }
        }
    }

    private fun initButtons(answerButtonsData: List<Pair<String, Int>>) {
        for (i in answerButtonsData.indices) {
            val answerButton = AnswerChoicerButtonView(context)
            answerButton.tag = i
            answerButton.setText(answerButtonsData[i].first)
            answerButton.setCountOfClicks(answerButtonsData[i].second)
            answerButton.setAnswerButtonClickListener { clickedAnswerButton, clickedPosition ->
                answerButtonClick(clickedAnswerButton, clickedPosition)
            }
            if (i < answerButtonsData.size - 1) {
                answerButton.showSeparator()
            } else {
                answerButton.hideSeparator()
            }
            buttonsContainer.addView(answerButton)
        }
    }

    private fun answerButtonClick(
        clickedAnswerButton: AnswerChoicerButtonView,
        clickedPosition: Int
    ) {
        clickedAnswerButton.increaseCountOfClicks()
        calculateRightPercent { listOfRoundedPercent ->
            applyToAllButtons { position, answerButton ->
                val roundedPercent = listOfRoundedPercent[position]
                when (position) {
                    positionOfRightAnswer -> {
                        if (position == clickedPosition) {
                            answerListener?.onDone(true)
                            answerButton.showRightAnswer(roundedPercent)
                        } else {
                            answerButton.showNotSelectedRightAnswer(roundedPercent)
                        }
                    }
                    else -> {
                        if (position == clickedPosition) {
                            answerListener?.onDone(false)
                            answerButton.showWrongAnswer(roundedPercent)
                        } else {
                            answerButton.showNormalAnswer(roundedPercent)
                        }
                    }
                }
                answerButton.isEnabled = false
            }
        }

        var countOfAnswers = 0
        applyToAllButtons { _, answerButton ->
            countOfAnswers += answerButton.getCountOfClicks()
        }
        countOfAnswersText.setCountOfAnswers(countOfAnswers)
    }

    override fun getCountOfAnswers(): Int {
        var summOfClicks = 0
        applyToAllButtons { _, answerButton ->
            summOfClicks += answerButton.getCountOfClicks()
        }
        return summOfClicks
    }

    override fun setQuestionTitleTextSize(sp: Int) {
        questionTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(sp))
    }

    override fun getQuestionTitleTextSize(): Int = px2sp(questionTitleText.textSize)

    override fun setButtonsTextSize(sp: Int) {
        applyToAllButtons { _, answerButton -> answerButton.setTextSize(sp) }
    }

    override fun getButtonsTextSize(): Int {
        var textSize = 0
        applyToAllButtons { _, answerButton ->
            textSize = answerButton.getTextSize()
            return@applyToAllButtons
        }
        return textSize
    }

    private fun applyToAllButtons(action: (position: Int, answerButton: AnswerChoicerButtonView) -> Unit) {
        buttonsContainer.children.forEachIndexed { position, answerButton ->
            if (answerButton is AnswerChoicerButtonView) {
                action(position, answerButton)
            }
        }
    }

    private fun AnswerChoicerButtonView.setAnswerButtonClickListener(
        onClick: (clickedAnswerButton: AnswerChoicerButtonView, clickedPosition: Int) -> Unit
    ) {
        this.setOnClickListener {
            if (it is AnswerChoicerButtonView) {
                val tag = it.tag
                val clickedPosition: Int = if (tag is Int) {
                    tag
                } else {
                    0
                }
                onClick(it, clickedPosition)
            }
        }
    }

    private fun calculateRightPercent(onSuccess: (listOfRoundedPercent: List<Int>) -> Unit) {
        val summOfClicks = getCountOfAnswers().toFloat()
        val mapOfPairsRoundAndFraction = mutableMapOf<Int, Pair<Int, Float>>()

        applyToAllButtons { position, answerButton ->
            val percent = answerButton.getCountOfClicks().toFloat() * 100f / summOfClicks
            val roundedPercent = percent.toInt()
            mapOfPairsRoundAndFraction[position] =
                Pair(roundedPercent, percent - roundedPercent)
        }

        var summOfFractions = 0f
        mapOfPairsRoundAndFraction.forEach { summOfFractions += it.value.second }
        val sortedPercentByFraction =
            (mapOfPairsRoundAndFraction.toList()
                .sortedBy { (_, value) -> value.second }).reversed().toMap()
                .toMutableMap()

        var remains = summOfFractions.roundToInt()
        sortedPercentByFraction.entries.forEach {
            if (remains != 0) {
                mapOfPairsRoundAndFraction[it.key] = Pair(it.value.first + 1, it.value.second)
                remains--
            }
        }

        val listOfRoundedPercent = arrayListOf<Int>()
        mapOfPairsRoundAndFraction.forEach {
            listOfRoundedPercent.add(it.value.first)
        }

        onSuccess(listOfRoundedPercent)
    }

    private fun TextView.setCountOfAnswers(count: Int) {
        countOfAnswersText.text = context.resources.getString(
            R.string.answer_choicer_button_text_count_of_answers,
            count
        )
    }

    private fun getDimensionFromRes(dimensionRes: Int) =
        context.resources.getDimension(dimensionRes)

    private fun sp2px(sp: Int) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(),
        context.resources.displayMetrics
    )

    private fun px2sp(px: Float) = (px / context.resources.displayMetrics.scaledDensity).toInt()
}

