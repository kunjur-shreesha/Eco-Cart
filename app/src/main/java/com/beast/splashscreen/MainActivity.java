package com.beast.splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN = 4000;

    Animation topAnim,bottomAnim,fadeIn;
    ImageView image;
    TextView logo,slogan;
    GifImageView gifImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        fadeIn = AnimationUtils.loadAnimation(this,R.anim.fadein);

        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);
        gifImageView = findViewById(R.id.gifImage1);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);
        gifImageView.setAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            Intent i = new Intent(MainActivity.this,Login_Activity.class);
            Pair[] pairs = new Pair[2];
            pairs[0] = new Pair<View,String>(image,"logo_image");
            pairs[1] = new Pair<View,String>(logo,"logo_text");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(i,options.toBundle());
                finish();
            }
        },SPLASH_SCREEN);
    }
}