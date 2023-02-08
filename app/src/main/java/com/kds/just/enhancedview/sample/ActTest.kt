package com.kds.just.enhancedview.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.kds.just.enhancedview.sample.databinding.ActMainBinding
import com.kds.just.enhancedview.sample.databinding.ActTestBinding
import com.kds.just.enhancedview.view.EnhancedImageView

class ActTest : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.testImg0.setOnClickListener(this)
        binding.testImg1.setOnClickListener(this)
        binding.testImg2.setOnClickListener(this)
        binding.testImg3.setOnClickListener(this)
    }
    var imgArray = arrayOf(
        "https://w.namu.la/s/2cb3e601e7fa749b3c9b2d09b75901f0d4e1d8f909a0ffd936895c5594e27cb460731507e00ace3177dc8ecc28cb37ec6d613065083b53a6ffdb09888eba5894e3bdbd62eb6d0e4a93236ff147d953bafe8bd33000d0bd0c719d2dced9d2a46b",
        "https://upload.wikimedia.org/wikipedia/ko/c/cf/%EA%B7%B9%ED%95%9C%EC%A7%81%EC%97%85_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg"
    )
    var index = 0
    override fun onClick(v: View) {
//        v.isSelected = !v.isSelected
        (v as EnhancedImageView).setImageUrl(imgArray[index%2])
        index++

    }
}