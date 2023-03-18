package com.example.food.interfaceI;

import com.example.food.model.ChatUserStore;

import java.util.ArrayList;

public interface Chatuserstorecallback {
    void onSuccess(ArrayList<ChatUserStore> lists);
    void onError(String message);
}
