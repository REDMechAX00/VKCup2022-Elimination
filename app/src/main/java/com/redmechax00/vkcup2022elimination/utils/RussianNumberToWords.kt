package com.redmechax00.vkcup2022elimination.utils

import android.content.Context
import com.redmechax00.vkcup2022elimination.R

class RussianNumberToWords(private val context: Context) {
    private val tensNames = arrayOf(
        "",
        context.getString(R.string.text_ten),
        context.getString(R.string.text_twenty),
        context.getString(R.string.text_thirty),
        context.getString(R.string.text_forty),
        context.getString(R.string.text_fifty),
        context.getString(R.string.text_sixty),
        context.getString(R.string.text_seventy),
        context.getString(R.string.text_eighty),
        context.getString(R.string.text_ninety)
    )
    private val numNames = arrayOf(
        context.getString(R.string.text_zero),
        context.getString(R.string.text_one),
        context.getString(R.string.text_two),
        context.getString(R.string.text_three),
        context.getString(R.string.text_four),
        context.getString(R.string.text_five),
        context.getString(R.string.text_six),
        context.getString(R.string.text_seven),
        context.getString(R.string.text_eight),
        context.getString(R.string.text_nine),
        context.getString(R.string.text_ten),
        context.getString(R.string.text_eleven),
        context.getString(R.string.text_twelve),
        context.getString(R.string.text_thirteen),
        context.getString(R.string.text_fourteen),
        context.getString(R.string.text_fifteen),
        context.getString(R.string.text_sixteen),
        context.getString(R.string.text_seventeen),
        context.getString(R.string.text_eighteen),
        context.getString(R.string.text_nineteen)
    )

    fun convertLessThanOneHundred(convertedNumber: Int): String {
        var number = convertedNumber
        var soFar: String
        if (number % 100 < 20) {
            soFar = numNames[number % 100]
            number /= 100
        } else {
            soFar = if (number % 10 != 0) " " + numNames[number % 10]
            else ""
            number /= 10
            soFar = tensNames[number % 10] + soFar
            number /= 10
        }
        return if (number == 0) soFar else context.getString(R.string.text_one_hundred_more)
    }
}