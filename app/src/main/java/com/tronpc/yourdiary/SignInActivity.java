package com.tronpc.yourdiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tronpc.yourdiary.model.User;

public class SignInActivity extends AppCompatActivity {

    ImageView ribbon;
    EditText email,password;
    TextView newUser;
    Button login, googleSignIn;
    LinearLayout page;
    Animation animationRibbon, ribbonExit;
    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    ProgressBar progressBar;
    private static final int RC_SIGN_IN = 2;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(this, LockScreen.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        newUser = findViewById(R.id.new_here);
        page = findViewById(R.id.page);
        ribbon = findViewById(R.id.ribbon);
        ribbon.setVisibility(View.VISIBLE);
        googleSignIn = findViewById(R.id.googleSignIn);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                progressBar.setVisibility(View.VISIBLE);
                googleSignIn.setVisibility(View.INVISIBLE);
                login.setVisibility(View.GONE);
            }
        });

        animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbonExit = AnimationUtils.loadAnimation(this, R.anim.uptodown_exit);


        ribbon.startAnimation(animationRibbon);

//        page.setAnimation(fade);
        email = findViewById(R.id.email);
        password = findViewById(R.id.SignInpassword);
//        heading = findViewById(R.id.loginHeading);
        login = findViewById(R.id.login);
//        dairy = findViewById(R.id.dairy);
//        dairy.setAnimation(leftToRight);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if(!userEmail.equals("")){
                    if(!userPassword.equals("")){
                        signIn(userEmail,userPassword);
                        login.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }else
                        Toast.makeText(SignInActivity.this,"Password can't be empty",Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(SignInActivity.this,"Emial can't be empty",Toast.LENGTH_SHORT).show();
            }
        });



        ribbonExit.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ribbon.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationRibbon.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ribbon.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        buttonSetter();
    }

    private void buttonSetter(){

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                page.setVisibility(View.INVISIBLE);
//                ribbon.setVisibility(View.INVISIBLE);
                ribbonExit.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ribbon.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(SignInActivity.this, SignUp.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ribbon.startAnimation(ribbonExit);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ribbon.startAnimation(animationRibbon);
        buttonSetter();
    }
    private void signIn(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ribbonExit.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            page.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ribbon.startAnimation(ribbonExit);
                } else
                {
                    login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    googleSignIn.setVisibility(View.VISIBLE);
                }


            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken(),account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                googleSignIn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Sign in Failed!", Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken, final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                            Log.d("TAG", "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Sign in Failed!", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}