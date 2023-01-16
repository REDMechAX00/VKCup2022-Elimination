package com.redmechax00.vkcup2022elimination.views.drag_and_drop.view_items

import android.view.MotionEvent

interface ITouchableDragAndDrop {

    fun onDownAnswer(answerTextView: DragAndDropAnswerTextView, event: MotionEvent)

    fun onMoveAnswer(answerTextView: DragAndDropAnswerTextView, dX:Int, dY:Int)

    fun onUpAnswer(answerTextView: DragAndDropAnswerTextView)

    fun onCancelMoveAnswer(answerTextView: DragAndDropAnswerTextView)
}