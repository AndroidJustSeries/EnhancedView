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
import android.util.Log
import com.kds.just.enhancedview.view.EnhancedImageView

class ActImageViewScaleSample : AppCompatActivity(), View.OnClickListener {
    lateinit var img0 : EnhancedImageView
    lateinit var img1 : EnhancedImageView
    lateinit var img2 : EnhancedImageView
    lateinit var fullImage : EnhancedImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_imageview_scale_sample)
        findViewById<View>(R.id.img0).setOnClickListener(this)
        findViewById<View>(R.id.img1).setOnClickListener(this)
        findViewById<View>(R.id.img2).setOnClickListener(this)
        findViewById<View>(R.id.fullImage).setOnClickListener(this)
        img0 = findViewById(R.id.img0)
        img1 = findViewById(R.id.img1)
        img2 = findViewById(R.id.img2)

        fullImage = findViewById(R.id.fullImage)

    }
//    app:imgUrl="https://w.namu.la/s/2cb3e601e7fa749b3c9b2d09b75901f0d4e1d8f909a0ffd936895c5594e27cb460731507e00ace3177dc8ecc28cb37ec6d613065083b53a6ffdb09888eba5894e3bdbd62eb6d0e4a93236ff147d953bafe8bd33000d0bd0c719d2dced9d2a46b"
//    app:imgUrl="https://upload.wikimedia.org/wikipedia/ko/c/cf/%EA%B7%B9%ED%95%9C%EC%A7%81%EC%97%85_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg"

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.img0) {
            fullImage.mImageScaleType = img0.mImageScaleType
            fullImage.visibility = View.VISIBLE
            fullImage.setImageUrl(img1.mImageUrl)
        } else if (id == R.id.img1) {
            fullImage.mImageScaleType = img1.mImageScaleType
            fullImage.visibility = View.VISIBLE
            fullImage.setImageUrl(img1.mImageUrl)
        } else if (id == R.id.img2) {
            fullImage.mImageScaleType = img2.mImageScaleType
            fullImage.visibility = View.VISIBLE
            fullImage.setImageUrl(img2.mImageUrl)
        } else if (id == R.id.fullImage) {
            fullImage.visibility = View.GONE
        }
    }
}