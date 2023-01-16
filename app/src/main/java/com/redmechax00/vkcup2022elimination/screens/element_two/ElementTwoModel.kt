package com.redmechax00.vkcup2022elimination.screens.element_two

import com.redmechax00.vkcup2022elimination.screens.base.BaseElement
import com.redmechax00.vkcup2022elimination.views.matching.MatchingViewModel

data class ElementTwoModel(
    val itemId: Int,
    val matchingViewModel: MatchingViewModel
) : BaseElement {

    override fun equals(other: Any?): Boolean {
        other as ElementTwoModel
        if (itemId != other.itemId) return false
        if (matchingViewModel != other.matchingViewModel) return false
        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + matchingViewModel.hashCode()
        return result
    }
}