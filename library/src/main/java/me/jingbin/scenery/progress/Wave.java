package me.jingbin.scenery.progress;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.UiThread;

public class Wave {

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
    private Paint mPaint= new Paint();;
    int stroke_width = 4;

    {
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setColor(@ColorInt int color) {
        paint.setColor(color);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    private final Path path = new Path();

    @UiThread
    public void onDraw(Canvas canvas) {


        canvas.translate(0, -waveOffsetRatio * height);
        // 波长
        final float waveLength = height / WAVE_COUNT;
        // 振幅
        final float waveAmplitude = WAVE_AMPLITUDE_RATIO * waveLength;

        final float progressWidth = waveAmplitude + progress * width;

        path.reset();
        path.moveTo(0, 0);
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


//        super.onDraw(canvas);
    }

    @UiThread
    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void dr(Canvas canvas) {

        RectF strokeRect = new RectF(0.5f * stroke_width, 0.5f * stroke_width, canvas.getWidth() - 0.5f * stroke_width, canvas.getHeight() - 0.5f * stroke_width);
        if (stroke_width > 0) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(stroke_width);
            mPaint.setColor(Color.GRAY);
            canvas.drawRoundRect(strokeRect, canvas.getHeight()/2, canvas.getHeight()/2, mPaint);
        }
    }
}
