package com.kds.just.enhancedview.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.doOnLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.*
import com.kds.just.enhancedview.EnhancedUtils
import com.kds.just.enhancedview.R
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter


open class EnhancedImageView(context: Context, attrs: AttributeSet? = null) : ShapeableImageView(context, attrs) {
    companion object {
        const val NONE = 0          //ImageView의 ScaleType에 따라간다
        const val FIT_CENTER = 1  //무조건 이미지에 맞춰서 View가 커질수 있는 한계만큰 그려줌
        const val CENTER_CROP_LARGER_HEIGHT = 2  //가로보다 세로가 큰 이미지는 Center crop함
        const val FIT_CENTER_LARGER_DRAWABLE = 3 //View보다 큰 이미지만 View에 맞추고 작은 이미지는 원본 이미지크기로 보여줌

        private var mIsLogging = false
        open fun setLogging(isLogging:Boolean) {
            mIsLogging = isLogging
        }
    }

    var mImageScaleType = NONE
        set(value) {
            field = value
            invalidate()
        }

    var normalImage : Drawable? = null
    var pressedImage : Drawable? = null
    var selectedImage : Drawable? = null

    var normalStrokeColor = Color.TRANSPARENT
    var selectedStrokeColor = Color.TRANSPARENT

    var isGroup = false //Radio group과 같이 같은 레벨의 그룹중 selected동작이 연동할지 여부
    lateinit var roundBuilder: RoundBuilder  //image round
    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)

            val isSelected = ta.getBoolean(R.styleable.EnhancedView_selected, false)
            setSelected(isSelected)

            isGroup = ta.getBoolean(R.styleable.EnhancedView_isGroup, false)

            mImageScaleType = ta.getInt(R.styleable.EnhancedView_viewType, mImageScaleType)

            var imageUrl = ta.getString(R.styleable.EnhancedView_imgUrl)
            if (!TextUtils.isEmpty(imageUrl)) {
                setImageUrl(imageUrl)
            }

            var normal = ta.getDrawable(R.styleable.EnhancedView_imgNormal)
            var selected = ta.getDrawable(R.styleable.EnhancedView_imgSelect)
            var pressed = ta.getDrawable(R.styleable.EnhancedView_imgPressed)
            if (normal != null || selected != null || pressed != null ) {
                setImageSelector(normal,selected,pressed)
            }

            normalStrokeColor = ta.getColor(R.styleable.EnhancedView_BgStrokeColorNormal, Color.TRANSPARENT)
            selectedStrokeColor = ta.getColor(R.styleable.EnhancedView_BgStrokeColorSelected, Color.TRANSPARENT)

            if (normalStrokeColor != Color.TRANSPARENT || selectedStrokeColor != Color.TRANSPARENT) {
                setStrokeColorSet(normalStrokeColor,selectedStrokeColor)
            }
            var strokeWidth = ta.getDimensionPixelSize(R.styleable.EnhancedView_StrokeWidth, 0)
            if (strokeWidth > 0) {
                setStrokeWidthSet(strokeWidth.toFloat())
            }

            roundBuilder = RoundBuilder().setAttr(context,attrs)
            doOnLayout {
                setRound(roundBuilder)
            }
        }
    }

    var mStateListDrawable : StateListDrawable? = null
    fun setImageSelector(normal:Drawable?,pressed:Drawable?,selected:Drawable?) {
        normalImage = normal
        pressedImage = pressed
        selectedImage = selected
        mStateListDrawable = StateListDrawable()
        mStateListDrawable!!.setExitFadeDuration(200)
        if (selectedImage != null) {
            mStateListDrawable!!.addState(intArrayOf(android.R.attr.state_selected ), selectedImage)
        }
        if (pressedImage != null) {
            mStateListDrawable!!.addState(intArrayOf(android.R.attr.state_pressed), pressedImage)
        } else if (selectedImage != null) {
            mStateListDrawable!!.addState(intArrayOf(android.R.attr.state_pressed), selectedImage)
        }
        if (mImageUrl == null && normalImage != null) {
            mStateListDrawable!!.addState(intArrayOf(), normalImage)
        }
        if (mImageUrl == null) {
            setImageDrawable(mStateListDrawable)
        }
    }

    /**
     * 같은 레벨의 View와 연동하여 Selected가 동잘할 필요가 있는 경우
     * (Radio group 과 같은 동작이 필요한 경우)
     */
    fun setGroupSelected(selected: Boolean) {
        super.setSelected(selected)
        if (isGroup && selected && parent != null && parent is ViewGroup) {
            for (index:Int in 0 until (parent as ViewGroup).childCount) {
                if ((parent as ViewGroup).getChildAt(index) != this) {
                    (parent as ViewGroup).getChildAt(index).isSelected = false
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d : Drawable? = this.drawable
        if (mImageScaleType != NONE && d != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            var height = Math.ceil((width * d.intrinsicHeight.toFloat() / d.intrinsicWidth).toDouble()).toInt()
            if (mImageScaleType == FIT_CENTER) { //무조건 이미지에 맞춰서 View가 커질수 있는 한계만큰 그려줌
                scaleType = ScaleType.FIT_CENTER
            } else if (mImageScaleType == CENTER_CROP_LARGER_HEIGHT) { //가로보다 세로가 큰 이미지는 Center crop함
                if (height > width) {
                    height = width
                    scaleType = ScaleType.CENTER_CROP
                } else {
                    scaleType = ScaleType.FIT_CENTER
                }
            } else if (mImageScaleType == FIT_CENTER_LARGER_DRAWABLE) { //View보다 큰 이미지만 View에 맞추고 작은 이미지는 원본 이미지크기로 보여줌
                if (width > d.intrinsicWidth) {
                    scaleType = ScaleType.CENTER
                    setMeasuredDimension(d.intrinsicWidth, d.intrinsicHeight)
                    return
                } else {
                    scaleType = ScaleType.FIT_CENTER
                }
            }
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        try {
            super.onDraw(canvas)
            drawStrokeN(canvas!!)
        } catch (e:Exception) {
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            Log.e("ERROR", "Canvas Exception\n$sw");
        }
    }

    //------------------------- Image Load ------------------------------
    var mImageUrl : Uri? = null
        private set
    var mImageScaleBaseView = 0f
    var mResizeWidth = 0;
    var mResizeHeight = 0;
    var mIsOriginalImageSize = false;

    var mImageFadeAnimation = true   //Image Show animation
    var mIsImageCenterCrop = false
    var mErrorImgRes : Int? = null

    fun setImageScaleBaseView(isViewScaling:Float) : EnhancedImageView{
        mImageScaleBaseView = isViewScaling
        return this
    }

    fun setImageOverride(w:Int,h:Int) : EnhancedImageView {
        mResizeWidth = w
        mResizeHeight = h
        return this
    }

    fun setImageOriginalSize() : EnhancedImageView {
        mIsOriginalImageSize = true
        return this
    }

    fun isResize(): Boolean {
        return mImageScaleBaseView > 0f || mResizeWidth > 0 && mResizeHeight > 0
    }

    fun setImageFade(fade:Boolean) : EnhancedImageView {
        mImageFadeAnimation = fade
        return this
    }

    fun centerCrop(isCenterCrop:Boolean) : EnhancedImageView {
        mIsImageCenterCrop = isCenterCrop
        return this
    }

    fun error(@DrawableRes res:Int) : EnhancedImageView {
        mErrorImgRes = res
        return this
    }
    var mIsSkipMemoryCache = false
    var mDiskCache : DiskCacheStrategy? = null
    fun skipMemoryCache(skipMemoryCache:Boolean) : EnhancedImageView {
        mIsSkipMemoryCache = skipMemoryCache
        return this
    }

    fun setImageDiskCache(diskCache:DiskCacheStrategy) : EnhancedImageView {
        mDiskCache = diskCache
        return this
    }

    /**
     * 외부 네트워크로부터 이미지 로딩
     * @param imageSize original image size
     */
    fun setImageUrl(url:String?, imageSize: Size? = null) { setImageUrl(if (TextUtils.isEmpty(url)) null else Uri.parse(url),imageSize) }
    fun setImageUrl(url:Uri?, imageSize: Size? = null) : EnhancedImageView {
        mImageUrl = url
        if (mImageUrl == null) {
            removeImage()
            if(mErrorImgRes != null) {
                Glide().load(mErrorImgRes).into(this)
            }
            return this
        }

        if (isResize() && imageSize == null) {
            if ("file".equals(mImageUrl!!.scheme)) {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(mImageUrl!!.path, options)
                val imageWidth = options.outWidth
                val imageHeight = options.outHeight
                setImageUrl(url,Size(imageWidth,imageHeight))
            } else {
                Glide().asFile()
                    .load(url)
                    .skipMemoryCache(true)
                    .into(object : CustomTarget<File>() {
                        override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                            val options = BitmapFactory.Options()
                            options.inJustDecodeBounds = true
                            BitmapFactory.decodeFile(resource.path, options)
                            val imageWidth = options.outWidth
                            val imageHeight = options.outHeight
                            setImageUrl(url,Size(imageWidth,imageHeight))
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                    })
            }
        } else {
            if (width == 0) {
                doOnLayout {
                    if (width > 0 || height > 0) {
                        loadImageUrl(imageSize)
                    }
                }
            } else {
                loadImageUrl(imageSize)
            }
        }
        return this
    }

    fun removeImage() {
        if (mImageUrl != null) {
            mImageUrl = null
            setImageDrawable(null)
            setImageSelector(normalImage,pressedImage,selectedImage)
        }
    }

    /**
     * Glide의 RequestManager를 등록하여 사용한다.
     */
    private var mGlideRequestManager: RequestManager? = null
    fun setGlide(glide:RequestManager?) : EnhancedImageView {
        mGlideRequestManager = glide
        return this
    }

    fun Glide() : RequestManager {
        if (mGlideRequestManager == null) {
            mGlideRequestManager = Glide.with(this)
        }
        return mGlideRequestManager!!
    }
    /**
     * Glide를 통한 image load
     */
    private fun loadImageUrl(imageSize: Size? = null) {
        var glide: RequestManager? = mGlideRequestManager
        if (glide == null) {
            glide = Glide.with(this)
        }
        var builder = glide.load(mImageUrl)
        if (mImageFadeAnimation) {
            builder.transition(DrawableTransitionOptions.withCrossFade())
        }
        if (mImageScaleBaseView > 0f) {
            var overrideW = (width * mImageScaleBaseView).toInt()
            var overrideH = (height * mImageScaleBaseView).toInt()
            if (imageSize != null && (overrideW == 0 || overrideH == 0)) {
                var overrideSize = EnhancedUtils.getFitScaleSize(imageSize.width,imageSize.height,width,height)
                overrideW = (overrideSize.width * mImageScaleBaseView).toInt()
                overrideH = (overrideSize.height * mImageScaleBaseView).toInt()
            }
            if (imageSize == null || (overrideW * overrideH) < (imageSize.width * imageSize.height)) {
                builder.override(overrideW,overrideH)
                EnhancedUtils.log("${getIdName()} >> loadImageUrl ImageScaleBaseView[$mImageScaleBaseView] ViewSize[$width,$height] image[${imageSize?.width},${imageSize?.height}] override[$overrideW,$overrideH]")
            } else {
                builder.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                EnhancedUtils.log("${getIdName()} >> loadImageUrl ImageScaleBaseView[$mImageScaleBaseView] ViewSize[$width,$height] image[${imageSize?.width},${imageSize?.height}] not override")
            }
        } else if (imageSize != null && mResizeWidth > 0 && mResizeHeight > 0) {
            var overrideSize = EnhancedUtils.getFitScaleSize(imageSize.width,imageSize.height,mResizeWidth,mResizeHeight)
            builder.override(overrideSize.width,overrideSize.height)
            EnhancedUtils.log("${getIdName()} >> loadImageUrl image[${imageSize.width},${imageSize.height}] resize[$mResizeWidth,$mResizeHeight] override[${overrideSize.width},${overrideSize.height}]")
        } else if (mIsOriginalImageSize){
            builder.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            EnhancedUtils.log("${getIdName()} >> loadImageUrl ViewSize[$width,$height]")
        }

        if (mIsImageCenterCrop) {
            builder.centerCrop()
        }

        if (mErrorImgRes != null) {
            builder.error(mErrorImgRes)
        }

        if (mIsSkipMemoryCache) {
            builder.skipMemoryCache(true)
        }

        if (mDiskCache != null) {
            builder.diskCacheStrategy(mDiskCache!!)
        }

        builder.listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                EnhancedUtils.log("${getIdName()} >> ImageDownload onLoadFailed")
                if (mStateListDrawable != null) {
                    if (normalImage != null) {
                        mStateListDrawable!!.addState(intArrayOf(), normalImage)
                    }
                    setImageDrawable(mStateListDrawable)
                }
                return false
            }
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                EnhancedUtils.log("${getIdName()} >> ImageDownload onResourceReady imageSize[${resource?.intrinsicWidth},${resource?.intrinsicHeight}]")
                if (mStateListDrawable != null) {
                    mStateListDrawable!!.addState(intArrayOf(), resource)
                    setImageDrawable(mStateListDrawable)
                    return true
                }
                return false
            }
        })
        builder.into(this)
    }

    //------------------------- Round Corner ------------------------------

    fun circleCrop() : EnhancedImageView {
        setRound(RoundBuilder(isCircle = true))
        return this
    }

    class RoundBuilder(var isCircle:Boolean = false, var allCorner:Float = 0f,
                       var topLeft:Float = 0f, var topRight:Float = 0f, var bottomLeft:Float = 0f, var bottomRight:Float = 0f) {
        var maxCorner = 1f
        fun setAttr(context: Context, attrs: AttributeSet?) : RoundBuilder {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            isCircle = ta.getBoolean(R.styleable.EnhancedView_isCircle, false)
            allCorner = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundAll, 0).toFloat()

            topLeft = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundTL, 0).toFloat()
            topRight = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundTR, 0).toFloat()
            bottomLeft = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundBL, 0).toFloat()
            bottomRight = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundBR, 0).toFloat()
            return this
        }

        fun build(width:Int, height:Int) : ShapeAppearanceModel {
            var builder = ShapeAppearanceModel.builder()
            if (isCircle) {
                builder.setAllCornerSizes(RelativeCornerSize(0.5f))
            } else if (allCorner > 0) {
                allCorner = getMinViewSize(allCorner,width,height)
                builder.setAllCornerSizes(AbsoluteCornerSize(allCorner))
                maxCorner = allCorner
            } else if (topLeft > 0 || topRight > 0 || bottomLeft > 0 || bottomRight > 0) {
                maxCorner = topLeft
                if (topLeft > 0) {
                    topLeft = getMinViewSize(topLeft,width,height)
                    builder.setTopLeftCorner(CornerFamily.ROUNDED,  AbsoluteCornerSize(topLeft))
                }
                if (topRight > 0) {
                    topRight = getMinViewSize(topRight,width,height)
                    builder.setTopRightCorner(CornerFamily.ROUNDED,  AbsoluteCornerSize(topRight))
                    if (maxCorner < topRight) {
                        maxCorner = topRight
                    }
                }
                if (bottomLeft > 0) {
                    bottomLeft = getMinViewSize(bottomLeft,width,height)
                    builder.setBottomLeftCorner(CornerFamily.ROUNDED,  AbsoluteCornerSize(bottomLeft))
                    if (maxCorner < bottomLeft) {
                        maxCorner = bottomLeft
                    }
                }
                if (bottomRight > 0) {
                    bottomRight = getMinViewSize(bottomRight,width,height)
                    builder.setBottomRightCorner(CornerFamily.ROUNDED,  AbsoluteCornerSize(bottomRight))
                    if (maxCorner < bottomRight) {
                        maxCorner = bottomRight
                    }
                }
            }
            return builder.build()
        }

        fun getMinViewSize(value:Float,width:Int,height:Int) : Float {
            var result = value
            if (result > width) {
                result = width.toFloat()
            }
            if (result > height) {
                result = height.toFloat()
            }
            return result

        }
        //TODO round angle과 stroke에 따라 interpolator를 설정해줘야 함
        fun getStrokeInterpolator(stroke:Float, width:Float,height:Float) : Float {
            if (isCircle) {
                return 1f
            }
            var offset = stroke / maxCorner
            var interpolator = Math.max(1f - maxCorner/width + offset,1f - maxCorner/height + offset)
            if (interpolator > 1f) {
                interpolator = 1f
            }
            return interpolator
        }
    }

    fun setAllCornor(cornor:Float) : EnhancedImageView {
        setRound(RoundBuilder(allCorner = cornor))
        return this
    }

    fun setCornor(topLeft:Float = roundBuilder.topLeft,topRight:Float = roundBuilder.topRight,
                     bottomLeft:Float = roundBuilder.bottomLeft,bottomRight:Float = roundBuilder.bottomRight) {
        setRound(RoundBuilder(topLeft = topLeft,topRight = topRight,bottomLeft = bottomLeft,bottomRight = bottomRight))
    }

    private fun setRound(builder:RoundBuilder) {
        roundBuilder = builder
        shapeAppearanceModel = roundBuilder.build(width,height)
    }


    //------------------------- Stroke ------------------------------
    fun setStrokeColorSet(normal: Int, sel: Int) {
        mStrokeDrawColor = EnhancedUtils.makeColorSelector(normal, sel)
        mBGSelectStrokePaint = Paint()
        mBGSelectStrokePaint!!.style = Paint.Style.STROKE
        mBGSelectStrokePaint!!.isAntiAlias = true
        destination = RectF()
        path = Path()
        pathProvider = ShapeAppearancePathProvider()
        invalidate()
    }

    fun setStrokeWidthSet(width:Float) {
        strokeWidthN = width
        invalidate()
    }

    var mStrokeDrawColor: ColorStateList? = null
    private var mBGSelectStrokePaint: Paint? = null
    private var destination: RectF? = null
    private var path: Path? = null
    private var pathProvider : ShapeAppearancePathProvider? = null
    private var strokeWidthN = 0f
    private fun drawStrokeN(canvas: Canvas) {
        if (mStrokeDrawColor == null || strokeWidthN <= 0) {
            return
        }
        var sWidth = strokeWidthN / 2
        destination!!.set(sWidth, sWidth, (width - sWidth), (height - sWidth))

        pathProvider!!.calculatePath(shapeAppearanceModel, 1f , destination, path!!)
        if (mStrokeDrawColor == null || mBGSelectStrokePaint == null) {
            return
        }
        mBGSelectStrokePaint!!.strokeWidth = strokeWidthN
        val colorForState = mStrokeDrawColor!!.getColorForState(drawableState, mStrokeDrawColor!!.defaultColor)
        if (strokeWidthN > 0 && colorForState != Color.TRANSPARENT) {
            mBGSelectStrokePaint!!.color = colorForState
            canvas.drawPath(path!!, mBGSelectStrokePaint!!)
        }
    }

    var offset = 1f

    //------------------------------------------------------------------------
    fun getIdName() : String {
        return EnhancedUtils.getIdString(context,id)
    }
}
