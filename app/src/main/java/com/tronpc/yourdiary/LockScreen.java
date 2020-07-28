package com.tronpc.yourdiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LockScreen extends AppCompatActivity {
    private static final String PASSWORD_KEY = "password";
    private static final String SHARED_PREFS = "YourNote";
    private static final String LOCK_STATUS = "lockStatus";
    String password_actual;

    EditText password;
    Button open;

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,0);
        if(!preferences.getBoolean(LOCK_STATUS,false)){
            startActivity(new Intent(LockScreen.this,MainActivity.class));
            finish();
        }else{
            password_actual = preferences.getString(PASSWORD_KEY,"");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        password = findViewById(R.id.passwordInput);
        open = findViewById(R.id.enterButton);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                if(pass.equals(password_actual)){
                    startActivity(new Intent(LockScreen.this,MainActivity.class));
                    finish();
                }
            }
        });
    }
}