package com.redmechax00.vkcup2022elimination.views.filling

import android.content.Context
import android.graphics.Rect
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.widget.addTextChangedListener
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.flexbox.FlexboxLayout
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewDragAndDropBinding
import com.redmechax00.vkcup2022elimination.views.IPopulatable


class FillingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IPopulatable<FillingViewModel> {

    companion object {
        private const val ANSWER_MARK = "$$$"
        private const val ANSWER_TEXT = "___"
        fun String.markAsAnswer(): String = "$ANSWER_MARK$this$ANSWER_MARK"
    }

    interface AnswerListener {
        fun onDone(isRight: Boolean)
    }

    private var answerListener: AnswerListener? = null

    fun setAnswerListener(answerListener: AnswerListener?) {
        this.answerListener = answerListener
    }

    private val questionContainer: FlexboxLayout
    private var lottieButtonVerify: LottieAnimationView? = null

    private val rightAnswersMap = hashMapOf<Int, String>()
    private val editTextList = mutableListOf<EditText>()

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewDragAndDropBinding.inflate(inflater, this, true)

        questionContainer = binding.dragAndDropQuestionContainer
    }

    override fun populate(model: FillingViewModel) {
        if (questionContainer.isEmpty()) {
            createQuestion(model.dragAndDropQuestionText)
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
            val textItem = createNewTextItems(s)
            questionContainer.addView(textItem)
        }

        createVerifyButton()
    }

    private fun createNewTextItems(s: String): View {
        val params =
            FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        if (s == ANSWER_TEXT) {
            val editText = EditText(context)
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            editText.imeOptions = EditorInfo.IME_ACTION_DONE
            editText.layoutParams = params
            editText.gravity = Gravity.CENTER
            editText.setTextAppearance(R.style.FillingText)

            val bounds = editText.getCharacterBounds()
            val indent = bounds.width() / 2
            val minWidth = bounds.width() * 6
            val minHeight = bounds.height() * 3
            editText.minWidth = minWidth
            editText.minHeight = minHeight
            editText.setPadding(indent, 0, indent, indent / 2)

            editText.addCheckIsEmptyTextChangeListener()
            editText.tag = editTextList.size
            editTextList.add(editText)
            return editText
        } else {
            val textView = TextView(context)
            textView.layoutParams = params
            textView.gravity = Gravity.CENTER_VERTICAL
            textView.setTextAppearance(R.style.FillingText)
            textView.text = s

            val bounds = textView.getCharacterBounds()
            val padding = bounds.width() / 2
            textView.setPadding(padding, 0, padding, padding / 2)
            val minHeight = bounds.height() * 3
            textView.minHeight = minHeight
            return textView
        }
    }

    private fun createVerifyButton() {
        val size = if (questionContainer.isNotEmpty()) {
            val textItem = questionContainer[0]
            if (textItem is EditText || textItem is TextView) {
                (textItem as TextView).getCharacterBounds().height() * 3
            } else 0
        } else 0

        lottieButtonVerify = LottieAnimationView(context)
        lottieButtonVerify?.let { lottieButtonVerify ->
            lottieButtonVerify.layoutParams =
                FlexboxLayout.LayoutParams(size, size)
            lottieButtonVerify.setAnimation(R.raw.filling_verify)
            lottieButtonVerify.pauseAnimation()
            lottieButtonVerify.visibility = View.GONE
            lottieButtonVerify.repeatMode = LottieDrawable.RESTART
            lottieButtonVerify.repeatCount = LottieDrawable.INFINITE
            lottieButtonVerify.scaleX = 1.4f
            lottieButtonVerify.scaleY = 1.4f
            lottieButtonVerify.setOnClickListener { onVerifyAnswer() }
            questionContainer.addView(lottieButtonVerify)
        }
    }

    private fun onVerifyAnswer() {
        val listOfAnswers = mutableListOf<Pair<Int, String>>()
        editTextList.forEach { editText ->
            val tag = editText.getTagAsInt()
            listOfAnswers.add(Pair(tag, editText.text.toString().prepareTextAnswer()))
        }

        var isRightAnswer = true
        listOfAnswers.forEach { answer ->
            if (answer.second != rightAnswersMap[answer.first]) {
                isRightAnswer = false
            }
        }

        when (isRightAnswer) {
            true -> {
                lottieButtonVerify?.setAnimation(R.raw.filling_right)
            }
            false -> {
                lottieButtonVerify?.setAnimation(R.raw.filling_wrong)
            }
        }

        lottieButtonVerify?.pauseAnimation()
        lottieButtonVerify?.clearAnimation()
        lottieButtonVerify?.repeatCount = 0
        lottieButtonVerify?.progress = 0f
        lottieButtonVerify?.playAnimation()
        answerListener?.onDone(isRightAnswer)
        editTextList.forEach { it.isEnabled = false }
        lottieButtonVerify?.isEnabled = false
    }

    private fun View.getTagAsInt(): Int {
        var intTag = 0
        if (this.tag is Int) {
            intTag = this.tag as Int
        }
        return intTag
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

    private fun EditText.addCheckIsEmptyTextChangeListener() {
        this.addTextChangedListener {
            if (checkOneOfEditTextIsEmpty()) {
                lottieButtonVerify?.visibility = View.GONE
                lottieButtonVerify?.pauseAnimation()
            } else {
                lottieButtonVerify?.visibility = View.VISIBLE
                lottieButtonVerify?.playAnimation()
            }
        }
    }

    private fun checkOneOfEditTextIsEmpty(): Boolean {
        editTextList.forEach {
            if (it.text.isEmpty()) {
                return true
            }
        }
        return false
    }

    private fun TextView.getCharacterBounds(): Rect {
        val referenceCharacter = "C"
        val bounds = Rect()
        this.paint.getTextBounds(
            referenceCharacter,
            0,
            referenceCharacter.length,
            bounds
        )
        return bounds
    }
}