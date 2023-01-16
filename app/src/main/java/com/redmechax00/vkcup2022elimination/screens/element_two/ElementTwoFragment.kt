package com.redmechax00.vkcup2022elimination.screens.element_two

import com.redmechax00.vkcup2022elimination.screens.base.BaseElementFragment
import com.redmechax00.vkcup2022elimination.utils.toWords
import com.redmechax00.vkcup2022elimination.views.matching.MatchingViewModel
import kotlin.random.Random

class ElementTwoFragment : BaseElementFragment() {

    override fun initAdapter() {
        adapter = ElementTwoAdapter { answerIsRight -> showSuccessAnimationOrNot(answerIsRight) }
        (adapter as ElementTwoAdapter).addElements(createElements(15, 4))
        recyclerView.adapter = adapter
    }

    override fun doWhenScrollReachMax() {
        (adapter as ElementTwoAdapter).addElements(createElements(10, 4))
    }

    @Suppress("SameParameterValue")
    private fun createElements(
        countOfElements: Int,
        countOfAnswers: Int
    ): MutableList<ElementTwoModel> {
        val newElements = mutableListOf<ElementTwoModel>()
        for (i in 0 until countOfElements) {
            val newAnswers = createAnswers(countOfAnswers)
            newElements.add(ElementTwoModel(adapter.itemCount + i, MatchingViewModel(newAnswers)))
        }
        return newElements
    }

    private fun createAnswers(countOfAnswers: Int): MutableList<Pair<String, String>> {
        val uniqueNumAnswers = hashSetOf<Int>()
        while (uniqueNumAnswers.size < countOfAnswers) {
            val rnd = Random.nextInt(100)
            uniqueNumAnswers.add(rnd)
        }
        val newAnswers = mutableListOf<Pair<String, String>>()
        for (item in uniqueNumAnswers) {
            newAnswers.add(Pair(item.toString(), item.toWords(requireContext())))
        }
        return newAnswers
    }
}