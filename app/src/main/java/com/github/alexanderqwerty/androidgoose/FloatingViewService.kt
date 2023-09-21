package com.github.alexanderqwerty.androidgoose


import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.view.*
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.R)
class FloatingViewService : Service() {

    private lateinit var view1: FloatingView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        view1 = FloatingView(this, View.inflate(this, R.layout.draggableview, null))
        view1.setOnTouchListener()
        view1.animatedMoveTo(
            view1.screenWidth - view1.floatingView.width / 2,
            view1.screenHeight - view1.floatingView.height
        )

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        view1.remove()
    }
}
