package com.redmechax00.vkcup2022elimination.screens.element_five

import android.widget.Toast
import com.redmechax00.vkcup2022elimination.screens.base.BaseElementFragment
import kotlin.random.Random

class ElementFiveFragment : BaseElementFragment() {

    override fun initAdapter() {
        adapter = ElementFiveAdapter { rating -> showToast(rating.toString()) }
        (adapter as ElementFiveAdapter).addElements(createElements(15))
        recyclerView.adapter = adapter
    }

    override fun doWhenScrollReachMax() {
        (adapter as ElementFiveAdapter).addElements(createElements(10))
    }

    private fun createElements(count: Int): MutableList<ElementFiveModel> {
        val newElements = mutableListOf<ElementFiveModel>()
        for (i in 0 until count) {
            newElements.add(ElementFiveModel(adapter.itemCount + i, Random.nextInt(9) + 4))
        }
        return newElements
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}