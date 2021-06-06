package com.example.android.restaurant;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private static final String TAG = ItemAdapter.class.getSimpleName();

    private List<Item> mData;
    private LayoutInflater mInflater;
    private SharedViewModel model;
    private Context context;


    //data is passed into the constructor
    public ItemAdapter(Context context, List<Item> data, SharedViewModel model) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.model = model;


    }

    public void setData(List<Item> newData) {
        this.mData = newData;
        notifyDataSetChanged();
    }


    //inflate the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.number_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    //bind data to the TextView in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Item item = mData.get(position);
        String name = item.getName();
        holder.itemName.setText(name);
        String price = item.getPrice();
        holder.itemPrice.setText(price);
        String category = item.getCategory();
        holder.itemCategory.setText(category);
        holder.itemImage.setImageBitmap(item.getImage());

    }

    //total number of cells
    @Override
    public int getItemCount() {
        return mData.size();

    }

    //stores and recycles views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout linearLayout;
        ImageView itemImage;
        TextView itemName;
        TextView itemPrice;
        TextView itemCategory;
        Button addOrderBtn;
        Button quantityPlus;
        Button quantityMinus;
        Button okBtn;
        Button cancelBtn;
        TextView quantityDynamicTV;

        ViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.toggle_view);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemCategory = itemView.findViewById(R.id.item_category);
            addOrderBtn = itemView.findViewById(R.id.add_order);
            quantityPlus = itemView.findViewById(R.id.quantity_plus);
            quantityMinus = itemView.findViewById(R.id.quantity_minus);
            okBtn = itemView.findViewById(R.id.add_order_ok);
            cancelBtn = itemView.findViewById(R.id.add_order_cancel);
            quantityDynamicTV = itemView.findViewById(R.id.quantity_dynamicTV);
            addOrderBtn.setOnClickListener(this);
            quantityPlus.setOnClickListener(this);
            quantityMinus.setOnClickListener(this);
            okBtn.setOnClickListener(this);
            cancelBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (view == addOrderBtn) {
                System.out.println("ToggleView");
                linearLayout.setVisibility(LinearLayout.VISIBLE);


            } else if (view == quantityPlus) {

                int quantity = Integer.parseInt(quantityDynamicTV.getText().toString());
                ++quantity;
                quantityDynamicTV.setText(String.valueOf(quantity));

            } else if (view == quantityMinus) {

                int quantity = Integer.parseInt(quantityDynamicTV.getText().toString());
                if (quantity > 1) {
                    --quantity;
                    quantityDynamicTV.setText(String.valueOf(quantity));
                }

            } else if (view == okBtn) {

                linearLayout.setVisibility(LinearLayout.INVISIBLE);
                model.select(new Order(mData.get(getAdapterPosition()), quantityDynamicTV.getText().toString()));
                quantityDynamicTV.setText("1");


            } else {

                linearLayout.setVisibility(LinearLayout.INVISIBLE);
                quantityDynamicTV.setText("1");


            }

        }

    }

    //method for getting data at click position
    Item getItem(int id) {
        return mData.get(id);
    }

}
