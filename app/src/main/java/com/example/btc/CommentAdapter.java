package com.example.btc;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.hash.HashingOutputStream;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.commentHolder> {

    Context context;
    List<Map> modelClasses;
    String tabName;

    public CommentAdapter(Context c, List<Map> L, String tabName){
        this.context = c;
        this.modelClasses = L;
    }
    @NonNull
    @Override
    public commentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new commentHolder(LayoutInflater.from(context).inflate(R.layout.row_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull commentHolder holder, int position) {
        // map id of uid key to a user name
        System.out.println("position    "+ position);
        holder.userName.setText(String.valueOf(modelClasses.get(position).get("user")));
        holder.commentContent.setText(String.valueOf(modelClasses.get(position).get("comment")));
    }
    public void setModelClasses(List<Map> newComments){this.modelClasses = newComments;}
    @Override
    public int getItemCount() {
        return modelClasses.size();
    }

    public class commentHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView commentContent;
        public commentHolder(@NonNull View itemView) {
            super(itemView);
            commentContent = itemView.findViewById(R.id.commentContentField);
            userName = itemView.findViewById(R.id.CommentUserNameField);
        }
    }
}
