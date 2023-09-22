package com.github.alexanderqwerty.androidgoose

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ViewConstructor")
open class FloatingView(context: Context) : View(context),
    FloatingInterface {
    final override var floatingView: View = inflate(context, R.layout.draggableview, null)
        set(value) {
            windowManager.removeView(floatingView)
            field = value
            floatingView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            floatingView.layout(0, 0, floatingView.measuredWidth, floatingView.measuredHeight)
            windowManager.addView(floatingView, params)
        }

    override fun update() {
        windowManager.removeView(floatingView)
        floatingView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        floatingView.layout(0, 0, floatingView.measuredWidth, floatingView.measuredHeight)
        windowManager.addView(floatingView, params)
    }

    override var lastX: Int = 0
    override var lastY: Int = 0
    override val screenWidth: Int = context.resources.displayMetrics.widthPixels
    override val screenHeight: Int = context.resources.displayMetrics.heightPixels
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val animator: ValueAnimator = ValueAnimator.ofInt(0)

    @RequiresApi(Build.VERSION_CODES.O)
    final override val params: WindowManager.LayoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    init {
        params.gravity = Gravity.TOP or Gravity.START
        windowManager.addView(floatingView, params)

        floatingView.measure(
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        floatingView.layout(0, 0, floatingView.measuredWidth, floatingView.measuredHeight)
    }

    /** set default onTouchListener */
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setOnTouchListener() {

        floatingView.setOnTouchListener { _, event ->
            if (animator.isRunning) animator.cancel()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX.toInt()
                    lastY = event.rawY.toInt()
                    Log.i("FloatingViewManager", "lastX: $lastX, lastY: $lastY")
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaX = (event.rawX - lastX).toInt()
                    val deltaY = (event.rawY - lastY).toInt()
                    moveTo(params.x + deltaX, params.y + deltaY)

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

    /** move view to new position by x and y */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun moveTo(x: Int, y: Int) {
        Log.i("FloatingViewManager", "x: $x, y: $y")
        params.x = x
        params.y = y
        val view = floatingView.rootView
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
    }

    /** animated move view to new position by x and y **/
    //TODO(reformat animation moving)
    override fun animatedMoveTo(x: Int, y: Int) {

        if (animator.isRunning) {
            animator.cancel()
        }
        val deltaX = x - params.x
        val deltaY = y - params.y
        val distance = kotlin.math.sqrt((deltaX * deltaX + deltaY * deltaY).toDouble())
        animator.duration =
            (distance).toLong()
        val startX = params.x
        val startY = params.y
        animator.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction
            moveTo((startX + (deltaX) * fraction).toInt(), (startY + (deltaY) * fraction).toInt())
        }
        animator.start()
    }


    /** remove view */
    override fun remove() {
        windowManager.removeView(floatingView)
    }
}