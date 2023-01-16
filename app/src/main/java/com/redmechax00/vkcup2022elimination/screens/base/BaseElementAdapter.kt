package com.redmechax00.vkcup2022elimination.screens.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseElementAdapter<VB : ViewBinding, VM : BaseElement, VH : RecyclerView.ViewHolder>(
    @LayoutRes private val layoutRes: Int,
    diffUtil: DiffUtil.ItemCallback<VM>
) :
    ListAdapter<VM, RecyclerView.ViewHolder>(
        diffUtil
    ) {

    private var _binding: VB? = null
    private val binding get() = requireNotNull(_binding) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutRes, parent, false)
        _binding = createBinding(view)
        return createViewHolder(binding)
    }

    abstract fun createBinding(view: View): VB

    abstract fun createViewHolder(binding: VB): VH

    override fun getItemCount() = currentList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    fun addElements(newElements: MutableList<VM>) {
        val data = mutableListOf<VM>()
        data.addAll(currentList)
        data.addAll(newElements)
        submitList(data)
    }
}