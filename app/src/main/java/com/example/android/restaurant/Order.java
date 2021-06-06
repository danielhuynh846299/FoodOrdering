package com.example.android.restaurant;

import java.io.Serializable;

public class Order implements Serializable {
    private Item item;
    private String quantity;

    public Order(Item item, String quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public Item getItem() {
        return item;
    }

    public String getQuantity() {
        return quantity;
    }
}
