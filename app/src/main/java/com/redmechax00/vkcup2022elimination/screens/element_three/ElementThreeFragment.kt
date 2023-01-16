package com.redmechax00.vkcup2022elimination.screens.element_three

import com.redmechax00.vkcup2022elimination.screens.base.BaseElementFragment
import com.redmechax00.vkcup2022elimination.views.drag_and_drop.DragAndDropView.Companion.markAsAnswer
import com.redmechax00.vkcup2022elimination.views.drag_and_drop.DragAndDropViewModel

class ElementThreeFragment : BaseElementFragment() {

    override fun initAdapter() {
        adapter = ElementThreeAdapter { answerIsRight -> showSuccessAnimationOrNot(answerIsRight) }
        (adapter as ElementThreeAdapter).addElements(createElements(15))
        recyclerView.adapter = adapter
    }

    override fun doWhenScrollReachMax() {
        (adapter as ElementThreeAdapter).addElements(createElements(10))
    }

    private fun createElements(count: Int): MutableList<ElementThreeModel> {
        val newElements = mutableListOf<ElementThreeModel>()
        for (i in 0 until count) {
            val questionData = "Я пришел " +
                    "к".markAsAnswer() +
                    " тебе с " +
                    "приветом".markAsAnswer() +
                    " рассказать что " +
                    "солнце встало".markAsAnswer()
            val inCorrectAnswers = mutableListOf<String>()
            inCorrectAnswers.addAll(listOf("с", "где", "вместе", "рядом"))
            newElements.add(
                ElementThreeModel(
                    adapter.itemCount + i,
                    DragAndDropViewModel(questionData, inCorrectAnswers)
                )
            )
        }
        return newElements
    }
}