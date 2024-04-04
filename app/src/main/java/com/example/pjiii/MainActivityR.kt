package com.example.pjiii

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pjiii.databinding.ActivityMainRBinding

class MainActivityR : AppCompatActivity() {
    private lateinit var biding: ActivityMainRBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        biding = ActivityMainRBinding.inflate(layoutInflater)
        setContentView(biding.root)

    }
}