package com.aversoft.medicinepoint;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aversoft.medicinepoint.model.Order;
import com.aversoft.medicinepoint.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopBasedOrdersActivity extends AppCompatActivity {

    ListView lvOrder;
    Intent intent;
    String orderCode;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ArrayList<Order> allOrders;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_based_orders);

        init();

        actionBar.setTitle("Today's Order");
        actionBar.setDisplayHomeAsUpEnabled(true);

        getOrders();

        lvOrder.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return allOrders.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.seller_container, parent, false);

            TextView tvDate = convertView.findViewById(R.id.tv_seller_container_name);
            TextView tvStatus = convertView.findViewById(R.id.tv_seller_container_address);


            tvDate.setText(allOrders.get(position).getDate());
            if(allOrders.get(position).getStatus().equals("Delivered")){
                tvStatus.setTextColor(Color.parseColor("#cc1496fa"));
                tvStatus.setText(allOrders.get(position).getStatus());
            } else if(allOrders.get(position).getStatus().equals("Requested")){
                tvStatus.setTextColor(Color.parseColor("#ccfa1414"));
                tvStatus.setText(allOrders.get(position).getStatus());
            } else if(allOrders.get(position).getStatus().equals("Processing")){
                tvStatus.setTextColor(Color.parseColor("#cc14fa4e"));
                tvStatus.setText(allOrders.get(position).getStatus());
            }

            return convertView;
        }
    };

    private void getOrders() {
        reference.child("/Order List").child("/"+orderCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Order order = data.getValue(Order.class);
                    allOrders.add(order);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        lvOrder = findViewById(R.id.lv_shop_based_order);
        intent = getIntent();
        orderCode = intent.getStringExtra("orderCode");
        firebaseDatabase = FirebaseDatabase.getInstance();
        allOrders = new ArrayList<>();
        reference = firebaseDatabase.getReference("Medicine Point DB");
        actionBar = getSupportActionBar();
    }
}
