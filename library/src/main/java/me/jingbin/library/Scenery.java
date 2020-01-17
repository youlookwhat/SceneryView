package me.jingbin.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 风景View
 * 2020-01-01 11:51
 *
 * @author jingbin
 */
public class Scenery extends View {

    private int mParentWidth = 525;
    private int mParentHeight = 525;
    private int sunWidth = 70;
    private Path mLeftCloudPath; //the left cloud's path
    private Path mRightCloudPath; //the left cloud's path
    private final static float CLOUD_SCALE_RATIO = 0.85f;
    private Matrix mComputeMatrix = new Matrix(); //The matrix for computing

    private RectF mRainRect; //the rain rect
    private RectF mRainClipRect; //the rain clip rect
    private float mMaxTranslationX; //The max translation x when do animation.
    private float mLeftCloudAnimatorValue; //The left cloud animator value
    private float mRightCloudAnimatorValue; //The right cloud animator value
    private Path mComputePath = new Path(); //The path for computing
    private Paint mLeftCloudPaint;
    private Paint mRightCloudPaint;

    private ValueAnimator mLeftCloudAnimator;
    private ValueAnimator mRightCloudAnimator;

    private long mLeftCloudAnimatorPlayTime;
    private long mRightCloudAnimatorPlayTime;


    public Scenery(Context context) {
        this(context, null);
    }

    public Scenery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Scenery(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 步骤1：初始化动画属性
        initAttrs(context, attrs);

        // 步骤2：初始化自定义View
        init();
    }

    // 获取View宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mParentWidth = getWidth();
        mParentHeight = getHeight();
        Log.e("Scenery", "width = " + getWidth() + "， height = " + getHeight());
//        drawYun(w, h);
        drawYun(getWidth(), getHeight());
    }

    private void init() {
        mLeftCloudPath = new Path();
        mRightCloudPath = new Path();
        mRainRect = new RectF();
        mRainClipRect = new RectF();

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mLeftCloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeftCloudPaint.setColor(Color.parseColor("#B6C4F3"));
        mLeftCloudPaint.setStyle(Paint.Style.FILL);

        mRightCloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightCloudPaint.setColor(Color.GRAY);
        mRightCloudPaint.setStyle(Paint.Style.FILL);

        mLeftCloudPath = new Path();
        mRightCloudPath = new Path();
        mRainRect = new RectF();
        mRainClipRect = new RectF();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("Scenery", "onDraw： " + "width = " + getWidth() + "， height = " + getHeight());

        // 背景
        Paint p = new Paint();
        // 自定义颜色 Color.WHITE
        p.setColor(Color.parseColor("#6ABDE8"));
        // 设置画笔的锯齿效果
        p.setAntiAlias(true);
        // 画圆
        canvas.drawCircle(mParentWidth / 2, mParentHeight / 2, mParentWidth / 2, p);

        // 太阳
        Paint sunP = new Paint();
        // 自定义颜色 Color.WHITE
        sunP.setColor(Color.parseColor("#FFF589"));
        // 设置画笔的锯齿效果
        sunP.setAntiAlias(true);
        // 画圆
        canvas.drawCircle((mParentWidth / 2) - 100, (mParentHeight / 2) - 100, sunWidth / 2, sunP);

        // 三座山
        drawMo(canvas, mParentWidth / 2, (mParentHeight / 2) - 10);

//        canvas.save();
        canvas.clipRect(mRainClipRect);
//        drawRainDrops(canvas);
        canvas.restore();

        mComputeMatrix.reset();
        mComputeMatrix.postTranslate((mMaxTranslationX / 2) * mRightCloudAnimatorValue, 0);
        mRightCloudPath.transform(mComputeMatrix, mComputePath);
        canvas.drawPath(mComputePath, mRightCloudPaint);

        mComputeMatrix.reset();
        mComputeMatrix.postTranslate(mMaxTranslationX * mLeftCloudAnimatorValue, 0);
        mLeftCloudPath.transform(mComputeMatrix, mComputePath);
        canvas.drawPath(mComputePath, mLeftCloudPaint);
    }

    /**
     * 画中间的三个山
     *
     * @param left  中心点左坐标
     * @param right 中心点右坐标
     */
    private void drawMo(Canvas canvas, int left, int right) {
        Paint lmp = new Paint();
        lmp.setAntiAlias(true);
        lmp.setColor(Color.parseColor("#E6E6E8"));
        //实例化路径
        Path path = new Path();
        path.moveTo(left - 80, right + 20);// 此点为多边形的起点
        path.lineTo(left - 80 + 110, right + 20 + 130);
        path.lineTo(left - 80 - 110, right + 20 + 130);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, lmp);

        Paint lmp2 = new Paint();
        lmp2.setAntiAlias(true);
        lmp2.setColor(Color.parseColor("#E6E6E8"));
        //实例化路径
        Path path2 = new Path();
        path2.moveTo(left + 80, right + 20);// 此点为多边形的起点
        path2.lineTo(left + 80 + 110, right + 20 + 130);
        path2.lineTo(left + 80 - 110, right + 20 + 130);
        path2.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path2, lmp2);

        Paint mmp = new Paint();
        mmp.setAntiAlias(true);
        mmp.setColor(Color.WHITE);
        //实例化路径
        Path path3 = new Path();
        path3.moveTo(left, right);// 此点为多边形的起点
        path3.lineTo(left + 160, right + 180);
        path3.lineTo(left - 160, right + 180);
        path3.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path3, mmp);
    }

    private void drawYun(int w, int h) {
        mLeftCloudPath.reset();
        mRightCloudPath.reset();


        float centerX = w / 2; //view's center x coordinate
        float minSize = Math.min(w, h); //get the min size

        float leftCloudWidth = minSize / 2.5f; //the width of cloud
        float leftCloudBottomHeight = leftCloudWidth / 3f; //the bottom height of cloud
        float leftCloudBottomRoundRadius = leftCloudBottomHeight; //the bottom round radius of cloud

        float rightCloudTranslateX = leftCloudWidth * 2 / 3; //the distance of the cloud on the right
        float leftCloudEndX = (w - leftCloudWidth - leftCloudWidth * CLOUD_SCALE_RATIO / 2) / 2 + leftCloudWidth; //the left cloud end x coordinate
        float leftCloudEndY = h / 3; //clouds' end y coordinate

        //add the bottom round rect
        mLeftCloudPath.addRoundRect(new RectF(leftCloudEndX - leftCloudWidth, leftCloudEndY - leftCloudBottomHeight,
                leftCloudEndX, leftCloudEndY), leftCloudBottomHeight, leftCloudBottomRoundRadius, Path.Direction.CW);

        float leftCloudTopCenterY = leftCloudEndY - leftCloudBottomHeight;
        float leftCloudRightTopCenterX = leftCloudEndX - leftCloudBottomRoundRadius;
        float leftCloudLeftTopCenterX = leftCloudEndX - leftCloudWidth + leftCloudBottomRoundRadius;

        mLeftCloudPath.addCircle(leftCloudRightTopCenterX, leftCloudTopCenterY, leftCloudBottomRoundRadius * 3 / 4, Path.Direction.CW);
        mLeftCloudPath.addCircle(leftCloudLeftTopCenterX, leftCloudTopCenterY, leftCloudBottomRoundRadius / 2, Path.Direction.CW);

        //************************compute right cloud**********************
        //The cloud on the right is CLOUD_SCALE_RATIO size of the left
        float rightCloudCenterX = rightCloudTranslateX + centerX - leftCloudWidth / 2; //the right cloud center x

        RectF calculateRect = new RectF();
        mLeftCloudPath.computeBounds(calculateRect, false); //compute the left cloud's path bounds

        mComputeMatrix.reset();
        mComputeMatrix.preTranslate(rightCloudTranslateX, -calculateRect.height() * (1 - CLOUD_SCALE_RATIO) / 2);
        mComputeMatrix.postScale(CLOUD_SCALE_RATIO, CLOUD_SCALE_RATIO, rightCloudCenterX, leftCloudEndY);
        mLeftCloudPath.transform(mComputeMatrix, mRightCloudPath);

        float left = calculateRect.left + leftCloudBottomRoundRadius;
        mRightCloudPath.computeBounds(calculateRect, false); //compute the right cloud's path bounds

        float right = calculateRect.right;
        float top = calculateRect.bottom;
        //************************compute right cloud**********************
        mRainRect.set(left, top, right, h * 3 / 4f); //compute the rect of rain...
        mRainClipRect.set(0, mRainRect.top, w, mRainRect.bottom);

        mMaxTranslationX = leftCloudBottomRoundRadius / 2;
//        setupAnimator();
    }


    /**
     * 设置云的动画
     */
    private void setupAnimator() {
        mLeftCloudAnimatorPlayTime = 0;
        mRightCloudAnimatorPlayTime = 0;

        mLeftCloudAnimator = ValueAnimator.ofFloat(0, 1);
        mLeftCloudAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mLeftCloudAnimator.setDuration(1000);
        mLeftCloudAnimator.setInterpolator(new LinearInterpolator());
        mLeftCloudAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mLeftCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLeftCloudAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mRightCloudAnimator = ValueAnimator.ofFloat(1, 0f);
        mRightCloudAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRightCloudAnimator.setDuration(800);
        mRightCloudAnimator.setInterpolator(new LinearInterpolator());
        mRightCloudAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mRightCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRightCloudAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        mLeftCloudAnimator.start();
        mRightCloudAnimator.start();
    }

}
