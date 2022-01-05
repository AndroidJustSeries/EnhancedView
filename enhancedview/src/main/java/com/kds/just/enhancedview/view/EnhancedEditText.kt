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

class EnhancedEditText : AppCompatEditText, EnhancedControl {
    var mDrawableHelper: DrawableHelper

    constructor(context: Context) : super(context) {
        mDrawableHelper = DrawableHelper()
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mDrawableHelper = DrawableHelper()
        val ta = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
        mDrawableHelper.mStrokeColorNormal = ta.getColor(R.styleable.EnhancedView_BgStrokeColorNormal, mDrawableHelper.mStrokeColorNormal)
        mDrawableHelper.mStrokeColorSelected = ta.getColor(R.styleable.EnhancedView_BgStrokeColorSelected, mDrawableHelper.mStrokeColorSelected)
        mDrawableHelper.mBGColorNormal = ta.getColor(R.styleable.EnhancedView_BgColorNormal, mDrawableHelper.mBGColorNormal)
        mDrawableHelper.mBGColorSelected = ta.getColor(R.styleable.EnhancedView_BgColorSelected, mDrawableHelper.mBGColorSelected)
        mDrawableHelper.mRoundRadius = ta.getDimensionPixelSize(R.styleable.EnhancedView_BgRadius, mDrawableHelper.mRoundRadius)
        mDrawableHelper.mStrokeWidth = ta.getDimensionPixelSize(R.styleable.EnhancedView_StrokeWidth, mDrawableHelper.mStrokeWidth)
        setTextColor(ta.getColor(R.styleable.EnhancedView_textColorNormal, mDrawableHelper.mTextColorNormal),
            ta.getColor(R.styleable.EnhancedView_textColorSelected, mDrawableHelper.mTextColorSelected))
        if (ta.getBoolean(R.styleable.EnhancedView_underline, false)) {
            setUnderLine(true)
        }
        setSelected(ta.getBoolean(R.styleable.EnhancedView_selected, false))

        val htmlText = ta.getString(R.styleable.EnhancedView_html)
        if (!TextUtils.isEmpty(htmlText)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setText(Html.fromHtml(htmlText,Html.FROM_HTML_MODE_LEGACY))
            } else {
                setText(Html.fromHtml(htmlText))
            }
        }

        EnhancedHalper.setFont(context, this, attrs)

        init()
    }

    private fun init() {
        setWillNotDraw(false)
        mDrawableHelper.init()
        if (mDrawableHelper.isDraw) {
            background = null
        }
    }

    override fun setUnderLine(isUnderLine: Boolean) {
        paintFlags = if (isUnderLine) {
            paintFlags or Paint.UNDERLINE_TEXT_FLAG
        } else {
            paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }

    override val strokeNormalColor: Int
        get() = mDrawableHelper.mStrokeColorNormal
    override val strokeSelectedColor: Int
        get() = mDrawableHelper.mStrokeColorSelected

    override fun setStrokeColor(normal: Int, selected: Int) {
        mDrawableHelper.mStrokeColorNormal = normal
        mDrawableHelper.mStrokeColorSelected = selected
        mDrawableHelper.init()
        invalidate()
    }

    override fun setStrokeWidth(w: Int) {
        mDrawableHelper.mStrokeWidth = w
        mDrawableHelper.init()
        invalidate()
    }

    override val backgroundNormalColor: Int
        get() = mDrawableHelper.mBGColorNormal
    override val backgroundSelectedColor: Int
        get() = mDrawableHelper.mBGColorSelected

    override fun setBackgroundColor(normal: Int, selected: Int) {
        mDrawableHelper.mBGColorNormal = normal
        mDrawableHelper.mBGColorSelected = selected
        mDrawableHelper.init()
        invalidate()
    }

    override fun setRoundRadius(radius: Int) {
        mDrawableHelper.mRoundRadius = radius
        mDrawableHelper.init()
        invalidate()
    }

    override val textNormalColor: Int
        get() = mDrawableHelper.mTextColorNormal
    override val textSelectedColor: Int
        get() = mDrawableHelper.mTextColorSelected

    override fun setTextColor(normal: Int, selected: Int) {
        mDrawableHelper.mTextColorNormal = normal
        mDrawableHelper.mTextColorSelected = selected
        val textColor = mDrawableHelper.getTextColor(android.R.attr.state_focused)
        textColor?.let { setTextColor(it) }
    }

    override fun setTypeface(tf: Typeface?, style: Int) {
        if (style == Typeface.NORMAL) {
            setFont(FontType.Font_Regular)
        } else if (style == Typeface.BOLD) {
            setFont(FontType.Font_Bold)
        }
    }

    override fun setFont(type: FontType?) {
        when (type) {
            FontType.Font_Light -> EnhancedHalper.setTextLight(context, this)
            FontType.Font_Regular -> EnhancedHalper.setTextRegular(context, this)
            FontType.Font_Bold -> EnhancedHalper.setTextBold(context, this)
            else -> {}
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (background == null) {
            val rect = Rect()
            getDrawingRect(rect)
            canvas.save()
            canvas.translate(rect.left.toFloat(), rect.top.toFloat())
            mDrawableHelper.drawBg(canvas, width, height, isFocused, isSelected)
            canvas.restore()
        }
        super.onDraw(canvas)
    }

    companion object {
        private const val TAG = "EnhancedTextView"
    }
}