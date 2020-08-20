package com.kds.just.enhancedview;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class DrawableHelper {
    public int mBGColorNormal = 0;
    public int mBGColorSelected = 0;
    public int mStrokeColorNormal = 0;
    public int mStrokeColorSelected = 0;
    public int mRoundRadius = dp2px(10);
    public int mStrokeWidth = dp2px(1);

    private Paint mBGNormalPaint;
    private Paint mBGNormalStrokePaint;

    private Paint mBGSelectPaint;
    private Paint mBGSelectStrokePaint;

    private Paint mMainPaint;
    private int mStrokePadding = 0;

    public void init() {
        mBGNormalPaint = new Paint();
        mBGNormalPaint.setStyle(Paint.Style.FILL);
        mBGNormalPaint.setColor(mBGColorNormal);
        mBGNormalPaint.setAntiAlias(true);

        mBGNormalStrokePaint = new Paint();
        mBGNormalStrokePaint.setStyle(Paint.Style.STROKE);
        mBGNormalStrokePaint.setColor(mStrokeColorNormal);
        mBGNormalStrokePaint.setStrokeWidth(mStrokeWidth);
        mBGNormalStrokePaint.setAntiAlias(true);
        mBGNormalStrokePaint.setDither(true);
        mBGNormalStrokePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want

        mBGSelectPaint = new Paint();
        mBGSelectPaint.setStyle(Paint.Style.FILL);
        mBGSelectPaint.setColor(mBGColorSelected);
        mBGSelectPaint.setAntiAlias(true);

        mBGSelectStrokePaint = new Paint();
        mBGSelectStrokePaint.setStyle(Paint.Style.STROKE);
        mBGSelectStrokePaint.setColor(mStrokeColorSelected);
        mBGSelectStrokePaint.setStrokeWidth(mStrokeWidth);
        mBGSelectStrokePaint.setAntiAlias(true);
        mBGSelectStrokePaint.setDither(true);
        mBGSelectStrokePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want

        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
    }

    public void drawBg(Canvas canvas, int w, int h, boolean isPressed, boolean isSelected) {
        mStrokePadding = mStrokeWidth / 2;
        RectF rectF = new RectF(
                mStrokePadding, // left
                mStrokePadding, // top
                w - mStrokePadding, // right
                h - mStrokePadding  // bottom
        );
        if (isSelected) {
            canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGSelectPaint);
            canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGSelectStrokePaint);
        } else if (isPressed) {
            canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGSelectPaint);
            canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGSelectStrokePaint);
        } else {
            canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGNormalPaint);
            canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGNormalStrokePaint);
        }
    }

    public int mTextColorNormal = 0;
    public int mTextColorSelected = 0;

    public ColorStateList getTextColor() {
        if (mTextColorNormal != 0 && mTextColorSelected != 0) {
            return makeTextColorSelector(mTextColorNormal,mTextColorSelected);
        }
        return null;
    }

    private static ColorStateList makeTextColorSelector(int normal, int selected) {
        if (selected == 0) {
            selected = normal;
        }

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_pressed}, // press
                new int[] { android.R.attr.state_selected}, // selected
                new int[] {-android.R.attr.state_empty}  // unpressed
        };

        int[] colors = new int[] {
                selected,
                selected,
                normal
        };

        return new ColorStateList(states, colors);
    }

    public static int dp2px(float dp) {
        Resources resources = Resources.getSystem();
        float px = dp * resources.getDisplayMetrics().density;
        return (int) Math.ceil(px);
    }
}
