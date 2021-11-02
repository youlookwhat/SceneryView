package me.jingbin.scenery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

import me.jingbin.scenery.progress.ProgressView;
import me.jingbin.scenery.utils.ImageUtil;
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

        final HorizontalWaveProgressView circleWaveProgressView = findViewById(R.id.progress_horizontal);
        final TextView tv_progress = findViewById(R.id.tv_progress);
        //设置字体数值显示监听
        circleWaveProgressView.setUpdateTextListener(new HorizontalWaveProgressView.UpdateTextListener() {
            @Override
            public void updateText(float currentProgress, float maxProgress) {
                //取一位整数和并且保留两位小数
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String text_value = decimalFormat.format(currentProgress * maxProgress);
                Log.e("progress", String.valueOf(text_value));
            }
        });
        //设置进度和时间
        circleWaveProgressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_progress.setText("识别中");

                circleWaveProgressView.setProgress(80, 1500, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                });

//                tv_progress.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        circleWaveProgressView.setProgressEnd(1000, new AnimatorListenerAdapter() {
//                            @Override
//                            public void onAnimationEnd(Animator animation) {
//                                super.onAnimationEnd(animation);
//                                tv_progress.setText("识别成功");
//                            }
//                        });
//                    }
//                }, 1000);

//                circleWaveProgressView.setProgressEnd(3000, new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        tv_progress.setText("识别成功");
//                    }
//                });
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
