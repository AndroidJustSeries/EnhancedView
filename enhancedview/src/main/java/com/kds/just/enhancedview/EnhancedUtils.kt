package com.kds.just.enhancedview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Point
import android.text.TextUtils
import android.util.Log
import android.util.Size
import com.kds.just.enhancedview.view.EnhancedImageView

object EnhancedUtils {
    fun dp2px(dp: Float): Int {
        val resources = Resources.getSystem()
        val px = dp * resources.displayMetrics.density
        return Math.ceil(px.toDouble()).toInt()
    }

    fun makeTextColorSelector(option: Int, normal: Int, sel: Int): ColorStateList {
        var selected = sel
        if (selected == 0) {
            selected = normal
        }
        val states = arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(option),
            intArrayOf(-android.R.attr.state_empty)
        )
        val colors = intArrayOf(
            selected,
            selected,
            normal
        )
        return ColorStateList(states, colors)
    }

    fun makeColorSelector(normal: Int, sel: Int): ColorStateList {
        var selected = sel
        if (selected == 0) {
            selected = normal
        }
        val states = arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_empty)
        )
        val colors = intArrayOf(
            selected,
            selected,
            normal
        )
        return ColorStateList(states, colors)
    }

    /**
     * width와 height 모두 max_size보다 작은 비율로 scale한 사이즈를 반환한다.
     * @param isUpSize : 반환되는 fitScaleSize가 width와 height보다 커지는 것을 허용할지 여부
     */
    fun getFitScaleSize(width: Int, height: Int, max_width: Int, max_height: Int, isUpSize:Boolean = false): Size {
        val wScale: Float = if (max_width > 0) width.toFloat() / max_width.toFloat() else 0f
        val hScale: Float = if (max_height > 0) height.toFloat() / max_height.toFloat() else 0f
        val scale = Math.max(wScale , hScale)
        if (isUpSize && scale <= 1) {
            return Size(width,height)
        } else {
            return Size((width / scale).toInt(),(height / scale).toInt())
        }
    }

    private var mIsLogging = false
    fun setLogging(isLogging:Boolean) {
        mIsLogging = isLogging
    }

    fun log(log:String) {
        if (mIsLogging) {
            Log.i("EnhancedView",log)
        }
    }

    fun getIdString(context: Context,id:Int) : String {
        try {
            var name = context.getResources().getResourceName(id)
            if (!TextUtils.isEmpty(name) && name.lastIndexOf("/") > 0) {
                name = name.substring(name.lastIndexOf("/") + 1,name.length)
            }
            return name
        } catch (e:Exception) {
        }
        return ""
    }
}