package com.tronpc.yourdiary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private static final String TITLE_KEY = "title_note";
    private static final String DATE_KEY = "date_note";
    private static final String CONTENT_KEY = "content_note";
    private static final String NOTE_ID = "note_id";

    private ArrayList<String> title;
    private ArrayList<String> date;
    private ArrayList<String> content;
    private ArrayList<String> id;
    Context context;

    public NoteAdapter(Context context ,ArrayList<String> title, ArrayList<String> date,ArrayList<String> content,ArrayList<String> id) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.context = context;
        this.id = id;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.note_card,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String setContent;
        final String note_title = title.get(position);
        final String note_date = date.get(position);
        final String note_content = content.get(position);
        final String note_id = id.get(position);


        if(note_content.length()>200){
            setContent = note_content.substring(0,200)+"...";
        }else setContent = note_content;

        holder.mTitle.setText(note_title);
        holder.mDate.setText(note_date);
        holder.mContent.setText(setContent);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewNote.class);
                intent.putExtra(TITLE_KEY,note_title).putExtra(DATE_KEY,note_date).putExtra(CONTENT_KEY,note_content).putExtra(NOTE_ID,note_id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView mTitle,mContent,mDate;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.noteTitle);
            mContent = itemView.findViewById(R.id.noteContent);
            mDate = itemView.findViewById(R.id.noteDate);
        }
    }
}
