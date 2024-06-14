package com.dicoding.pukulenamcapstone.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.pukulenamcapstone.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        bottomNavigation.setupWithNavController(navController)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

    }
}