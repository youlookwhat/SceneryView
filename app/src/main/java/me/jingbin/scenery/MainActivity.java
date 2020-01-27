package me.jingbin.scenery;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.jingbin.library.Scenery;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void start(View view) {
        Scenery scenery_icon = findViewById(R.id.scenery_icon);
        Scenery scenery = findViewById(R.id.scenery);
        scenery_icon.playAnimator();
//        scenery.playAnimator();
    }
}
