package com.redmechax00.vkcup2022elimination.screens.base

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.airbnb.lottie.LottieAnimationView
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.databinding.FragmentBaseElementBinding
import com.redmechax00.vkcup2022elimination.utils.addOnScrolledListener

abstract class BaseElementFragment :
    BaseFragment<FragmentBaseElementBinding>(R.layout.fragment_base_element) {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    private lateinit var foregroundAnimation: LottieAnimationView

    override fun createBinding(view: View): FragmentBaseElementBinding =
        FragmentBaseElementBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        addListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeListeners()
    }

    private fun initFields() {
        foregroundAnimation = binding.baseElementForegroundAnimation
        recyclerView = binding.baseElementRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        initAdapter()
    }

    abstract fun initAdapter()

    abstract fun doWhenScrollReachMax()

    private fun addListeners() {
        addRecyclerViewScrollListener()
        addForegroundAnimationListener()
    }

    private fun removeListeners() {
        recyclerView.clearOnScrollListeners()
        foregroundAnimation.removeAllAnimatorListeners()
    }

    private fun addRecyclerViewScrollListener() {
        recyclerView.addMaxScrollLoaderListener { onLoaded ->
            doWhenScrollReachMax()
            onLoaded()
        }
    }

    private fun addForegroundAnimationListener() {
        foregroundAnimation.addAnimatorUpdateListener {
            if (it.isRunning) {
                when (it.animatedValue as Float) {
                    0f -> {
                        foregroundAnimation.visibility = View.VISIBLE
                    }
                    1f -> {
                        foregroundAnimation.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun RecyclerView.addMaxScrollLoaderListener(maxScrollReached: (onLoaded: () -> Unit) -> Unit) {
        var isLoading = false
        this.addOnScrolledListener {
            if (!isLoading) {
                if (this.isMaxScrollReached()) {
                    isLoading = true
                    maxScrollReached {
                        isLoading = false
                    }
                }
            }
        }
    }

    private fun RecyclerView.isMaxScrollReached(): Boolean {
        val maxScroll =
            this.computeVerticalScrollRange() - this.computeVerticalScrollExtent()
        val currentScroll =
            this.computeVerticalScrollOffset() + this.computeVerticalScrollExtent()
        return currentScroll >= maxScroll
    }

    fun showSuccessAnimationOrNot(answerIsRight: Boolean) {
        when (answerIsRight) {
            true -> {
                foregroundAnimation.visibility = View.VISIBLE
                foregroundAnimation.playAnimation()
            }
            false -> {}
        }
    }
}