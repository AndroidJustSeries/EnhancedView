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

object EnhancedHalper {
    const val INI_FILENAME_CONFIG = "enhanced_font_path"

    var NAME_LIGHT: String? = null
    var NAME_REGULAR: String? = null
    var NAME_BOLD: String? = null
    var NAME_EXTRA_BOLD: String? = null

    const val FONT_PATH_LIGHT = "FONT_PATH_LIGHT"
    const val FONT_PATH_REGULAR = "FONT_PATH_REGULAR"
    const val FONT_PATH_BOLD = "FONT_PATH_BOLD"
    const val FONT_PATH_EXTRA_BOLD = "FONT_PATH_EXTRA_BOLD"

    var mFont_Light: Typeface? = null
    var mFont_Regular: Typeface? = null
    var mFont_Bold: Typeface? = null
    var mFont_ExtraBold: Typeface? = null

    fun initFontAssetPath(ctx: Context, light: String?, regular: String?, bold: String?, extrabold: String?) {
        NAME_LIGHT = light
        NAME_REGULAR = regular
        NAME_BOLD = bold
        NAME_EXTRA_BOLD = extrabold
        put(ctx, FONT_PATH_LIGHT, NAME_LIGHT)
        put(ctx, FONT_PATH_REGULAR, NAME_REGULAR)
        put(ctx, FONT_PATH_BOLD, NAME_BOLD)
        put(ctx, FONT_PATH_EXTRA_BOLD, NAME_EXTRA_BOLD)
    }

    fun getLightPath(ctx: Context): String? {
        if (TextUtils.isEmpty(NAME_LIGHT)) {
            NAME_LIGHT = EnhancedHalper[ctx, FONT_PATH_LIGHT]
        }
        return NAME_LIGHT
    }

    fun getRegularPath(ctx: Context): String? {
        if (TextUtils.isEmpty(NAME_REGULAR)) {
            NAME_REGULAR = EnhancedHalper[ctx, FONT_PATH_REGULAR]
        }
        return NAME_REGULAR
    }

    fun getBoldPath(ctx: Context): String? {
        if (TextUtils.isEmpty(NAME_BOLD)) {
            NAME_BOLD = EnhancedHalper[ctx, FONT_PATH_BOLD]
        }
        return NAME_BOLD
    }

    fun getExtraBoldPath(ctx: Context): String? {
        if (TextUtils.isEmpty(NAME_EXTRA_BOLD)) {
            NAME_EXTRA_BOLD = EnhancedHalper[ctx, FONT_PATH_EXTRA_BOLD]
        }
        return NAME_EXTRA_BOLD
    }

    fun getFontType(ctx: Context, attrs: AttributeSet?): FontType {
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
        return FontType.fromId(a.getInt(R.styleable.EnhancedView_cfont, 0))
    }

    fun setFont(ctx: Context, v: TextView, attrs: AttributeSet?) {
        val fontType = getFontType(ctx, attrs)
        var isFontChanged = false
        when (fontType) {
            FontType.Font_Light -> isFontChanged = setTextLight(ctx, v)
            FontType.Font_Regular -> isFontChanged = setTextRegular(ctx, v)
            FontType.Font_Bold -> isFontChanged = setTextBold(ctx, v)
            FontType.Font_ExtraBold -> isFontChanged = setTextExtraBold(ctx, v)
            else -> {}
        }
        if (isFontChanged) {
            v.includeFontPadding = false
        }
    }

    fun setFont(v: TextView, fontType: FontType?) {
        var isFontChanged = false
        when (fontType) {
            FontType.Font_Light -> isFontChanged = setTextLight(v.context, v)
            FontType.Font_Regular -> isFontChanged = setTextRegular(v.context, v)
            FontType.Font_Bold -> isFontChanged = setTextBold(v.context, v)
            FontType.Font_ExtraBold -> isFontChanged = setTextExtraBold(v.context, v)
            else -> {}
        }
        if (isFontChanged) {
            v.includeFontPadding = false
        }
    }

    fun setTextLight(ctx: Context, v: TextView): Boolean {
        return if (mFont_Light == null) {
            try {
                mFont_Light = Typeface.createFromAsset(ctx.assets, NAME_LIGHT)
                v.typeface = mFont_Light
                true
            } catch (e: Exception) {
                try {
                    mFont_Light = Typeface.createFromFile(File(Environment.getExternalStorageDirectory(), NAME_LIGHT))
                    v.typeface = mFont_Light
                    return true
                } catch (e1: Exception) {
                }
                false
            }
        } else {
            v.typeface = mFont_Light
            true
        }
    }

    fun setTextRegular(ctx: Context, v: TextView): Boolean {
        return if (mFont_Regular == null) {
            try {
                mFont_Regular = Typeface.createFromAsset(ctx.assets, NAME_REGULAR)
                v.typeface = mFont_Regular
                true
            } catch (e: Exception) {
                try {
                    mFont_Regular = Typeface.createFromFile(File(Environment.getExternalStorageDirectory(), NAME_REGULAR))
                    v.typeface = mFont_Regular
                    return true
                } catch (e1: Exception) {
                }
                false
            }
        } else {
            v.typeface = mFont_Regular
            true
        }
    }

    fun setTextBold(ctx: Context, v: TextView): Boolean {
        return if (mFont_Bold == null) {
            try {
                mFont_Bold = Typeface.createFromAsset(ctx.assets, NAME_BOLD)
                v.typeface = mFont_Bold
                true
            } catch (e: Exception) {
                try {
                    mFont_Bold = Typeface.createFromFile(
                        File(
                            Environment.getExternalStorageDirectory(),
                            NAME_BOLD
                        )
                    )
                    v.typeface = mFont_Bold
                    return true
                } catch (e1: Exception) {
                }
                false
            }
        } else {
            v.typeface = mFont_Bold
            true
        }
    }

    fun setTextExtraBold(ctx: Context, v: TextView): Boolean {
        return if (mFont_ExtraBold == null) {
            try {
                mFont_ExtraBold = Typeface.createFromAsset(ctx.assets, NAME_EXTRA_BOLD)
                v.typeface = mFont_ExtraBold
                true
            } catch (e: Exception) {
                try {
                    mFont_ExtraBold = Typeface.createFromFile(
                        File(
                            Environment.getExternalStorageDirectory(),
                            NAME_EXTRA_BOLD
                        )
                    )
                    v.typeface = mFont_ExtraBold
                    return true
                } catch (e1: Exception) {
                }
                false
            }
        } else {
            v.typeface = mFont_ExtraBold
            true
        }
    }

    fun put(ctx: Context, key: String?, value: String?) {
        val pref = ctx.getSharedPreferences(INI_FILENAME_CONFIG, Activity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        commit(editor)
    }

    operator fun get(ctx: Context, key: String?): String? {
        val pref = ctx.getSharedPreferences(INI_FILENAME_CONFIG, Activity.MODE_PRIVATE)
        return try {
            pref.getString(key, "")
        } catch (e: Exception) {
            null
        }
    }

    fun commit(editor: Editor) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
            editor.apply()
        } else {
            editor.commit()
        }
    }

    enum class FontType(private val mId: Int) {
        Font_None(0), Font_Light(1), Font_Regular(2), Font_Bold(3), Font_ExtraBold(4);

        companion object {
            fun fromId(id: Int): FontType {
                for (t in values()) {
                    if (t.mId == id) {
                        return t
                    }
                }
                return Font_None
            }
        }
    }
}