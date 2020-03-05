package me.jingbin.library.scenery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * ios 菊花加载
 * https://www.jianshu.com/p/b392b53f3f1e
 * Created by jingbin on 2020-03-05.
 */
public class PullLoadingView extends View {
    /**
     * 菊花颜色
     */
    private int mColor = Color.argb(255, 255, 255, 255);
    /**
     * 开始角度
     */
    private int mStartAngle = 0;
    /**
     * 每条线的宽度
     */
    private float mStrokeWidth = 0;
    /**
     * 是否自动开始旋转
     */
    private boolean isAutoStart;
    /**
     * 一共多少条先
     */
    private final int mLineCount = 12;
    private final int minAlpha = 0;
    /**
     * 每一条线叠加的
     */
    private final int mAngleGradient = 360 / mLineCount;
    /**
     * 每条线的颜色
     */
    private int[] mColors = new int[mLineCount];
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 动画Handler
     */
    private Handler mAnimHandler = new Handler(Looper.getMainLooper());
    /**
     * 动画任务
     */
    private Runnable mAnimRunnable = new Runnable() {
        @Override
        public void run() {
            mStartAngle += mAngleGradient;
            invalidate();
            mAnimHandler.postDelayed(mAnimRunnable, 60);
        }
    };

    public PullLoadingView(Context context) {
        this(context, null);
    }

    public PullLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs, defStyleAttr, 0);
    }

    private void setup(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullLoadingView, defStyleAttr, defStyleRes);
        mColor = typedArray.getColor(R.styleable.PullLoadingView_aiv_color, mColor);
        mStartAngle = typedArray.getInt(R.styleable.PullLoadingView_aiv_startAngle, mStartAngle);
        mStrokeWidth = typedArray.getDimension(R.styleable.PullLoadingView_aiv_strokeWidth, mStrokeWidth);
        isAutoStart = typedArray.getBoolean(R.styleable.PullLoadingView_aiv_auto_start, true);
        typedArray.recycle();
        initialize();
    }

    private void initialize() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int alpha = Color.alpha(mColor);
        int red = Color.red(mColor);
        int green = Color.green(mColor);
        int blue = Color.blue(mColor);
        int alphaGradient = Math.abs(alpha - minAlpha) / mLineCount;
        for (int i = 0; i < mColors.length; i++) {
            mColors[i] = Color.argb(alpha - alphaGradient * i, red, green, blue);
        }
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        float radius = Math.min(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom()) * 0.5f;
        if (mStrokeWidth == 0) {
            mStrokeWidth = pointX(mAngleGradient / 2, radius / 2) / 2;
        }
        mPaint.setStrokeWidth(mStrokeWidth);
        for (int i = 0; i < mColors.length; i++) {
            mPaint.setColor(mColors[i]);
            canvas.drawLine(
                    centerX + pointX(-mAngleGradient * i + mStartAngle, radius / 2),
                    centerY + pointY(-mAngleGradient * i + mStartAngle, radius / 2),
                    //这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                    centerX + pointX(-mAngleGradient * i + mStartAngle, radius - mStrokeWidth / 2),
                    //这里计算Y值时, 之所以减去线宽/2, 是防止没有设置的Padding时,图像会超出View范围
                    centerY + pointY(-mAngleGradient * i + mStartAngle, radius - mStrokeWidth / 2),
                    mPaint);
        }
    }

    private float pointX(int angle, float radius) {
        return (float) (radius * Math.cos(angle * Math.PI / 180));
    }

    private float pointY(int angle, float radius) {
        return (float) (radius * Math.sin(angle * Math.PI / 180));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAutoStart) {
            start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimHandler != null) {
            stop();
        }
    }

    /**
     * 开始动画
     */
    public void start() {
        mAnimHandler.post(mAnimRunnable);
    }

    /**
     * 停止动画
     */
    public void stop() {
        mAnimHandler.removeCallbacks(mAnimRunnable);
    }

    /**
     * 设置菊花颜色
     */
    public void setColor(int color) {
        this.mColor = color;
    }

    /**
     * 设置线宽
     */
    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
    }

    /**
     * 设置开始的角度
     */
    public void setStartAngle(int startAngle) {
        this.mStartAngle = startAngle;
    }
}
