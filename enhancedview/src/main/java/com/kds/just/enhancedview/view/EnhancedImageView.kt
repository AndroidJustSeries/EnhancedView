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
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.doOnAttach
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.CenterCrop
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


class EnhancedImageView(context: Context, attrs: AttributeSet? = null) : ShapeableImageView(context, attrs) {
    companion object {
        const val NONE = 0          //ImageView의 ScaleType에 따라간다
        const val FIT_CENTER = 1  //무조건 이미지에 맞춰서 View가 커질수 있는 한계만큰 그려줌
        const val CENTER_CROP_LARGER_HEIGHT = 2  //가로보다 세로가 큰 이미지는 Center crop함
        const val FIT_CENTER_LARGER_DRAWABLE = 3 //View보다 큰 이미지만 View에 맞추고 작은 이미지는 원본 이미지크기로 보여줌
    }

    var mImageScaleType = NONE
        set(value) {
            field = value
            invalidate()
        }
    var mImageUrl : String? = null
        private set
    var mIsScalingBaseView = 0f
    var mResizeWidth = 0;
    var mResizeHeight = 0;
    var mIsOriginalImageSize = false;

    var normalImage : Drawable? = null
    var pressedImage : Drawable? = null
    var selectedImage : Drawable? = null

    var normalStrokeColor = Color.TRANSPARENT
    var selectedStrokeColor = Color.TRANSPARENT

    var isGroup = false //Radio group과 같이 같은 레벨의 그룹중 selected동작이 연동할지 여부
    var roundBuilder = RoundBuilder()   //image round
    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)

            val isSelected = ta.getBoolean(R.styleable.EnhancedView_selected, false)
            setSelected(isSelected)

            isGroup = ta.getBoolean(R.styleable.EnhancedView_isGroup, false)

            mImageScaleType = ta.getInt(R.styleable.EnhancedView_scaleType, mImageScaleType)

            mImageUrl = ta.getString(R.styleable.EnhancedView_imgUrl)
            if (!TextUtils.isEmpty(mImageUrl)) {
                setImageUrl(mImageUrl)
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

            roundBuilder.setAttr(context,attrs)
            doOnLayout {
                setRound()
            }
        }
    }

    fun setIsScalingBaseView(isViewScaling:Float) : EnhancedImageView{
        mIsScalingBaseView = isViewScaling
        return this
    }

    fun setOverride(w:Int,h:Int) : EnhancedImageView {
        mResizeWidth = w
        mResizeHeight = h
        return this
    }

    fun setOriginalImageSize() : EnhancedImageView {
        mIsOriginalImageSize = true
        return this
    }

    fun isResize(): Boolean {
        return mIsScalingBaseView > 0f || mResizeWidth > 0 && mResizeHeight > 0
    }

    /**
     * 외부 네트워크로부터 이미지 로딩
     * @param imageSize original image size
     */
    fun setImageUrl(url:String?, imageSize: Size? = null) : EnhancedImageView {
        mImageUrl = url
        if (TextUtils.isEmpty(mImageUrl)) {
            setImageDrawable(null)
            return this
        }

        if (isResize() && imageSize == null) {
            Glide.with(this)
                .asFile()
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
    fun setGlide(glide:RequestManager?) {
        mGlideRequestManager = glide
    }

    /**
     * Glide를 통한 image load
     */
    private fun loadImageUrl(imageSize: Size? = null) {
        var glide: RequestManager? = mGlideRequestManager
        if (glide == null) {
            glide = Glide.with(this)
        }
        var builder = glide.load(mImageUrl).transition(DrawableTransitionOptions.withCrossFade())
        if (mIsScalingBaseView > 0f) {
            builder.override((width * mIsScalingBaseView).toInt(),(height * mIsScalingBaseView).toInt())
        } else if (imageSize != null && mResizeWidth > 0 && mResizeHeight > 0) {
            var overrideSize = EnhancedUtils.getFitScaleSize(imageSize.width,imageSize.height,mResizeWidth,mResizeHeight)
            builder.override(overrideSize.width,overrideSize.height)
        } else if (mIsOriginalImageSize){
            builder.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        }

        builder.listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                if (mStateListDrawable != null) {
                    if (normalImage != null) {
                        mStateListDrawable!!.addState(intArrayOf(), normalImage)
                    }
                    setImageDrawable(mStateListDrawable)
                }
                return false
            }
            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
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
        if (TextUtils.isEmpty(mImageUrl) && normalImage != null) {
            mStateListDrawable!!.addState(intArrayOf(), normalImage)
        }
        if (TextUtils.isEmpty(mImageUrl)) {
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
        super.onDraw(canvas)
        drawStrokeN(canvas!!)
    }

    //------------------------- Round Corner ------------------------------
    class RoundBuilder(var isCircle:Boolean = false, var allCorner:Float = 0f,
                       var topLeft:Float = 0f, var topRight:Float = 0f, var bottomLeft:Float = 0f, var bottomRight:Float = 0f) {
        var maxCorner = 1f
        fun setAttr(context: Context, attrs: AttributeSet?) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView)
            isCircle = ta.getBoolean(R.styleable.EnhancedView_isCircle, false)
            allCorner = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundAll, 0).toFloat()

            topLeft = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundTopLeft, 0).toFloat()
            topRight = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundTopRight, 0).toFloat()
            bottomLeft = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundBottomLeft, 0).toFloat()
            bottomRight = ta.getDimensionPixelSize(R.styleable.EnhancedView_roundBottomRight, 0).toFloat()
        }

        fun build(width:Int, height:Int) : ShapeAppearanceModel? {
            var build : ShapeAppearanceModel? = null
            if (isCircle) {
                var builder = ShapeAppearanceModel.builder()
                build = builder.setAllCornerSizes(RelativeCornerSize(0.5f)).build()
            } else if (allCorner > 0) {
                var builder = ShapeAppearanceModel.builder()
                allCorner = getMinViewSize(allCorner,width,height)
                build = builder.setAllCornerSizes(AbsoluteCornerSize(allCorner)).build()
                maxCorner = allCorner

            } else if (topLeft > 0 || topRight > 0 || bottomLeft > 0 || bottomRight > 0){
                var builder = ShapeAppearanceModel.builder()
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
                build = builder.build()

            }
            return build
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

    fun setAllCornor(cornor:Float) {
        roundBuilder.allCorner = cornor
        setRound()
    }

    fun setCornor(topLeft:Float = roundBuilder.topLeft,topRight:Float = roundBuilder.topRight,
                     bottomLeft:Float = roundBuilder.bottomLeft,bottomRight:Float = roundBuilder.bottomRight) {
        roundBuilder.allCorner = 0f
        roundBuilder.topLeft = topLeft
        roundBuilder.topRight = topRight
        roundBuilder.bottomLeft = bottomLeft
        roundBuilder.bottomRight = bottomRight
        setRound()
    }
    fun setRound() {
        var builder : ShapeAppearanceModel? = roundBuilder.build(width,height)
        if (builder != null) {
            shapeAppearanceModel = builder
        }
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

    fun getIdString() : String? {
        var name5: String? = getResources().getResourceName(id);
        return name5
    }
}