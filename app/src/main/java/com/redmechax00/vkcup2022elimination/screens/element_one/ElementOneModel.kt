package com.redmechax00.vkcup2022elimination.screens.element_one

import com.redmechax00.vkcup2022elimination.screens.base.BaseElement
import com.redmechax00.vkcup2022elimination.views.answer_choicer.AnswerChoicerViewModel

data class ElementOneModel(
    val itemId: Int,
    val answerChoicerViewModel: AnswerChoicerViewModel
) : BaseElement {

    override fun equals(other: Any?): Boolean {
        other as ElementOneModel
        if (itemId != other.itemId) return false
        if (answerChoicerViewModel != other.answerChoicerViewModel) return false
        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + answerChoicerViewModel.hashCode()
        return result
    }
}