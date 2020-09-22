package com.aversoft.medicinepoint;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import com.aversoft.medicinepoint.model.Order;
import com.aversoft.medicinepoint.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SellerListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Intent intent;
    EditText etSeller;
    ListView lvSeller;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String myMedicineList, patientAddress;
    ArrayList<User> allSeller;
    SharedPreferences sp;
    String myCode, myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_list);

        init();

        getSellers();

        lvSeller.setAdapter(adapter);

        etSeller.addTextChangedListener(new TextWatcher() {
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

        lvSeller.setOnItemClickListener(this);

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
                    if(user.getAddress().equals(patientAddress)){
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
                    if(user.getAddress().equals(patientAddress)){
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
        intent = getIntent();
        myMedicineList = intent.getStringExtra("myMedicineList");
        patientAddress = intent.getStringExtra("patient_address");
        etSeller = findViewById(R.id.et_seller_list);
        lvSeller = findViewById(R.id.lv_seller_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB");
        allSeller = new ArrayList<>();
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myCode = sp.getString("shortCode", "none");
        myId = sp.getString("myId", "none");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = allSeller.get(position);
        final String orderCode = myCode+"_"+user.getShortCode();

        new AlertDialog.Builder(SellerListActivity.this)
                .setTitle("Confirm Order")
                .setMessage(myMedicineList)
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                        DatabaseReference pushRef = reference.child("Order List").child("/" + orderCode).push();
                        Order order = new Order(orderCode, pushRef.getKey(), myId, myMedicineList, "Requested", sdf.format(calendar.getTime()));
                        pushRef.setValue(order);
                        startActivity(new Intent(SellerListActivity.this, HomeActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
