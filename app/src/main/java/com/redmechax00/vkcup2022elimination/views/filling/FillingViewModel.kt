package com.redmechax00.vkcup2022elimination.views.filling

data class FillingViewModel(
    val dragAndDropQuestionText: String
) {

    override fun equals(other: Any?): Boolean {
        other as FillingViewModel
        if (dragAndDropQuestionText != other.dragAndDropQuestionText) return false
        return true
    }

    override fun hashCode(): Int {
        return dragAndDropQuestionText.hashCode()
    }
}