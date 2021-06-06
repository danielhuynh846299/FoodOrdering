package com.example.android.restaurant;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Payment extends Fragment {

    private SharedViewModel data;
    private Observer<List<Order>> payment;
    private DBHandler db;
    private String dola = "$";
    private TableLayout table;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("Payment onAttach");
    }

    //communicating data
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(getContext());
        System.out.println("Payment onCreate");

        data = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        payment = new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable List<Order> orders) {
                List<Order> listOrder = db.getAllOrders();
                for (Order newOrder : orders) {
                    listOrder.add(newOrder);
                    db.addOrder(newOrder);


                }
                System.out.println(listOrder.size());
                int subtotalCost = 0;
                double totalCost = 0;
                TextView subTotal = getView().findViewById(R.id.subtotalTF);
                TextView totalcost = getView().findViewById(R.id.totalTF);
                table = (TableLayout) getView().findViewById(R.id.tableLayout);

                for (Order o : listOrder) {
                    TableRow row = new TableRow(getContext());
                    TextView tv1 = new TextView(getContext());
                    tv1.setText(o.getItem().getName());
                    TextView tv2 = new TextView(getContext());
                    tv2.setText(o.getItem().getPrice());
                    TextView tv3 = new TextView(getContext());
                    tv3.setText(o.getQuantity());
                    TextView tv4 = new TextView(getContext());
                    int cost = Integer.parseInt(o.getItem().getPrice().substring(0, o.getItem().getPrice().length() - 1)) * Integer.parseInt(o.getQuantity());
                    tv4.setText(String.valueOf(cost) + dola);
                    row.addView(tv1);
                    row.addView(tv2);
                    row.addView(tv3);
                    row.addView(tv4);
                    table.addView(row);


                    subtotalCost += cost;
                    totalCost = subtotalCost * 1.1;
                    System.out.println(o.getItem().getName());

                }
                subTotal.setText(String.valueOf(subtotalCost) + dola);
                totalcost.setText(String.format("%.2f", totalCost) + dola);

                data.getOrder().removeObservers(getActivity());
            }
        };
        data.getOrder().observe(this, payment);

    }

    public Payment() {
        System.out.println("Create instanse");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("Payment onCreateView");

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("Payment onActivityCreated");
    }


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Payment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Payment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("Payment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("Payment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("Payment onDestroyView");
        //data.getOrder().removeObserver(payment);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Payment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("Payment onDetach");
    }
}
