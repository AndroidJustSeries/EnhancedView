package com.kds.just.enhancedview;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class DrawableHelper {
    private static final String TAG = "DrawableHelper";

    public int mBGColorNormal = 0;
    public int mBGColorSelected = 0;
    public int mStrokeColorNormal = 0;
    public int mStrokeColorSelected = 0;
    public int mRoundRadius = 0;
    public int mStrokeWidth = 0;

    private Paint mBGNormalPaint;
    private Paint mBGNormalStrokePaint;

    private Paint mBGSelectPaint;
    private Paint mBGSelectStrokePaint;

    private Paint mMainPaint;
    private int mStrokePadding = 0;

    public void init() {
        if (mBGColorNormal == 0) {
            mBGNormalPaint = null;
        } else {
            mBGNormalPaint = new Paint();
            mBGNormalPaint.setStyle(Paint.Style.FILL);
            mBGNormalPaint.setColor(mBGColorNormal);
            mBGNormalPaint.setAntiAlias(true);
        }

        if (mStrokeColorNormal == 0) {
            mBGNormalStrokePaint = null;
        } else {
            mBGNormalStrokePaint = new Paint();
            mBGNormalStrokePaint.setStyle(Paint.Style.STROKE);
            mBGNormalStrokePaint.setColor(mStrokeColorNormal);
            mBGNormalStrokePaint.setStrokeWidth(mStrokeWidth);
            mBGNormalStrokePaint.setAntiAlias(true);
            mBGNormalStrokePaint.setDither(true);
            mBGNormalStrokePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        }

        if (mBGColorSelected == 0) {
            mBGSelectPaint = null;
        } else {
            mBGSelectPaint = new Paint();
            mBGSelectPaint.setStyle(Paint.Style.FILL);
            mBGSelectPaint.setColor(mBGColorSelected);
            mBGSelectPaint.setAntiAlias(true);
        }

        if (mStrokeColorSelected == 0) {
            mBGSelectStrokePaint = null;
        } else {
            mBGSelectStrokePaint = new Paint();
            mBGSelectStrokePaint.setStyle(Paint.Style.STROKE);
            mBGSelectStrokePaint.setColor(mStrokeColorSelected);
            mBGSelectStrokePaint.setStrokeWidth(mStrokeWidth);
            mBGSelectStrokePaint.setAntiAlias(true);
            mBGSelectStrokePaint.setDither(true);
            mBGSelectStrokePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        }


        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
    }

    public boolean isDraw() {
        return mBGNormalPaint != null ||  mBGNormalStrokePaint != null || mBGSelectPaint != null ||  mBGSelectStrokePaint != null;
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
            drawRect(canvas,rectF,mRoundRadius,mBGSelectPaint);
            if (mStrokeWidth > 0) {
                drawRect(canvas,rectF,mRoundRadius,mBGSelectStrokePaint);
            }
        } else if (isPressed) {
            drawRect(canvas,rectF,mRoundRadius,mBGSelectPaint);
            if (mStrokeWidth > 0) {
                drawRect(canvas,rectF,mRoundRadius,mBGSelectStrokePaint);
            }
        } else {
            drawRect(canvas,rectF,mRoundRadius,mBGNormalPaint);
            if (mStrokeWidth > 0) {
                drawRect(canvas,rectF,mRoundRadius,mBGNormalStrokePaint);
            }
        }
    }

    private void drawRect(Canvas canvas, RectF rectF, int roundRadius,Paint paint) {
        if (paint != null) {
            if (roundRadius == 0) {
                paint.setStrokeJoin(Paint.Join.MITER);
                canvas.drawRect(rectF,paint);
            } else {
                paint.setStrokeJoin(Paint.Join.ROUND);
                canvas.drawRoundRect(rectF,roundRadius,roundRadius,paint);
            }
        }
    }

    public int mTextColorNormal = 0;
    public int mTextColorSelected = 0;

    public ColorStateList getTextColor(int option) {
        if (mTextColorNormal != 0 && mTextColorSelected != 0) {
            return makeTextColorSelector(option, mTextColorNormal,mTextColorSelected);
        }
        return null;
    }

    private static ColorStateList makeTextColorSelector(int option, int normal, int selected) {
        if (selected == 0) {
            selected = normal;
        }

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_pressed}, // press
                new int[] { option}, // selected
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
