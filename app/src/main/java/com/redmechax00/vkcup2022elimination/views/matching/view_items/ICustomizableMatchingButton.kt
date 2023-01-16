package com.redmechax00.vkcup2022elimination.views.matching.view_items

interface ICustomizableMatchingButton {

    fun setText(text: String)
    fun getText(): String

    fun setTextSize(sp: Int)
    fun getTextSize(): Int

    fun setSelected(isSelected: Boolean)
    fun getButtonIsSelected(): Boolean
    fun cleanSelected()
}