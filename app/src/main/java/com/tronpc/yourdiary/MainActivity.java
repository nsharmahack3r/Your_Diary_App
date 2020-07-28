package com.tronpc.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;
import com.tronpc.yourdiary.model.Note;
import com.tronpc.yourdiary.model.User;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<String> noteTitle = new ArrayList<>();
    private static ArrayList<String> noteDate = new ArrayList<>();
    private static ArrayList<String> noteContent = new ArrayList<>();
    private static ArrayList<String> noteId = new ArrayList<>();
    RecyclerView noteView;
    FloatingActionButton newNoteButton;

    private static final String SHARED_PREF = "UserData";
    private static final String KEY = "isRegistered";

    ProgressBar mainProgress;
    TextView userName;
    private static final String USER_NAME = "name";
    private static final String PREFERENCE_NAME = "your_note_user";
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference database, noteData;
    ValueEventListener listener;
    CircleImageView profile;
    String mName,email,uId,uRi;
    ImageView ribbon,logout,empty;
    ConstraintLayout dashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try{FirebaseDatabase.getInstance().setPersistenceEnabled(true);}
        catch (Exception e){

        }

        //Get the current user who is logged in.
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();



        //Store user info in new variables.
        assert firebaseUser != null;
        mName = firebaseUser.getDisplayName();
        email = firebaseUser.getEmail();
        uId = firebaseUser.getUid();

        try {
            uRi = firebaseUser.getPhotoUrl().toString();
        }catch (Exception e){
            uRi = "default";
        }
        profile = findViewById(R.id.profilePicture);
        logout = findViewById(R.id.logoutButton);

        userName = findViewById(R.id.userName);
        empty = findViewById(R.id.empty);
        empty.setVisibility(View.INVISIBLE);
        ribbon = findViewById(R.id.mainActivityRibbon);
        noteView = findViewById(R.id.noteView);
        noteView.setLayoutManager(new LinearLayoutManager(this));
        mainProgress = findViewById(R.id.mainScreenProgress);
        dashboard = findViewById(R.id.dashboard);
        newNoteButton = findViewById(R.id.addNoteButton);
        final Animation animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbon.startAnimation(animationRibbon);

        userName.setText(mName+"'s"+"" + "\nNote");

        if(uRi.equals("default")){
            profile.setImageResource(R.drawable.account);
        }else{
            Picasso.get().load(uRi).into(profile);
        }

         newNoteButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(MainActivity.this, NewNote.class));
             }
         });

        database = FirebaseDatabase.getInstance().getReference("users");
        final User user = new User(mName, email, uId, uRi);
        ValueEventListener testListener = database.child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User mUser = snapshot.getValue(User.class);
                try{
                    String name = mUser.getName();
                    userName.setText(name+"'s"+"" + "\nNote");
                }catch (Exception e){
                    database.child(uId).setValue(user);
                    userName.setText(mName+"'s"+"" + "\nNote");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.removeEventListener(testListener);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair pair = new Pair<View,String>(profile,profile.getTransitionName());

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pair);
                Intent intent = new Intent(MainActivity.this,Settings.class);
                startActivity(intent,options.toBundle());
            }
        });




        noteData = FirebaseDatabase.getInstance().getReference("users/"+uId+"/data");
        noteData.keepSynced(true);
        listener = noteData.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                noteDate.clear();
                noteContent.clear();
                noteTitle.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Note note = dataSnapshot.getValue(Note.class);
                    assert note != null;
                    noteTitle.add(note.getTitle());
                    noteDate.add(note.getDate());
                    noteContent.add(note.getContent());
                    noteId.add(dataSnapshot.getKey());
                }
                try{
                    String s = noteTitle.get(0);
                        Collections.reverse(noteTitle);
                        Collections.reverse(noteDate);
                        Collections.reverse(noteContent);
                        Collections.reverse(noteId);
                        mainProgress.setVisibility(View.INVISIBLE);
                        noteView.setAdapter(new NoteAdapter(MainActivity.this, noteTitle, noteDate, noteContent, noteId));
                        noteView.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.INVISIBLE);
                }catch (Exception e){
                    noteView.setVisibility(View.INVISIBLE);
                    empty.setVisibility(View.VISIBLE);
                    mainProgress.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    public boolean isNetworkAvailable(){
        try{
             ConnectivityManager manager =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo networkInfo =  null;
              if(manager!=null){
                   networkInfo = manager.getActiveNetworkInfo();
              }
              return networkInfo!=null && networkInfo.isConnected();
        }catch (NullPointerException e){
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isNetworkAvailable()){
            Snackbar.make(dashboard,"Working Offline", BaseTransientBottomBar.LENGTH_SHORT).show();
        }

    }
}