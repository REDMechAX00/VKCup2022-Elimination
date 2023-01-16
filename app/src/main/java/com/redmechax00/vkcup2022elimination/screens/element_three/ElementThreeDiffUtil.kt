package com.redmechax00.vkcup2022elimination.screens.element_three

import androidx.recyclerview.widget.DiffUtil

object ElementThreeDiffUtil : DiffUtil.ItemCallback<ElementThreeModel>() {

    override fun areItemsTheSame(oldItem: ElementThreeModel, newItem: ElementThreeModel): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ElementThreeModel, newItem: ElementThreeModel): Boolean {
        return oldItem == newItem
    }
}