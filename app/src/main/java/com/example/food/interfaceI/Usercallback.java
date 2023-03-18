package com.example.food.interfaceI;
import com.example.food.model.User;

import java.util.ArrayList;

public interface Usercallback {
    void onSuccess(ArrayList<User> lists);
    void onError(String message);
}
