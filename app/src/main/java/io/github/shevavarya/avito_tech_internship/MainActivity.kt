package io.github.shevavarya.avito_tech_internship

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import io.github.shevavarya.avito_tech_internship.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.libraryFragment -> showBottomNav()
                R.id.chartsFragment -> showBottomNav()
                R.id.settingsFragment -> showBottomNav()
                R.id.playerFragment -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavigation.isVisible = true
        binding.view.isVisible = true
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.isGone = true
        binding.view.isGone = true
    }
}