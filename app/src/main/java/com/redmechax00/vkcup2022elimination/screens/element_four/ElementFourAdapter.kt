package com.redmechax00.vkcup2022elimination.screens.element_four

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ItemElementFourBinding
import com.redmechax00.vkcup2022elimination.screens.base.BaseElementAdapter
import com.redmechax00.vkcup2022elimination.views.filling.FillingView

class ElementFourAdapter(private val answerIsSelected: (isRight: Boolean) -> Unit) :
    BaseElementAdapter<ItemElementFourBinding, ElementFourModel, ElementFourAdapter.ElementFourHolder>(
        R.layout.item_element_four,
        ElementFourDiffUtil
    ) {

    override fun createBinding(view: View): ItemElementFourBinding =
        ItemElementFourBinding.bind(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ElementFourHolder
        holder.bind(currentList[position])
    }

    override fun createViewHolder(binding: ItemElementFourBinding): ElementFourHolder =
        ElementFourHolder(binding)

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder as ElementFourHolder
        holder.setAnswerListener { isRight ->
            answerIsSelected(isRight)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder as ElementFourHolder
        holder.removeAnswerListener()
    }

    class ElementFourHolder(binding: ItemElementFourBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val fillingView: FillingView = binding.elementFourItemFilling

        fun bind(currentElement: ElementFourModel) {
            fillingView.populate(currentElement.fillingViewModel)
        }

        fun setAnswerListener(answerIsSelected: (isRight: Boolean) -> Unit) {
            fillingView.setAnswerListener(object : FillingView.AnswerListener {
                override fun onDone(isRight: Boolean) {
                    answerIsSelected(isRight)
                }
            })
        }

        fun removeAnswerListener() {
            fillingView.setAnswerListener(null)
        }
    }

}