package me.jingbin.scenery;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.jingbin.library.scenery.SceneryView;


/**
 * @author jingbin
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void start(View view) {
        SceneryView sceneryIcon = findViewById(R.id.scenery_icon);
        SceneryView scenery = findViewById(R.id.scenery);
        sceneryIcon.playAnimator();
        scenery.setColorBackground(Color.GRAY)
                .setSunColor(Color.RED)
                .playAnimator();
    }
}
