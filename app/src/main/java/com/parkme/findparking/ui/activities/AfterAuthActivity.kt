package com.parkme.findparking.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.parkme.findparking.R
import com.parkme.findparking.databinding.ActivityAfterAuthBinding
import com.parkme.findparking.ui.fragments.home.HomeFragment
import com.parkme.findparking.utils.BackPressedUtils.goBackPressed
import com.parkme.findparking.utils.gone
import com.parkme.findparking.utils.visible

class AfterAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterAuthBinding
    private lateinit var navHostFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterAuthBinding.inflate(layoutInflater)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_home)!!
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        setUpBottomBar()
        handleBackPressed()
    }

    private fun setUpBottomBar() {
        binding.bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment || destination.id == R.id.bookingsFragment || destination.id == R.id.profileFragment) {
                binding.bottomNavigationView.visible()
            } else {
                binding.bottomNavigationView.gone()
            }
        }
    }

    private fun handleBackPressed() {
        goBackPressed {
            if (navHostFragment.childFragmentManager.fragments.first() is HomeFragment) {
                finishAffinity()
            } else {
                navHostFragment.findNavController().popBackStack()
            }
        }
    }

}