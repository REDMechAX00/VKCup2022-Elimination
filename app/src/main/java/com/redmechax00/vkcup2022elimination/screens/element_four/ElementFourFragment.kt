package com.redmechax00.vkcup2022elimination.screens.element_four

import com.redmechax00.vkcup2022elimination.screens.base.BaseElementFragment
import com.redmechax00.vkcup2022elimination.views.filling.FillingView.Companion.markAsAnswer
import com.redmechax00.vkcup2022elimination.views.filling.FillingViewModel
import kotlin.random.Random

class ElementFourFragment : BaseElementFragment() {

    override fun initAdapter() {
        adapter = ElementFourAdapter { answerIsRight -> showSuccessAnimationOrNot(answerIsRight) }
        (adapter as ElementFourAdapter).addElements(createElements(15))
        recyclerView.adapter = adapter
    }

    override fun doWhenScrollReachMax() {
        (adapter as ElementFourAdapter).addElements(createElements(10))
    }

    private fun createElements(count: Int): MutableList<ElementFourModel> {
        val newElements = mutableListOf<ElementFourModel>()
        for (i in 0 until count) {
            val questionData = answersRepository(Random.nextInt(6))

            newElements.add(
                ElementFourModel(
                    adapter.itemCount + i,
                    FillingViewModel(questionData)
                )
            )
        }
        return newElements
    }

    private fun answersRepository(n: Int): String {
        return when (n) {
            0 -> {
                "Я пришел" + "к".markAsAnswer() + "тебе с " + "приветом".markAsAnswer() +
                        " рассказать что" + "солнце".markAsAnswer() + " встало "
            }
            1 -> {
                "Уж" + "небо".markAsAnswer() + "осенью".markAsAnswer() + "дышало, " +
                        "Уж" + "реже".markAsAnswer() + "солнышко блистало..."
            }
            2 -> {
                "Я помню" + "чудное".markAsAnswer() + "мгновенье: " +
                        "Передо" + "мной".markAsAnswer() + "явилась".markAsAnswer() + "ты..."
            }
            3 -> {
                "А" + "вы".markAsAnswer() + "ноктюрн " + "сыграть".markAsAnswer() +
                        "могли бы на " + "флейте".markAsAnswer() + "водосточных труб?"
            }
            4 -> {
                "Травка" + "зеленеет".markAsAnswer() + ", солнышко " + "блестит".markAsAnswer() +
                        ": ласточка с весною в" + "сени".markAsAnswer() + "к нам летит"
            }
            else -> {
                "Не верь тому," + "кто".markAsAnswer() +
                        ", говорит красиво, В его" + "словах".markAsAnswer() +
                        "всегда игра. Поверь тому, кто молчаливо, Творит красивые" +
                        "дела".markAsAnswer()
            }
        }
    }
}