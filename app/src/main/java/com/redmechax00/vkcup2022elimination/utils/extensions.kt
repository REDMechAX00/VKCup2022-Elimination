package com.redmechax00.vkcup2022elimination.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.vkcup2022elimination.R

fun Fragment.startFragment(fragment: Fragment?) {
    if (fragment != null) {
        this.requireActivity().supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_fragment_container, fragment)
            .commit()
    }
}

fun RecyclerView.addOnScrolledListener(onScrolled: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onScrolled()
        }
    })
}

fun Int.toWords(context: Context): String =
    RussianNumberToWords(context).convertLessThanOneHundred(this)


