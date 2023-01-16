package com.redmechax00.vkcup2022elimination.views.drag_and_drop.view_items

interface IEditableDragAndDropAnswerText {

    var answerPosition: Int

    fun setText(text: String)

    fun getText(): String
}