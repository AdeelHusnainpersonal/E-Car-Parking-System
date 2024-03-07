package com.parkme.findparking.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.parkme.findparking.R
import com.parkme.findparking.databinding.ActivityBeforeAuthBinding

class BeforeAuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBeforeAuthBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityBeforeAuthBinding.inflate(layoutInflater)
            setContentView(binding.root)
            inIt()
        }

        private fun inIt() {
            setOnClickListener()
        }

        private fun setOnClickListener() {

        }
}