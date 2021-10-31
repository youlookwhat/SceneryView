package me.jingbin.scenery;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import androidx.annotation.Nullable;

import me.jingbin.library.scenery.R;
import me.jingbin.scenery.wave.DensityUtil;

/**
 * 横向双水波浪进度条
 *
 * @author jingbin
 * https://github.com/youlookwhat/SceneryView
 **/
public class HorizontalWaveProgressView extends View {

    //绘制波浪画笔
    private Paint wavePaint;
    //绘制波浪Path
    private Path wavePath;
    //波浪的宽度
    private final float waveLength;
    //波浪的高度
    private final float waveHeight;
    //波浪组的数量 一个波浪是一低一高
    private int waveNumber;
    //默认View的宽高
    private int waveDefaultWidth;
    private int waveDefaultHeight;
    //测量后的View实际宽高
    private int waveActualSizeWidth;
    private int waveActualSizeHeight;
    //当前进度值占总进度值的占比
    private float currentPercent;
    //当前进度值
    private int currentProgress;
    //进度的最大值
    private int maxProgress;
    //动画对象
    private WaveProgressAnimal waveProgressAnimator;
    private ValueAnimator mProgressAnimator;
    private ValueAnimator mEndAnimator;
    //波浪平移距离
    private float moveDistance = 0;
    //圆形背景画笔
    private Paint backgroundPaint;
    // 边框
    private Paint borderPaint;
    //bitmap
    private Bitmap circleBitmap;
    //bitmap画布
    private Canvas bitmapCanvas;
    //波浪颜色
    private final int wave_color;
    //圆形背景进度框颜色
    private final int backgroundColor;
    //进度条显示值监听接口
    private UpdateTextListener updateTextListener;
    //是否绘制双波浪线
    private boolean isShowSecondWave;
    //第二层波浪的颜色
    private final int secondWaveColor;
    //边框色
    private final int borderColor;
    //第二层波浪的画笔
    private Paint secondWavePaint;
    private Path secondWavePath;
    private int dp1;
    // 圆角角度
    private int dp27;
    private RectF rectBg;
    private RectF rectBorder;

    public HorizontalWaveProgressView(Context context) {
        this(context, null);
    }

    public HorizontalWaveProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalWaveProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取attrs文件下配置属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HorizontalWaveProgressView);
        //获取波浪宽度 第二个参数，如果xml设置这个属性，则会取设置的默认值 也就是说xml没有指定wave_length这个属性，就会取Density.dip2px(context,25)
        waveLength = typedArray.getDimension(R.styleable.HorizontalWaveProgressView_wave_length, DensityUtil.dip2px(context, 25));
        //获取波浪高度
        waveHeight = typedArray.getDimension(R.styleable.HorizontalWaveProgressView_wave_height, DensityUtil.dip2px(context, 5));
        //获取波浪颜色
        wave_color = typedArray.getColor(R.styleable.HorizontalWaveProgressView_wave_color, Color.parseColor("#B76EFF"));
        //圆形背景颜色
        backgroundColor = typedArray.getColor(R.styleable.HorizontalWaveProgressView_wave_background_color, Color.WHITE);
        //当前进度
        currentProgress = typedArray.getInteger(R.styleable.HorizontalWaveProgressView_currentProgress, 0);
        //最大进度
        maxProgress = typedArray.getInteger(R.styleable.HorizontalWaveProgressView_maxProgress, 100);
        //是否显示第二层波浪
        isShowSecondWave = typedArray.getBoolean(R.styleable.HorizontalWaveProgressView_second_show, false);
        //第二层波浪的颜色
        secondWaveColor = typedArray.getColor(R.styleable.HorizontalWaveProgressView_second_color, Color.parseColor("#DEBCFF"));
        //边框色
        borderColor = typedArray.getColor(R.styleable.HorizontalWaveProgressView_border_color, Color.parseColor("#DEBCFF"));
        //记得把TypedArray回收
        //程序在运行时维护了一个 TypedArray的池，程序调用时，会向该池中请求一个实例，用完之后，调用 recycle() 方法来释放该实例，从而使其可被其他模块复用。
        //那为什么要使用这种模式呢？答案也很简单，TypedArray的使用场景之一，就是上述的自定义View，会随着 Activity的每一次Create而Create，
        //因此，需要系统频繁的创建array，对内存和性能是一个不小的开销，如果不使用池模式，每次都让GC来回收，很可能就会造成OutOfMemory。
        //这就是使用池+单例模式的原因，这也就是为什么官方文档一再的强调：使用完之后一定 recycle,recycle,recycle
        typedArray.recycle();
        init(context);
    }


    /**
     * 初始化一些画笔路径配置
     */
    private void init(Context context) {
        //设置自定义View的宽高
        waveDefaultWidth = DensityUtil.dip2px(context, 152);
        waveDefaultHeight = DensityUtil.dip2px(context, 40);
        dp1 = DensityUtil.dip2px(getContext(), 1);
        dp27 = DensityUtil.dip2px(getContext(), 27);

        wavePath = new Path();
        wavePaint = new Paint();
        //设置画笔为取交集模式
        wavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //设置波浪颜色
        wavePaint.setColor(wave_color);
        //设置抗锯齿
        wavePaint.setAntiAlias(true);

        //矩形背景
        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setAntiAlias(true);

        //边框
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(borderColor);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(dp1);
        borderPaint.setStyle(Paint.Style.STROKE);

        if (isShowSecondWave) {
            //是否绘制双波浪线
            secondWavePath = new Path();
            //初始化第二层波浪画笔
            secondWavePaint = new Paint();
            secondWavePaint.setColor(secondWaveColor);
            secondWavePaint.setAntiAlias(true);
            //因为要覆盖在第一层波浪上，且要让半透明生效，所以选SRC_ATOP模式
            secondWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        }

        //占比一开始设置为0
        currentPercent = currentProgress * 1f / maxProgress;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这里用到了缓存 根据参数创建新位图
        if (circleBitmap == null) {
            circleBitmap = Bitmap.createBitmap(waveActualSizeWidth, waveActualSizeHeight, Bitmap.Config.ARGB_8888);
        }
        //以该bitmap为底创建一块画布
        if (bitmapCanvas == null) {
            bitmapCanvas = new Canvas(circleBitmap);
        }
        // 绘制背景，为了能让波浪填充完整个圆形背景
        if (rectBg == null) {
            rectBg = new RectF(0, 0, waveActualSizeWidth, waveActualSizeHeight);
        }
        bitmapCanvas.drawRoundRect(rectBg, dp27, dp27, backgroundPaint);
        if (isShowSecondWave) {
            //绘制第二层波浪
            bitmapCanvas.drawPath(canvasSecondPath(), secondWavePaint);
        }
        //绘制波浪形
        bitmapCanvas.drawPath(canvasWavePath(), wavePaint);
        //裁剪图片
        canvas.drawBitmap(circleBitmap, 0, 0, null);
        // 绘制边框
        if (rectBorder == null) {
            rectBorder = new RectF(0.5f * dp1, 0.5f * dp1, waveActualSizeWidth - 0.5f * dp1, waveActualSizeHeight - 0.5f * dp1);
        }
        canvas.drawRoundRect(rectBorder, dp27, dp27, borderPaint);
    }

    /**
     * 绘制波浪线
     */
    private Path canvasWavePath() {
        //要先清掉路线
        wavePath.reset();
        //起始点移至(0,0) p0 -p1 的高度随着进度的变化而变化
        wavePath.moveTo((currentPercent) * waveActualSizeWidth, -moveDistance);
        //最多能绘制多少个波浪
        //其实也可以用 i < getWidth() ;i+=waveLength来判断 这个没那么完美
        //绘制p0 - p1 绘制波浪线 这里有一段是超出View的，在View右边距的右边 所以是* 2
        for (int i = 0; i < waveNumber * 2; i++) {
            wavePath.rQuadTo(waveHeight, waveLength / 2, 0, waveLength);
            wavePath.rQuadTo(-waveHeight, waveLength / 2, 0, waveLength);
        }
        //连接p1 - p2
        wavePath.lineTo(0, waveActualSizeHeight);
        //连接p2 - p0
        wavePath.lineTo(0, 0);
        //封闭起来填充
        wavePath.close();
        return wavePath;
    }

    /**
     * 绘制第二层波浪
     */
    private Path canvasSecondPath() {
        float secondWaveHeight = waveHeight;
        secondWavePath.reset();
        //移动到右上方，也就是p1点
        secondWavePath.moveTo((currentPercent) * waveActualSizeWidth, waveActualSizeHeight + moveDistance);
        //p1 - p0
        for (int i = 0; i < waveNumber * 2; i++) {
            secondWavePath.rQuadTo(secondWaveHeight, -waveLength / 2, 0, -waveLength);
            secondWavePath.rQuadTo(-secondWaveHeight, -waveLength / 2, 0, -waveLength);
        }
        //p3-p0的高度随着进度变化而变化
        secondWavePath.lineTo(0, 0);
        //连接p3 - p2
        secondWavePath.lineTo(0, waveActualSizeHeight);
        secondWavePath.lineTo(waveActualSizeHeight, waveActualSizeWidth);
        //连接p2 - p1
        secondWavePath.lineTo(waveActualSizeWidth, waveActualSizeHeight + moveDistance);
        //封闭起来填充
        secondWavePath.close();
        return secondWavePath;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureSize(waveDefaultWidth, widthMeasureSpec);
        int height = measureSize(waveDefaultHeight, heightMeasureSpec);
        //把View改为正方形
        setMeasuredDimension(width, height);
        //waveActualSize是实际的宽高
        waveActualSizeWidth = width;
        waveActualSizeHeight = height;
        //Math.ceil(a)返回求不小于a的最小整数
        // 举个例子:
        // Math.ceil(125.9)=126.0
        // Math.ceil(0.4873)=1.0
        // Math.ceil(-0.65)=-0.0
        //这里是调整波浪数量 就是View中能容下几个波浪 用到ceil就是一定让View完全能被波浪占满 为循环绘制做准备 分母越小就约精准
        waveNumber = (int) Math.ceil(Double.parseDouble(String.valueOf(waveActualSizeHeight / waveLength / 2)));
    }

    /**
     * 返回指定的值
     *
     * @param defaultSize 默认的值
     * @param measureSpec 模式
     */
    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //View.MeasureSpec.EXACTLY：如果是match_parent 或者设置定值就
        //View.MeasureSpec.AT_MOST：wrap_content
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    //新建一个动画类
    public class WaveProgressAnimal extends Animation {

        //在绘制动画的过程中会反复的调用applyTransformation函数，
        // 每次调用参数interpolatedTime值都会变化，该参数从0渐 变为1，当该参数为1时表明动画结束
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            //左边的距离
            moveDistance = interpolatedTime * waveNumber * waveLength * 2;
            //重新绘制
            invalidate();
        }
    }

    /**
     * 直接结束
     *
     * @param duration 结束时间
     */
    public void setProgressEnd(long duration, AnimatorListenerAdapter listenerAdapter) {
        // 如果是100会不满，因为在波动
        if (currentProgress == maxProgress) {
            // 到底了就从头开始
            currentPercent = 0;
        }
        mEndAnimator = ValueAnimator.ofFloat(currentPercent, 1.1f);
        mEndAnimator.setInterpolator(new DecelerateInterpolator());
        mEndAnimator.setDuration(duration);
        mEndAnimator.addUpdateListener(listener);
        mEndAnimator.addListener(listenerAdapter);
        mEndAnimator.start();

        // 波浪线
        startWaveAnimal();
    }

    /**
     * 设置进度
     *
     * @param currentProgress 进度
     * @param duration        达到进度需要的时间
     */
    public void setProgress(int currentProgress, long duration, AnimatorListenerAdapter listenerAdapter) {
        float percent = currentProgress * 1f / maxProgress;
        this.currentProgress = currentProgress;
        //从0开始变化
        currentPercent = 0;
        moveDistance = 0;
        mProgressAnimator = ValueAnimator.ofFloat(0, percent);
        //设置动画时间
        mProgressAnimator.setDuration(duration);
        //让动画匀速播放，避免出现波浪平移停顿的现象
        mProgressAnimator.setInterpolator(new LinearInterpolator());
        mProgressAnimator.addUpdateListener(listener);
        mProgressAnimator.addListener(listenerAdapter);
        mProgressAnimator.start();

        // 波浪线
        startWaveAnimal();
    }

    /**
     * 波浪动画
     */
    private void startWaveAnimal() {
        //动画实例化
        if (waveProgressAnimator == null) {
            waveProgressAnimator = new WaveProgressAnimal();
            //设置动画时间
            waveProgressAnimator.setDuration(2000);
            //设置循环播放
            waveProgressAnimator.setRepeatCount(Animation.INFINITE);
            //让动画匀速播放，避免出现波浪平移停顿的现象
            waveProgressAnimator.setInterpolator(new LinearInterpolator());
            //当前视图开启动画
            this.startAnimation(waveProgressAnimator);
        }
    }

    /**
     * 进度的监听
     */
    ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 当前进度百分比，[0,1]
            currentPercent = (float) animation.getAnimatedValue();
            //这里直接根据进度值显示
            if (updateTextListener != null) {
                updateTextListener.updateText(currentPercent, maxProgress);
            }
        }
    };


    public interface UpdateTextListener {
        /**
         * 提供接口 给外部修改数值样式 等
         *
         * @param currentPercent 当前进度百分比
         * @param maxProgress    进度条的最大数值
         */
        void updateText(float currentPercent, float maxProgress);
    }

    /**
     * 设置监听
     */
    public void setUpdateTextListener(UpdateTextListener updateTextListener) {
        this.updateTextListener = updateTextListener;

    }

    /**
     * 停止动画，销毁对象
     */
    public void stopAnimal() {
        if (waveProgressAnimator != null) {
            waveProgressAnimator.cancel();
        }
        if (mProgressAnimator != null && mProgressAnimator.isStarted()) {
            mProgressAnimator.removeAllListeners();
            mProgressAnimator.cancel();
        }
        if (mEndAnimator != null && mEndAnimator.isStarted()) {
            mEndAnimator.removeAllListeners();
            mEndAnimator.cancel();
        }
    }
}
