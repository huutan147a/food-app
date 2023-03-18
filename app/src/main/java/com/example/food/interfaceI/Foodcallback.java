package com.example.food.interfaceI;
import com.example.food.model.Food;
import java.util.ArrayList;

public interface Foodcallback {
    void onSuccess(ArrayList<Food> lists);
    void onError(String message);

}
