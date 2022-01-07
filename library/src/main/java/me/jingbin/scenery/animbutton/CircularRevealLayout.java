package me.jingbin.scenery.animbutton;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * @author jingbin
 */
public class CircularRevealLayout extends FrameLayout {

    private static final String TAG = "CircularRevealLayout";
    private float revealRadius;
    private int centerX;
    private int centerY;
    private float startRadius;
    private float endRadius;
    private View childRevealView;
    //动画是否正在执行
    private boolean isRunning = false;
    //绘制路径
    private Path path;

    public CircularRevealLayout(Context context) {
        this(context, null, 0);
    }

    public CircularRevealLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularRevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        setFocusable(false);
    }

    /**
     * 设置要揭示的子view
     *
     * @param childRevealView
     */
    public void setChildRevealView(View childRevealView) {
        this.childRevealView = childRevealView;
    }

    /**
     * 设置要揭示的子view的下标
     *
     * @param index
     */
    public void setChildRevealViewIndex(int index) {
        if (getChildCount() > index) {
            this.childRevealView = getChildAt(index);
        }
    }

    /**
     * 设置揭示半径
     *
     * @param revealRadius
     */
    private void setRevealRadius(float revealRadius) {
        this.revealRadius = revealRadius;
//        Log.e(TAG, "revealRadius=" + revealRadius);
        invalidate();
    }

    /**
     * 设置揭示的中心点x坐标
     *
     * @param centerX
     */
    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    /**
     * 设置揭示的中心点y坐标
     *
     * @param centerY
     */

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    /**
     * 设置揭示的开始半径
     *
     * @param startRadius
     */
    public void setStartRadius(float startRadius) {
        this.startRadius = startRadius;
    }

    /**
     * 设置揭示的结束半径
     *
     * @param endRadius
     */
    public void setEndRadius(float endRadius) {
        this.endRadius = endRadius;
    }


    public Animator getAnimator() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "revealRadius", startRadius, endRadius);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isRunning = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                isRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return objectAnimator;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (isRunning && child == childRevealView) {
            final int state = canvas.save();
            path.reset();
            //Pat.Direction.CW：顺时针
            path.addCircle(centerX, centerY, revealRadius, Path.Direction.CW);
            //裁剪
            canvas.clipPath(path);
            boolean isInvalided = super.drawChild(canvas, child, drawingTime);
            canvas.restoreToCount(state);
            return isInvalided;
        }

        return super.drawChild(canvas, child, drawingTime);
    }

}