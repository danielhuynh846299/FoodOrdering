package com.example.android.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlreadyOrderAdapter extends ArrayAdapter<Order> {
    private List<Order> orders;
    List<Integer> foodIndex;

    public AlreadyOrderAdapter(Context ct, List<Order> orders) {
        super(ct, 0, orders);
        this.orders = orders;
        foodIndex = new ArrayList<>();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ordered, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.tv_ordered_name);
        TextView tvStatus = convertView.findViewById(R.id.tv__ordered_status);
        tvName.setText(order.getItem().getName() + " " + "x" + order.getQuantity());

            if (foodIndex.contains(position)) {
                tvStatus.setText("Ready to serve");
            } else {
                tvStatus.setText("Processing");
            }

        return convertView;
    }

    public void setFoodIndex(int foodIndex) {
        this.foodIndex.add(foodIndex);
        AlreadyOrderAdapter.this.notifyDataSetChanged();

    }
}
