/*
 * Copyright (c) 2022
 * David de Andrés and Juan Carlos Ruiz
 * Development of apps for mobile devices
 * Universitat Politècnica de València
 */
package upv.dadm.ex12_actionbarandmenus.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import upv.dadm.ex12_actionbarandmenus.R
import upv.dadm.ex12_actionbarandmenus.databinding.ActivityMainBinding

/**
 * Lets the user navigate deeper and deeper into the app.
 * Each new level is represented by a new Fragment.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get the automatically generated view binding for the layout resource
        val binding = ActivityMainBinding.inflate(layoutInflater)
        // Set the activity content to the root element of the generated view
        setContentView(binding.root)

        // Make the custom ToolBar the ActionBar
        setSupportActionBar(binding.tbMain)
        // Get an instance of the NavController.
        // findNavController() does not work properly with FragmentContainerView in onCreate()
        val navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        // Configure the ActionBar to work with the NavController,
        // so that its title is updated when navigating
        setupActionBarWithNavController(navController, AppBarConfiguration(navController.graph))
    }

    /**
     * Manages the Up navigation.
     * First it tries to navigate Up in the navigation hierarchy from the NavController and,
     * if it does not succeed, then tries again from the AppCompatActivity.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}