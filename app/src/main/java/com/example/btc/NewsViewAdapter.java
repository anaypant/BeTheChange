package com.example.btc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    public static VoteInterface vI;
    int numUp = 0, numDown = 0;


    public NewsViewAdapter(Context context, ArrayList<ModelClass> modelClassArrayList, String tabName, VoteInterface vI) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
        this.tabName =tabName;
        this.vI = vI;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item,null,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {



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
        //holder.time.setText("Source: "+modelClassArrayList.get(position).getPublishedAt());
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("votes").child(tabName).child(String.valueOf(position));
        numUp = 0;
        numDown = 0;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(Integer.parseInt(Objects.requireNonNull(ds.getValue(String.class))) == 1){
                        numUp += 1;
                    }
                    else{
                        numDown += 1;
                    }
                }
                holder.downVoteCt.setText(String.valueOf(numDown));//fix here :0
                holder.upvoteCt.setText(String.valueOf(numUp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        //@TODO aiclub
        holder.content.setText(modelClassArrayList.get(position).getDescription());
        if(holder.imageView != null){
            Glide.with(context).load(modelClassArrayList.get(position).getUrlToImage()).into(holder.imageView);
        }
        
    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading,content,author;
        CardView cardView;
        ImageView imageView;
        ImageButton upVoteButton, downVoteButton;
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
            upVoteButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForColorStateLists")
                @Override
                public void onClick(View view) {
                    if(upVoteButton.getBackgroundTintList() != context.getResources().getColorStateList(R.color.up_vote_color)){
                        vI.upVoteOnClick(view, getAdapterPosition());
                    }
                    downVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dead_vote));
                    upVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.up_vote_color));

                }
            });
            downVoteButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("UseCompatLoadingForColorStateLists")
                @Override
                public void onClick(View view) {
                    if(downVoteButton.getBackgroundTintList() != context.getResources().getColorStateList(R.color.down_vote_color)){
                        vI.downVoteOnClick(view, getAdapterPosition());
                    }
                    downVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.down_vote_color));
                    upVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dead_vote));

                }
            });




        }
    }
}