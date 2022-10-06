package com.anaypant.qrated.Interfaces;

import com.anaypant.qrated.Frames.ModelNews;

import java.util.ArrayList;

public interface FirebaseNewsCallback {
    void onDBCallback(ArrayList<ModelNews> val);
}
