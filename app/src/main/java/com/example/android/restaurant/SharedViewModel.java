package com.example.android.restaurant;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Order> selected = new MutableLiveData<>();
    private final MutableLiveData<List<Order>> processPayment = new MutableLiveData<>();


    //data from menu to youroder
    public void select(Order order) {
        selected.setValue(order);
    }

    public LiveData<Order> getSeleted() {
        return selected;
    }

    //data from yourorder to payment

    public void toPayment(List<Order> orders) {
        processPayment.setValue(orders);
    }

    public LiveData<List<Order>> getOrder() {
        return processPayment;
    }


}
