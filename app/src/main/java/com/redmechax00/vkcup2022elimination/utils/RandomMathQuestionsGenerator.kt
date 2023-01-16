package com.redmechax00.vkcup2022elimination.utils

import android.content.Context
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.views.answer_choicer.AnswerChoicerViewModel
import kotlin.random.Random

class RandomMathQuestionsGenerator(private val context: Context) {

    fun generate(countOfQuestions: Int): MutableList<AnswerChoicerViewModel> {
        val newQuestions = mutableListOf<AnswerChoicerViewModel>()

        for (questionPosition in 0 until countOfQuestions) {
            val countOfAnswers = Random.nextInt(3) + 4
            val rightAnswerPosition = Random.nextInt(countOfAnswers)

            val listOfAnswerButtonsData = arrayListOf<Pair<String, Int>>()

            var question = ""
            var answer = 0
            var randomAnswer: () -> Int = { 0 }

            when (Random.nextInt(3)) {
                //Addition
                0 -> {
                    val first = Random.nextInt(51)
                    val second = Random.nextInt(50)
                    answer = first + second
                    question = context.resources.getString(
                        R.string.text_fragment_element_one_question_addition,
                        first.toWords(context),
                        second.toWords(context)
                    )
                    randomAnswer = { (Random.nextInt(51) + Random.nextInt(50)) }
                }
                //Subtraction
                1 -> {
                    var first = Random.nextInt(100)
                    var second = Random.nextInt(100)

                    if (first < second) {
                        first = second.also { second = first }
                    }

                    answer = first - second
                    question = context.resources.getString(
                        R.string.text_fragment_element_one_question_subtraction,
                        first.toWords(context),
                        second.toWords(context)
                    )
                    randomAnswer = {
                        var randFirst = Random.nextInt(100)
                        var randSecond = Random.nextInt(100)

                        if (randFirst < randSecond) {
                            randFirst = randSecond.also { randSecond = randFirst }
                        }
                        randFirst - randSecond
                    }
                }
                //Multiplication
                2 -> {
                    val first = Random.nextInt(11)
                    val second = Random.nextInt(10)
                    answer = first * second
                    question = context.resources.getString(
                        R.string.text_fragment_element_one_question_multiplication,
                        first.toWords(context),
                        second.toWords(context)
                    )
                    randomAnswer = { (Random.nextInt(11) * Random.nextInt(10)) }
                }
            }

            listOfAnswerButtonsData.addGeneratedMathActionQuestionData(
                answer,
                randomAnswer,
                countOfAnswers,
                rightAnswerPosition
            )

            val answerChoicerView = AnswerChoicerViewModel(
                question,
                listOfAnswerButtonsData,
                rightAnswerPosition
            )

            newQuestions.add(answerChoicerView)
        }

        return newQuestions
    }

    private fun ArrayList<Pair<String, Int>>.addGeneratedMathActionQuestionData(
        answer: Int,
        randomAnswer: () -> Int,
        countOfAnswers: Int,
        rightAnswerPosition: Int,
    ) {
        val listOfAnswerButtonsData = arrayListOf<Pair<String, Int>>()
        for (answerButtonPosition in 0 until countOfAnswers) {
            val randomCountOfClicks = Random.nextInt(500)
            val answerData: Pair<String, Int> =
                if (answerButtonPosition == rightAnswerPosition) {
                    Pair(answer.toWords(context), randomCountOfClicks)
                } else {
                    var newAnswer = randomAnswer()
                    while (listOfAnswerButtonsData.find {
                            it.first == newAnswer.toWords(
                                context
                            )
                        } != null) {
                        newAnswer = randomAnswer()
                    }
                    Pair(newAnswer.toWords(context), randomCountOfClicks)
                }
            listOfAnswerButtonsData.add(answerData)
        }
        this.addAll(listOfAnswerButtonsData)
    }

}