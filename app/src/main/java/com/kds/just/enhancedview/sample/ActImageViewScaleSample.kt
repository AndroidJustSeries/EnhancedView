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
import com.kds.just.enhancedview.sample.databinding.ActImageviewScaleSampleBinding
import com.kds.just.enhancedview.sample.databinding.ActMainBinding
import com.kds.just.enhancedview.view.EnhancedImageView

//    app:imgUrl="https://w.namu.la/s/2cb3e601e7fa749b3c9b2d09b75901f0d4e1d8f909a0ffd936895c5594e27cb460731507e00ace3177dc8ecc28cb37ec6d613065083b53a6ffdb09888eba5894e3bdbd62eb6d0e4a93236ff147d953bafe8bd33000d0bd0c719d2dced9d2a46b"
//    app:imgUrl="https://upload.wikimedia.org/wikipedia/ko/c/cf/%EA%B7%B9%ED%95%9C%EC%A7%81%EC%97%85_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg"
class ActImageViewScaleSample : AppCompatActivity() {
    private lateinit var binding: ActImageviewScaleSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActImageviewScaleSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.img0.setOnClickListener {
            binding.fullImage.mImageScaleType = binding.img0.mImageScaleType
            binding.fullImage.visibility = View.VISIBLE
            binding.fullImage.setImageUrl(binding.img1.mImageUrl)
        }
        binding.img1.setOnClickListener {
            binding.fullImage.mImageScaleType = binding.img1.mImageScaleType
            binding.fullImage.visibility = View.VISIBLE
            binding.fullImage.setImageUrl(binding.img1.mImageUrl)
        }
        binding.img2.setOnClickListener {
            binding.fullImage.mImageScaleType = binding.img2.mImageScaleType
            binding.fullImage.visibility = View.VISIBLE
            binding.fullImage.setImageUrl(binding.img2.mImageUrl)
        }
        binding.fullImage.setOnClickListener {
            binding.fullImage.visibility = View.GONE
        }
    }
}