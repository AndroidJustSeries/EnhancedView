package com.kds.just.enhancedview.view

import android.widget.LinearLayout
import android.view.View
import com.kds.just.enhancedview.view.SegmentedView
import com.kds.just.enhancedview.EnhancedHalper.FontType
import com.kds.just.enhancedview.view.SegmentedView.OnSelectedItemListener
import android.content.Context
import android.util.AttributeSet
import com.kds.just.enhancedview.R
import com.kds.just.enhancedview.EnhancedHalper
import android.widget.TextView
import android.util.TypedValue
import android.view.Gravity
import android.content.res.Resources
import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatCheckBox
import com.kds.just.enhancedview.view.EnhancedControl
import com.kds.just.enhancedview.DrawableHelper
import android.content.res.TypedArray
import android.text.TextUtils
import android.text.Html
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import java.lang.Exception
import java.io.File
import android.os.Environment
import android.content.SharedPreferences
import android.app.Activity
import android.content.SharedPreferences.Editor
import android.graphics.*
import android.os.Build
import com.kds.just.enhancedview.EnhancedUtils

open class SegmentedView : LinearLayout, View.OnClickListener {
    private var mTextColorNormal = Color.parseColor("#d8d8d8")
    private var mTextColorSelected = Color.parseColor("#727af2")
    private var mTextSize = dp2px(10f)
    private var mBGColorNormal = Color.WHITE
    private var mBGColorSelected = Color.parseColor("#727af2")
    private var mStrokeColorNormal = Color.parseColor("#d8d8d8")
    private var mStrokeColorSelected = Color.parseColor("#727af2")
    private var mRoundRadius = dp2px(10f)
    private var mStrokeWidth = dp2px(1f)
    var selectedIndex = -1
        private set
    var mFontType: FontType? = null
    private var mOnSelectedItemListener: OnSelectedItemListener? = null
    fun setOnSelectedItemListener(l: OnSelectedItemListener?) {
        mOnSelectedItemListener = l
    }

    interface OnSelectedItemListener {
        fun onSelected(v: SegmentedView?, selectedView: View?, index: Int)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mTextColorNormal = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getColor(R.styleable.EnhancedView_textColorNormal, mTextColorNormal)
        mTextColorSelected = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getColor(R.styleable.EnhancedView_textColorSelected, mTextColorSelected)
        mStrokeColorNormal = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getColor(R.styleable.EnhancedView_BgStrokeColorNormal, mStrokeColorNormal)
        mStrokeColorSelected = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getColor(R.styleable.EnhancedView_BgStrokeColorSelected, mStrokeColorSelected)
        mBGColorNormal = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getColor(R.styleable.EnhancedView_BgColorNormal, Color.WHITE)
        mBGColorSelected = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getColor(R.styleable.EnhancedView_BgColorSelected, Color.parseColor("#727af2"))
        mRoundRadius = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getDimensionPixelSize(R.styleable.EnhancedView_BgRadius, mRoundRadius)
        mStrokeWidth = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getDimensionPixelSize(R.styleable.EnhancedView_StrokeWidth, mStrokeWidth)
        mTextSize = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            .getDimensionPixelSize(R.styleable.EnhancedView_textSize, mStrokeWidth)
        mFontType = EnhancedHalper.getFontType(context, attrs)
        init()
    }

    private fun init() {
        orientation = HORIZONTAL
        setWillNotDraw(false)
        setPadding(mStrokeWidth / 2, 0, mStrokeWidth / 2, 0)
        initPaint()
    }

    private var mBGNormalPaint: Paint? = null
    private var mBGNormalStrokePaint: Paint? = null
    private var mBGSelectPaint: Paint? = null
    private var mBGSelectStrokePaint: Paint? = null
    private var mMainPaint: Paint? = null
    private fun initPaint() {
        mBGNormalPaint = Paint()
        mBGNormalPaint!!.style = Paint.Style.FILL
        mBGNormalPaint!!.color = mBGColorNormal
        mBGNormalPaint!!.isAntiAlias = true
        mBGNormalStrokePaint = Paint()
        mBGNormalStrokePaint!!.style = Paint.Style.STROKE
        mBGNormalStrokePaint!!.color = mStrokeColorNormal
        mBGNormalStrokePaint!!.strokeWidth = mStrokeWidth.toFloat()
        mBGNormalStrokePaint!!.isAntiAlias = true
        mBGNormalStrokePaint!!.isDither = true
        mBGNormalStrokePaint!!.strokeJoin = Paint.Join.ROUND // set the join to round you want
        mBGSelectPaint = Paint()
        mBGSelectPaint!!.style = Paint.Style.FILL
        mBGSelectPaint!!.color = mBGColorSelected
        mBGSelectPaint!!.isAntiAlias = true
        mBGSelectStrokePaint = Paint()
        mBGSelectStrokePaint!!.style = Paint.Style.STROKE
        mBGSelectStrokePaint!!.color = mStrokeColorSelected
        mBGSelectStrokePaint!!.strokeWidth = mStrokeWidth.toFloat()
        mBGSelectStrokePaint!!.isAntiAlias = true
        mBGSelectStrokePaint!!.isDither = true
        mBGSelectStrokePaint!!.strokeJoin = Paint.Join.ROUND // set the join to round you want
        mMainPaint = Paint()
        mMainPaint!!.isAntiAlias = true
    }

    fun setFont(fonttype: FontType?) {
        mFontType = fonttype
        for (i in 0 until childCount) {
            val v = getChildAt(i)
            if (v is TextView) {
                EnhancedHalper.setFont(v, mFontType)
            }
        }
    }

    fun addItem(text: String?) {
        val tv = TextView(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        tv.layoutParams = params
        tv.setTextColor(EnhancedUtils.makeColorSelector(mTextColorNormal, mTextColorSelected))
        tv.text = text
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
        tv.gravity = Gravity.CENTER
        tv.tag = childCount
        tv.setOnClickListener(this)
        tv.setPadding(mStrokeWidth / 2, mStrokeWidth, mStrokeWidth / 2, mStrokeWidth)
        if (mFontType != null) {
            EnhancedHalper.setFont(tv, mFontType)
        }
        addView(tv)
    }

    fun setSelected(index: Int) {
        selectedIndex = index
        for (i in 0 until childCount) {
            val tv = getChildAt(i) as TextView
            tv.isSelected = tv.tag as Int == index
        }
        invalidate()
    }

    override fun onClick(v: View) {
        val selectIndex = v.tag as Int
        if (selectedIndex != selectIndex) {
            selectedIndex = selectIndex
            for (i in 0 until childCount) {
                val item = getChildAt(i)
                item.isSelected = v === item
            }
            invalidate()
        }
        if (mOnSelectedItemListener != null) {
            mOnSelectedItemListener!!.onSelected(this, v, selectIndex)
        }
    }

    private var mStrokePadding = 0
    override fun onDraw(canvas: Canvas) {
        val childCount = childCount
        mStrokePadding = mStrokeWidth / 2
        val rectF = RectF(
            mStrokePadding.toFloat(),  // left
            mStrokePadding.toFloat(),  // top
            (canvas.width - mStrokePadding).toFloat(),  // right
            (canvas.height - mStrokePadding).toFloat() // bottom
        )

        //draw background
        canvas.drawRoundRect(
            rectF,
            mRoundRadius.toFloat(),
            mRoundRadius.toFloat(),
            mBGNormalPaint!!
        )
        //draw round stroke
        canvas.drawRoundRect(
            rectF,
            mRoundRadius.toFloat(),
            mRoundRadius.toFloat(),
            mBGNormalStrokePaint!!
        )
        drawDivider(canvas, childCount)
        drawSelectRect(canvas, rectF, childCount)
    }

    private fun drawDivider(canvas: Canvas, childCount: Int) {
        //draw divider line
        val w = canvas.width - mStrokeWidth
        val h = canvas.height
        for (i in 1 until childCount) {
            val x = w / childCount * i + mStrokeWidth / 2
            canvas.drawLine(x.toFloat(), 0f, x.toFloat(), h.toFloat(), mBGNormalStrokePaint!!)
        }
    }

    private fun drawSelectRect(canvas: Canvas, rectF: RectF, childCount: Int) {
        //draw select item
        if (selectedIndex >= 0) {
            if (getChildCount() > 1) {
                val v = getChildAt(selectedIndex)
                val itemRectf = RectF(
                    v.left.toFloat(), (v.top + mStrokePadding).toFloat(), v.right
                        .toFloat(), (v.height - mStrokePadding).toFloat()
                )
                if (selectedIndex == 0) {    //first item
                    val path = getDrawRoundPath(itemRectf, mRoundRadius, 0, mRoundRadius, 0)
                    canvas.drawPath(path, mBGSelectPaint!!)
                    canvas.drawPath(path, mBGSelectStrokePaint!!)
                } else if (selectedIndex == childCount - 1) {    //list Item
                    val path = getDrawRoundPath(itemRectf, 0, mRoundRadius, 0, mRoundRadius)
                    canvas.drawPath(path, mBGSelectPaint!!)
                    canvas.drawPath(path, mBGSelectStrokePaint!!)
                } else {
                    canvas.drawRoundRect(itemRectf, 0f, 0f, mBGSelectPaint!!)
                    canvas.drawRoundRect(itemRectf, 0f, 0f, mBGSelectStrokePaint!!)
                }
            } else if (getChildCount() == 1) {
                //draw background
                canvas.drawRoundRect(
                    rectF,
                    mRoundRadius.toFloat(),
                    mRoundRadius.toFloat(),
                    mBGSelectPaint!!
                )
                //draw round stroke
                canvas.drawRoundRect(
                    rectF,
                    mRoundRadius.toFloat(),
                    mRoundRadius.toFloat(),
                    mBGSelectStrokePaint!!
                )
            }
        }
    }

    private fun getDrawRoundPath(rectF: RectF, lt: Int, rt: Int, lb: Int, rb: Int): Path {
        val topLeftArcBound = RectF()
        val topRightArcBound = RectF()
        val bottomLeftArcBound = RectF()
        val bottomRightArcBound = RectF()
        topRightArcBound[rectF.right - rt * 2, rectF.top, rectF.right] = rectF.top + rt * 2
        bottomRightArcBound[rectF.right - rb * 2, rectF.bottom - rb * 2, rectF.right] =
            rectF.bottom
        bottomLeftArcBound[rectF.left, rectF.bottom - lb * 2, rectF.left + lb * 2] = rectF.bottom
        topLeftArcBound[rectF.left, rectF.top, rectF.left + lt * 2] = rectF.top + lt * 2
        val path = Path()
        path.reset()
        path.moveTo(rectF.left + lt, rectF.top)
        //draw top horizontal line
        path.lineTo(rectF.right - rt, rectF.top)
        //draw top-right corner
        path.arcTo(topRightArcBound, -90f, 90f)
        //draw right vertical line
        path.lineTo(rectF.right, rectF.bottom - rb)
        //draw bottom-right corner
        path.arcTo(bottomRightArcBound, 0f, 90f)
        //draw bottom horizontal line
        path.lineTo(rectF.left + lb, rectF.bottom)
        //draw bottom-left corner
        path.arcTo(bottomLeftArcBound, 90f, 90f)
        //draw left vertical line
        path.lineTo(rectF.left, rectF.top + lt)
        //draw top-left corner
        path.arcTo(topLeftArcBound, 180f, 90f)
        path.close()
        return path
    }

    companion object {
        private const val TAG = "SegmentedView"
        fun dp2px(dp: Float): Int {
            val resources = Resources.getSystem()
            val px = dp * resources.displayMetrics.density
            return Math.ceil(px.toDouble()).toInt()
        }
    }
}