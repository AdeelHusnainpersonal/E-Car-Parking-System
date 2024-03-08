package com.parkme.findparking.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.parkme.findparking.R
import com.parkme.findparking.databinding.ActivitySplashBinding
import com.parkme.findparking.utils.startActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
    }

    private fun inIt() {
        setUpSplash()
    }

    private fun setUpSplash() {
        lifecycleScope.launch{
            delay(1200)
            startActivity(AfterAuthActivity::class.java)
            finish()
        }
    }
}