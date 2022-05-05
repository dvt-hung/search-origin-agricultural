package com.example.apptxng.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.apptxng.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    TextView text_codeBy;
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Init View
        text_codeBy = findViewById(R.id.text_codeBy);
        lottieAnimationView = findViewById(R.id.lottie);

        // Animation
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash_screen);
        text_codeBy.startAnimation(animation);

        // Go to LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                finishAffinity();
            }
        },2500);
    }
}