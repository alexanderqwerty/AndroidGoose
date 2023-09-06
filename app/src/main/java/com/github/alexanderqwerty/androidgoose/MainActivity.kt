package com.github.alexanderqwerty.androidgoose

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val floatingImage = FloatingImage(this)
//        val serviceIntent = Intent(this, FloatingImageService::class.java)
//        startService(serviceIntent)
    }
}