package com.example.btc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    Context context;
    ArrayList<ModelClass> modelClassArrayList;
    private ViewHolder holder;
    private int position;
    private String tabName;

    public NewsViewAdapter(Context context, ArrayList<ModelClass> modelClassArrayList, String tabName) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
        this.tabName =tabName;
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

        this.holder = holder;
        this.position = position;
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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(tabName);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.getKey() == String.valueOf(position)){
                        modelClassArrayList.get(position).setDownVotes(ds.getValue(ModelClass.class).getDownVotes());
                        modelClassArrayList.get(position).setUpVotes(ds.getValue(ModelClass.class).getUpVotes());
                    }
                }
                holder.upVoteButton.setText(modelClassArrayList.get(position).getUpVotes());
                holder.downVoteButton.setText(modelClassArrayList.get(position).getDownVotes());
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
        TextView heading,content,author,category;//time;
        CardView cardView;
        ImageView imageView;
        Button upVoteButton, downVoteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.NewsHeading);
            content = itemView.findViewById(R.id.NewsContent);
            author = itemView.findViewById(R.id.NewsAuthor);
            imageView = itemView.findViewById(R.id.NewsImageView);
            cardView = itemView.findViewById(R.id.NewsCardView);
            upVoteButton = itemView.findViewById(R.id.UpVote);
            downVoteButton = itemView.findViewById(R.id.DownVote);
        }
    }
}
