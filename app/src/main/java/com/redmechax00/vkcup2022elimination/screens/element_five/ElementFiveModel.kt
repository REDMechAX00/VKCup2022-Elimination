package com.redmechax00.vkcup2022elimination.screens.element_five

import com.redmechax00.vkcup2022elimination.screens.base.BaseElement

data class ElementFiveModel(
    val itemId: Int,
    val countOfStars: Int
) : BaseElement {

    override fun equals(other: Any?): Boolean {
        other as ElementFiveModel
        if (itemId != other.itemId) return false
        if (countOfStars != other.countOfStars) return false
        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + countOfStars.hashCode()
        return result
    }
}