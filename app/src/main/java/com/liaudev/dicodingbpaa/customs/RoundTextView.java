package com.liaudev.dicodingbpaa.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.liaudev.dicodingbpaa.R;
import com.liaudev.dicodingbpaa.utils.Utils;

/**
 * Created by Budiliauw87 on 2022-06-08.
 * budiliauw87.github.io
 * Budiliauw87@gmail.com
 */
public class RoundTextView extends View {

    private String mTitleText;
    private int mTitleTextColor;
    private int mTitleTextSize;
    private int tvBackGroundColor;
    private float mCornerSize;
    private final Rect mTitleBound;
    private final Paint mTitlePaint;
    private boolean isCirCle = false;

    public RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundTextView, defStyle, 0);
        mTitleText = a.getString(R.styleable.RoundTextView_rtvText);
        tvBackGroundColor = a.getColor(R.styleable.RoundTextView_rtvBackground, ContextCompat.getColor(context,android.R.color.black));// 默认背景黑色
        mTitleTextColor = a.getColor(R.styleable.RoundTextView_rtvTextColor, Color.WHITE);                                       // 默认文字颜色黑色
        mTitleTextSize = a.getDimensionPixelSize(R.styleable.RoundTextView_rtvTextSize, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));                       // 默认文字字体大小16sp
        mCornerSize = a.getFloat(R.styleable.RoundTextView_rtvCornerSize, 0);
        isCirCle = a.getBoolean(R.styleable.RoundTextView_rtvIsCircle, false);                                 // 默认不是圆形
        a.recycle();
        mTitlePaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTextSize(mTitleTextSize);
        mTitlePaint.setTextAlign(Paint.Align.LEFT);
        mTitleBound = new Rect();
        mTitleText = TextUtils.isEmpty(mTitleText) ? "" : mTitleText;
        mTitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTitleBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mTitlePaint.setTextSize(mTitleTextSize);
            mTitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTitleBound);
            int desired = getPaddingLeft() + mTitleBound.width() + getPaddingRight();
            width = desired <= widthSize ? desired : widthSize;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mTitlePaint.setTextSize(mTitleTextSize);
            mTitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTitleBound);
            int desired = getPaddingTop() + mTitleBound.height() + getPaddingBottom();
            height = desired <= heightSize ? desired : heightSize;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isCirCle) {
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            paint.setAntiAlias(true);
            paint.setDither(true);//防抖动
            paint.setColor(tvBackGroundColor);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()) / 2, paint);
        }else{
            Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
            RectF rec = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
            paint.setAntiAlias(true);
            paint.setDither(true);//防抖动
            paint.setColor(tvBackGroundColor);
            canvas.drawRoundRect(rec, Utils.dip2px(getContext(), mCornerSize), Utils.dip2px(getContext(), mCornerSize), paint);
        }

        mTitlePaint.setColor(mTitleTextColor);

        Paint.FontMetricsInt fontMetrics = mTitlePaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mTitleText, getMeasuredWidth() / 2 - mTitleBound.width() / 2, baseline, mTitlePaint);

    }

    public void setBackgroundColor(int rtvBackgroundColor) {
        this.tvBackGroundColor = rtvBackgroundColor;
        invalidate();
    }

    public int getRoundBackGroundColor() {
        return this.tvBackGroundColor;
    }
    public void setTextColor(int mTitleTextColor) {
        this.mTitleTextColor = mTitleTextColor;
        invalidate();
    }
    public void setTextSize(int mTitleTextSize) {
        this.mTitleTextSize = mTitleTextSize;
        invalidate();
    }
    public void setText(String mTitleText) {
        this.mTitleText = TextUtils.isEmpty(mTitleText) ? "" : mTitleText;
        invalidate();
    }
    public void setCorner(float mCornerSize) {
        this.mCornerSize = mCornerSize;
        invalidate();
    }
    public void setIsCirCle(boolean isCirCle) {
        this.isCirCle = isCirCle;
    }


}
