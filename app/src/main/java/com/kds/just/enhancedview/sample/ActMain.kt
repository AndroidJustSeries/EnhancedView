package com.kds.just.enhancedview.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class ActMain : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        findViewById<View>(R.id.sample_enhanced_imageview).setOnClickListener(this)
        findViewById<View>(R.id.sample_enhanced_scale_imageview).setOnClickListener(this)
        findViewById<View>(R.id.sample_enhanced_textview).setOnClickListener(this)
        findViewById<View>(R.id.sample_enhanced_edittext).setOnClickListener(this)
        findViewById<View>(R.id.sample_enhanced_checkbox).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.sample_enhanced_imageview -> startActivity(Intent(this, ActImageViewSample::class.java))
            R.id.sample_enhanced_scale_imageview -> startActivity(Intent(this, ActImageViewScaleSample::class.java))
            R.id.sample_enhanced_textview -> startActivity(Intent(this, SampleEnhancedView::class.java))
            R.id.sample_enhanced_edittext -> {
                val intent = Intent(this, SampleEnhancedView::class.java)
                intent.putExtra(
                    SampleEnhancedView.EXTRA_VIEW_TYPE,
                    SampleEnhancedView.TYPE_EDITTEXT
                )
                startActivity(intent)
            }
            R.id.sample_enhanced_checkbox -> {
                val intent = Intent(this, SampleEnhancedView::class.java)
                intent.putExtra(
                    SampleEnhancedView.EXTRA_VIEW_TYPE,
                    SampleEnhancedView.TYPE_CHECKBOX
                )
                startActivity(intent)
            }
        }
    }
}