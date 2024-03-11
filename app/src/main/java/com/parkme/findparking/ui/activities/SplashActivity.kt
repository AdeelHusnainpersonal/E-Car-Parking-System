package com.parkme.findparking.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.parkme.findparking.R
import com.parkme.findparking.databinding.ActivitySplashBinding
import com.parkme.findparking.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    @Inject lateinit var auth: FirebaseAuth
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
            if (auth.currentUser != null){
                startActivity(AfterAuthActivity::class.java)
                finish()
            }
            else{
                startActivity(BeforeAuthActivity::class.java)
                finish()
            }
        }
    }
}