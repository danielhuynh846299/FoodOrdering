package com.example.android.restaurant;


import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class YourOrder extends Fragment {


    private List<Order> listOrder;
    private List<Order> listOrdered;
    private ArrayList<String> listToKitchen;
    private ListView listTv;
    private ListView listOrderedTV;
    private OrderAdapter adapter;
    private Button toKitchen;
    private Button toPayment;
    private AlertDialog.Builder builder;
    private SharedViewModel model;
    private AlreadyOrderAdapter alAdapter;

    //communicating data
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listOrder = new ArrayList<>();

        //create adapter ,View model to communicate data between each fragment
        adapter = new OrderAdapter(getActivity(), listOrder);

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        final Observer<Order> orderObserve = new Observer<Order>() {
            @Override
            public void onChanged(@Nullable Order order) {
                listOrder.add(order);
                listTv = getView().findViewById(R.id.list_order);
                listTv.setAdapter(adapter);

            }
        };
        model.getSeleted().observe(this, orderObserve);
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter("receive-food"));


    }

    public YourOrder() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("YourOrder");
        View view = inflater.inflate(R.layout.fragment_your_order, container, false);
        listOrdered = new ArrayList<>();
        listOrderedTV = view.findViewById(R.id.list_ordered);
        alAdapter = new AlreadyOrderAdapter(getActivity(), listOrdered);
        listOrderedTV.setAdapter(alAdapter);
        toKitchen = view.findViewById(R.id.go_to_kitchen);
        toPayment = view.findViewById(R.id.btn_process_payment);
        //alert dialog
        builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This is a final payment,so you can not add more orders,Are you sure to pay?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        model.toPayment(listOrdered);
                        listOrder.clear();
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        // send to kitchen
         listToKitchen = new ArrayList<>();
        final EditText table = getActivity().findViewById(R.id.write_table);
        //set action
        toKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Order o : listOrder) {
                    listToKitchen.add(table.getText().toString()+" "+o.getItem().getName()+" "+"x"+o.getQuantity());
                    listOrdered.add(o);

                }
                sendBroadcast();
                listToKitchen.clear();
                listOrder.clear();
                adapter.notifyDataSetChanged();
                alAdapter.notifyDataSetChanged();

            }
        });
        toPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        return view;
    }
    //send broadcast to kitchen
    private void sendBroadcast() {
        Intent intent = new Intent();
        intent.setAction("com.example.android.restaurant");
        System.out.println(intent.getAction());
        intent.putStringArrayListExtra("list-order",listToKitchen);
        getActivity().sendBroadcast(intent);
    }
    //receive food back from my receiver
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            int foodIndex = b.getInt("food-index");
            alAdapter.setFoodIndex(foodIndex);
            System.out.println("At Res: "+foodIndex);
        }
    };

}
