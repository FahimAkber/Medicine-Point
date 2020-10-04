package com.aversoft.medicinepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aversoft.medicinepoint.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShopOrderListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvShopOrder;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    SharedPreferences sp;
    String myCode, today;
    ProgressBar pbShopOrderList;
    LinearLayout layoutShopOderList;
    ArrayList<Order> myOrders;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order_list);

        init();

        actionBar.setTitle("Orders");
        actionBar.setDisplayHomeAsUpEnabled(true);

        layoutShopOderList.setVisibility(View.GONE);
        pbShopOrderList.setVisibility(View.VISIBLE);

        getToday();

        getOrders();

        lvShopOrder.setAdapter(adapter);
        lvShopOrder.setOnItemClickListener(this);

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
            return myOrders.size();
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

            tvDate.setText(myOrders.get(position).getDate());
            tvStatus.setText(myOrders.get(position).getStatus());

            return convertView;
        }
    };

    private void getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        today = sdf.format(Calendar.getInstance().getTime());
    }

    private void getOrders() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().substring(10, 19).equals(myCode)){
                        getDateWiseOrders(data.getKey());
                    }
                }
                pbShopOrderList.setVisibility(View.GONE);
                layoutShopOderList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDateWiseOrders(String key) {
        reference.child("/"+key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Order order = data.getValue(Order.class);
                    if(order.getDate().equals(today) && order.getStatus().equals("Requested")){
                        myOrders.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        lvShopOrder = findViewById(R.id.lv_shop_order);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/Order List");
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myCode = sp.getString("shortCode", "none");
        myOrders = new ArrayList<>();
        pbShopOrderList = findViewById(R.id.pb_shop_order_list);
        layoutShopOderList = findViewById(R.id.layout_shop_order_list);
        actionBar = getSupportActionBar();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getApplicationContext(), ShopOrderDetailsActivity.class).putExtra("order", myOrders.get(position)));
    }
}
