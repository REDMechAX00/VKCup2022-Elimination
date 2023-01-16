package com.redmechax00.vkcup2022elimination.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.redmechax00.vkcup2022elimination.R
import com.redmechax00.vkcup2022elimination.screens.base.BaseFragment
import com.redmechax00.vkcup2022elimination.databinding.FragmentStartMenuBinding
import com.redmechax00.vkcup2022elimination.screens.element_five.ElementFiveFragment
import com.redmechax00.vkcup2022elimination.screens.element_four.ElementFourFragment
import com.redmechax00.vkcup2022elimination.screens.element_one.ElementOneFragment
import com.redmechax00.vkcup2022elimination.screens.element_three.ElementThreeFragment
import com.redmechax00.vkcup2022elimination.screens.element_two.ElementTwoFragment
import com.redmechax00.vkcup2022elimination.utils.startFragment

class StartMenuFragment : BaseFragment<FragmentStartMenuBinding>(R.layout.fragment_start_menu),
    View.OnClickListener {

    private val listOfButtons = mutableListOf<View>()
    private lateinit var mapOfFragments: HashMap<Any, Fragment>

    override fun createBinding(view: View): FragmentStartMenuBinding =
        FragmentStartMenuBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        setOnClickListeners()
    }

    private fun initFields() {
        val buttonsContainer = binding.startMenuButtonsContainer

        for (i in 0 until buttonsContainer.childCount) {
            listOfButtons.add(buttonsContainer.getChildAt(i).also { it.tag = i })
        }

        mapOfFragments = hashMapOf(
            Pair(0, ElementOneFragment()),
            Pair(1, ElementTwoFragment()),
            Pair(2, ElementThreeFragment()),
            Pair(3, ElementFourFragment()),
            Pair(4, ElementFiveFragment())
        )
    }

    private fun setOnClickListeners() {
        listOfButtons.forEach { it.setOnClickListener(this) }
    }

    override fun onClick(v: View) {
        startFragment(mapOfFragments[v.tag])
    }
}
