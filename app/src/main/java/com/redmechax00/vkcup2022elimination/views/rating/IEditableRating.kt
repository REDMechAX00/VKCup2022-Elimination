package com.redmechax00.vkcup2022elimination.views.rating

interface IEditableRating {

    fun setCountOfStars(count: Int)
    fun getCountOfStars(): Int

    fun setCurrentRating(rating: Int)
    fun getCurrentRating(): Int
}