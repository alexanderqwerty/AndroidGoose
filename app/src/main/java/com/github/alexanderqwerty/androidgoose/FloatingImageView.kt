package com.github.alexanderqwerty.androidgoose

import android.content.Context
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class FloatingImageView(context: Context) : FloatingView(context) {

    fun setImage(resId: Int) {
        floatingView.findViewById<ImageView>(R.id.imageView).setImageResource(resId)
        update()
    }
}