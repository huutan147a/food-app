package com.example.food.interfaceI;
import com.example.food.model.HoaDon;


import java.util.ArrayList;

public interface HoaDoncallback {
    void onSuccess(ArrayList<HoaDon> lists);
    void onError(String message);
}
