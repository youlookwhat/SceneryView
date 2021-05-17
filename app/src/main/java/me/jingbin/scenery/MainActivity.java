package me.jingbin.scenery;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

import me.jingbin.scenery.progress.ProgressView;
import me.jingbin.scenery.utils.ImageUtil;
import me.jingbin.scenery.wave.WaveSlipView;
import me.jingbin.scenery.wave.WaveSlipView2;


/**
 * @author jingbin
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化一个雪花样式的fallObject
        FallObject.Builder builder = new FallObject.Builder(getResources().getDrawable(R.drawable.snow_flake));
        FallObject fallObject = builder
                .setSpeed(6, true)
                .setSize(40, 40, true)
                .setWind(5, true, true)
                .build();

//        FallingView fallingView = findViewById(R.id.fallingView);
//        fallingView.addFallObject(fallObject, 100);//添加下落物体对象

        final ProgressView progressView = findViewById(R.id.progressView);
        final WaveSlipView2 waveSlipView2 = findViewById(R.id.progressView22);

        waveSlipView2.startAnimation();
        progressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.addBubble();
                progressView.setProgress(2);
            }
        });

        final CircleWaveProgressView2 circleWaveProgressView = findViewById(R.id.progressView33);
        final TextView tv_value = findViewById(R.id.tv_value);
        final TextView tv_progress = findViewById(R.id.tv_progress);
        //是否绘制第二层波浪
        circleWaveProgressView.isSetCanvasSecondWave(false);
        //将TextView设置进度条里
        circleWaveProgressView.setTextViewVaule(tv_value);
        //设置字体数值显示监听
        circleWaveProgressView.setUpdateTextListener(new CircleWaveProgressView2.UpdateTextListener() {
            @Override
            public String updateText(float interpolatedTime, float currentProgress, float maxProgress) {
                //取一位整数和并且保留两位小数
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String text_value = decimalFormat.format(interpolatedTime * currentProgress / maxProgress * 100) + "%";
                //最终把格式好的内容(数值带进进度条)
                return text_value;
            }
        });
        //设置进度和时间
//        circleWaveProgressView.setProgress(5, 100);
        circleWaveProgressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_progress.setText("识别中");
                circleWaveProgressView.setProgress(80, 3000);
            }
        }, 1500);
    }

    public void start(View view) {
        SceneryView sceneryIcon = findViewById(R.id.scenery_icon);
        sceneryIcon.playAnimator();

        final SceneryView scenery = findViewById(R.id.scenery);


        ImageUtil.saveToLocal(this, ImageUtil.createViewBitmap(scenery));
//        scenery.setOnAnimationListener(new SceneryView.AnimationListener() {
//            @Override
//            public void onAnimationEnd() {
//                scenery.setMidMouColor(Color.BLACK);
//            }
//        });
//        scenery.setColorBackground(Color.GRAY)
//                .setSunColor(Color.RED)
//                .playAnimator();


    }
}
