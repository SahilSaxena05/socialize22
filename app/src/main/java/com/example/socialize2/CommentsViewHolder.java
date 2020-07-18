package com.example.socialize2;

import android.app.Application;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
    TextView textView1,textView2;

    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void Commentsfeature(Application application,String comments,String date,String time , String username){
        textView1 = itemView.findViewById(R.id.comment_time_date);
        textView2 = itemView.findViewById(R.id.comment_username_item);
        textView1.setText(date +"-" + time);
        textView2.setText(username +" : " + comments);
    }
}
