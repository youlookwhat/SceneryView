package me.jingbin.scenery.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import java.util.ArrayList;
import java.util.List;

public class ProgressView extends View {

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        wave.dr(canvas);
        super.onDraw(canvas);
        for (Bubble bubble : bubbles) {
            bubble.onDraw(canvas);
        }
        wave.onDraw(canvas);
//        wave2.onDraw(canvas);
        invalidate();
    }

    @FloatRange(from = 0, to = 1)
    private float progress = 0.5f;

    @UiThread
    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (Bubble bubble : bubbles) {
            bubble.onSizeChanged(w, h, oldw, oldh);
        }

        wave.setColor(0xFFFF0000);
        wave.onSizeChanged(w, h, oldw, oldh);
//        wave2.setColor(0xFF2483D9);
//        wave2.onSizeChanged(w, h, oldw, oldh);
    }

    public void addBubble(){
        Bubble bubble = new Bubble();
        bubble.setColor(0xFFFF0000);
        bubble.onSizeChanged(getWidth(), getHeight(), getWidth(), getHeight());
        bubbles.add(bubble);
    }

    private List<Bubble> bubbles = new ArrayList<>();
    private Wave wave = new Wave();
    private Wave wave2 = new Wave();

}