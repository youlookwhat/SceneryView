package me.jingbin.scenery.wave;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;


/**
 * 乘风破浪的小船
 *
 * @author : Lambert
 * date   : 2021/3/4 6:43 PM
 */
public class WaveSlipView2 extends View {

    private Paint mPaint; //用于绘制第1条波浪
    private Paint mPaint2; //用于绘制第2条波浪

    private int mWidth;
    private int mHeight;
    private int mWaveHeight; //波浪高度可调节
    private int mWaveDx; //波长的长度(这里设置为屏幕的宽度)
    private int dx; //偏移量

//    private Bitmap mSlipBitMap; //船
//    int slipHeight, slipWidth; //小船图片宽高


    /**
     * 通过y轴的偏移造成视觉上的波浪效果，值越大，波浪的速度越快
     * <p>
     * waveOffset = waveOffsetRatio * height
     */
    private float waveOffsetRatio = MIN_WAVE_OFFSET_RATIO;
    private static final float MIN_WAVE_OFFSET_RATIO = 1 / 60f;
    private static final float MAX_WAVE_OFFSET_RATIO = 4 / 60f;


    /**
     * waveAmplitude = waveAmplitudeRatio * waveLength
     */
    private final static float WAVE_AMPLITUDE_RATIO = 5f / 60;
    /**
     * 可见的波浪的个数（一个完整正弦波形为一个波浪）
     */
    private static final float WAVE_COUNT = 1.0f;

    @FloatRange(from = 0, to = 1)
    private float progress = 0.3f;

    private int width;
    private int height;

    private final Paint paint = new Paint();
    //    private Paint mPaint= new Paint();;
    int stroke_width = 4;
    private final Path path = new Path();

    {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public WaveSlipView2(Context context) {
        this(context, null);
    }

    public WaveSlipView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveSlipView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
//        mWaveDx = getResources().getDisplayMetrics().widthPixels;
        mWaveDx = DensityUtil.dip2px(context, 50);

//        mSlipBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.snow_flake);

//        slipWidth = mSlipBitMap.getWidth();
//        slipHeight = mSlipBitMap.getHeight();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //控件的宽高
        mWidth = MeasureUtils.measureView(widthMeasureSpec, getResources().getDisplayMetrics().widthPixels);
        mHeight = MeasureUtils.measureView(heightMeasureSpec, 300);
        //水波的高度
        mWaveHeight = DensityUtil.dip2px(getContext(), 16);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制波浪
//        drawWave(canvas);
        drawWave2(canvas, 1);
        drawWave2(canvas, 2);
        //绘制小船位置
//        drawSlip(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void drawWave2(Canvas canvas, int type) {
//        canvas.translate(0, -waveOffsetRatio * height);
        int i1 = DensityUtil.dip2px(getContext(), 20);
//        path.moveTo(i1 / 2, -mWidth + dx);
        // 波长
        final float waveLength = height / WAVE_COUNT;
        // 振幅
        final float waveAmplitude = WAVE_AMPLITUDE_RATIO * waveLength;

        final float progressWidth = waveAmplitude + progress * width;

        path.reset();
        path.moveTo(0, 0);
//        path.moveTo(-mWaveDx + dx, mHeight / 2);
        path.rLineTo(progressWidth, 0);

        final int realWaveCount = (int) Math.ceil(WAVE_COUNT + 1f);
        for (int i = 0; i < realWaveCount; i++) {
            path.rLineTo(0, 0);
            path.rQuadTo(waveAmplitude, waveLength / 4, 0, waveLength / 2);

            path.rLineTo(0, 0);
            path.rQuadTo(-waveAmplitude, waveLength / 4, 0, waveLength / 2);
        }


        path.rLineTo(-progressWidth, 0);
        path.close();
        canvas.drawPath(path, paint);

        waveOffsetRatio += MIN_WAVE_OFFSET_RATIO;
        if (waveOffsetRatio > 1) {
            waveOffsetRatio = 0;
        }
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

//        int x_2 = 50;

        // 这个是纵向的
//        path.moveTo(-mWaveDx + dx, mHeight / 2);
//        path2.moveTo(-mWaveDx + dx - mWaveDx / 4, mHeight / 2);
//        for (int i = -mWaveDx; i < getWidth() + mWaveDx; i += mWaveDx) {
//            path.rQuadTo(mWaveDx / 4, -mWaveHeight, mWaveDx / 2, 0);
//            path.rQuadTo(mWaveDx / 4, mWaveHeight, mWaveDx / 2, 0);
//
//            path2.rQuadTo(mWaveDx / 4, -mWaveHeight, mWaveDx / 2, 0);
//            path2.rQuadTo(mWaveDx / 4, mWaveHeight, mWaveDx / 2, 0);
//
//        }
        // 横向的 起点坐标
        int i1 = DensityUtil.dip2px(getContext(), 20);
        path.moveTo(i1 / 2, -mWidth + dx);
        path2.moveTo(i1 / 2, -mWidth + dx - i1 / 4);

        for (int i = -mWaveDx; i < getHeight() + mWaveDx; i += mWaveDx) {
//            path.rQuadTo(-mWaveHeight, mWaveDx / 4, mWaveDx / 2, 0);
            path.rLineTo(0, 0);
            path.rQuadTo(mWaveHeight, mWaveDx / 4, mWaveDx / 2, mWaveDx);

            path2.rLineTo(0, 0);
//            path2.rQuadTo(-mWaveHeight, mWaveDx / 4, mWaveDx / 2, 0);
            path2.rQuadTo(mWaveHeight, mWaveDx / 4, mWaveDx / 2, mWaveDx);

        }

//        for (int i = 0; i < realWaveCount; i++) {
//            path.rLineTo(0, 0);
//            path.rQuadTo(waveAmplitude, waveLength / 4, 0, waveLength / 2);
//
//            path.rLineTo(0, 0);
//            path.rQuadTo(-waveAmplitude, waveLength / 4, 0, waveLength / 2);
//        }

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
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, DensityUtil.dip2px(getContext(), 20));
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