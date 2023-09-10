package com.github.alexanderqwerty.androidgoose

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import kotlin.math.sqrt


@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("ClickableViewAccessibility")
class FloatingImage(context: Context) {
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val imageView: ImageView = ImageView(context)
    private val textView: TextView = TextView(context)
    private var lastX = 0
    private var lastY = 0
    private val displayMetrics: Rect
    private val screenWidth: Int
    private val screenHeight: Int
    private val animator: ValueAnimator = ValueAnimator.ofInt(0, 1)
    private val speed: Float = 10F

    init {
        // Настройте изображение и его размеры здесь
        imageView.setImageResource(R.drawable.ic_launcher_foreground)
        imageView.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_200))

        textView.text = "0"
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val screenWidth = rootView.width
//        val screenHeight = rootView.height
        displayMetrics = windowManager.currentWindowMetrics.bounds

        screenWidth = context.display?.width!!.toInt()
        screenHeight = context.display?.height!!.toInt()
        Log.i("FloatingImage", "screenWidth: $screenWidth screenHeight: $screenHeight")

        params.gravity = Gravity.TOP or Gravity.START
        windowManager.addView(imageView, params)
        windowManager.addView(textView, params)
//         Добавьте слушатель событий перемещения
        imageView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                    Log.i("FloatingImage1", "params: x: ${params.x}, y: ${params.y}")
                    Log.i("FloatingImage1", "x: $lastX, y: $lastY")
                    true
                }
                MotionEvent.ACTION_MOVE -> {
//                    val deltaX = (event.rawX - lastX).toInt()
//                    val deltaY = (event.rawY - lastY).toInt()
//                    params.x += deltaX
//                    params.y += deltaY
////                    imageView.translationX = params.x.toFloat()
////                    imageView.translationY = params.y.toFloat()
//                    // Ограничьте перемещение изображения, чтобы оно не выходило за пределы экрана
//                    if (params.x < 0) imageView.translationX = params.x.toFloat()
//                    if (params.y < 0) imageView.translationY = params.y.toFloat()
//                    if (params.x > screenWidth - imageView.width) imageView.translationX =
//                        params.x.toFloat() - screenWidth + imageView.width
//                    if (params.y > screenHeight - imageView.height) imageView.translationY =
//                        params.y.toFloat() - screenHeight + imageView.height
//                    if (params.x > 0 && params.y > 0 && params.x < screenWidth - imageView.width && params.y < screenHeight - imageView.height) {
//                        imageView.translationY = 0f
//                        imageView.translationX = 0f
//                    }
//                    windowManager.updateViewLayout(imageView, params)
//
//                    // Проверьте, близко ли изображение к краю экрана и удалите службу при необходимости
//                    if (params.x < 0 || params.y < 0 || params.x > screenWidth || params.y > screenHeight) {
//                        //remove()
//                    }


                    true
                }

                MotionEvent.ACTION_UP -> {
                    val deltaX = (event.rawX - lastX).toInt()
                    val deltaY = (event.rawY - lastY).toInt()

                    if (animator.isRunning) {
                        animator.cancel()
                    }

                    val distance = sqrt((deltaX * deltaX + deltaY * deltaY).toDouble())
                    animator.duration =
                        (distance / speed).toLong() // Продолжительность анимации в миллисекундах (1 секунда)
                    val startX = params.x
                    val startY = params.y
                    animator.addUpdateListener { valueAnimator ->
                        val fraction = valueAnimator.animatedFraction
                        params.x = (startX + (deltaX) * fraction).toInt()
                        params.y = (startY + (deltaY) * fraction).toInt()
                        if (params.x > 0 && params.x < screenWidth - imageView.width)
                            imageView.translationX = 0f
                        if (params.y > 0 && params.y < screenHeight - imageView.height)
                            imageView.translationY = 0f
                        if (params.x < 0)
                            imageView.translationX = params.x.toFloat()
                        if (params.y < 0)
                            imageView.translationY = params.y.toFloat()
                        if (params.x > screenWidth - imageView.width)
                            imageView.translationX = params.x.toFloat() - screenWidth + imageView.width
                        if (params.y > screenHeight - imageView.height)
                            imageView.translationY = params.y.toFloat() - screenHeight + imageView.height

                        windowManager.updateViewLayout(imageView, params)
                    }

                    animator.start()


                    Log.i("FloatingImage2", "params: x: ${params.x}, y: ${params.y}")
                    Log.i("FloatingImage2", "x: $lastX, y: $lastY")
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