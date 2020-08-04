package com.aversoft.medicinepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientOrderListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText etShop;
    ListView lvShop;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Intent intent;
    String address;
    ArrayList<User> allSeller;
    SharedPreferences sp;
    String myCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_order_list);

        init();

        getSellers();

        lvShop.setAdapter(adapter);

        etShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    getYourShop(s.toString());
                }
            }
        });

        lvShop.setOnItemClickListener(this);

    }

    private void getYourShop(final String shop) {
        allSeller.clear();
        adapter.notifyDataSetChanged();

        reference.child("/User").child("/Saller").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allSeller.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    if(user.getAddress().equals(address)){
                        if(user.getName().toLowerCase().startsWith(shop.toLowerCase())){
                            allSeller.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return allSeller.size();
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
            TextView tvName = convertView.findViewById(R.id.tv_seller_container_name);
            TextView tvAddress = convertView.findViewById(R.id.tv_seller_container_address);

            tvName.setText(allSeller.get(position).getName());
            tvAddress.setText(allSeller.get(position).getAddress());

            return convertView;
        }
    };

    private void getSellers() {
        reference.child("/User").child("/Saller").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    if(user.getAddress().equals(address)){
                        allSeller.add(user);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        etShop = findViewById(R.id.et_order_list);
        lvShop = findViewById(R.id.lv_order_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB");
        intent = getIntent();
        allSeller = new ArrayList<>();
        address = intent.getStringExtra("patient_address");
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myCode = sp.getString("shortCode", "none");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = allSeller.get(position);
        String orderCode = myCode+"_"+user.getShortCode();
        Toast.makeText(this, orderCode, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(PatientOrderListActivity.this, ShopBasedOrdersActivity.class).putExtra("orderCode", orderCode));
    }
}
