package com.redmechax00.vkcup2022elimination.views.drag_and_drop.view_items

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.vkcup2022elimination.databinding.ViewDragAndDropAnswerTextBinding

class DragAndDropAnswerTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), IEditableDragAndDropAnswerText {

    private var touchableCallback: ITouchableDragAndDrop? = null
    private val textView: TextView
    override var answerPosition = -1

    private var pressedX = 0
    private var pressedY = 0

    private var defaultMarginTop = 0
    private var defaultMarginLeft = 0

    init {
        val inflater = LayoutInflater.from(context)
        val binding = ViewDragAndDropAnswerTextBinding.inflate(inflater, this, true)

        textView = binding.dragAndDropAnswerText
    }

    override fun setText(text: String) {
        textView.text = text
    }

    override fun getText(): String = textView.text.toString()

    fun setTouchableCallback(callback: ITouchableDragAndDrop?) {
        touchableCallback = callback
    }

    fun setDefaultMargins(top: Int, left: Int) {
        defaultMarginTop = top
        defaultMarginLeft = left
    }

    fun getDefaultMarginsTop() = defaultMarginTop

    fun getDefaultMarginsLeft() = defaultMarginLeft

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                disallowScrollInAllParents(true)
                pressedX = event.rawX.toInt()
                pressedY = event.rawY.toInt()
                touchableCallback?.onDownAnswer(this, event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val dX = (pressedX - event.rawX).toInt()
                val dY = (pressedY - event.rawY).toInt()
                (this.layoutParams as RelativeLayout.LayoutParams).topMargin = -dY
                (this.layoutParams as RelativeLayout.LayoutParams).leftMargin = -dX
                touchableCallback?.onMoveAnswer(this, dX, dY)
            }

            MotionEvent.ACTION_UP -> {
                performClick()
                (this.layoutParams as RelativeLayout.LayoutParams).topMargin = defaultMarginTop
                (this.layoutParams as RelativeLayout.LayoutParams).leftMargin = defaultMarginLeft
                disallowScrollInAllParents(false)
                touchableCallback?.onUpAnswer(this)
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                (this.layoutParams as RelativeLayout.LayoutParams).topMargin = defaultMarginTop
                (this.layoutParams as RelativeLayout.LayoutParams).leftMargin = defaultMarginLeft
                disallowScrollInAllParents(false)
                touchableCallback?.onCancelMoveAnswer(this)
            }
        }
        return false
    }

    private fun View.disallowScrollInAllParents(disallow: Boolean) {
        var rootParent = this.parent
        while (rootParent.parent != null) {
            rootParent = rootParent.parent
            if (rootParent is RecyclerView) {
                rootParent.requestDisallowInterceptTouchEvent(disallow)
            }
            if (rootParent is ScrollView) {
                rootParent.requestDisallowInterceptTouchEvent(disallow)
            }
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

}