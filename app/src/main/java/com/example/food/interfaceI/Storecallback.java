package com.example.food.interfaceI;






import com.example.food.model.Store;

import java.util.ArrayList;

public interface Storecallback {
    void onSuccess(ArrayList<Store> lists);
    void onError(String message);
}
