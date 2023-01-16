package com.redmechax00.vkcup2022elimination.views.drag_and_drop

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.*
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import com.google.android.flexbox.FlexboxLayout
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewDragAndDropBinding
import com.redmechax00.vkcup2022elimination.views.IPopulatable
import com.redmechax00.vkcup2022elimination.views.drag_and_drop.view_items.DragAndDropAnswerTextView
import com.redmechax00.vkcup2022elimination.views.drag_and_drop.view_items.ITouchableDragAndDrop
import kotlin.math.abs

class DragAndDropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IPopulatable<DragAndDropViewModel>,
    ITouchableDragAndDrop {

    companion object {
        private const val ANSWER_MARK = "$$$"
        private const val ANSWER_TEXT = "_____"
        fun String.markAsAnswer(): String = ANSWER_MARK + this + ANSWER_MARK
    }

    interface AnswerListener {
        fun onDone(isRight: Boolean)
    }

    private var answerListener: AnswerListener? = null

    fun setAnswerListener(answerListener: AnswerListener?) {
        this.answerListener = answerListener
    }

    private val parentViewGroup: ConstraintLayout
    private val questionContainer: FlexboxLayout
    private val answersContainer: RelativeLayout

    private val rightAnswersMap = hashMapOf<Int, String>()
    private val listOfAnswers = mutableListOf<DragAndDropAnswerTextView>()
    private val listOfTextQuestions = mutableListOf<TextView>()

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewDragAndDropBinding.inflate(inflater, this, true)

        parentViewGroup = binding.dragAndDropParent
        questionContainer = binding.dragAndDropQuestionContainer
        answersContainer = binding.dragAndDropAnswersContainer
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        answersContainer.forEach {
            if (it is DragAndDropAnswerTextView) {
                it.setTouchableCallback(null)
            }
        }
    }

    override fun populate(model: DragAndDropViewModel) {
        if (questionContainer.isEmpty()) {
            createQuestion(model.dragAndDropQuestionText)
            createAnswers(model.dragAndDropIncorrectAnswers)
        }
    }

    private fun createQuestion(questionText: String) {
        var questionPreparedText = ""

        var startWords = 0
        var startAnswerMark = questionText.indexOf(ANSWER_MARK)
        var position = 0
        while (startAnswerMark != -1) {
            val startAnswer = startAnswerMark + ANSWER_MARK.length
            val endAnswer = questionText.indexOf(ANSWER_MARK, startAnswer)
            val endAnswerMark = endAnswer + ANSWER_MARK.length

            if (endAnswerMark != -1 && startAnswer < endAnswer) {
                val answer = questionText.substring(startAnswer, endAnswer)
                rightAnswersMap[position] = answer.prepareTextAnswer()
                position++

                questionPreparedText +=
                    "${questionText.substring(startWords, startAnswerMark)} $ANSWER_TEXT "
                startWords = endAnswerMark
            }

            startAnswerMark = questionText.indexOf(ANSWER_MARK, endAnswerMark)
            if (startAnswerMark == -1) {
                questionPreparedText += questionText.substring(startWords)
            }
        }

        val questionData = questionPreparedText.split(" ")

        questionData.forEach { s ->
            val textView = createNewQuestionTextView(s)
            questionContainer.addView(textView)
        }
    }

    private fun createAnswers(dragAndDropIncorrectAnswers: List<String>) {
        val allAnswers = mutableListOf<DragAndDropAnswerTextView>()

        dragAndDropIncorrectAnswers.forEach {
            val newAnswer = DragAndDropAnswerTextView(context)
            newAnswer.setText(it)
            allAnswers.add(newAnswer)
        }

        rightAnswersMap.forEach { (t, u) ->
            val newAnswer = DragAndDropAnswerTextView(context)
            newAnswer.answerPosition = t
            newAnswer.setText(u)
            allAnswers.add(newAnswer)
        }
        allAnswers.shuffle()

        allAnswers.forEach { answer ->
            answer.setTouchableCallback(this)
            listOfAnswers.add(answer)
            answersContainer.addView(answer)
        }

        setLayoutParamsToAnswers()
    }

    private fun setLayoutParamsToAnswers() {
        answersContainer.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                answersContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                createAnswerLayoutParams()
            }
        })
    }

    private fun createAnswerLayoutParams() {
        val parentWidth = answersContainer.measuredWidth
        var summAnswersWidth = 0
        var topMargin = measuredHeight +
                context.resources.getDimensionPixelSize(R.dimen.drag_and_drop_indent_normal)
        var firstRawHeights = if (listOfAnswers.isNotEmpty()) listOfAnswers[0].measuredHeight else 0
        listOfAnswers.forEach { answer ->
            answer.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            if (summAnswersWidth + answer.measuredWidth > parentWidth) {
                topMargin += firstRawHeights
                firstRawHeights = answer.measuredHeight
                summAnswersWidth = 0
            }
            (answer.layoutParams as RelativeLayout.LayoutParams).topMargin = topMargin
            (answer.layoutParams as RelativeLayout.LayoutParams).leftMargin = summAnswersWidth
            answer.setDefaultMargins(topMargin, summAnswersWidth)
            summAnswersWidth += answer.measuredWidth
        }
    }

    private fun createNewQuestionTextView(s: String): TextView {
        val textView = TextView(context)
        textView.layoutParams =
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textView.gravity = Gravity.CENTER
        textView.setTextAppearance(R.style.DragAndDropText)

        if (s == ANSWER_TEXT) {
            listOfTextQuestions.add(textView)
        }
        textView.text = s
        val bounds = Rect()
        textView.paint.getTextBounds(
            textView.text.toString(),
            0,
            if (textView.text.isNotEmpty()) 1 else 0,
            bounds
        )
        val padding = bounds.width() / 2
        textView.setPadding(padding, padding, padding, padding)
        return textView
    }

    override fun onDownAnswer(answerTextView: DragAndDropAnswerTextView, event: MotionEvent) {
        answerTextView.bringToFront()
    }

    override fun onMoveAnswer(answerTextView: DragAndDropAnswerTextView, dX: Int, dY: Int) {
        val params = answerTextView.layoutParams as RelativeLayout.LayoutParams
        params.leftMargin = -dX + answerTextView.getDefaultMarginsLeft()
        params.topMargin = -dY + answerTextView.getDefaultMarginsTop()
        answerTextView.layoutParams = params
    }

    override fun onUpAnswer(answerTextView: DragAndDropAnswerTextView) {
        val params = answerTextView.layoutParams as RelativeLayout.LayoutParams
        params.leftMargin = answerTextView.getDefaultMarginsLeft()
        params.topMargin = answerTextView.getDefaultMarginsTop()
        answerTextView.layoutParams = params

        val textView = checkTriggered(answerTextView.getCoordinatesInRootContainer())
        textView?.text = answerTextView.getText()
    }

    override fun onCancelMoveAnswer(answerTextView: DragAndDropAnswerTextView) {
        val params = answerTextView.layoutParams as RelativeLayout.LayoutParams
        params.leftMargin = answerTextView.getDefaultMarginsLeft()
        params.topMargin = answerTextView.getDefaultMarginsTop()
        answerTextView.layoutParams = params

        val textView = checkTriggered(answerTextView.getCoordinatesInRootContainer())
        textView?.text = answerTextView.getText()
    }

    private fun checkTriggered(answerRect: IntArray): TextView? {
        listOfTextQuestions.forEach {
            val questionRect = it.getCoordinatesInRootContainer()
            val answerCenterY = (answerRect[2] - answerRect[0]) / 2 + answerRect[0]
            val questionCenterY = (questionRect[2] - questionRect[0]) / 2 + questionRect[0]
            val answerCenterX = (answerRect[3] - answerRect[1]) / 2 + answerRect[1]
            val questionCenterX = (questionRect[3] - questionRect[1]) / 2 + questionRect[1]
            val triggeredZone = questionRect[2] - questionRect[0]
            if ((abs(answerCenterY - questionCenterY) <= triggeredZone) &&
                (abs(answerCenterX - questionCenterX) <= triggeredZone)
            ) {
                return it
            }
        }
        return null
    }

    private fun String.prepareTextAnswer(): String {
        this.lowercase()
        var inquireString = this
        while (inquireString.isNotEmpty() && inquireString[0].toString() == " ") {
            inquireString = inquireString.substring(1)
        }
        while (inquireString.isNotEmpty() && inquireString[inquireString.length - 1].toString() == " ") {
            inquireString = inquireString.substring(0, inquireString.length - 1)
        }
        return inquireString
    }

    private fun View.getCoordinatesInRootContainer(): IntArray {
        val offsetViewBounds = Rect()
        this.getDrawingRect(offsetViewBounds)
        (parentViewGroup as ViewGroup).offsetDescendantRectToMyCoords(this, offsetViewBounds)
        val relativeTop = offsetViewBounds.top
        val relativeLeft = offsetViewBounds.left
        val relativeBottom = offsetViewBounds.bottom
        val relativeRight = offsetViewBounds.right

        return intArrayOf(relativeTop, relativeLeft, relativeBottom, relativeRight)
    }
}