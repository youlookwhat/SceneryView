package me.jingbin.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 风景View
 * 2020-01-01 11:51
 *
 * @author jingbin
 */
public class Scenery extends View {

    private int mParentWidth = 394;
    private int mParentHeight = 394;
    private int sunWidth = 70;
    private Path mLeftCloudPath;      // the cloud's path
    private Path mLeftMountainPath;   // the left mountain's path
    private Path mRightMountainPath;  // the right mountain's path
    private Path mMidMountainPath;    // the mid mountain's path
    private final static float CLOUD_SCALE_RATIO = 0.85f;
    private Matrix mComputeMatrix = new Matrix(); //The matrix for computing

    private float mMaxTranslationX; //The max translation x when do animation.
    private float mLeftCloudAnimatorValue; //The left cloud animator value
    private Path mComputePath = new Path(); //The path for computing
    private Paint mLeftCloudPaint;
    private Paint mLeftMountainPaint;
    private Paint mRightMountainPaint;
    private Paint mMidMountainPaint;

    private ValueAnimator mLeftCloudAnimator;

    private long mLeftCloudAnimatorPlayTime;


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
        // 取宽高的最小值
        mParentWidth = mParentHeight = Math.min(getWidth(), getHeight());

        sunWidth = getValue(70);
        Log.e("onSizeChanged", "width = " + getWidth() + "， height = " + getHeight());
        drawYun(getWidth() + getValue(200), getHeight());
    }

    private void init() {
        mLeftCloudPath = new Path();
        mLeftMountainPath = new Path();
        mRightMountainPath = new Path();
        mMidMountainPath = new Path();

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mLeftCloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeftCloudPaint.setColor(Color.parseColor("#B6C4F3"));
        // 中间重复的覆盖
        mLeftCloudPaint.setAntiAlias(true);
        mLeftCloudPaint.setStyle(Paint.Style.FILL);

        mLeftMountainPaint = new Paint();
        mLeftMountainPaint.setAntiAlias(true);
        mLeftMountainPaint.setColor(Color.parseColor("#E6E6E8"));

        mRightMountainPaint = new Paint();
        mRightMountainPaint.setAntiAlias(true);
        mRightMountainPaint.setColor(Color.parseColor("#E6E6E8"));

        mMidMountainPaint = new Paint();
        mMidMountainPaint.setAntiAlias(true);
        mMidMountainPaint.setColor(Color.WHITE);

//        mRightCloudPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mRightCloudPaint.setColor(Color.GRAY);
//        mRightCloudPaint.setStyle(Paint.Style.FILL);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 圆框
        Path mPath = new Path();
        mPath.addCircle(mParentWidth / 2, mParentWidth / 2, mParentWidth / 2, Path.Direction.CW);
        canvas.clipPath(mPath);

        super.onDraw(canvas);
        Log.e("Scenery", "onDraw： " + "width = " + getWidth() + "， height = " + getHeight());

        // 背景
        Paint p = new Paint();
        // 自定义颜色
        p.setColor(Color.parseColor("#6ABDE8"));
        // 设置画笔的锯齿效果
        p.setAntiAlias(true);
        // 画圆
        canvas.drawCircle(mParentWidth / 2, mParentHeight / 2, mParentWidth / 2, p);

        // 太阳
        Paint sunP = new Paint();
        // 自定义颜色
//        sunP.setColor(Color.parseColor("#FFF589"));
        sunP.setColor(Color.YELLOW);
        // 设置画笔的锯齿效果
        sunP.setAntiAlias(true);
        // 画圆
        canvas.drawCircle((mParentWidth / 2) - getValue(90), (mParentHeight / 2) - getValue(80), sunWidth / 2, sunP);

        // 三座山
        drawMo(canvas, mParentWidth / 2, (mParentHeight / 2) - getValue(10), getValue(10));

//        canvas.save();
//        canvas.clipRect(mRainClipRect);
//        drawRainDrops(canvas);
//        canvas.restore();

//        mComputeMatrix.reset();
//        mComputeMatrix.postTranslate((mMaxTranslationX / 2) * mRightCloudAnimatorValue, 0);
//        mRightCloudPath.transform(mComputeMatrix, mComputePath);
//        canvas.drawPath(mComputePath, mRightCloudPaint);

        mComputeMatrix.reset();
        mComputeMatrix.postTranslate(mMaxTranslationX * mLeftCloudAnimatorValue, 0);
        mLeftCloudPath.transform(mComputeMatrix, mComputePath);
        canvas.drawPath(mComputePath, mLeftCloudPaint);

//        canvas.restore();
    }

    /**
     * 画中间的三座山
     *
     * @param left  中心点左坐标
     * @param right 中心点右坐标
     */
    private void drawMo(Canvas canvas, int left, int right, int down) {
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
        mLeftMountainPath.moveTo(left - lrdPoint, right + lrmYpoint);
        mLeftMountainPath.lineTo(left - lrdPoint + lrBanDis, right + lrmYpoint + lrBanGao);
        mLeftMountainPath.lineTo(left - lrdPoint - lrBanDis, right + lrmYpoint + lrBanGao);
        // 使这些点构成封闭的多边形
        mLeftMountainPath.close();
        canvas.drawPath(mLeftMountainPath, mLeftMountainPaint);

        // 右山
        mRightMountainPath.reset();
        mRightMountainPath.moveTo(left + lrdPoint + getValue(10), right + lrmYpoint);
        mRightMountainPath.lineTo(left + lrdPoint + getValue(10) + lrBanDis, right + lrmYpoint + lrBanGao);
        mRightMountainPath.lineTo(left + lrdPoint + getValue(10) - lrBanDis, right + lrmYpoint + lrBanGao);
        mRightMountainPath.close();
        canvas.drawPath(mRightMountainPath, mRightMountainPaint);

        // 中山
        mMidMountainPath.reset();
        mMidMountainPath.moveTo(left, right + down);
        mMidMountainPath.lineTo(left + getValue(200), right + down + mParentHeight / 2);
        mMidMountainPath.lineTo(left - getValue(200), right + down + mParentHeight / 2);
        mMidMountainPath.close();
        canvas.drawPath(mMidMountainPath, mMidMountainPaint);
    }

    /**
     * 云
     */
    private void drawYun(int w, int h) {
        mLeftCloudPath.reset();

        float centerX = w / 2; //view's center x coordinate
        float minSize = Math.min(w, h); //get the min size

        // 云的宽度
//        float leftCloudWidth = minSize / 2f; //the width of cloud
        float leftCloudWidth = minSize / 1.4f; //the width of cloud
        // 云的 最底下圆柱的高度
        float leftCloudBottomHeight = leftCloudWidth / 3f; //the bottom height of cloud
        // 云的 最底下圆柱的半径
        float leftCloudBottomRoundRadius = leftCloudBottomHeight; //the bottom round radius of cloud

        float leftCloudEndX = (w - leftCloudWidth - leftCloudWidth * CLOUD_SCALE_RATIO / 2) / 2 + leftCloudWidth; //the left cloud end x coordinate
//        float leftCloudEndY = (h / 3) + getValue(318); //clouds' end y coordinate
        float leftCloudEndY = (h / 3) + getValue(342); //clouds' end y coordinate
//        float leftCloudEndY = (h / 3)+getValue(100); //clouds' end y coordinate

        //add the bottom round rect
        mLeftCloudPath.addRoundRect(new RectF(leftCloudEndX - leftCloudWidth, leftCloudEndY - leftCloudBottomHeight,
                leftCloudEndX, leftCloudEndY), leftCloudBottomHeight, leftCloudBottomRoundRadius, Path.Direction.CW);

        float leftCloudTopCenterY = leftCloudEndY - leftCloudBottomHeight;
        float leftCloudRightTopCenterX = leftCloudEndX - leftCloudBottomRoundRadius;
        float leftCloudLeftTopCenterX = leftCloudEndX - leftCloudWidth + leftCloudBottomRoundRadius;

        // 最右边的云
        mLeftCloudPath.addCircle(leftCloudRightTopCenterX + getValue(12), leftCloudTopCenterY + getValue(14), leftCloudBottomRoundRadius * 3 / 4, Path.Direction.CW);
        // 中间的云
        mLeftCloudPath.addCircle((leftCloudRightTopCenterX + leftCloudLeftTopCenterX - getValue(23)) / 2 - getValue(10), leftCloudTopCenterY - getValue(0), leftCloudBottomRoundRadius / 7, Path.Direction.CW);
        // 左边的云
        mLeftCloudPath.addCircle(leftCloudLeftTopCenterX - getValue(32), leftCloudTopCenterY + getValue(16), leftCloudBottomRoundRadius / 2, Path.Direction.CW);

        mMaxTranslationX = leftCloudBottomRoundRadius / 2;
    }


    /**
     * 设置云的动画
     */
    public void setupAnimator() {
        mLeftCloudAnimatorPlayTime = 0;
//        mLeftCloudAnimator = ValueAnimator.ofFloat(0, 5);
        mLeftCloudAnimator = ValueAnimator.ofFloat(-8, 0);
        mLeftCloudAnimator.setDuration(1500);
        mLeftCloudAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        mLeftCloudAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mLeftCloudAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLeftCloudAnimatorValue = (float) animation.getAnimatedValue();
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
     * 得到父布局为 150dp 时的值
     *
     * @param originalValue 原始值
     */
    private int getValue(float originalValue) {
        return (int) (mParentWidth / (394 / originalValue));
    }
}
