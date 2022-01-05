package com.kds.just.enhancedview.sample

import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.os.Bundle
import com.kds.just.enhancedview.sample.R
import android.content.Intent
import com.kds.just.enhancedview.sample.SampleEnhancedView
import android.widget.TextView
import com.kds.just.enhancedview.view.EnhancedControl
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import android.content.res.Resources

class SampleEnhancedView : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var type = TYPE_TEXTVIEW
        if (intent != null) {
            type = intent.getIntExtra(EXTRA_VIEW_TYPE, TYPE_TEXTVIEW)
        }
        if (type == TYPE_TEXTVIEW) {
            setContentView(R.layout.sample_enhanced_textview)
            findViewById<View>(R.id.enhanced_view_01).setOnClickListener(this)
            findViewById<View>(R.id.enhanced_view_02).setOnClickListener(this)
        } else if (type == TYPE_EDITTEXT) {
            setContentView(R.layout.sample_enhanced_edittext)
            findViewById<View>(R.id.enhanced_view_01).setOnClickListener(this)
            findViewById<View>(R.id.enhanced_view_02).setOnClickListener(this)
        } else if (type == TYPE_CHECKBOX) {
            setContentView(R.layout.sample_enhanced_checkbox)
        }
        setupProgrammaticallyUI()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.enhanced_view_01) {
            v.isSelected = !v.isSelected
        } else if (id == R.id.enhanced_view_02) {
            v.isSelected = !v.isSelected
        }
        if (v === mNormalEnhancedTextView) {
            v.isSelected = !v.isSelected
            (mNormalEnhancedTextView as TextView?)!!.text =
                if (v.isSelected) "Selected state" else "Normal state"
        } else if (v === mSelectedEnhancedTextView) {
            v.isSelected = !v.isSelected
            (mSelectedEnhancedTextView as TextView?)!!.text =
                if (v.isSelected) "Selected state" else "Normal state"
        } else if (v === mNormalBGButton) {
            showColorPicker(mNormalBGButton)
        } else if (v === mSelectedBGButton) {
            showColorPicker(mSelectedBGButton)
        } else if (v === mNormalStrokeButton) {
            showColorPicker(mNormalStrokeButton)
        } else if (v === mSelectedStrokeButton) {
            showColorPicker(mSelectedStrokeButton)
        } else if (v === mNormalTextColorButton) {
            showColorPicker(mNormalTextColorButton)
        } else if (v === mSelectedTextColorButton) {
            showColorPicker(mSelectedTextColorButton)
        }
    }

    private lateinit var mNormalEnhancedTextView: EnhancedControl
    private lateinit var mSelectedEnhancedTextView: EnhancedControl
    private lateinit var mNormalBGButton: Button
    private lateinit var mSelectedBGButton: Button
    private lateinit var mNormalStrokeButton: Button
    private lateinit var mSelectedStrokeButton: Button
    private lateinit var mStrokeWidthSeekBar: SeekBar
    private lateinit var mCornerRadiusSeekBar: SeekBar
    private lateinit var mNormalTextColorButton: Button
    private lateinit var mSelectedTextColorButton: Button
    private fun setupProgrammaticallyUI() {
        mNormalEnhancedTextView = findViewById(R.id.enhanced_normal)
        mSelectedEnhancedTextView = findViewById(R.id.enhanced_selected)
        mNormalBGButton = findViewById(R.id.btn_normal_bg)
        mSelectedBGButton = findViewById(R.id.btn_selected_bg)
        mNormalStrokeButton = findViewById(R.id.btn_normal_stroke)
        mSelectedStrokeButton = findViewById(R.id.btn_selected_stroke)
        mStrokeWidthSeekBar = findViewById(R.id.seekbar_stroke_width)
        mCornerRadiusSeekBar = findViewById(R.id.seekbar_corner_radius)
        mNormalTextColorButton = findViewById(R.id.btn_normal_textcolor)
        mSelectedTextColorButton = findViewById(R.id.btn_selected_textcolor)
        (mNormalEnhancedTextView as View?)!!.setOnClickListener(this)
        (mSelectedEnhancedTextView as View?)!!.setOnClickListener(this)
        mNormalBGButton.setOnClickListener(this)
        mSelectedBGButton.setOnClickListener(this)
        mNormalStrokeButton.setOnClickListener(this)
        mSelectedStrokeButton.setOnClickListener(this)
        mNormalTextColorButton.setOnClickListener(this)
        mSelectedTextColorButton.setOnClickListener(this)
        mStrokeWidthSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mNormalEnhancedTextView.setStrokeWidth(dp2px(progress.toFloat()))
                    mSelectedEnhancedTextView.setStrokeWidth(dp2px(progress.toFloat()))
                    (findViewById<View>(R.id.seekbar_stroke_width_tv) as TextView).text =
                        "Stroke Width(" + progress + "dp)"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        mCornerRadiusSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mNormalEnhancedTextView.setRoundRadius(dp2px(progress.toFloat()))
                    mSelectedEnhancedTextView.setRoundRadius(dp2px(progress.toFloat()))
                    (findViewById<View>(R.id.seekbar_corner_radius_tv) as TextView).text =
                        "Corner Radius(" + progress + "dp)"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun showColorPicker(v: View?) {
        val dialog =
            ColorPickerDialog.newBuilder().setDialogType(ColorPickerDialog.TYPE_PRESETS).create()
        dialog.setColorPickerDialogListener(object : ColorPickerDialogListener {
            override fun onColorSelected(dialogId: Int, color: Int) {
                if (v === mNormalBGButton) {
                    mNormalBGButton.setBackgroundColor(color)
                    mNormalEnhancedTextView.setBackgroundColor(color, mSelectedEnhancedTextView.backgroundSelectedColor)
                    mSelectedEnhancedTextView.setBackgroundColor(color, mSelectedEnhancedTextView.backgroundSelectedColor)
                } else if (v === mSelectedBGButton) {
                    mSelectedBGButton.setBackgroundColor(color)
                    mNormalEnhancedTextView.setBackgroundColor(mSelectedEnhancedTextView.backgroundNormalColor, color)
                    mSelectedEnhancedTextView.setBackgroundColor(mSelectedEnhancedTextView.backgroundNormalColor, color)
                } else if (v === mNormalStrokeButton) {
                    mNormalStrokeButton.setBackgroundColor(color)
                    mNormalEnhancedTextView.setStrokeColor(color, mNormalEnhancedTextView.strokeSelectedColor)
                    mSelectedEnhancedTextView.setStrokeColor(color,mSelectedEnhancedTextView.strokeSelectedColor)
                } else if (v === mSelectedStrokeButton) {
                    mSelectedStrokeButton.setBackgroundColor(color)
                    mNormalEnhancedTextView.setStrokeColor(mNormalEnhancedTextView.strokeNormalColor, color)
                    mSelectedEnhancedTextView.setStrokeColor(mSelectedEnhancedTextView.strokeNormalColor,color)
                } else if (v === mNormalTextColorButton) {
                    mNormalTextColorButton.setTextColor(color)
                    mNormalEnhancedTextView.setTextColor(color, mNormalEnhancedTextView.textSelectedColor)
                    mSelectedEnhancedTextView.setTextColor(color, mSelectedEnhancedTextView.textSelectedColor)
                } else if (v === mSelectedTextColorButton) {
                    mSelectedTextColorButton.setTextColor(color)
                    mNormalEnhancedTextView.setTextColor(mNormalEnhancedTextView.textNormalColor, color)
                    mSelectedEnhancedTextView.setTextColor(mSelectedEnhancedTextView.textNormalColor,color)
                }
            }

            override fun onDialogDismissed(dialogId: Int) {}
        })
        dialog.show(supportFragmentManager, "color-picker-dialog")
    }

    companion object {
        private const val TAG = "SampleEnhancedView"
        const val TYPE_TEXTVIEW = 0
        const val TYPE_EDITTEXT = 1
        const val TYPE_CHECKBOX = 2
        const val EXTRA_VIEW_TYPE = "EXTRA_VIEW_TYPE"
        fun dp2px(dp: Float): Int {
            val resources = Resources.getSystem()
            val px = dp * resources.displayMetrics.density
            return Math.ceil(px.toDouble()).toInt()
        }
    }
}