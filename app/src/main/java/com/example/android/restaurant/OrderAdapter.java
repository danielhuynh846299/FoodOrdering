package com.example.android.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {

    private List<Order> orders;


    public OrderAdapter(Context ct, List<Order> orders) {
        super(ct, 0, orders);
        this.orders = orders;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order, parent, false);
        }
        TextView tvName = convertView.findViewById(R.id.tv_order_name);
        Button btnCancel = convertView.findViewById(R.id.btn_cancel_order);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.remove(position);
                OrderAdapter.this.notifyDataSetChanged();
            }
        });
        tvName.setText(order.getItem().getName() + "  " + "x" + order.getQuantity());
        return convertView;
    }
}
