package com.redmechax00.vkcup2022elimination.views.answer_choicer.view_items

interface ICustomizableAnswerChoicerButton {

    fun setText(text: String)
    fun getText(): String

    fun setTextSize(sp: Int)
    fun getTextSize(): Int

    fun increaseCountOfClicks()
    fun setCountOfClicks(count: Int)
    fun getCountOfClicks(): Int
}