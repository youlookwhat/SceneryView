package me.jingbin.scenery.progress;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

public class Bubble {

    @NonNull
    private final PointF start = new PointF(1, (float) Math.random());
    @NonNull
    private final PointF control1 = new PointF((float) Math.random(), (float) Math.random());
    @NonNull
    private final PointF control2 = new PointF((float) Math.random(), (float) Math.random());
    @NonNull
    private final PointF end = new PointF(0, (float) Math.random());
    private static final float MIN_SPEED = 0.005f;
    private static final float MAX_SPEED = 0.03f;

    private final float speed = (float) (Math.random() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED);

    private static final float MIN_RADIUS_RATIO = 1 / 60f;
    private static final float MAX_RADIUS_RATIO = 15 / 60f;
    /**
     * radius = radiusRatio * height
     */
    private final float radiusRatio = (float) (Math.random() * (MAX_RADIUS_RATIO - MIN_RADIUS_RATIO) + MIN_RADIUS_RATIO);

    /**
     * 贝塞尔曲线的参数t
     */
    @FloatRange(from = 0, to = 1)
    private float t;

    private final Path path = new Path();

    private final Paint circlePaint = new Paint();

    {
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
    }

    private final Paint linePaint = new Paint();

    {
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setAntiAlias(true);
    }

    public void setColor(@ColorInt int color) {
        circlePaint.setColor(color);
        linePaint.setColor(color);
    }

    @UiThread
    private void onDrawLine(Canvas canvas) {
        path.reset();
        path.moveTo(start.x * width, start.y * height);
        path.cubicTo(control1.x * width, control1.y * height, control2.x * width, control2.y * height, end.x * width, end.y * height);
        canvas.drawPath(path, linePaint);
    }

    @UiThread
    public void onDraw(Canvas canvas) {
//        onDrawLine(canvas);

        final float p_1_t = 1 - t;
        final float p_1_t_2 = (float) Math.pow(p_1_t, 2);
        final float p_1_t_3 = (float) Math.pow(p_1_t, 3);
        final float t = this.t;
        final float t_2 = (float) Math.pow(t, 2);
        final float t_3 = (float) Math.pow(t, 3);
        final float t_p_1_t_2 = t * p_1_t_2;
        final float t_2_p_1_t = t_2 * p_1_t;

        final float x = start.x * p_1_t_3 + 3 * control1.x * t_p_1_t_2 + 3 * control2.x * t_2_p_1_t + end.x * t_3;
        final float y = start.y * p_1_t_3 + 3 * control1.y * t_p_1_t_2 + 3 * control2.y * t_2_p_1_t + end.y * t_3;
        canvas.drawCircle(x * width, y * height, radiusRatio * height, circlePaint);
        this.t += speed;
    }

    private int width;
    private int height;

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

}
