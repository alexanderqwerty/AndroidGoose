package com.github.alexanderqwerty.androidgoose

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ResourceType")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val serviceIntent = Intent(this, FloatingViewService::class.java)
        startService(serviceIntent)

    }
}