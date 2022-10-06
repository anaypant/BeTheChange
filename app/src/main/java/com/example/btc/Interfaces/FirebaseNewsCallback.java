package com.example.btc.Interfaces;

import com.example.btc.Frames.ModelNews;

import java.util.ArrayList;

public interface FirebaseNewsCallback {
    void onDBCallback(ArrayList<ModelNews> val);
}
