package com.tronpc.yourdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    private static final String PASSWORD_KEY = "password";
    private static final String SHARED_PREFS = "YourNote";
    EditText password,confPassword;
    Button save;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageView ribbon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        password = findViewById(R.id.changePassword);
        save = findViewById(R.id.savePassword);
        confPassword = findViewById(R.id.confPassword);

        preferences = getSharedPreferences(SHARED_PREFS,0);
        editor = preferences.edit();
        ribbon = findViewById(R.id.passwordActivityRibbon);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String conf = confPassword.getText().toString();
                if(!pass.equals("")){
                    if(!conf.equals("")){
                        if(pass.equals(conf)){
                            editor.putString(PASSWORD_KEY,pass);
                            editor.apply();
                            Toast.makeText(ChangePassword.this, "Password Saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }else Toast.makeText(ChangePassword.this,"Passwords must match",Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(ChangePassword.this, "Confirm Password can't be empty", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(ChangePassword.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
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