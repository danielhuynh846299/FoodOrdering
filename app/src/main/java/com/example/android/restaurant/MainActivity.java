package com.example.android.restaurant;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MainActivity extends AppCompatActivity {
    LinearLayout setTable;
    TabLayout tabLayout;
    Button btnSetTable;
    Button okWriteTable;
    EditText writeTable;
    TextView displayTable;
    Button callStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the ViewPager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //set icon for tabs
        setUpViewPager(viewPager);


        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        //function on top bar

        setTable = (LinearLayout) findViewById(R.id.set_table);
        btnSetTable = (Button) findViewById(R.id.btn_set_table);
        okWriteTable = (Button) findViewById(R.id.ok_write_table);
        writeTable = (EditText) findViewById(R.id.write_table);
        displayTable = (TextView) findViewById(R.id.display_table);
        callStaff = (Button) findViewById(R.id.btn_call_staff);


        //set action
        btnSetTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTable.setVisibility(LinearLayout.VISIBLE);
            }
        });
        okWriteTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTable.setVisibility(LinearLayout.GONE);
                displayTable.setText("TABLE: "+ writeTable.getText().toString());
            }
        });
        callStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBroadcast(writeTable.getText().toString());
            }
        });



    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.mipmap.menu);
        tabLayout.getTabAt(1).setIcon(R.mipmap.your_order);
        tabLayout.getTabAt(2).setIcon(R.mipmap.payment);
    }

    //set view pager
    private void setUpViewPager(ViewPager viewPager) {
        RestaurantFragmentPagerAdapter adapter = new RestaurantFragmentPagerAdapter(this, getSupportFragmentManager());
        adapter.addFrag(new Menu(), "Menu");
        adapter.addFrag(new YourOrder(), "YourOrder");
        adapter.addFrag(new Payment(), "Payment");
        viewPager.setAdapter(adapter);
    }
    //send broadcast call staff
    private void sendBroadcast(String table) {
        Intent intent = new Intent();
        intent.setAction("com.example.android.restaurant_call");
        intent.putExtra("CALL",table);
        sendBroadcast(intent);
    }
}
