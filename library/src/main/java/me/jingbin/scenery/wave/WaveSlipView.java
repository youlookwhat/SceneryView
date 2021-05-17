package me.jingbin.scenery.wave;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import me.jingbin.library.scenery.R;


/**
 * 乘风破浪的小船
 * @author : Lambert
 * date   : 2021/3/4 6:43 PM
 */
public class WaveSlipView extends View {

    private Paint mPaint; //用于绘制第1条波浪
    private Paint mPaint2; //用于绘制第2条波浪

    private int mWidth;
    private int mHeight;
    private int mWaveHeight; //波浪高度可调节
    private int mWaveDx; //波长的长度(这里设置为屏幕的宽度)
    private int dx; //偏移量

//    private Bitmap mSlipBitMap; //船
//    int slipHeight, slipWidth; //小船图片宽高

    public WaveSlipView(Context context) {
        this(context, null);
    }

    public WaveSlipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveSlipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.parseColor("#1296db"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(80);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setColor(Color.parseColor("#1296db"));
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setAlpha(80);

        //波长的长度(这里设置为屏幕的宽度)
        mWaveDx = getResources().getDisplayMetrics().widthPixels;

//        mSlipBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.snow_flake);

//        slipWidth = mSlipBitMap.getWidth();
//        slipHeight = mSlipBitMap.getHeight();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控件的宽高
        mWidth = MeasureUtils.measureView(widthMeasureSpec, mWaveDx);
        mHeight = MeasureUtils.measureView(heightMeasureSpec, 300);
        //水波的高度
        mWaveHeight = DensityUtil.dip2px(getContext(), 16);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制波浪
        drawWave(canvas);
        //绘制小船位置
        drawSlip(canvas);
    }


    //绘制波浪
    private void drawWave(Canvas canvas) {
        Path path = new Path();
        path.reset();

        Path path2 = new Path();
        path2.reset();

        Log.d("wave23", " -mWaveDx :" + (-mWaveDx));
        Log.d("wave23", " -  + dx:" + (+dx));
        Log.d("wave23", " -mWaveDx + dx:" + (-mWaveDx + dx));

        int x_2 = 50;

        path.moveTo(-mWaveDx + dx, mHeight / 2);

        path2.moveTo(-mWaveDx + dx - mWaveDx / 4, mHeight / 2);

        for (int i = -mWaveDx; i < getWidth() + mWaveDx; i += mWaveDx) {
            path.rQuadTo(mWaveDx / 4, -mWaveHeight, mWaveDx / 2, 0);
            path.rQuadTo(mWaveDx / 4, mWaveHeight, mWaveDx / 2, 0);

            path2.rQuadTo(mWaveDx / 4, -mWaveHeight, mWaveDx / 2, 0);
            path2.rQuadTo(mWaveDx / 4, mWaveHeight, mWaveDx / 2, 0);

        }
        //绘制封闭的区域
        path.lineTo(mWidth, mHeight);
        path.lineTo(0, mHeight);
        //path.close() 绘制封闭的区域
        path.close();
        canvas.drawPath(path, mPaint);


        //绘制封闭的区域
        path2.lineTo(mWidth, mHeight);
        path2.lineTo(0, mHeight);
        //path.close() 绘制封闭的区域
        path2.close();
        canvas.drawPath(path2, mPaint2);

    }

    float t = 0; //用于计算 x/y 的高度比
    //绘制小船位置
    private void drawSlip(Canvas canvas) {

        int ht = 0;

        float dx2 = Math.abs(dx);
        int pointW = getWidth() / 2;  //半个周期
        float wx = pointW / 2;        //半个周期中最高点


        if (dx2 > 0 && dx2 < wx) {
            // 1/4 周期 （最高点 x= 1/2 π ，y = 1）
            t = dx2 / wx;
        } else if (dx2 > wx && dx2 < wx * 2) {
            // 1/2 周期 （x= 1 π ，y =0）
            t = 1 - (dx2 - wx) / wx;
        } else if (dx2 > wx * 2 && dx2 < wx * 3) {
            // 3/4 周期 (y 最低点 x= 3/2 π ，y = -1）
            t = -(dx2 - wx * 2) / wx;
        } else if (dx2 > wx * 3 && dx2 < wx * 4) {
            // 1个周期 （x= 2 π ，y =0）
            t = -1 + (dx2 - wx * 3) / wx;
        }


        Log.d("wave23", " dx2 :" + dx2);
        Log.d("wave23", " t :" + t);

        //根据周期x/y 比率计算出高度
//        ht = (int) (mWaveHeight / 2 * t);

        // h  是 x所对应的 y 的高度点
//        int h = mHeight / 2 - slipHeight - ht;

        //绘制小船位置
//        canvas.drawBitmap(mSlipBitMap, mWidth / 2 - slipWidth / 2, h, null);
    }

    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mWaveDx);
        valueAnimator.setDuration(3500);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //水平方向的偏移量
                dx = (int) animation.getAnimatedValue();

                invalidate();
            }

        });
        valueAnimator.start();
    }
}