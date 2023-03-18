package com.example.foodstore.callback;
import com.example.foodstore.model.HoaDon;


import java.util.ArrayList;

public interface HDCTcallback {
    void onSuccess(ArrayList<HoaDon> lists);
    void onError(String message);
}
