package com.redmechax00.vkcup2022elimination.views.drag_and_drop

data class DragAndDropViewModel(
    val dragAndDropQuestionText: String,
    val dragAndDropIncorrectAnswers: List<String>
) {

    override fun equals(other: Any?): Boolean {
        other as DragAndDropViewModel
        if (dragAndDropQuestionText != other.dragAndDropQuestionText) return false
        for (i in dragAndDropIncorrectAnswers.indices) {
            if (dragAndDropIncorrectAnswers[i] != other.dragAndDropIncorrectAnswers[i]) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = 0
        dragAndDropIncorrectAnswers.forEach {
            result = 31 * result + it.hashCode()
        }
        result = 31 * result + dragAndDropQuestionText.hashCode()
        return result
    }
}