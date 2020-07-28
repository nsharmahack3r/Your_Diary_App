package com.tronpc.yourdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdpter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    public SliderAdpter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.lock,
            R.drawable.password,
            R.drawable.layout_sample,
            R.drawable.page
    };

    public String[] title = {
            "Your Privacy is Our priority",
            "Custom Password",
            "Neat and Clean",
            "Let’s Go!"
    };

    public String[] content = {
            "All your data is encrypted Before saving.",
            "Secure your records with custom passwords to secure locally.",
            "Easy and simple Interface. No Fancy Features. Only what counts.",
            "Write anything whatever is your heart's content and we will ensure only you get to read it!"
    };
    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
         inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View view = inflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImage = view.findViewById(R.id.imageView);
        TextView slideTitle = view.findViewById(R.id.title);
        TextView slideContent = view.findViewById(R.id.content);

         slideImage.setImageResource(slide_images[position]);
         slideTitle.setText(title[position]);
         slideContent.setText(content[position]);

         container.addView(view);

         return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
