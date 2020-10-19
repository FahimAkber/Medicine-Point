package com.aversoft.medicinepoint;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aversoft.medicinepoint.model.Payment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShopRecordsActivity extends AppCompatActivity {

    TextView tvMonth, tvToday;
    ListView lvToday;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    SharedPreferences sp;
    String myId;
    ArrayList<Payment> allPayment;
    int monthPrice, todayPrice;
    String month,  today;
    LinearLayout layout;
    ProgressBar pbRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_records);

        init();

        layout.setVisibility(View.GONE);
        pbRecord.setVisibility(View.VISIBLE);

        getDateInfo();
        
        getPayments();

        lvToday.setAdapter(adapter);

    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return allPayment.size();
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
            convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView tvPrice = convertView.findViewById(android.R.id.text1);
            tvPrice.setText(allPayment.get(position).getPrice()+"");
            return convertView ;
        }
    };

    private void getDateInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        month = sdf.format(Calendar.getInstance().getTime());
        SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd yyyy");
        today = sdf1.format(Calendar.getInstance().getTime());
    }

    private void getPayments() {
        reference.child(myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Payment payment = data.getValue(Payment.class);
                    if(payment.getMonth().equals(month)){
                        monthPrice += payment.getPrice();
                        if(payment.getDate().equals(today)){
                            todayPrice += payment.getPrice();
                            allPayment.add(payment);
                        }
                    }
                }
                pbRecord.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                tvMonth.setText(monthPrice+"");
                tvToday.setText(todayPrice+"");
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        tvMonth = findViewById(R.id.tv_month_record);
        tvToday = findViewById(R.id.tv_today_price);
        lvToday = findViewById(R.id.lv_today_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/Payment/");
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myId = sp.getString("myId", "none");
        allPayment = new ArrayList<>();
        layout = findViewById(R.id.layout_record);
        pbRecord = findViewById(R.id.pb_record);
    }
}
