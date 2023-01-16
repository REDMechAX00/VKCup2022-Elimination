package com.redmechax00.vkcup2022elimination.screens.element_four

import com.redmechax00.vkcup2022elimination.screens.base.BaseElement
import com.redmechax00.vkcup2022elimination.views.filling.FillingViewModel

data class ElementFourModel(
    val itemId: Int,
    val fillingViewModel: FillingViewModel
) : BaseElement {

    override fun equals(other: Any?): Boolean {
        other as ElementFourModel
        if (itemId != other.itemId) return false
        if (fillingViewModel != other.fillingViewModel) return false
        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + fillingViewModel.hashCode()
        return result
    }
}