package com.github.alexanderqwerty.androidgoose

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.*
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
class FloatingViewManager(private val context: Context) {

    private var windowManager: WindowManager? = null
    private var floatingView: View? = null
    private var lastX: Int = 0
    private var lastY: Int = 0
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var params: WindowManager.LayoutParams? = null

    init {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        floatingView = createFloatingView()
        setWindowParams()
    }

    @SuppressLint("InflateParams")
    private fun createFloatingView(): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.draggableview, null)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setWindowParams() {
        windowManager?.let {
            val displayMetrics = it.currentWindowMetrics.bounds
            screenWidth = displayMetrics.width()
            screenHeight = displayMetrics.height()

            params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )

            params!!.gravity = Gravity.TOP or Gravity.START
            it.addView(floatingView, params)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListener(listener: ((View, MotionEvent) -> Boolean)?) {

        floatingView?.setOnTouchListener(listener)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListener() {
        floatingView?.setOnTouchListener { view, event ->
            onTouch(view, event)
        }
    }

    fun getView(): View? {
        return floatingView
    }

    fun remove() {
        windowManager?.removeView(floatingView)
    }

    private fun onTouch(view: View, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = (event.rawX - lastX).toInt()
                val deltaY = (event.rawY - lastY).toInt()
                params!!.x += deltaX
                params!!.y += deltaY
                if (params!!.x < 0) view.translationX = params!!.x.toFloat()
                if (params!!.y < 0) view.translationY = params!!.y.toFloat()
                if (params!!.x > screenWidth - view.width) view.translationX =
                    params!!.x.toFloat() - screenWidth + view.width
                if (params!!.y > screenHeight - view.height) view.translationY =
                    params!!.y.toFloat() - screenHeight + view.height
                if (params!!.x > 0 && params!!.y > 0 && params!!.x < screenWidth - view.width && params!!.y < screenHeight - view.height) {
                    view.translationY = 0f
                    view.translationX = 0f
                }

                windowManager!!.updateViewLayout(view, params)

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