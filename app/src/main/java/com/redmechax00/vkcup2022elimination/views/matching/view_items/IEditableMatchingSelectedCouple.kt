package com.redmechax00.vkcup2022elimination.views.matching.view_items

interface IEditableMatchingSelectedCouple {

    var leftPosition: Int
    var rightPosition: Int

    fun setCoupleText(left: String, right: String)
}