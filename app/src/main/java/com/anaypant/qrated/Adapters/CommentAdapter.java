package com.anaypant.qrated.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anaypant.qrated.R;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.commentHolder> {

    Context context;
    List<Map> modelClasses;
    String tabName;


    public CommentAdapter(Context c, List<Map> L, String tabName){
        this.context = c;
        this.modelClasses = L;
        this.tabName = tabName;
    }
    @NonNull
    @Override
    public commentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new commentHolder(LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull commentHolder holder, int position) {
        // map id of uid key to a user name
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
