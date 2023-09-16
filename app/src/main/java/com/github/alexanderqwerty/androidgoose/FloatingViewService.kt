package com.github.alexanderqwerty.androidgoose


import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.*
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
class FloatingViewService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var view: View

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        val floatingViewManager = FloatingViewManager(this)
        floatingViewManager.setOnTouchListener()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        remove()
    }

    private fun remove() {
        windowManager.removeView(view)
    }
}
