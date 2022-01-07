package me.jingbin.scenery.animbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * @author jingbin
 */
public class AnimationHelper {

    // 半径
    public static final int MINI_RADIUS = 0;
    // private
    private static final int DEFAULT_DURATION = 300;

    /**
     * 显示动画
     *
     * @param haveShowView  已经显示的view，且和展开的view同宽同高。用于处理要操作的view不显示导致获取不到宽高的问题
     * @param view          要操作的view
     * @param startRadius   开始扩散时，圆的半径
     * @param durationMills 动画时长
     * @param listener      动画结束监听
     */
    public static void show(final View view, View haveShowView, float startRadius, long durationMills, final AnimatorEndListener listener) {
        /**如果第一次view是隐藏的，则获取到的宽度为0*/
        final int xCenter = haveShowView.getWidth() / 2;
        final int yCenter = haveShowView.getHeight() / 2;

//        Log.e("xCenter", xCenter + "");
//        Log.e("yCenter", xCenter + "");

        // 获取扩散的半径 函数返回它的所有参数的平方和的平方根
        float endRadius = (float) Math.hypot((double) xCenter, (double) yCenter);

        Animator animator = ViewAnimationCompatUtils.createCircularReveal(view, xCenter, yCenter, startRadius, endRadius);
        animator.setDuration(durationMills);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onEnd();
                }
            }
        });
        animator.start();
    }

    /**
     * 展示动画，会有获取不到宽高问题！
     *
     * @param view          要操作的view
     * @param startRadius   开始扩散时，圆的半径
     * @param durationMills 动画时长
     * @param listener      动画结束监听
     */
    public static void show(final View view, float startRadius, long durationMills, final AnimatorEndListener listener) {
        /**如果第一次view是隐藏的，则获取到的宽度为0*/
        final int xCenter = view.getWidth() / 2;
        final int yCenter = view.getHeight() / 2;

        // 获取扩散的半径 函数返回它的所有参数的平方和的平方根
        float endRadius = (float) Math.hypot((double) xCenter, (double) yCenter);
        Animator animator = ViewAnimationCompatUtils.createCircularReveal(view, xCenter, yCenter, startRadius, endRadius);
        animator.setDuration(durationMills);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onEnd();
                }
            }
        });
        animator.start();
    }

    /**
     * 收起动画
     *
     * @param view          要操作的view
     * @param endRadius     结束时，圆的半径
     * @param durationMills 动画时长
     * @param visible       动画结束后是否显示
     * @param listener      动画结束监听
     */
    public static void hide(final View view, float endRadius, long durationMills, final int visible, final AnimatorEndListener listener) {
        final int xCenter = view.getWidth() / 2;
        final int yCenter = view.getHeight() / 2;

        // 获取扩散的半径
        float startRadius = (float) Math.hypot((double) xCenter, (double) yCenter);

        Animator animator = ViewAnimationCompatUtils.createCircularReveal(view, xCenter, yCenter, startRadius, endRadius);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(visible);
                if (listener != null) {
                    listener.onEnd();
                }
            }
        });
        animator.setDuration(durationMills);
        animator.start();
    }

    public interface AnimatorEndListener {
        /**
         * 动画结束
         */
        void onEnd();
    }

    public static void hide(View myView, float startRadius, AnimatorEndListener listenerAdapter) {
        hide(myView, startRadius, DEFAULT_DURATION, View.GONE, listenerAdapter);
    }

    public static void show(final View view, View haveShowView, float startRadius, final AnimatorEndListener listener) {
        show(view, haveShowView, startRadius, DEFAULT_DURATION, listener);
    }

}
