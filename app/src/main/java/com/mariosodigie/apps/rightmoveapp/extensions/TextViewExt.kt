package com.mariosodigie.apps.rightmoveapp.extensions

import android.widget.TextView

fun TextView.formatString(resId: Int, formatArgs: Float){
    text = context.getString(resId, formatArgs)
}

fun TextView.formatString(resId: Int, formatArgs: Int){
    text = context.getString(resId, formatArgs)
}

fun TextView.formatString(resId: Int, formatArgs: String){
    text = context.getString(resId, formatArgs)
}