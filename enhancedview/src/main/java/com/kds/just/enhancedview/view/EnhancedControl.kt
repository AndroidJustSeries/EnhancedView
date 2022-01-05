package com.kds.just.enhancedview.view

import android.widget.LinearLayout
import android.view.View
import android.graphics.Color
import com.kds.just.enhancedview.view.SegmentedView
import com.kds.just.enhancedview.EnhancedHalper.FontType
import com.kds.just.enhancedview.view.SegmentedView.OnSelectedItemListener
import android.content.Context
import android.util.AttributeSet
import com.kds.just.enhancedview.R
import com.kds.just.enhancedview.EnhancedHalper
import android.graphics.Paint
import android.widget.TextView
import android.util.TypedValue
import android.view.Gravity
import android.graphics.Canvas
import android.graphics.RectF
import android.content.res.Resources
import android.content.res.ColorStateList
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatCheckBox
import com.kds.just.enhancedview.view.EnhancedControl
import com.kds.just.enhancedview.DrawableHelper
import android.content.res.TypedArray
import android.text.TextUtils
import android.text.Html
import androidx.appcompat.widget.AppCompatEditText
import android.graphics.Rect
import androidx.appcompat.widget.AppCompatTextView
import java.lang.Exception
import java.io.File
import android.os.Environment
import android.content.SharedPreferences
import android.app.Activity
import android.content.SharedPreferences.Editor
import android.os.Build

interface EnhancedControl {
    fun setUnderLine(isUnderLine: Boolean)
    val strokeNormalColor: Int
    val strokeSelectedColor: Int
    fun setStrokeColor(normal: Int, selected: Int)
    fun setStrokeWidth(w: Int)
    val backgroundNormalColor: Int
    val backgroundSelectedColor: Int
    fun setBackgroundColor(normal: Int, selected: Int)
    fun setRoundRadius(radius: Int)
    val textNormalColor: Int
    val textSelectedColor: Int
    fun setTextColor(normal: Int, selected: Int)
    fun setTypeface(tf: Typeface?, style: Int)
    fun setFont(type: FontType?)
}