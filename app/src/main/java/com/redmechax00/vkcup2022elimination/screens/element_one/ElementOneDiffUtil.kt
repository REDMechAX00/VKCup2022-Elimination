package com.redmechax00.vkcup2022elimination.screens.element_one

import androidx.recyclerview.widget.DiffUtil

object ElementOneDiffUtil : DiffUtil.ItemCallback<ElementOneModel>() {

    override fun areItemsTheSame(oldItem: ElementOneModel, newItem: ElementOneModel): Boolean {
        return oldItem.itemId == newItem.itemId
    }

    override fun areContentsTheSame(oldItem: ElementOneModel, newItem: ElementOneModel): Boolean {
        return oldItem == newItem
    }
}