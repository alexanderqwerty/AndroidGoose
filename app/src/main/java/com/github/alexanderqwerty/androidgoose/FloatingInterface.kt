package com.github.alexanderqwerty.androidgoose

import android.view.View
import android.view.WindowManager

interface FloatingInterface {
    var lastX: Int
    var lastY: Int
    val screenWidth: Int
    val screenHeight: Int
    val params: WindowManager.LayoutParams
    var floatingView: View

    fun moveTo(x: Int, y: Int)
    fun animatedMoveTo(x: Int, y: Int)
    fun remove()
}