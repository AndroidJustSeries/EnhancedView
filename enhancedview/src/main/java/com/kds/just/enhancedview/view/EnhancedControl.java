package com.kds.just.enhancedview.view;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.annotation.Nullable;

import com.kds.just.enhancedview.EnhancedHalper;

public interface EnhancedControl {
    void setUnderLine(boolean isUnderLine);
    int getStrokeNormalColor();
    int getStrokeSelectedColor();
    void setStrokeColor(int normal,int selected);
    void setStrokeWidth(int w);
    int getBackgroundNormalColor();
    int getBackgroundSelectedColor();
    void setBackgroundColor(int normal,int selected);
    void setRoundRadius(int radius);
    int getTextNormalColor();
    int getTextSelectedColor();
    void setTextColor(int normal, int selected);
    void setTypeface(@Nullable Typeface tf, int style);
    void setFont(EnhancedHalper.FontType type);
}
