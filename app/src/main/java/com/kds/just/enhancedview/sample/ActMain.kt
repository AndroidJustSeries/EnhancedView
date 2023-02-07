package com.kds.just.enhancedview.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kds.just.enhancedview.sample.databinding.ActMainBinding

class ActMain : AppCompatActivity() {
    private lateinit var binding: ActMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sampleEnhancedImageTest.setOnClickListener {
            startActivity(Intent(this, ActTest::class.java))
        }
        binding.sampleEnhancedImageview.setOnClickListener {
            startActivity(Intent(this, ActImageViewSample::class.java))
        }
        binding.sampleEnhancedScaleImageview.setOnClickListener {
            startActivity(Intent(this, ActImageViewScaleSample::class.java))
        }
        binding.sampleEnhancedTextview.setOnClickListener {
            startActivity(Intent(this, SampleEnhancedView::class.java))
        }
        binding.sampleEnhancedEdittext.setOnClickListener {
            val intent = Intent(this, SampleEnhancedView::class.java)
            intent.putExtra(
                SampleEnhancedView.EXTRA_VIEW_TYPE,
                SampleEnhancedView.TYPE_EDITTEXT
            )
            startActivity(intent)
        }
        binding.sampleEnhancedCheckbox.setOnClickListener {
            val intent = Intent(this, SampleEnhancedView::class.java)
            intent.putExtra(
                SampleEnhancedView.EXTRA_VIEW_TYPE,
                SampleEnhancedView.TYPE_CHECKBOX
            )
            startActivity(intent)
        }
    }
}