package com.example.btc;

import android.view.View;
import android.widget.ImageButton;

public interface VoteInterface {
    void upVoteOnClick(View w, int position);

    void downVoteOnClick(View w, int position);
}
