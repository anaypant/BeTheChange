package com.example.btc;

public interface AdapterSelecterListener {
    void onUpvoteClick(ModelClass c, int position, String tabName);
    void onDownVoteClick(ModelClass c, int position, String tabName);
    void onCommentClick(ModelClass c, int position, String tabName);
}
