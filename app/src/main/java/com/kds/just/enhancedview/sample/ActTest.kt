package com.kds.just.enhancedview.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.kds.just.enhancedview.sample.databinding.ActMainBinding
import com.kds.just.enhancedview.sample.databinding.ActTestBinding

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

    override fun onClick(v: View) {
        v.isSelected = !v.isSelected
    }
}