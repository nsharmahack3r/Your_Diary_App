package com.tronpc.yourdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Walkthrough extends AppCompatActivity {

    private ViewPager pager;
    private LinearLayout dots;
    ImageView back, forth;

    private TextView[] mDots;

    private static final String FIRST_BOOT = "first_boot";
    private static final String PREFS = "your_note_prefs";
    private int current_item = 0;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS,MODE_PRIVATE);
        if(sharedPreferences.getBoolean(FIRST_BOOT,false)){
            Intent intent = new Intent(Walkthrough.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }else{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(FIRST_BOOT,true);
            editor.apply();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        pager = findViewById(R.id.pager);
        dots = findViewById(R.id.dots);
        back = findViewById(R.id.back);
        forth = findViewById(R.id.forward);


        SliderAdpter sliderAdpter = new SliderAdpter(this);
        pager.setAdapter(sliderAdpter);

        addDotsIndicator(0);
        pager.setOnPageChangeListener(viewListener);
        forth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveForward();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveBackward();
            }
        });
    }

    public void addDotsIndicator(int position){
        dots.removeAllViews();

        mDots = new TextView[4];
        for(int i = 0;i<4;i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(25);
            mDots[i].setTextColor(getResources().getColor(R.color.green_dark));
            mDots[i].setGravity(0);
            dots.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.green_light));
            mDots[position].setTextSize(40);
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            if(position==0)
                back.setVisibility(View.INVISIBLE);
            else
                back.setVisibility(View.VISIBLE);

            if(position==3) {
                forth.setImageResource(R.drawable.arrow);
                forth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Walkthrough.this,intro.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            else
                forth.setImageResource(R.drawable.ic_round_arrow_forward_24);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void moveForward(){
        ++current_item;
        if(current_item<4)
        pager.setCurrentItem(current_item);
    }
    private void moveBackward(){
        --current_item;
        if(current_item>-1)
            pager.setCurrentItem(current_item);
        else if(current_item == 0){
            back.setVisibility(View.INVISIBLE);
        }
    }

}