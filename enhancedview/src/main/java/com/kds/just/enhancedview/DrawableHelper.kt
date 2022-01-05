package com.kds.just.enhancedview

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

class DrawableHelper {
    var mBGColorNormal = 0
    var mBGColorSelected = 0
    var mStrokeColorNormal = 0
    var mStrokeColorSelected = 0
    var mRoundRadius = 0
    var mStrokeWidth = 0
    private var mBGNormalPaint: Paint? = null
    private var mBGNormalStrokePaint: Paint? = null
    private var mBGSelectPaint: Paint? = null
    private var mBGSelectStrokePaint: Paint? = null
    private var mMainPaint: Paint? = null
    private var mStrokePadding = 0
    fun init() {
        if (mBGColorNormal == 0) {
            mBGNormalPaint = null
        } else {
            mBGNormalPaint = Paint()
            mBGNormalPaint!!.style = Paint.Style.FILL
            mBGNormalPaint!!.color = mBGColorNormal
            mBGNormalPaint!!.isAntiAlias = true
        }
        if (mStrokeColorNormal == 0) {
            mBGNormalStrokePaint = null
        } else {
            mBGNormalStrokePaint = Paint()
            mBGNormalStrokePaint!!.style = Paint.Style.STROKE
            mBGNormalStrokePaint!!.color = mStrokeColorNormal
            mBGNormalStrokePaint!!.strokeWidth = mStrokeWidth.toFloat()
            mBGNormalStrokePaint!!.isAntiAlias = true
            mBGNormalStrokePaint!!.isDither = true
            mBGNormalStrokePaint!!.strokeJoin = Paint.Join.ROUND // set the join to round you want
        }
        if (mBGColorSelected == 0) {
            mBGSelectPaint = null
        } else {
            mBGSelectPaint = Paint()
            mBGSelectPaint!!.style = Paint.Style.FILL
            mBGSelectPaint!!.color = mBGColorSelected
            mBGSelectPaint!!.isAntiAlias = true
        }
        if (mStrokeColorSelected == 0) {
            mBGSelectStrokePaint = null
        } else {
            mBGSelectStrokePaint = Paint()
            mBGSelectStrokePaint!!.style = Paint.Style.STROKE
            mBGSelectStrokePaint!!.color = mStrokeColorSelected
            mBGSelectStrokePaint!!.strokeWidth = mStrokeWidth.toFloat()
            mBGSelectStrokePaint!!.isAntiAlias = true
            mBGSelectStrokePaint!!.isDither = true
            mBGSelectStrokePaint!!.strokeJoin = Paint.Join.ROUND // set the join to round you want
        }
        mMainPaint = Paint()
        mMainPaint!!.isAntiAlias = true
    }

    val isDraw: Boolean
        get() = mBGNormalPaint != null || mBGNormalStrokePaint != null || mBGSelectPaint != null || mBGSelectStrokePaint != null

    fun drawBg(canvas: Canvas, w: Int, h: Int, isPressed: Boolean, isSelected: Boolean) {
        mStrokePadding = mStrokeWidth / 2
        val rectF = RectF(
            mStrokePadding.toFloat(),  // left
            mStrokePadding.toFloat(),  // top
            (w - mStrokePadding).toFloat(),  // right
            (h - mStrokePadding).toFloat() // bottom
        )
        if (isSelected) {
            drawRect(canvas, rectF, mRoundRadius, mBGSelectPaint)
            if (mStrokeWidth > 0) {
                drawRect(canvas, rectF, mRoundRadius, mBGSelectStrokePaint)
            }
        } else if (isPressed) {
            drawRect(canvas, rectF, mRoundRadius, mBGSelectPaint)
            if (mStrokeWidth > 0) {
                drawRect(canvas, rectF, mRoundRadius, mBGSelectStrokePaint)
            }
        } else {
            drawRect(canvas, rectF, mRoundRadius, mBGNormalPaint)
            if (mStrokeWidth > 0) {
                drawRect(canvas, rectF, mRoundRadius, mBGNormalStrokePaint)
            }
        }
    }

    private fun drawRect(canvas: Canvas, rectF: RectF, roundRadius: Int, paint: Paint?) {
        if (paint != null) {
            if (roundRadius == 0) {
                paint.strokeJoin = Paint.Join.MITER
                canvas.drawRect(rectF, paint)
            } else {
                paint.strokeJoin = Paint.Join.ROUND
                canvas.drawRoundRect(rectF, roundRadius.toFloat(), roundRadius.toFloat(), paint)
            }
        }
    }

    var mTextColorNormal = 0
    var mTextColorSelected = 0
    fun getTextColor(option: Int): ColorStateList? {
        return if (mTextColorNormal != 0 && mTextColorSelected != 0) {
            EnhancedUtils.makeTextColorSelector(option, mTextColorNormal, mTextColorSelected)
        } else null
    }

    companion object {
        private const val TAG = "DrawableHelper"
    }
}