package com.redmechax00.vkcup2022elimination.screens.element_two

import androidx.recyclerview.widget.DiffUtil

object ElementTwoDiffUtil : DiffUtil.ItemCallback<ElementTwoModel>() {

    override fun areItemsTheSame(oldItem: ElementTwoModel, newItem: ElementTwoModel): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ElementTwoModel, newItem:ElementTwoModel): Boolean {
        return oldItem == newItem
    }
}