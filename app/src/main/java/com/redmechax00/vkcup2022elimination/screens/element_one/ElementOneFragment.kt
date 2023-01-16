package com.redmechax00.vkcup2022elimination.screens.element_one

import com.redmechax00.vkcup2022elimination.screens.base.BaseElementFragment
import com.redmechax00.vkcup2022elimination.utils.RandomMathQuestionsGenerator
import com.redmechax00.vkcup2022elimination.views.answer_choicer.AnswerChoicerViewModel

class ElementOneFragment : BaseElementFragment() {

    override fun initAdapter() {
        adapter = ElementOneAdapter { answerIsRight -> showSuccessAnimationOrNot(answerIsRight) }

        val initQuestionsData = RandomMathQuestionsGenerator(requireContext()).generate(30)
        (adapter as ElementOneAdapter).addElements(createElements(initQuestionsData))
        recyclerView.adapter = adapter
    }

    override fun doWhenScrollReachMax() {
        val newQuestions = RandomMathQuestionsGenerator(requireContext()).generate(20)
        (adapter as ElementOneAdapter).addElements(createElements(newQuestions))
    }

    private fun createElements(questionsData: MutableList<AnswerChoicerViewModel>): MutableList<ElementOneModel> {
        val elements = mutableListOf<ElementOneModel>()
        questionsData.forEachIndexed { i, answerChoicerView ->
            val element = ElementOneModel(adapter.itemCount + i, answerChoicerView)
            elements.add(element)
        }
        return elements
    }
}