package com.tronpc.yourdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class intro extends AppCompatActivity {
    ImageView lock, ribbon;
    Animation rotate, drop ,fade;
    TextView secureText;
    private static final int DELAY = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        lock = findViewById(R.id.lock);
        secureText = findViewById(R.id.secureText);
        ribbon = findViewById(R.id.ribbon_small);
        lock.setVisibility(View.INVISIBLE);
        ribbon.setVisibility(View.INVISIBLE);
        rotate = AnimationUtils.loadAnimation(this,R.anim.rotation_fade);
        drop = AnimationUtils.loadAnimation(this,R.anim.ribbon_animation_1);
        fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lock.setVisibility(View.VISIBLE);
                ribbon.setVisibility(View.VISIBLE);
                ribbon.startAnimation(drop);
                lock.startAnimation(rotate);
            }
        },DELAY);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(intro.this, SignInActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}