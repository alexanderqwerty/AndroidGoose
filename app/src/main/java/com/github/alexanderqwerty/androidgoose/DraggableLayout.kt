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

    private var offsetX = 0f
    private var offsetY = 0f
    private var isDragging = false
    private lateinit var draggableView: View

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
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Проверяем, находится ли касание внутри дочернего view
                for (i in 0 until childCount) {
                    val child = getChildAt(i)
                    if (isPointInsideView(x, y, child)) {
                        draggableView = child
                        isDragging = true
                        offsetX = x - child.x
                        offsetY = y - child.y
                        return true
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    // Перемещаем дочернее view в соответствии с перемещением касания
                    val newX = x - offsetX
                    val newY = y - offsetY
                    draggableView.layout(
                        newX.toInt(),
                        newY.toInt(),
                        (newX + draggableView.measuredWidth).toInt(),
                        (newY + draggableView.measuredHeight).toInt()
                    )
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }


    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val left = view.left
        val top = view.top
        val right = left + view.width
        val bottom = top + view.height
        return x >= left && x <= right && y >= top && y <= bottom
    }


}