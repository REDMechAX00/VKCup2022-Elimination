package com.redmechax00.vkcup2022elimination.views.answer_choicer

data class AnswerChoicerViewModel(
    val questionTitle: String,
    val answerButtonsData: List<Pair<String, Int>>,
    val positionOfRightAnswer: Int
) {

    override fun equals(other: Any?): Boolean {
        other as AnswerChoicerViewModel

        if (questionTitle != other.questionTitle) return false
        if (answerButtonsData.size != other.answerButtonsData.size) return false

        for (i in answerButtonsData.indices) {
            if (answerButtonsData[i].first != other.answerButtonsData[i].first) return false
            if (answerButtonsData[i].second != other.answerButtonsData[i].second) return false
        }

        if (positionOfRightAnswer != other.positionOfRightAnswer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = questionTitle.hashCode()
        answerButtonsData.forEach {
            result = 31 * result + it.first.hashCode()
            result = 31 * result + it.second.hashCode()
        }
        result = 31 * result + positionOfRightAnswer.hashCode()
        return result
    }
}