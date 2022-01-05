package com.kds.just.enhancedview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.kds.just.enhancedview.DrawableHelper
import com.kds.just.enhancedview.EnhancedHalper
import com.kds.just.enhancedview.EnhancedHalper.FontType
import com.kds.just.enhancedview.R

open class EnhancedTextView : AppCompatTextView, EnhancedControl {
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

        val isUnderLine = ta.getBoolean(R.styleable.EnhancedView_underline, false)
        if (isUnderLine) {
            setUnderLine(true)
        }
        val isSelected = ta.getBoolean(R.styleable.EnhancedView_selected, false)
        setSelected(isSelected)
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
        val textColor = mDrawableHelper.getTextColor(android.R.attr.state_selected)
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
            mDrawableHelper.drawBg(canvas, width, height, isPressed, isSelected)
        }
        super.onDraw(canvas)
    }

    companion object {
        private const val TAG = "EnhancedTextView"
    }
}