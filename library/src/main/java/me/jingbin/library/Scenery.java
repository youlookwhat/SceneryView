package me.jingbin.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

/**
 * 风景View
 * 2020-01-01 11:51
 *
 * @author jingbin
 */
public class Scenery extends View {

    private int mParentWidth = 525;
    private int mParentHeight = 525;
    private int sunWidth = 50;

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
        Log.e("Scenery", "width = " + getWidth() + "height = " + getHeight());
    }

    private void init() {

    }

    private void initAttrs(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        // 自定义颜色 Color.WHITE
        p.setColor(ContextCompat.getColor(getContext(), R.color.colorBackground));
        // 设置画笔的锯齿效果
        p.setAntiAlias(true);
        // 画圆
        canvas.drawCircle(mParentWidth / 2, mParentHeight / 2, mParentWidth / 2, p);

        Paint sunP = new Paint();
        // 自定义颜色 Color.WHITE
        sunP.setColor(Color.RED);
        // 设置画笔的锯齿效果
        sunP.setAntiAlias(true);
        // 画圆
        canvas.drawCircle(sunWidth / 2, sunWidth / 2, sunWidth / 2, sunP);

        drawMo(canvas, 160, 220);
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
        lmp.setColor(Color.BLACK);
        //实例化路径
        Path path = new Path();
        path.moveTo(left, right);// 此点为多边形的起点
        path.lineTo(left + 90, right + 130);
        path.lineTo(left - 90, right + 130);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, lmp);

        Paint lmp2 = new Paint();
        lmp2.setAntiAlias(true);
        lmp2.setColor(Color.BLACK);
        //实例化路径
        Path path2 = new Path();
        path2.moveTo(left + 160, right);// 此点为多边形的起点
        path2.lineTo(left + 160 + 90, right + 130);
        path2.lineTo(left + 160 - 90, right + 130);
        path2.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path2, lmp2);

        Paint mmp = new Paint();
        mmp.setAntiAlias(true);
        mmp.setColor(Color.WHITE);
        //实例化路径
        Path path3 = new Path();
        path3.moveTo(left + 80, right - 30);// 此点为多边形的起点
        path3.lineTo(left + 80 + 160, right + 130 + 40);
        path3.lineTo(left + 80 - 160, right + 130 + 40);
        path3.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path3, mmp);
    }


}
