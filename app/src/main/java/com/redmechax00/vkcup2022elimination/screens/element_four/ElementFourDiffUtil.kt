package com.redmechax00.vkcup2022elimination.screens.element_four

import androidx.recyclerview.widget.DiffUtil

object ElementFourDiffUtil : DiffUtil.ItemCallback<ElementFourModel>() {

    override fun areItemsTheSame(oldItem: ElementFourModel, newItem: ElementFourModel): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ElementFourModel, newItem: ElementFourModel): Boolean {
        return oldItem == newItem
    }
}