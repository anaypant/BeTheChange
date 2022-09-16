package com.example.btc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.ViewHolder>{
    Context context;
    ArrayList<ModelClass> modelClassArrayList;
    private ViewHolder holder;
    private int position;

    public NewsViewAdapter(Context context, ArrayList<ModelClass> modelClassArrayList) {
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;
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
//                Intent intent = new Intent(context,webView.class);
//                intent.putExtra("url",modelClassArrayList.get(position).getUrl());
//                context.startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(modelClassArrayList.get(position).getUrl()));
                context.startActivity(intent);
                //redirect to URL in browser
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
        holder.time.setText("Source: "+modelClassArrayList.get(position).getPublishedAt());
        holder.author.setText("By: "+modelClassArrayList.get(position).getAuthor());
        holder.heading.setText(modelClassArrayList.get(position).getTitle());
        //content
        // get content
        //feed into ai
        //get output
        //set text to output
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
        TextView heading,content,author,category,time;
        CardView cardView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.NewsHeading);
            content = itemView.findViewById(R.id.NewsContent);
            author = itemView.findViewById(R.id.NewsAuthor);
            time = itemView.findViewById(R.id.NewsPublished);
            imageView = itemView.findViewById(R.id.NewsImageView);
            cardView = itemView.findViewById(R.id.NewsCardView);
        }
    }
}
