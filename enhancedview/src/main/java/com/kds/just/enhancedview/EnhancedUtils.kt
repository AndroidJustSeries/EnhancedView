package com.kds.just.enhancedview

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Point
import android.util.Size

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
        val wScale: Float = width.toFloat() / max_width.toFloat()
        val hScale: Float = height.toFloat() / max_height.toFloat()
        val scale = if (wScale > hScale) wScale else hScale
        if (isUpSize && scale <= 1) {
            return Size(width,height)
        } else {
            return Size((width / scale).toInt(),(height / scale).toInt())
        }
    }
}