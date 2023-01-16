package com.redmechax00.vkcup2022elimination.views.answer_choicer

interface ICustomizableAnswerChoicer {

    fun setQuestionTitleTextSize(sp: Int)
    fun getQuestionTitleTextSize(): Int

    fun setButtonsTextSize(sp: Int)
    fun getButtonsTextSize(): Int
}