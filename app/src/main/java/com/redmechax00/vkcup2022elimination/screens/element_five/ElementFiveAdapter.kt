package com.redmechax00.vkcup2022elimination.screens.element_five

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.ItemElementFiveBinding
import com.redmechax00.vkcup2022elimination.screens.base.BaseElementAdapter
import com.redmechax00.vkcup2022elimination.views.rating.RatingView

class ElementFiveAdapter(private val answerIsSelected: (rating: Int) -> Unit) :
    BaseElementAdapter<ItemElementFiveBinding, ElementFiveModel, ElementFiveAdapter.ElementFiveHolder>(
        R.layout.item_element_five,
        ElementFiveDiffUtil
    ) {

    override fun createBinding(view: View): ItemElementFiveBinding =
        ItemElementFiveBinding.bind(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ElementFiveHolder
        holder.bind(currentList[position])
    }

    override fun createViewHolder(binding: ItemElementFiveBinding): ElementFiveHolder =
        ElementFiveHolder(binding)

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder as ElementFiveHolder
        holder.setAnswerListener { isRight ->
            answerIsSelected(isRight)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder as ElementFiveHolder
        holder.removeAnswerListener()
    }

    class ElementFiveHolder(binding: ItemElementFiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val ratingView: RatingView = binding.elementFiveItemRating

        fun bind(currentElement: ElementFiveModel) {
            ratingView.setCountOfStars(currentElement.countOfStars)
        }

        fun setAnswerListener(answerIsSelected: (rating: Int) -> Unit) {
            ratingView.setAnswerListener(object : RatingView.RatingListener {
                override fun onDone(rating: Int) {
                    answerIsSelected(rating)
                }
            })
        }

        fun removeAnswerListener() {
            ratingView.setAnswerListener(null)
        }
    }

}