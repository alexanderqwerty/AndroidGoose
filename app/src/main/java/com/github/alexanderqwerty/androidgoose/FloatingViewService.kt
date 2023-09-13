package com.github.alexanderqwerty.androidgoose

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.R)
class FloatingViewService : Service() {
    lateinit var windowManager: WindowManager
    lateinit var view: View
    var lastX = 0
    var lastY = 0
    var screenWidth = 0
    var screenHeight = 0


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.draggableview, null)
        // Настройте изображение и его размеры здесь
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        val displayMetrics = windowManager.currentWindowMetrics.bounds
        screenWidth = displayMetrics.width()
        screenHeight = displayMetrics.height()
        Log.i("FloatingImage", "screenWidth: $screenWidth screenHeight: $screenHeight")

        params.gravity = Gravity.TOP or Gravity.START
        windowManager.addView(view, params)
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - lastX).toInt()
                    val deltaY = (event.rawY - lastY).toInt()
                    params.x += deltaX
                    params.y += deltaY
//                    imageView.translationX = params.x.toFloat()
//                    imageView.translationY = params.y.toFloat()
                    // Ограничьте перемещение изображения, чтобы оно не выходило за пределы экрана
                    if (params.x < 0) view.translationX = params.x.toFloat()
                    if (params.y < 0) view.translationY = params.y.toFloat()
                    if (params.x > screenWidth - view.width) view.translationX =
                        params.x.toFloat() - screenWidth + view.width
                    if (params.y > screenHeight - view.height) view.translationY =
                        params.y.toFloat() - screenHeight + view.height
                    if (params.x > 0 && params.y > 0 && params.x < screenWidth - view.width && params.y < screenHeight - view.height) {
                        view.translationY = 0f
                        view.translationX = 0f
                    }
                    windowManager.updateViewLayout(view, params)

                    // Проверьте, близко ли изображение к краю экрана и удалите службу при необходимости
                    if (params.x < 0 || params.y < 0 || params.x > screenWidth || params.y > screenHeight) {
                        //remove()
                    }
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    true
                }
                else -> false
            }
        }
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
