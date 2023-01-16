package com.redmechax00.vkcup2022elimination.screens.element_one

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ItemElementOneBinding
import com.redmechax00.vkcup2022elimination.screens.base.BaseElementAdapter
import com.redmechax00.vkcup2022elimination.views.answer_choicer.AnswerChoicerView

class ElementOneAdapter(private val answerIsSelected: (isRight: Boolean) -> Unit) :
    BaseElementAdapter<ItemElementOneBinding, ElementOneModel, ElementOneAdapter.ElementOneHolder>(
        R.layout.item_element_one,
        ElementOneDiffUtil
    ) {

    override fun createBinding(view: View): ItemElementOneBinding = ItemElementOneBinding.bind(view)

    override fun createViewHolder(binding: ItemElementOneBinding): ElementOneHolder =
        ElementOneHolder(binding)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ElementOneHolder
        holder.bind(currentList[position])
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder as ElementOneHolder
        holder.setAnswerListener { isRight ->
            answerIsSelected(isRight)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder as ElementOneHolder
        holder.removeAnswerListener()
    }

    class ElementOneHolder(binding: ItemElementOneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val answerChoicerView: AnswerChoicerView = binding.elementOneItemAnswerChoiceView

        fun bind(currentElement: ElementOneModel) {
            answerChoicerView.populate(currentElement.answerChoicerViewModel)
        }

        fun setAnswerListener(answerIsSelected: (isRight: Boolean) -> Unit) {
            answerChoicerView.setAnswerListener(object : AnswerChoicerView.AnswerListener {
                override fun onDone(isRight: Boolean) {
                    answerIsSelected(isRight)
                }
            })
        }

        fun removeAnswerListener() {
            answerChoicerView.setAnswerListener(null)
        }
    }


}