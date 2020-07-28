package com.tronpc.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tronpc.yourdiary.model.Note;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.EventListener;

public class NewNote extends AppCompatActivity {

    EditText title, content;
    TextView  date,heading;
    FloatingActionButton saveNote;
    DatabaseReference ref;
    String uId;
    ConstraintLayout layout;
    ImageView ribbon;
    String noteId = "";

    String setTitle;
    String setDate;
    String setContent;

    private static final String NOTE_ID = "note_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        title = findViewById(R.id.new_note_title);
        date = findViewById(R.id.new_note_date);
        content = findViewById(R.id.new_note_content);
        saveNote = findViewById(R.id.saveNote);
        layout = findViewById(R.id.newNoteMain);
        ribbon = findViewById(R.id.new_note_ribbon);
        heading = findViewById(R.id.noteEditorTitle);

        try {

            Bundle values = getIntent().getExtras();
            noteId = values.getString(NOTE_ID);
            values.clear();
        } catch (Exception e){

        }

        uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("users/"+uId+"/data");

        if(!noteId.equals("")){
            heading.setText("Edit Note");
            ref.child(noteId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Note note = snapshot.getValue(Note.class);
                    setTitle = note.getTitle();
                    setDate = note.getDate();
                    setContent = note.getContent();

                    title.setText(setTitle);
                    date.setText(setDate);
                    content.setText(setContent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            saveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveNote(title.getText().toString(),date.getText().toString(),content.getText().toString(),noteId);
                }
            });
        }else{

            final Calendar calendar = Calendar.getInstance();


            final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            date.setText(currentDate);

            saveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mTitle = title.getText().toString();
                    String mContent = content.getText().toString();
                    saveNote(mTitle,currentDate,mContent,noteId);
                }
            });
        }



    }

    private void saveNote(String title, String date, String content,String id){
        String noteId;
        if(id.equals(""))
        noteId = String.valueOf(Calendar.getInstance().getTimeInMillis());
        else noteId=id;

        Note note = new Note(title,date,content);
        ref.child(noteId).setValue(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();
                }
                else{
                    Snackbar.make(layout,"Failed to save note!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        if(!isNetworkAvailable()){
            Toast.makeText(this, "Note saved locally. It will be synced when connection is available.", Toast.LENGTH_SHORT).show();
            finish();
        }
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
        final Animation animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbon.startAnimation(animationRibbon);
    }
}
