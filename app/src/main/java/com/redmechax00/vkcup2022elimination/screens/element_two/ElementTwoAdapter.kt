package com.redmechax00.vkcup2022elimination.screens.element_two

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ItemElementTwoBinding
import com.redmechax00.vkcup2022elimination.screens.base.BaseElementAdapter
import com.redmechax00.vkcup2022elimination.views.matching.MatchingView

class ElementTwoAdapter(private val answerIsSelected: (isRight: Boolean) -> Unit) :
    BaseElementAdapter<ItemElementTwoBinding, ElementTwoModel, ElementTwoAdapter.ElementTwoHolder>(
        R.layout.item_element_two,
        ElementTwoDiffUtil
    ) {

    override fun createBinding(view: View): ItemElementTwoBinding = ItemElementTwoBinding.bind(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ElementTwoHolder
        holder.bind(currentList[position])
    }

    override fun createViewHolder(binding: ItemElementTwoBinding): ElementTwoHolder =
        ElementTwoHolder(binding)

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder as ElementTwoHolder
        holder.setAnswerListener { isRight ->
            answerIsSelected(isRight)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder as ElementTwoHolder
        holder.removeAnswerListener()
    }

    class ElementTwoHolder(binding: ItemElementTwoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val matchingView: MatchingView = binding.elementTwoItemMatchingView

        fun bind(currentElement: ElementTwoModel) {
            matchingView.populate(currentElement.matchingViewModel)
        }

        fun setAnswerListener(answerIsSelected: (isRight: Boolean) -> Unit) {
            matchingView.setAnswerListener(object : MatchingView.AnswerListener {
                override fun onDone(isRight: Boolean) {
                    answerIsSelected(isRight)
                }
            })
        }

        fun removeAnswerListener() {
            matchingView.setAnswerListener(null)
        }
    }

}