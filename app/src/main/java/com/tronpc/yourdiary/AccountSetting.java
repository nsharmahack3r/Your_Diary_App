package com.tronpc.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tronpc.yourdiary.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetting extends AppCompatActivity {

    Button signOut;
    FirebaseAuth auth;
    CircleImageView profile;
    DatabaseReference ref;
    TextView userName;

    private static final String SHARED_PREFS = "YourNote";
    private static final String LOCK_STATUS = "lockStatus";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);

        userName = findViewById(R.id.name_acc_settings);
        profile = findViewById(R.id.acc_settings_profile);
        signOut = findViewById(R.id.settings_signout);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("users"+"/"+auth.getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getName());
                if(user.getUri().equals("default")){
                    profile.setImageResource(R.drawable.account);
                }else{
                    Picasso.get().load(user.getUri()).into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                getSharedPreferences(SHARED_PREFS,0).edit().putBoolean(LOCK_STATUS,false).apply();
                startActivity(new Intent(AccountSetting.this,SignInActivity.class));
            }
        });
    }
}