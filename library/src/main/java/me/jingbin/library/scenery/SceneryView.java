package me.jingbin.library.scenery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 风景View
 * 2020-01-01 11:51 ，历时四个半天，基本完成
 *
 * @author jingbin
 */
public class SceneryView extends View {

    private final static float CLOUD_SCALE_RATIO = 0.85f;
    private final static int DEFAULT_SUN_COLOR = Color.YELLOW;
    private final static int DEFAULT_LEFT_RIGHT_MOU_COLOR = Color.parseColor("#E6E6E8");
    private final static int DEFAULT_MIDDLE_MOU_COLOR = Color.WHITE;
    private final static int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#6ABDE8");
    private final static int DEFAULT_CLOUD_COLOR = Color.parseColor("#B6C4F3");
    private int mParentWidth = 394;
    private int mParentHeight = 394;
    private int mViewCircle;
    private int mSunAnimCircle;
    private int mSunAnimX;
    private int mSunAnimY;
    private int[] mSunAnimXY;
    private boolean mIsStart = false;

    private float mMaxCloudTranslationX;
    private float mMaxLeftRightMouTranslationY;
    private float mCloudAnimatorValue;
    private float mLeftRightMouAnimatorValue;
    private float mMidMouAnimatorValue;
    private float mSunAnimatorValue = -120;

    private Path mCloudPath;
    private Path mLeftMountainPath;
    private Path mRightMountainPath;
    private Path mMidMountainPath;
    private Path mSunPath;
    private Path mRoundPath;

    private Paint mBackgroundPaint;
    private Paint mCloudPaint;
    private Paint mLeftMountainPaint;
    private Paint mRightMountainPaint;
    private Paint mMidMountainPaint;
    private Paint mSunPaint;

    private Matrix mComputeMatrix = new Matrix();
    private Matrix mComputeMatrix2 = new Matrix();
    private Matrix mComputeMatrix3 = new Matrix();
    private Matrix mComputeMatrix4 = new Matrix();
    private Path mComputePath = new Path();
    private Path mComputePath2 = new Path();
    private Path mComputePath3 = new Path();
    private Path mComputePath4 = new Path();

    private int mSunColor = DEFAULT_SUN_COLOR;
    private int mLeftMouColor = DEFAULT_LEFT_RIGHT_MOU_COLOR;
    private int mRightMouColor = DEFAULT_LEFT_RIGHT_MOU_COLOR;
    private int mMidMouColor = DEFAULT_MIDDLE_MOU_COLOR;
    private int mCloudColor = DEFAULT_CLOUD_COLOR;
    private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;

    public SceneryView(Context context) {
        this(context, null);
    }

    public SceneryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SceneryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 步骤1：初始化动画属性
        initAttrs(context, attrs);

        // 步骤2：初始化自定义View
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SceneryView);
        mSunColor = typedArray.getColor(R.styleable.SceneryView_sun_color, DEFAULT_SUN_COLOR);
        mLeftMouColor = typedArray.getColor(R.styleable.SceneryView_left_mountain_color, DEFAULT_LEFT_RIGHT_MOU_COLOR);
        mRightMouColor = typedArray.getColor(R.styleable.SceneryView_right_mountain_color, DEFAULT_LEFT_RIGHT_MOU_COLOR);
        mMidMouColor = typedArray.getColor(R.styleable.SceneryView_mid_mountain_color, DEFAULT_MIDDLE_MOU_COLOR);
        mCloudColor = typedArray.getColor(R.styleable.SceneryView_cloud_color, DEFAULT_CLOUD_COLOR);
        mBackgroundColor = typedArray.getColor(R.styleable.SceneryView_background_color, DEFAULT_BACKGROUND_COLOR);
        typedArray.recycle();
    }

    private void init() {
        mCloudPath = new Path();
        mLeftMountainPath = new Path();
        mRightMountainPath = new Path();
        mMidMountainPath = new Path();
        mSunPath = new Path();
        mRoundPath = new Path();

        mCloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCloudPaint.setColor(mCloudColor);
        // 中间重复的覆盖
        mCloudPaint.setAntiAlias(true);
        mCloudPaint.setStyle(Paint.Style.FILL);

        // 背景
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setAntiAlias(true);

        mLeftMountainPaint = new Paint();
        mLeftMountainPaint.setAntiAlias(true);
        mLeftMountainPaint.setColor(mLeftMouColor);

        mRightMountainPaint = new Paint();
        mRightMountainPaint.setAntiAlias(true);
        mRightMountainPaint.setColor(mRightMouColor);

        mMidMountainPaint = new Paint();
        mMidMountainPaint.setAntiAlias(true);
        mMidMountainPaint.setColor(mMidMouColor);

        mSunPaint = new Paint();
        mSunPaint.setColor(mSunColor);
        mSunPaint.setAntiAlias(true);
    }

    /**
     * start animator
     */
    public void playAnimator() {
        if (!mIsStart) {
            mIsStart = true;
            playCloudMouAnimator(true);
            setSunAnimator();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 取宽高的最小值
        mParentWidth = mParentHeight = Math.min(getWidth(), getHeight());
        // View的半径
        mViewCircle = mParentWidth >> 1;
        drawMo(mViewCircle, mViewCircle - getValue(10), getValue(10));
        drawYun(mParentWidth + getValue(200), mParentHeight);
        drawSun();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRoundPath.addCircle(mViewCircle, mViewCircle, mViewCircle, Path.Direction.CW);
        canvas.clipPath(mRoundPath);
        super.onDraw(canvas);
        canvas.drawCircle(mViewCircle, mViewCircle, mViewCircle, mBackgroundPaint);

        // 太阳
//        canvas.drawCircle((mParentWidth / 2) - getValue(90), (mParentHeight / 2) - getValue(80), sunWidth / 2, mSunPaint);

        // 三座山
//        drawMo(canvas, mParentWidth / 2, (mParentHeight / 2) - getValue(10), getValue(10));

        // 太阳
        mComputeMatrix4.reset();
        // x y 坐标
        int[] circleXY = getCircleXY(mSunAnimX, mSunAnimY, mSunAnimCircle, mSunAnimatorValue);
        mComputeMatrix4.postTranslate(circleXY[0] - mSunAnimXY[0], circleXY[1] - mSunAnimXY[1]);
        mSunPath.transform(mComputeMatrix4, mComputePath4);
        canvas.drawPath(mComputePath4, mSunPaint);

        // 左右的山
        mComputeMatrix2.reset();
        mComputeMatrix2.postTranslate(0, mMaxLeftRightMouTranslationY * mLeftRightMouAnimatorValue);
        mLeftMountainPath.transform(mComputeMatrix2, mComputePath2);
        canvas.drawPath(mComputePath2, mLeftMountainPaint);
        mRightMountainPath.transform(mComputeMatrix2, mComputePath2);
        canvas.drawPath(mComputePath2, mRightMountainPaint);

        // 中间的山
        mComputeMatrix3.reset();
        mComputeMatrix3.postTranslate(0, mMaxLeftRightMouTranslationY * mMidMouAnimatorValue);
        mMidMountainPath.transform(mComputeMatrix3, mComputePath3);
        canvas.drawPath(mComputePath3, mMidMountainPaint);

        // 云朵
        mComputeMatrix.reset();
        mComputeMatrix.postTranslate(mMaxCloudTranslationX * mCloudAnimatorValue, 0);
        mCloudPath.transform(mComputeMatrix, mComputePath);
        canvas.drawPath(mComputePath, mCloudPaint);
//        canvas.restore();
    }

    private void drawSun() {
        // sun图形的直径
        int sunWidth = getValue(70);
        // sun图形的半径
        int sunCircle = sunWidth / 2;
        // sun动画半径 = (sun半径 + 80(sun距离中心点的高度) + 整个View的半径 + sun半径 + 20(sun距离整个View的最下沿的间距)) / 2
        mSunAnimCircle = (sunWidth + getValue(100) + mViewCircle) / 2;
        // sun动画的圆心x坐标
        mSunAnimX = mViewCircle;
        // sun动画的圆心y坐标 = sun动画半径 + (整个View的半径 - 80(sun距离中心点的高度) - sun半径)
        mSunAnimY = mSunAnimCircle + (mViewCircle - getValue(80) - sunCircle);

        mSunAnimXY = getCircleXY(mSunAnimX, mSunAnimY, mSunAnimCircle, -120);
        mSunPath.addCircle(mSunAnimXY[0], mSunAnimXY[1], sunCircle, Path.Direction.CW);
    }

    /**
     * 画中间的三座山
     *
     * @param x 中心点左坐标
     * @param y 中心点右坐标
     */
    private void drawMo(int x, int y, int down) {
        // 左右山 Y坐标相对于中心点下移多少
        int lrmYpoint = down + getValue(30);
        // 左右山 X坐标相对于中心点左移或右移多少
        int lrdPoint = getValue(120);
        // 左右山 山的一半的X间距是多少
        int lrBanDis = getValue(140);
        // 中间山 山的一半的X间距是多少
        int lrBanGao = getValue(150);

        // 左山
        mLeftMountainPath.reset();
        // 起点
        mLeftMountainPath.moveTo(x - lrdPoint, y + lrmYpoint);
        mLeftMountainPath.lineTo(x - lrdPoint + lrBanDis, y + lrmYpoint + lrBanGao);
        mLeftMountainPath.lineTo(x - lrdPoint - lrBanDis, y + lrmYpoint + lrBanGao);
        // 使这些点构成封闭的多边形
        mLeftMountainPath.close();
        // canvas.drawPath(mLeftMountainPath, mLeftMountainPaint);

        // 右山
        mRightMountainPath.reset();
        mRightMountainPath.moveTo(x + lrdPoint + getValue(10), y + lrmYpoint);
        mRightMountainPath.lineTo(x + lrdPoint + getValue(10) + lrBanDis, y + lrmYpoint + lrBanGao);
        mRightMountainPath.lineTo(x + lrdPoint + getValue(10) - lrBanDis, y + lrmYpoint + lrBanGao);
        mRightMountainPath.close();

        // 中山
        mMidMountainPath.reset();
        mMidMountainPath.moveTo(x, y + down);
        mMidMountainPath.lineTo(x + getValue(220), y + down + mParentHeight / 2 + mParentHeight / 14);
        mMidMountainPath.lineTo(x - getValue(220), y + down + mParentHeight / 2 + mParentHeight / 14);
        mMidMountainPath.close();

        // 左右山移动的距离
        mMaxLeftRightMouTranslationY = (y + down + mParentHeight / 2) / 14;
    }

    /**
     * 云
     */
    private void drawYun(int w, int h) {
        mCloudPath.reset();

        // 云的宽度
        float leftCloudWidth = mParentWidth / 1.4f;
        // 云的 最底下圆柱的高度
        float leftCloudBottomHeight = leftCloudWidth / 3f;
        // 云的 最底下圆柱的半径
        float leftCloudBottomRoundRadius = leftCloudBottomHeight;

        float leftCloudEndX = (w - leftCloudWidth - leftCloudWidth * CLOUD_SCALE_RATIO / 2) / 2 + leftCloudWidth;
        float leftCloudEndY = (h / 3) + getValue(342);

        //add the bottom round rect
        mCloudPath.addRoundRect(new RectF(leftCloudEndX - leftCloudWidth, leftCloudEndY - leftCloudBottomHeight,
                leftCloudEndX, leftCloudEndY), leftCloudBottomHeight, leftCloudBottomRoundRadius, Path.Direction.CW);

        float leftCloudTopCenterY = leftCloudEndY - leftCloudBottomHeight;
        float leftCloudRightTopCenterX = leftCloudEndX - leftCloudBottomRoundRadius;
        float leftCloudLeftTopCenterX = leftCloudEndX - leftCloudWidth + leftCloudBottomRoundRadius;

        // 最右边的云
        mCloudPath.addCircle(leftCloudRightTopCenterX + getValue(12), leftCloudTopCenterY + getValue(14), leftCloudBottomRoundRadius * 3 / 4, Path.Direction.CW);
        // 中间的云
        mCloudPath.addCircle((leftCloudRightTopCenterX + leftCloudLeftTopCenterX - getValue(23)) / 2 - getValue(10), leftCloudTopCenterY - getValue(0), leftCloudBottomRoundRadius / 7, Path.Direction.CW);
        // 左边的云
        mCloudPath.addCircle(leftCloudLeftTopCenterX - getValue(32), leftCloudTopCenterY + getValue(16), leftCloudBottomRoundRadius / 2, Path.Direction.CW);

        mMaxCloudTranslationX = leftCloudBottomRoundRadius / 2;
    }

    /**
     * 开始云和左右两边山的动画
     *
     * @param isFirst 是否第一次播放
     */
    private void playCloudMouAnimator(boolean isFirst) {
        setYunAnimator(isFirst);
        setLeftRightMouAnimator(isFirst);
        setMidMouAnimator(isFirst);
    }

    /**
     * 云的动画
     */
    private void setYunAnimator(boolean isFirst) {
        ValueAnimator mLeftCloudAnimator;
        if (isFirst) {
            mLeftCloudAnimator = ValueAnimator.ofFloat(0, 5);
            mLeftCloudAnimator.setStartDelay(0);
        } else {
            mLeftCloudAnimator = ValueAnimator.ofFloat(-8, 0);
            mLeftCloudAnimator.setStartDelay(100);
        }
        mLeftCloudAnimator.setDuration(1000);
        mLeftCloudAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mLeftCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCloudAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLeftCloudAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });

        mLeftCloudAnimator.start();

    }

    /**
     * 左右两边山的动画
     */
    private void setLeftRightMouAnimator(boolean isFirst) {
        ValueAnimator mLeftRightMouAnimator;
        if (isFirst) {
            mLeftRightMouAnimator = ValueAnimator.ofFloat(0, -1, 10);
        } else {
            mLeftRightMouAnimator = ValueAnimator.ofFloat(10, 0);
        }
        mLeftRightMouAnimator.setDuration(1000);
        mLeftRightMouAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mLeftRightMouAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLeftRightMouAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLeftRightMouAnimator.start();
    }

    /**
     * 中间的山的动画
     */
    private void setMidMouAnimator(final boolean isFirst) {
        ValueAnimator mMidMouAnimator;
        if (isFirst) {
            mMidMouAnimator = ValueAnimator.ofFloat(0, -1, 10);
            mMidMouAnimator.setStartDelay(200);
            mMidMouAnimator.setDuration(1200);
        } else {
            mMidMouAnimator = ValueAnimator.ofFloat(10, 0);
            mMidMouAnimator.setStartDelay(0);
            mMidMouAnimator.setDuration(900);
        }
        mMidMouAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mMidMouAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMidMouAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mMidMouAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isFirst) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playCloudMouAnimator(false);
                        }
                    }, 100);
                }
            }
        });
        mMidMouAnimator.start();
    }

    /**
     * sun的动画
     */
    private void setSunAnimator() {
        ValueAnimator mSunAnimator = ValueAnimator.ofFloat(-120, 240);
        mSunAnimator.setDuration(2700);
        mSunAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mSunAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSunAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        mSunAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsStart = false;
            }
        });
        mSunAnimator.start();
    }


    /**
     * 得到父布局为 150dp 时的对应值
     *
     * @param originalValue 原始值
     */
    private int getValue(float originalValue) {
        return (int) (mParentWidth / (394 / originalValue));
    }

    /**
     * 求sun旋转时，圆上的点。起点为最右边的点，顺时针。
     * https://blog.csdn.net/can3981132/article/details/52559402
     * x1   =   x0   +   r   *   cos(a   *   PI  /180  )
     * y1   =   y0   +   r   *   sin(a   *   PI  /180  )
     *
     * @param angle         角度
     * @param circleCenterX 圆心x坐标
     * @param circleCenterY 圆心y坐标
     * @param circleR       半径
     */
    private int[] getCircleXY(int circleCenterX, int circleCenterY, int circleR, float angle) {
        int x = (int) (circleCenterX + circleR * Math.cos(angle * Math.PI / 180));
        int y = (int) (circleCenterY + circleR * Math.sin(angle * Math.PI / 180));
        return new int[]{x, y};
    }

    /**
     * Set the color of the background
     */
    public SceneryView setColorBackground(int colorBackground) {
        this.mBackgroundColor = colorBackground;
        mBackgroundPaint.setColor(mBackgroundColor);
        postInvalidate();
        return this;
    }

    public SceneryView setSunColor(int sunColor) {
        this.mSunColor = sunColor;
        mSunPaint.setColor(mSunColor);
        postInvalidate();
        return this;
    }

    public SceneryView setLeftMouColor(int leftMouColor) {
        this.mLeftMouColor = leftMouColor;
        mLeftMountainPaint.setColor(mLeftMouColor);
        postInvalidate();
        return this;
    }

    public SceneryView setRightMouColor(int rightMouColor) {
        this.mRightMouColor = rightMouColor;
        mRightMountainPaint.setColor(mRightMouColor);
        postInvalidate();
        return this;
    }

    public SceneryView setMidMouColor(int midMouColor) {
        this.mMidMouColor = midMouColor;
        mMidMountainPaint.setColor(mMidMouColor);
        postInvalidate();
        return this;
    }

    public SceneryView setCloudColor(int cloudColor) {
        this.mCloudColor = cloudColor;
        mCloudPaint.setColor(mCloudColor);
        postInvalidate();
        return this;
    }
}
