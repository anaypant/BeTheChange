package com.example.qrated.Interfaces;

import com.example.qrated.Frames.ModelNews;

import java.util.ArrayList;

public interface FirebaseNewsCallback {
    void onDBCallback(ArrayList<ModelNews> val);
}
