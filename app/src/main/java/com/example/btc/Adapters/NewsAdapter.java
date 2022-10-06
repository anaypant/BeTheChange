package com.example.btc.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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
import com.example.btc.CommentActivity;
import com.example.btc.R;
import com.example.btc.CommentActivity;
import com.example.btc.Frames.ModelNews;
import com.example.btc.Interfaces.ButtonCallback;
import com.example.btc.Interfaces.FirebaseBoolCallback;
import com.example.btc.Interfaces.FirebaseNewsCallback;
import com.example.btc.Interfaces.FirebaseStringCallback;
import com.example.btc.R;
import com.example.btc.utils.firebaseUtils;

import java.util.ArrayList;
import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private Context context;
    private ArrayList<ModelNews> news;
    private String tabName;
    private ButtonCallback buttonListener;


    public NewsAdapter(Context context, ArrayList<ModelNews> news, String tabName, ButtonCallback buttonListener){
        this.context = context;
        this.news = news;
        this.tabName = tabName;
        this.buttonListener = buttonListener;

    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item,null,false);
        return new NewsHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {


        int pos = holder.getAdapterPosition();
        ModelNews current = news.get(pos);

        //update arrow colors
        firebaseUtils.checkUserStatusItem(pos, tabName, new FirebaseStringCallback() {
            @SuppressLint({"UseCompatLoadingForColorStateLists"})
            @Override
            public void onStringCallback(String s) {
                if(s.equals("like")){
                    holder.upVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.purple_200));
                    holder.downVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dead_vote));

                }
                else if(s.equals("dislike")){
                    holder.upVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dead_vote));
                    holder.downVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.purple_200));

                }
                else if (s.equals("neutral")){
                    holder.upVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dead_vote));
                    holder.downVoteButton.setBackgroundTintList(context.getResources().getColorStateList(R.color.dead_vote));
                }
            }
        });


        holder.upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                firebaseUtils.upVoteAPost(holder.getAdapterPosition(), tabName, new FirebaseBoolCallback() {
                    @Override
                    public void onBoolCallback(boolean value) {
                        if(value){
                            firebaseUtils.findNewsFromDBatPosition(pos, tabName, new FirebaseNewsCallback() {
                                @Override
                                public void onDBCallback(ArrayList<ModelNews> val) {
                                    news.set(pos, val.get(0));
                                    buttonListener.onCallback(pos);
                                }
                            });

                        }
                    }
                });

            }
        });
        holder.downVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();

                // for now, just add user upVote to comments
                firebaseUtils.downVoteAPost(holder.getAdapterPosition(), tabName, new FirebaseBoolCallback() {
                    @Override
                    public void onBoolCallback(boolean value) {
                        if(value){
                            firebaseUtils.findNewsFromDBatPosition(pos, tabName, new FirebaseNewsCallback() {
                                @Override
                                public void onDBCallback(ArrayList<ModelNews> val) {
                                    firebaseUtils.findNewsFromDBatPosition(pos, tabName, new FirebaseNewsCallback() {
                                        @Override
                                        public void onDBCallback(ArrayList<ModelNews> val) {
                                            news.set(pos, val.get(0));
                                            buttonListener.onCallback(pos);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

            }
        });

        // go to comments activity
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to comments
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, CommentActivity.class);
                        intent.putExtra("pos", String.valueOf(holder.getAdapterPosition()));
                        intent.putExtra("tab", tabName);
                        context.startActivity(intent);
                    }
                },0);
            }
        });

        // click on the url of the news for each one
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(news.get(pos).getUrl()));
                context.startActivity(intent);
            }
        });

        // add ModelNews content to views on news items
        if(Objects.equals(news.get(position).getAuthor(), "null") || news.get(position).getAuthor() == null || news.get(position).getAuthor().equals("")){
            holder.author.setText("By: Not Found");
        }
        else{
            holder.author.setText("By: " + current.getAuthor());
        }
        //Published at
        if(current.getPublishedAt() != null){
            holder.author.setText(holder.author.getText() + "        Source: " + current.getPublishedAt());
        }

        // Setting title, description, image, etc.
        holder.heading.setText(current.getTitle());
        holder.content.setText(current.getDescription());
        holder.upvoteCt.setText(current.getUpVoteCt());
        holder.downVoteCt.setText(current.getDownVoteCt());
        holder.commentCt.setText(current.getCommentCt());


        if(holder.imageView != null){
            Glide.with(context).load(current.getUrlToImage()).into(holder.imageView);
        }
    }

    public void setNews(ArrayList<ModelNews> news) {
        this.news = news;
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public static class NewsHolder extends RecyclerView.ViewHolder {
        TextView heading,content,author, upvoteCt, downVoteCt, commentCt;
        CardView cardView;
        ImageView imageView;
        ImageButton upVoteButton, downVoteButton, commentButton;
        public NewsHolder(@NonNull View itemView) {
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
            commentCt = itemView.findViewById(R.id.commentCounter);

            commentButton = itemView.findViewById(R.id.CommentButton);
        }
    }
}
