package com.redmechax00.vkcup2022elimination.views.matching

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ViewMatchingBinding
import com.redmechax00.vkcup2022elimination.views.IPopulatable
import com.redmechax00.vkcup2022elimination.views.matching.view_items.MatchingButtonView
import com.redmechax00.vkcup2022elimination.views.matching.view_items.MatchingSelectedCoupleView

class MatchingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IPopulatable<MatchingViewModel> {

    interface AnswerListener {
        fun onDone(isRight: Boolean)
    }

    private var answerListener: AnswerListener? = null

    fun setAnswerListener(answerListener: AnswerListener?) {
        this.answerListener = answerListener
    }

    companion object {
        const val TAG_BUTTON_LEFT = "l"
        const val TAG_BUTTON_RIGHT = "r"
    }

    private val itemsContainer: LinearLayout
    private val tableItemsContainer: TableLayout
    private val answersContainer: LinearLayout
    private val separator: View
    private val verifyButton: LottieAnimationView

    private val leftButtons = mutableListOf<MatchingButtonView>()
    private val rightButtons = mutableListOf<MatchingButtonView>()

    private val leftOriginalData = mutableMapOf<Int, String>()
    private val rightOriginalData = mutableMapOf<Int, String>()
    private var mixedAndUnmixedLeft = mutableMapOf<Int, Int>()
    private var mixedAndUnmixedRight = mutableMapOf<Int, Int>()

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewMatchingBinding.inflate(inflater, this, true)

        itemsContainer = binding.matchingItemsContainer
        tableItemsContainer = binding.matchingItemsTableContainer
        tableItemsContainer.isStretchAllColumns = true
        answersContainer = binding.matchingAnswersContainer
        separator = binding.matchingSeparator
        verifyButton = binding.matchingButtonVerify
        verifyButton.setOnClickListener { verifyAnswers() }
    }

    override fun populate(model: MatchingViewModel) {
        if (tableItemsContainer.isEmpty()) {
            val listOfPositions = mutableListOf<Int>()
            model.matchingData.forEachIndexed { i, pair ->
                listOfPositions.add(i)
                leftOriginalData[i] = pair.first
                rightOriginalData[i] = pair.second
            }
            createItems(listOfPositions)
            itemsContainer.fixMinHeight()
        }
    }

    private fun View.fixMinHeight() {
        this.post { this.minimumHeight = this.height }
    }

    private fun createItems(listOfPositions: MutableList<Int>) {
        mixedAndUnmixedLeft = mixedAndUnmixedMap(listOfPositions)
        mixedAndUnmixedRight = mixedAndUnmixedMap(listOfPositions)

        val rowParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT,
        )

        for (i in listOfPositions.indices) {
            val tableRow = TableRow(context)
            tableRow.gravity = Gravity.CENTER_VERTICAL

            val buttonLeft = MatchingButtonView(context)
            buttonLeft.layoutParams = rowParams
            buttonLeft.tag = Pair(i, TAG_BUTTON_LEFT)
            val leftText = leftOriginalData[mixedAndUnmixedLeft[i]] ?: ""
            buttonLeft.setText(leftText)
            buttonLeft.setOnClickListener { (it as MatchingButtonView).onClicked() }
            tableRow.addView(buttonLeft)
            leftButtons.add(buttonLeft)

            val buttonRight = MatchingButtonView(context)
            buttonRight.layoutParams = rowParams
            buttonRight.tag = Pair(i, TAG_BUTTON_RIGHT)
            val rightText = rightOriginalData[mixedAndUnmixedRight[i]] ?: ""
            buttonRight.setText(rightText)
            buttonRight.setOnClickListener { (it as MatchingButtonView).onClicked() }
            tableRow.addView(buttonRight)
            rightButtons.add(buttonRight)

            tableItemsContainer.addView(tableRow)
        }
    }

    private fun MatchingButtonView.onClicked() {
        val isActionSelect = !getButtonIsSelected()
        this.actionByGravityTag(
            {//Left Button
                if (leftButtons.isItemSelected()) {
                    if (isActionSelect) {
                        leftButtons.clearItemsSelected()
                    }
                }
            },
            {//Right Button
                if (rightButtons.isItemSelected()) {
                    if (isActionSelect) {
                        rightButtons.clearItemsSelected()
                    }
                }
            }
        )

        this.isSelected = isActionSelect
        if (leftButtons.isItemSelected() && rightButtons.isItemSelected()) {
            val leftPosition = leftButtons.getSelectedPosition()
            val rightPosition = rightButtons.getSelectedPosition()
            addSelectedCouple(leftPosition, rightPosition)
        }
    }

    private fun MatchingButtonView.actionByGravityTag(leftTag: () -> Unit, rightTag: () -> Unit) {
        val tag = tag
        val buttonInfo: Pair<*, *> = if (tag is Pair<*, *>) tag else Pair(0, "")
        when (buttonInfo.second as String) {
            TAG_BUTTON_LEFT -> {
                leftTag()
            }
            else -> {
                rightTag()
            }
        }
    }

    private fun verifyAnswers() {
        var isRight = true
        answersContainer.forEach {
            if (it is MatchingSelectedCoupleView) {
                if (mixedAndUnmixedLeft[it.leftPosition] !=
                    mixedAndUnmixedRight[it.rightPosition]
                ) {
                    isRight = false
                }
                it.isVerified()
            }
        }

        separator.visibility = View.GONE

        verifyButton.cancelAnimation()
        verifyButton.clearAnimation()
        verifyButton.repeatMode = LottieDrawable.RESTART
        verifyButton.progress = 0f
        verifyButton.repeatCount = 0

        if (isRight) {
            verifyButton.setAnimation(R.raw.matching_right)
            answerListener?.onDone(true)
        } else {
            verifyButton.setAnimation(R.raw.matching_wrong)
            answerListener?.onDone(false)
        }
        verifyButton.playAnimation()
    }

    private fun addSelectedCouple(leftPosition: Int, rightPosition: Int) {
        leftButtons.clearItemsSelected()
        rightButtons.clearItemsSelected()
        leftButtons[leftPosition].visibility = View.INVISIBLE
        rightButtons[rightPosition].visibility = View.INVISIBLE

        val selectedCoupleView = MatchingSelectedCoupleView(context)
        selectedCoupleView.setRemoveButtonClickListener { deleteSelectedCouple(it) }
        val leftText = leftOriginalData[mixedAndUnmixedLeft[leftPosition]] ?: ""
        val rightText = rightOriginalData[mixedAndUnmixedRight[rightPosition]] ?: ""
        selectedCoupleView.setCoupleText(leftText, rightText)
        selectedCoupleView.leftPosition = leftPosition
        selectedCoupleView.rightPosition = rightPosition

        answersContainer.addView(selectedCoupleView)
        separator.visibility = View.VISIBLE
        answersContainer.visibility = View.VISIBLE

        if (answersContainer.children.toList().size >= tableItemsContainer.children.toList().size) {
            showVerifyButton()
        }
    }

    private fun showVerifyButton() {
        tableItemsContainer.visibility = View.GONE
        verifyButton.cancelAnimation()
        verifyButton.setAnimation(R.raw.matching_verify)
        verifyButton.clearAnimation()
        verifyButton.repeatMode = LottieDrawable.REVERSE
        verifyButton.visibility = View.VISIBLE
        verifyButton.playAnimation()
    }

    private fun deleteSelectedCouple(selectedCoupleView: MatchingSelectedCoupleView) {
        val leftButton = leftButtons[selectedCoupleView.leftPosition]
        val rightButton = rightButtons[selectedCoupleView.rightPosition]
        leftButton.visibility = View.VISIBLE
        rightButton.visibility = View.VISIBLE
        leftButton.cleanSelected()
        rightButton.cleanSelected()

        selectedCoupleView.clearRemoveButtonClickListener()
        answersContainer.removeView(selectedCoupleView)

        if (answersContainer.isEmpty()) {
            separator.visibility = View.GONE
            answersContainer.visibility = View.GONE
        }

        if (answersContainer.children.toList().size < tableItemsContainer.children.toList().size) {
            hideVerifyButton()
        }
    }

    private fun hideVerifyButton() {
        tableItemsContainer.visibility = View.VISIBLE
        verifyButton.visibility = View.GONE
        verifyButton.cancelAnimation()
    }

    private fun MutableList<MatchingButtonView>.isItemSelected(): Boolean {
        this.forEach { button ->
            if (button.getButtonIsSelected()) return true
        }
        return false
    }

    private fun MutableList<MatchingButtonView>.getSelectedPosition(): Int {
        this.forEachIndexed { i, button ->
            if (button.getButtonIsSelected()) return i
        }
        return -1
    }

    private fun MutableList<MatchingButtonView>.clearItemsSelected() {
        this.forEach { button ->
            button.cleanSelected()
        }
    }

    private fun mixedAndUnmixedMap(unmixed: MutableList<Int>): MutableMap<Int, Int> {
        val mixed = unmixed.shuffled()
        val map = mutableMapOf<Int, Int>()
        for (i in unmixed.indices) {
            map[unmixed[i]] = mixed[i]
        }
        return map
    }
}