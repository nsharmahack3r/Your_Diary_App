package com.tronpc.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tronpc.yourdiary.model.User;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    ImageView ribbon;
    EditText email,password,name,confPassword;
    Button signUpButton;
    LinearLayout page;
    FirebaseAuth mAuth;
    DatabaseReference firebaseDatabase;
    Animation ribbonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        page = findViewById(R.id.page);
        ribbon = findViewById(R.id.ribbon);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confPassword = findViewById(R.id.confirm_password);
        signUpButton = findViewById(R.id.signUp);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("users");
        final Animation animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbonExit = AnimationUtils.loadAnimation(this,R.anim.uptodown_exit);

        ribbon.startAnimation(animationRibbon);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userEmail = email.getText().toString();
                String mPassword = password.getText().toString();
                String mConfPassword = confPassword.getText().toString();

                if(!userName.equals("")){
                    if(!userEmail.equals("")){
                        if((mPassword.length()>5)){
                            if(mPassword.equals(mConfPassword))
                            signUp(userEmail,mPassword);
                            else Toast.makeText(SignUp.this, "Passwords must match!", Toast.LENGTH_SHORT).show();
                        } else Toast.makeText(SignUp.this, "Password must have at least 6 characters!", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(SignUp.this, "Email can't be empty!", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(SignUp.this, "Name can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUp(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userName = name.getText().toString();
                    String userId = mAuth.getUid();
                    User user = new User(userName,email,userId,"default");

                    assert userId != null;
                    firebaseDatabase.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                finishActivity();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }

    private void finishActivity(){
        ribbonExit.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ribbon.startAnimation(ribbonExit);
    }

}