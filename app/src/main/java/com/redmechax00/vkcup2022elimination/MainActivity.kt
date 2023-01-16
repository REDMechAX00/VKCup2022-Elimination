package com.redmechax00.vkcup2022elimination

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.redmechax00.vkcup2022elimination.screens.StartMenuFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
            startMainFragment()
        }
    }

    private fun startMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, StartMenuFragment())
            .commit()
    }
}