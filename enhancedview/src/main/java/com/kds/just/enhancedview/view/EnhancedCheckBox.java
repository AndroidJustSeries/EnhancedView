package com.kds.just.enhancedview.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;

import com.kds.just.enhancedview.DrawableHelper;
import com.kds.just.enhancedview.EnhancedHalper;
import com.kds.just.enhancedview.R;

public class EnhancedCheckBox extends AppCompatCheckBox implements EnhancedControl {
    private static final String TAG = "EnhancedTextView";

    DrawableHelper mDrawableHelper;

    public EnhancedCheckBox(Context context) {
        super(context);
        mDrawableHelper = new DrawableHelper();
        init();
    }

    public EnhancedCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDrawableHelper = new DrawableHelper();
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EnhancedView);
        mDrawableHelper.mStrokeColorNormal = ta.getColor(R.styleable.EnhancedView_BgStrokeColorNormal,mDrawableHelper.mStrokeColorNormal);
        mDrawableHelper.mStrokeColorSelected = ta.getColor(R.styleable.EnhancedView_BgStrokeColorSelected,mDrawableHelper.mStrokeColorSelected);
        mDrawableHelper.mBGColorNormal = ta.getColor(R.styleable.EnhancedView_BgColorNormal,mDrawableHelper.mBGColorNormal);
        mDrawableHelper.mBGColorSelected = ta.getColor(R.styleable.EnhancedView_BgColorSelected,mDrawableHelper.mBGColorSelected);
        mDrawableHelper.mRoundRadius = ta.getDimensionPixelSize(R.styleable.EnhancedView_BgRadius,mDrawableHelper.mRoundRadius);
        mDrawableHelper.mStrokeWidth = ta.getDimensionPixelSize(R.styleable.EnhancedView_StrokeWidth,mDrawableHelper.mStrokeWidth);

        setTextColor(ta.getColor(R.styleable.EnhancedView_textColorNormal,mDrawableHelper.mTextColorNormal),
                     ta.getColor(R.styleable.EnhancedView_textColorSelected,mDrawableHelper.mTextColorSelected));
        boolean isUnderLine = ta.getBoolean(R.styleable.EnhancedView_underline,false);
        if (isUnderLine) {
            setUnderLine(true);
        }
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
        if (mDrawableHelper.isDraw()) {
            setBackground(null);
        }
    }

    @Override
    public void setUnderLine(boolean isUnderLine) {
        if (isUnderLine) {
            setPaintFlags(getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        } else {
            setPaintFlags(getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        }
    }

    @Override
    public int getStrokeNormalColor() {
        return mDrawableHelper.mStrokeColorNormal;
    }

    @Override
    public int getStrokeSelectedColor() {
        return mDrawableHelper.mStrokeColorSelected;
    }

    @Override
    public void setStrokeColor(int normal,int selected) {
        mDrawableHelper.mStrokeColorNormal = normal;
        mDrawableHelper.mStrokeColorSelected = selected;
        mDrawableHelper.init();
        invalidate();
    }

    @Override
    public void setStrokeWidth(int w) {
        mDrawableHelper.mStrokeWidth = w;
        mDrawableHelper.init();
        invalidate();
    }

    @Override
    public int getBackgroundNormalColor() {
        return mDrawableHelper.mBGColorNormal;
    }

    @Override
    public int getBackgroundSelectedColor() {
        return mDrawableHelper.mBGColorSelected;
    }

    @Override
    public void setBackgroundColor(int normal,int selected) {
        mDrawableHelper.mBGColorNormal = normal;
        mDrawableHelper.mBGColorSelected = selected;
        mDrawableHelper.init();
        invalidate();
    }

    @Override
    public void setRoundRadius(int radius) {
        mDrawableHelper.mRoundRadius = radius;
        mDrawableHelper.init();
        invalidate();
    }

    @Override
    public int getTextNormalColor() {
        return mDrawableHelper.mTextColorNormal;
    }

    @Override
    public int getTextSelectedColor() {
        return mDrawableHelper.mTextColorSelected;
    }

    @Override
    public void setTextColor(int normal, int selected) {
        mDrawableHelper.mTextColorNormal = normal;
        mDrawableHelper.mTextColorSelected = selected;
        ColorStateList textColor = mDrawableHelper.getTextColor(android.R.attr.state_checked);
        if (textColor != null ){
            setTextColor(textColor);
        }
    }

    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {
        if (style == Typeface.NORMAL) {
            setFont(EnhancedHalper.FontType.Font_Regular);
        } else if (style == Typeface.BOLD) {
            setFont(EnhancedHalper.FontType.Font_Bold);
        }
    }

    @Override
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

    @Override
    protected void onDraw(Canvas canvas) {
        if (getBackground() == null) {
            mDrawableHelper.drawBg(canvas,getWidth(),getHeight(),isPressed(),isChecked());
        }
        super.onDraw(canvas);
    }
}
