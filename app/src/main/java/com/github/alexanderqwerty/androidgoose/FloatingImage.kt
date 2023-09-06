package com.github.alexanderqwerty.androidgoose

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

@SuppressLint("ClickableViewAccessibility")
class FloatingImage(context: Context) {
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val imageView: ImageView = ImageView(context)
    private val textView: TextView = TextView(context)
    private var lastX = 0
    private var lastY = 0
    private val displayMetrics = DisplayMetrics()
    private val screenWidth: Int
    private val screenHeight: Int

    init {
        // Настройте изображение и его размеры здесь
        imageView.setImageResource(R.drawable.ic_launcher_foreground)
        textView.text = "0"
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        params.gravity = Gravity.TOP or Gravity.START
        windowManager.addView(imageView, params)
        windowManager.addView(textView, params)
        // Добавьте слушатель событий перемещения
        imageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - lastX).toInt()
                    val deltaY = (event.rawY - lastY).toInt()
                    val params = imageView.layoutParams as WindowManager.LayoutParams
                    params.x += deltaX
                    params.y += deltaY

                    // Ограничьте перемещение изображения, чтобы оно не выходило за пределы экрана
                    if (params.x < 0) params.x = 0
                    if (params.y < 0) params.y = 0
                    if (params.x > screenWidth - imageView.width) params.x =
                        screenWidth - imageView.width
                    if (params.y > screenHeight - imageView.height) params.y =
                        screenHeight - imageView.height

                    windowManager.updateViewLayout(imageView, params)

                    // Проверьте, близко ли изображение к краю экрана и удалите службу при необходимости
                    if (params.x < 50 || params.y < 50 || params.x > screenWidth - 50 || params.y > screenHeight - 50) {
                        remove()
                    }

                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                    true
                }
                else -> false
            }
        }
    }

    private fun remove() {
        windowManager.removeView(imageView)
    }
}