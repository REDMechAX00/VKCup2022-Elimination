package com.redmechax00.vkcup2022elimination.screens.element_three

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ItemElementThreeBinding
import com.redmechax00.vkcup2022elimination.screens.base.BaseElementAdapter
import com.redmechax00.vkcup2022elimination.views.drag_and_drop.DragAndDropView

class ElementThreeAdapter(private val answerIsSelected: (isRight: Boolean) -> Unit) :
    BaseElementAdapter<ItemElementThreeBinding, ElementThreeModel, ElementThreeAdapter.ElementThreeHolder>(
        R.layout.item_element_three,
        ElementThreeDiffUtil
    ) {

    override fun createBinding(view: View): ItemElementThreeBinding =
        ItemElementThreeBinding.bind(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ElementThreeHolder
        holder.bind(currentList[position])
    }

    override fun createViewHolder(binding: ItemElementThreeBinding): ElementThreeHolder =
        ElementThreeHolder(binding)

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder as ElementThreeHolder
        holder.setAnswerListener { isRight ->
            answerIsSelected(isRight)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder as ElementThreeHolder
        holder.removeAnswerListener()
    }

    class ElementThreeHolder(binding: ItemElementThreeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val dragAndDropView: DragAndDropView = binding.elementThreeItemDragAndDrop

        fun bind(currentElement: ElementThreeModel) {
            dragAndDropView.populate(currentElement.dragAndDropViewModel)
        }

        fun setAnswerListener(answerIsSelected: (isRight: Boolean) -> Unit) {
            dragAndDropView.setAnswerListener(object : DragAndDropView.AnswerListener {
                override fun onDone(isRight: Boolean) {
                    answerIsSelected(isRight)
                }
            })
        }

        fun removeAnswerListener() {
            dragAndDropView.setAnswerListener(null)
        }
    }

}