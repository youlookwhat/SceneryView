package me.jingbin.scenery;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.jingbin.scenery.utils.ImageUtil;


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
