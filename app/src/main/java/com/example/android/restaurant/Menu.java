package com.example.android.restaurant;


import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.List;


/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *{@link Menu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Menu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Menu extends Fragment {

    ItemAdapter adapter;
    TabLayout tabLayout;
    List<Item> data;
    List<Item> newData;
    RecyclerView recyclerView;
    DBHandler db;
    int numberOfColumn = 3;
    private SharedViewModel model;
    ProgressDialog dialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);


    }

    public Menu() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        //set up RecyclerView
        System.out.println("Menu");
        db = new DBHandler(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_item);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumn));

        if (data == null) {
            new RetrieveMenuOnlineTask().execute("https://firebasestorage.googleapis.com/v0/b/restaurantproject-b6440.appspot.com/o/data.json?alt=media&token=964bcdad-cc63-4440-afdc-7213124e7fef");

        } else {
            adapter = new ItemAdapter(this.getContext(), data, model);
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                new FilterTask().execute(tab.getText().toString());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class FilterTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            newData = new ArrayList<>();
            switch (strings[0]) {
                case "FAVOURITE":
                    for (Item item : data) {
                        if (item.getFavourite().equals("true")) {
                            newData.add(item);
                        }
                    }
                    break;
                case "BURGER":
                    for (Item item : data) {
                        if (item.getCategory().equals("burger")) {
                            newData.add(item);
                        }
                    }
                    break;
                case "PIZZA":
                    for (Item item : data) {
                        if (item.getCategory().equals("pizza")) {
                            newData.add(item);
                        }
                    }
                    break;
                case "SUSHI":
                    for (Item item : data) {
                        if (item.getCategory().equals("sushi")) {
                            newData.add(item);
                        }
                    }
                    break;
                case "DRINKS":
                    for (Item item : data) {
                        if (item.getCategory().equals("drinks")) {
                            newData.add(item);
                        }
                    }
                    break;
                default:
                    newData = data;
                    break;

            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            adapter.setData(newData);
        }
    }


    class RetrieveMenuOnlineTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected Integer doInBackground(String... urlStrs) {
            try {
                java.net.URL url = new java.net.URL(urlStrs[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONObject jsonObject = new JSONObject(buffer.toString());
                JSONArray array = jsonObject.getJSONArray("menu");


                data = new ArrayList<>();
                db.deleteAll();
                for (int i = 0; i < array.length(); ++i) {
                    try {


                        java.net.URL url1 = new java.net.URL(array.getJSONObject(i).getString("image"));
                        HttpURLConnection connection2 = (HttpURLConnection) url1.openConnection();
                        connection2.setDoInput(true);
                        connection2.connect();
                        InputStream input = connection2.getInputStream();

                        Bitmap img = BitmapFactory.decodeStream(input);
                        Bitmap resizeBitmap = Bitmap.createScaledBitmap(img, 100, 100, false);
                        Item item = new Item(array.getJSONObject(i).get("name").toString(), array.getJSONObject(i).get("price").toString(), array.getJSONObject(i).get("favourite").toString(), array.getJSONObject(i).get("category").toString(), resizeBitmap);
                        data.add(item);
                        db.addMenu(item);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


                return 0;

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading menu...");
            dialog.setIndeterminate(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Integer res) {
            dialog.dismiss();
            adapter = new ItemAdapter(getContext(), data, model);
            recyclerView.setAdapter(adapter);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
