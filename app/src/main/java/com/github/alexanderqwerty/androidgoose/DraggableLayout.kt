package com.github.alexanderqwerty.androidgoose


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup


class DraggableLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // Разместите дочерние view внутри контейнера
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(
                child.left,
                child.top,
                child.left + child.measuredWidth,
                child.top + child.measuredHeight
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Измерьте дочерние view
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }
}