package com.kds.just.enhancedview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.io.File;

public class EnhancedTextView extends AppCompatTextView {
    private static final String TAG = "EnhancedTextView";

    DrawableHelper mDrawableHelper;

    public EnhancedTextView(Context context) {
        super(context);
        init();
    }

    public EnhancedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDrawableHelper = new DrawableHelper();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView);
        mDrawableHelper.mStrokeColorNormal = ta.getColor(R.styleable.EnhancedView_BgStrokeColorNormal,mDrawableHelper.mStrokeColorNormal);
        mDrawableHelper.mStrokeColorSelected = ta.getColor(R.styleable.EnhancedView_BgStrokeColorSelected,mDrawableHelper.mStrokeColorSelected);
        mDrawableHelper.mBGColorNormal = ta.getColor(R.styleable.EnhancedView_BgColorNormal,mDrawableHelper.mBGColorNormal);
        mDrawableHelper.mBGColorSelected = ta.getColor(R.styleable.EnhancedView_BgColorSelected,mDrawableHelper.mBGColorSelected);
        mDrawableHelper.mRoundRadius = ta.getDimensionPixelSize(R.styleable.EnhancedView_BgRadius,mDrawableHelper.mRoundRadius);
        mDrawableHelper.mStrokeWidth = ta.getDimensionPixelSize(R.styleable.EnhancedView_StrokeWidth,mDrawableHelper.mStrokeWidth);

        mDrawableHelper.mTextColorNormal = ta.getColor(R.styleable.EnhancedView_textColorNormal,mDrawableHelper.mTextColorNormal);
        mDrawableHelper.mTextColorSelected = ta.getColor(R.styleable.EnhancedView_textColorSelected,mDrawableHelper.mTextColorSelected);

        boolean isSelected = ta.getBoolean(R.styleable.EnhancedView_selected,false);

        setSelected(isSelected);

        String htmlText = ta.getString(R.styleable.EnhancedView_html);
        if (!TextUtils.isEmpty(htmlText)) {
            setText(Html.fromHtml(htmlText));
        }

        EnhancedHalper.setFont(context,this,attrs);

        init();
    }

    private void init() {
        setWillNotDraw(false);
        mDrawableHelper.init();
        ColorStateList textColor = mDrawableHelper.getTextColor();
        if (textColor != null ){
            setTextColor(mDrawableHelper.getTextColor());
        }
    }

    public int getStrokeNormalColor() {
        return mDrawableHelper.mStrokeColorNormal;
    }

    public int getStrokeSelectedColor() {
        return mDrawableHelper.mStrokeColorSelected;
    }

    public void setStrokeColor(int normal,int selected) {
        mDrawableHelper.mStrokeColorNormal = normal;
        mDrawableHelper.mStrokeColorSelected = selected;
        mDrawableHelper.init();
        invalidate();
    }

    public void setStrokeWidth(int w) {
        mDrawableHelper.mStrokeWidth = w;
        mDrawableHelper.init();
        invalidate();
    }

    public int getBackgroundNormalColor() {
        return mDrawableHelper.mBGColorNormal;
    }

    public int getBackgroundSelectedColor() {
        return mDrawableHelper.mBGColorSelected;
    }

    public void setBackgroundColor(int normal,int selected) {
        mDrawableHelper.mBGColorNormal = normal;
        mDrawableHelper.mBGColorSelected = selected;
        mDrawableHelper.init();
        invalidate();
    }

    public void setRoundRadius(int radius) {
        mDrawableHelper.mRoundRadius = radius;
        invalidate();
    }

    public int getTextNormalColor() {
        return mDrawableHelper.mTextColorNormal;
    }

    public int getTextSelectedColor() {
        return mDrawableHelper.mTextColorSelected;
    }

    public void setTextColor(int normal, int selected) {
        mDrawableHelper.mTextColorNormal = normal;
        mDrawableHelper.mTextColorSelected = selected;
        ColorStateList textColor = mDrawableHelper.getTextColor();
        if (textColor != null ){
            setTextColor(mDrawableHelper.getTextColor());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getBackground() == null) {
            mDrawableHelper.drawBg(canvas,getWidth(),getHeight(),isPressed(),isSelected());
        }
        super.onDraw(canvas);
    }


    public void setTypeface(@Nullable Typeface tf, int style) {
        if (style == Typeface.NORMAL) {
            setFont(EnhancedHalper.FontType.Font_Regular);
        } else if (style == Typeface.BOLD) {
            setFont(EnhancedHalper.FontType.Font_Bold);
        }
    }


    public void setFont(EnhancedHalper.FontType type) {
        boolean isFontChanged = false;
        switch (type) {
            case Font_Light:
                isFontChanged = EnhancedHalper.setTextNanumLight(getContext(), this);
                break;
            case Font_Regular:
                isFontChanged = EnhancedHalper.setTextNanum(getContext(), this);
                break;
            case Font_Bold:
                isFontChanged = EnhancedHalper.setTextNaumeBold(getContext(), this);
                break;
            default:
        }
    }


}
