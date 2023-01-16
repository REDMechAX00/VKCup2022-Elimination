package com.redmechax00.vkcup2022elimination.screens.element_five

import androidx.recyclerview.widget.DiffUtil

object ElementFiveDiffUtil : DiffUtil.ItemCallback<ElementFiveModel>() {

    override fun areItemsTheSame(oldItem: ElementFiveModel, newItem: ElementFiveModel): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ElementFiveModel, newItem: ElementFiveModel): Boolean {
        return oldItem == newItem
    }
}