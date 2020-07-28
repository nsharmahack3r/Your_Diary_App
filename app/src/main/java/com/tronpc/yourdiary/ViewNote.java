package com.tronpc.yourdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tronpc.yourdiary.model.Note;

public class ViewNote extends AppCompatActivity {

    private static final String NOTE_ID = "note_id";

    TextView titleView,dateView,contentView;
    ImageView deleteNote, editNote,ribbon;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_note);

        Bundle values = getIntent().getExtras();
        assert values != null;
        final String id = values.getString(NOTE_ID);
        values.clear();

        titleView = findViewById(R.id.view_note_title);
        dateView = findViewById(R.id.view_note_date);
        contentView = findViewById(R.id.view_note_content);

        deleteNote = findViewById(R.id.delete_note);
        editNote = findViewById(R.id.edit_note);

        database = FirebaseDatabase.getInstance().getReference("users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/data");
        database.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Note note = snapshot.getValue(Note.class);

                try{
                    titleView.setText(note.getTitle());
                    dateView.setText(note.getDate());
                    contentView.setText(note.getContent());
                }catch (Exception e){

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ribbon = findViewById(R.id.view_note_ribbon);

        final Animation animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbon.startAnimation(animationRibbon);

        deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDialog(id);
            }
        });

        editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNote(id);
            }
        });
    }

    private void deleteNote(String noteId){
        FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/data").child(noteId).setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ViewNote.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(ViewNote.this, "Failed to delete note", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void launchDialog(final String id)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewNote.this);
        builder.setTitle("Are You sure?")
                .setMessage("The Note will be deleted permanently")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(id);
                        finish();
                    }
                })
                .setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void editNote(String noteId){
        Intent intent = new Intent(ViewNote.this,NewNote.class);
        intent.putExtra(NOTE_ID,noteId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Animation animationRibbon = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        ribbon.startAnimation(animationRibbon);
    }

}