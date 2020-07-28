package com.tronpc.yourdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

public class LockSetupActivity extends AppCompatActivity {

    Switch aSwitch;
    Animation unlock,lock;
    ImageView lockUpper,modeLocked,modeUnlocked,ribbon;

    private static final String PASSWORD_KEY = "password";
    private static final String SHARED_PREFS = "YourNote";
    private static final String LOCK_STATUS = "lockStatus";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    LinearLayout changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setup);

       preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
       editor = preferences.edit();
       changePassword = findViewById(R.id.changePasswordButton);

        unlock = AnimationUtils.loadAnimation(LockSetupActivity.this,R.anim.unlock_animation);
        lock = AnimationUtils.loadAnimation(LockSetupActivity.this,R.anim.lock_animation);
        unlock.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lockUpper.setVisibility(View.INVISIBLE);
                modeUnlocked.setVisibility(View.VISIBLE);
                modeLocked.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lock.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               modeUnlocked.setVisibility(View.INVISIBLE);
               modeLocked.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lockUpper = findViewById(R.id.lockUpper);
        modeLocked = findViewById(R.id.modeLocked);
        modeUnlocked = findViewById(R.id.modeUnlocked);
        modeUnlocked.setVisibility(View.INVISIBLE);
        modeUnlocked.setY(modeUnlocked.getY()-200);
        modeLocked.setVisibility(View.INVISIBLE);
        aSwitch = findViewById(R.id.lockSwitch);
        ribbon = findViewById(R.id.settingsActivityRibbon);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LockSetupActivity.this,ChangePassword.class));
            }
        });


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    if (!preferences.getString(PASSWORD_KEY, "").equals("")) {
                        lockIt();
                        editor.putBoolean(LOCK_STATUS,b);
                    } else {
                        aSwitch.setChecked(false);
                        Toast.makeText(LockSetupActivity.this, "Change Password First", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    unLockIt();
                    editor.putBoolean(LOCK_STATUS,b);
                }
                editor.apply();
            }
        });
    }
    private void lockIt(){
        modeLocked.setVisibility(View.INVISIBLE);
        modeUnlocked.startAnimation(lock);
    }
    private void unLockIt(){
        modeLocked.setVisibility(View.INVISIBLE);
        lockUpper.startAnimation(unlock);
    }

    private boolean getLockStatus(){
        return preferences.getBoolean(LOCK_STATUS,false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        aSwitch.setChecked(getLockStatus());
        final Animation animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbon.startAnimation(animationRibbon);
        if(aSwitch.isChecked()){
            lockIt();
        }
        else unLockIt();
    }
}