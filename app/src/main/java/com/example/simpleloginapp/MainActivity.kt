package com.example.simpleloginapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simpleloginapp.databinding.ActivityMainBinding

/** Hosts the app navigation graph. */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
    }
}
