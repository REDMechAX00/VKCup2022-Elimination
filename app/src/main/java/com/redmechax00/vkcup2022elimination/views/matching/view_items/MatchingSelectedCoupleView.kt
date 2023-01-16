package com.redmechax00.vkcup2022elimination.views.matching.view_items

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.redmechax00.vkcup2022elimination.databinding.ViewMatchingSelectedCouplesBinding

class MatchingSelectedCoupleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IEditableMatchingSelectedCouple {

    private val leftView: MatchingButtonView
    private val rightView: MatchingButtonView
    private val removeButton: View

    override var leftPosition = 0
    override var rightPosition = 0

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewMatchingSelectedCouplesBinding.inflate(inflater, this, true)

        leftView = binding.matchingSelectedCoupleLeft
        rightView = binding.matchingSelectedCoupleRight
        removeButton = binding.matchingSelectedCoupleButtonRemove
    }

    override fun setCoupleText(left: String, right: String) {
        leftView.setText(left)
        rightView.setText(right)
    }

    fun setRemoveButtonClickListener(onClickRemove: (selectedCoupleView: MatchingSelectedCoupleView) -> Unit) {
        removeButton.setOnClickListener {
            onClickRemove(this)
        }
    }

    fun clearRemoveButtonClickListener() {
        removeButton.setOnClickListener(null)
    }

    fun isVerified() {
        clearRemoveButtonClickListener()
        removeButton.visibility = View.GONE
    }
}