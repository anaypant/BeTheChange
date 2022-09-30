package com.example.btc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.ViewHolder>{
    public static Context context;
    ArrayList<ModelClass> modelClassArrayList;
    private String tabName;
    //public static VoteInterface vI;
    private AdapterSelecterListener selecterListener;
    int numUp, numDown;


    public NewsViewAdapter(Context context, ArrayList<ModelClass> modelClassArrayList, String tabName, AdapterSelecterListener selecterListener) {
        this.selecterListener = selecterListener;
        NewsViewAdapter.context = context;
        this.modelClassArrayList = modelClassArrayList;
        this.tabName =tabName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item,null,false);
        return new ViewHolder(view);
    }
    public void setTabName(String name){
        this.tabName = name;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        // check if current user liked post
        //if so, update color buttons
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("articles").child(tabName).child(String.valueOf(position)).child("votes");

        holder.upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean found = false;
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid() ) && ds.getValue(String.class) == "1"){
                                found = true;
                            }
                        }
                        if(!found){
                            selecterListener.onUpvoteClick(modelClassArrayList.get(position), position, tabName);
                            holder.upvoteCt.setText(modelClassArrayList.get(position).getUpvotect());
                            holder.downVoteCt.setText(modelClassArrayList.get(position).getDownvotect());
                        }
                        else{
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });

        holder.downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean found = false;
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid() ) && ds.getValue(String.class) == "-1"){
                                found = true;
                            }
                        }
                        if(!found){
                            selecterListener.onDownVoteClick(modelClassArrayList.get(position), position, tabName);
                            holder.upvoteCt.setText(modelClassArrayList.get(position).getUpvotect());
                            holder.downVoteCt.setText(modelClassArrayList.get(position).getDownvotect());
                        }
                        else{
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecterListener.onCommentClick(modelClassArrayList.get(position), position, tabName);
            }
        });



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(modelClassArrayList.get(position).getUrl()));
                context.startActivity(intent);
            }
        });
        for (int z = 0; z < modelClassArrayList.size();z++){
            String content = modelClassArrayList.get(z).getDescription();
            String head = modelClassArrayList.get(z).getTitle();
            String urltoImg = modelClassArrayList.get(z).getUrlToImage();
            if(content == null || head == null || urltoImg==null || content.equals("")){
                modelClassArrayList.remove(z);
                z--;
            }
        }
        if(Objects.equals(modelClassArrayList.get(position).getAuthor(), "null") || modelClassArrayList.get(position).getAuthor() == null){
            holder.author.setText("By: Unknown");
        }
        else{
            holder.author.setText("By: " + modelClassArrayList.get(position).getAuthor());
        }
        if(modelClassArrayList.get(position).getPublishedAt() != null){
            holder.author.setText(holder.author.getText() + "        Source: " + modelClassArrayList.get(position).getPublishedAt());
        }
        holder.heading.setText(modelClassArrayList.get(position).getTitle());
        //@TODO aiclub
        holder.content.setText(modelClassArrayList.get(position).getDescription());
        if(holder.imageView != null){
            Glide.with(context).load(modelClassArrayList.get(position).getUrlToImage()).into(holder.imageView);
        }
        // set upvotes and downvotes
        numUp = 0;
        numDown = 0;

        holder.upvoteCt.setText(modelClassArrayList.get(position).getUpvotect());
        holder.downVoteCt.setText(modelClassArrayList.get(position).getDownvotect());


    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading,content,author;
        CardView cardView;
        ImageView imageView;
        ImageButton upVoteButton, downVoteButton, commentButton;
        TextView upvoteCt, downVoteCt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.NewsHeading);
            content = itemView.findViewById(R.id.NewsContent);
            author = itemView.findViewById(R.id.NewsAuthor);
            imageView = itemView.findViewById(R.id.NewsImageView);
            cardView = itemView.findViewById(R.id.NewsCardView);
            upVoteButton = itemView.findViewById(R.id.UpVote);
            downVoteButton = itemView.findViewById(R.id.DownVote);
            upvoteCt = itemView.findViewById(R.id.upVoteCounter);
            downVoteCt = itemView.findViewById(R.id.downVoteCounter);
            commentButton = itemView.findViewById(R.id.CommentButton);





        }
    }
}