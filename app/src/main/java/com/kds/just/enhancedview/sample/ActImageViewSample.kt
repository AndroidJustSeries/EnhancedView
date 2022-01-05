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
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.SeekBar.inflate
import com.bumptech.glide.Glide
import com.kds.just.enhancedview.EnhancedUtils
import com.kds.just.enhancedview.sample.databinding.ActImageviewSampleBinding
import com.kds.just.enhancedview.view.EnhancedImageView

class ActImageViewSample : AppCompatActivity(), View.OnClickListener {
    lateinit var binding:ActImageviewSampleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActImageviewSampleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Thread {
            Glide.get(this).clearDiskCache()
        }


        binding.fullImage.setOnClickListener(this)
        binding.selectImage0.setOnClickListener(this)
        binding.selectImage1.setOnClickListener(this)
        binding.selectImage2.setOnClickListener(this)

        binding.btnImageBg.setOnClickListener(this)
        binding.btnNormalBg.setOnClickListener(this)
        binding.btnPressedBg.setOnClickListener(this)
        binding.btnSelectedBg.setOnClickListener(this)
        binding.btnNormalStroke.setOnClickListener(this)
        binding.btnSelectedStroke.setOnClickListener(this)

        binding.seekbarStrokeWidth.setOnSeekBarChangeListener(onSeekBarChangeListener)
        binding.seekbarAllCornerRadius.setOnSeekBarChangeListener(onSeekBarChangeListener)
        binding.seekbarLeftCornerRadius.setOnSeekBarChangeListener(onSeekBarChangeListener)
        binding.seekbarTopCornerRadius.setOnSeekBarChangeListener(onSeekBarChangeListener)
        binding.seekbarRightCornerRadius.setOnSeekBarChangeListener(onSeekBarChangeListener)
        binding.seekbarBottomCornerRadius.setOnSeekBarChangeListener(onSeekBarChangeListener)

        binding.memoryTestNormalBtn.setOnClickListener(this)
        binding.memoryTestViewBtn.setOnClickListener(this)
        binding.memoryTestFixedBtn.setOnClickListener(this)

    }
//    app:imgUrl="https://w.namu.la/s/2cb3e601e7fa749b3c9b2d09b75901f0d4e1d8f909a0ffd936895c5594e27cb460731507e00ace3177dc8ecc28cb37ec6d613065083b53a6ffdb09888eba5894e3bdbd62eb6d0e4a93236ff147d953bafe8bd33000d0bd0c719d2dced9d2a46b"
//    app:imgUrl="https://upload.wikimedia.org/wikipedia/ko/c/cf/%EA%B7%B9%ED%95%9C%EC%A7%81%EC%97%85_%ED%8F%AC%EC%8A%A4%ED%84%B0.jpg"

    override fun onClick(v: View) {
        if (v == binding.fullImage) {
            binding.fullImage.visibility = View.GONE
        } else if (v == binding.selectImage0) {
            (v as EnhancedImageView).setGroupSelected(true)
        } else if (v == binding.selectImage1) {
            (v as EnhancedImageView).setGroupSelected(true)
        } else if (v == binding.selectImage2) {
            (v as EnhancedImageView).setGroupSelected(true)
        } else if (v == binding.btnImageBg) {
            if (TextUtils.isEmpty(binding.selectImage0.mImageUrl)) {
                binding.selectImage0.setImageUrl("https://c8.alamy.com/comp/EJWNX5/home-alone-movie-poster-EJWNX5.jpg")
                binding.selectImage1.setImageUrl("https://c8.alamy.com/comp/EJWNX5/home-alone-movie-poster-EJWNX5.jpg")
                binding.selectImage2.setImageUrl("https://c8.alamy.com/comp/EJWNX5/home-alone-movie-poster-EJWNX5.jpg")
            } else {
                binding.selectImage0.removeImage()
                binding.selectImage1.removeImage()
                binding.selectImage2.removeImage()
            }
        } else if (v == binding.btnNormalBg) {
            showColorPicker(v)
        } else if (v == binding.btnPressedBg) {
            showColorPicker(v)
        } else if (v == binding.btnSelectedBg) {
            showColorPicker(v)
        } else if (v == binding.btnNormalStroke) {
            showColorPicker(v)
        } else if (v == binding.btnSelectedStroke) {
            showColorPicker(v)
        } else if (v == binding.memoryTestNormalBtn) {
            memoryTestAddImage(0)
        } else if (v == binding.memoryTestViewBtn) {
            memoryTestAddImage(1)
        } else if (v == binding.memoryTestFixedBtn) {
            memoryTestAddImage(2)
        }
    }
    private fun showColorPicker(v: View?) {
        val dialog = ColorPickerDialog.newBuilder().setDialogType(ColorPickerDialog.TYPE_PRESETS).create()
        dialog.setColorPickerDialogListener(object : ColorPickerDialogListener {
            override fun onColorSelected(dialogId: Int, color: Int) {
                if (v == binding.btnNormalBg) {
                    binding.btnNormalBg.setBackgroundColor(color)
                    binding.selectImage2
                } else if (v == binding.btnPressedBg) {
                    binding.btnPressedBg.setBackgroundColor(color)
                } else if (v == binding.btnSelectedBg) {
                    binding.btnSelectedBg.setBackgroundColor(color)
                } else if (v == binding.btnNormalStroke) {
                    binding.btnNormalStroke.setBackgroundColor(color)
                    binding.selectImage0.setStrokeColorSet(binding.selectImage0.normalStrokeColor,color)
                    binding.selectImage1.setStrokeColorSet(binding.selectImage1.normalStrokeColor,color)
                    binding.selectImage2.setStrokeColorSet(binding.selectImage2.normalStrokeColor,color)
                } else if (v == binding.btnSelectedStroke) {
                    binding.btnSelectedStroke.setBackgroundColor(color)
                    binding.selectImage0.setStrokeColorSet(color,binding.selectImage0.selectedStrokeColor)
                    binding.selectImage1.setStrokeColorSet(color,binding.selectImage1.selectedStrokeColor)
                    binding.selectImage2.setStrokeColorSet(color,binding.selectImage2.selectedStrokeColor)
                }
            }

            override fun onDialogDismissed(dialogId: Int) {}
        })
        dialog.show(supportFragmentManager, "color-picker-dialog")
    }

    var onSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                if (seekBar == binding.seekbarStrokeWidth) {
                    binding.seekbarStrokeWidthTv.text = "Stroke Width(" + progress + "dp)"
                    var padding = EnhancedUtils.dp2px(progress.toFloat())
                    binding.selectImage0.setStrokeWidthSet(padding.toFloat())
                    binding.selectImage1.setStrokeWidthSet(padding.toFloat())
                    binding.selectImage2.setStrokeWidthSet(padding.toFloat())

//                    binding.selectImage0.setContentPadding(padding.toInt(),padding.toInt(),padding.toInt(),padding.toInt())
//                    binding.selectImage1.setContentPadding(padding.toInt(),padding.toInt(),padding.toInt(),padding.toInt())
//                    binding.selectImage2.setContentPadding(padding.toInt(),padding.toInt(),padding.toInt(),padding.toInt())
                } else if (seekBar == binding.seekbarAllCornerRadius) {
                    binding.seekbarAllCornerRadiusTv.text = "All Corner Radius(" + progress + "dp)"
                    binding.selectImage0.setAllCornor(progress.toFloat())
                    binding.selectImage1.setAllCornor(progress.toFloat())
                    binding.selectImage2.setAllCornor(progress.toFloat())
                } else if (seekBar == binding.seekbarLeftCornerRadius) {
                    binding.seekbarLeftCornerRadiusTv.text = "topLeft Corner Radius(" + progress + "dp)"
                    binding.selectImage0.setCornor(topLeft = progress.toFloat())
                    binding.selectImage1.setCornor(topLeft = progress.toFloat())
                    binding.selectImage2.setCornor(topLeft = progress.toFloat())

                } else if (seekBar == binding.seekbarTopCornerRadius) {
                    binding.seekbarTopCornerRadiusTv.text = "topRight Corner Radius(" + progress + "dp)"
                    binding.selectImage0.setCornor(topRight = progress.toFloat())
                    binding.selectImage1.setCornor(topRight = progress.toFloat())
                    binding.selectImage2.setCornor(topRight = progress.toFloat())

                } else if (seekBar == binding.seekbarRightCornerRadius) {
                    binding.seekbarRightCornerRadiusTv.text = "bottomLeft Corner Radius(" + progress + "dp)"
                    binding.selectImage0.setCornor(bottomLeft = progress.toFloat())
                    binding.selectImage1.setCornor(bottomLeft = progress.toFloat())
                    binding.selectImage2.setCornor(bottomLeft = progress.toFloat())

                } else if (seekBar == binding.seekbarBottomCornerRadius) {
                    binding.seekbarBottomCornerRadiusTv.text = "bottomRight Corner Radius(" + progress + "dp)"
                    binding.selectImage0.setCornor(bottomRight = progress.toFloat())
                    binding.selectImage1.setCornor(bottomRight = progress.toFloat())
                    binding.selectImage2.setCornor(bottomRight = progress.toFloat())

                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }


    fun memoryTestAddImage(type:Int) {
        binding.memoryTestLayout.addView(loadImage(type,"https://wallpaper.dog/large/822953.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://images.wallpapersden.com/image/download/minimalist-landscape-painting_a2xmaGuUmZqaraWkpJRobWllrWdma2U.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://i.pinimg.com/originals/92/88/96/928896b97f7ae949f0c14c02e968c4c7.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://wallpaperaccess.com/full/4516053.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://cutewallpaper.org/21/4k-background-wallpaper/Free-stock-photo-of-4k-4k-background-4k-wallpaper.jpeg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://wallpapercave.com/wp/wp2927809.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://wallpaperbat.com/img/406839-4k-ultra-hd-sunset-wallpaper-top-free-4k-ultra-hd-sunset.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://wallpaper.dog/large/10792515.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://data.1freewallpapers.com/download/castle-in-middle-of-lake-around-snow-covered-mountains-and-trees-under-blue-sky-4k-nature-2800x2100.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://wallpaperaccess.com/full/1094236.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://cutewallpaper.org/21/wallpaper-full-hd-4k/4K-Ultra-HD-Wallpapers-Top-Free-4K-Ultra-HD-Backgrounds-.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://data.1freewallpapers.com/download/castle-in-middle-of-lake-around-snow-covered-mountains-and-trees-under-blue-sky-4k-nature-2800x2100.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://me2t.com/wp-content/uploads/2021/05/wallpaper-gaming-4k-4k-gaming-wallpapers-top-free-4k-gaming-backgrounds-of-wallpaper-gaming-4k-2-scaled-pin.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://wallpaperaccess.com/full/1143632.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://i.pinimg.com/originals/be/32/3c/be323c2893f3adce42d7382665e02a7b.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6479/6479209_so.jpg"))
        binding.memoryTestLayout.addView(loadImage(type,"https://www.igeeksblog.com/wp-content/uploads/2021/05/Download-Free-Star-Wars-Wallpaper.jpg"))
    }

    fun loadImage(type:Int,url:String) : EnhancedImageView {
        var imageview = EnhancedImageView(this)
        if (type == 0) {    //NORMAL
        } else if (type == 1) { //ViewScale
            imageview.setIsScalingBaseView(0.5f)
        } else if (type == 2) { //fixed Size
            imageview.setOverride(30,30)
        }
        imageview.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        return imageview.setImageUrl(url)
    }
}