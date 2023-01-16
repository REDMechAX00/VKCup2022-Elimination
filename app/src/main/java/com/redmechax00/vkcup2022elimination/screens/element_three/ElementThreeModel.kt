package com.redmechax00.vkcup2022elimination.screens.element_three

import com.redmechax00.vkcup2022elimination.screens.base.BaseElement
import com.redmechax00.vkcup2022elimination.views.drag_and_drop.DragAndDropViewModel

data class ElementThreeModel(
    val itemId: Int,
    val dragAndDropViewModel: DragAndDropViewModel
) : BaseElement {

    override fun equals(other: Any?): Boolean {
        other as ElementThreeModel
        if (itemId != other.itemId) return false
        if (dragAndDropViewModel != other.dragAndDropViewModel) return false
        return true
    }

    override fun hashCode(): Int {
        var result = itemId.hashCode()
        result = 31 * result + dragAndDropViewModel.hashCode()
        return result
    }
}