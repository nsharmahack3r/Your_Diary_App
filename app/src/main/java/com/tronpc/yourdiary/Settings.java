package com.tronpc.yourdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Settings extends AppCompatActivity {

    ImageView ribbon,profile,profile_type;
    FirebaseAuth auth;
    LinearLayout noteLock, accoutnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ribbon = findViewById(R.id.settingsActivityRibbon);
        profile = findViewById(R.id.settings_profile);
        profile_type = findViewById(R.id.profile_type);
        accoutnSettings = findViewById(R.id.accountButton);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        Picasso.get().load(user.getPhotoUrl()).into(profile);
        if(!user.getDisplayName().equals("")){
            profile_type.setImageResource(R.drawable.ic_google_icon);
        }
        noteLock  = findViewById(R.id.noteLockButton);
        noteLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,LockSetupActivity.class));
            }
        });

        accoutnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair pair = new Pair<View,String>(profile,profile.getTransitionName());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Settings.this, pair);
                Intent intent = new Intent(Settings.this,AccountSetting.class);
                startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Animation animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbon.startAnimation(animationRibbon);
    }
}