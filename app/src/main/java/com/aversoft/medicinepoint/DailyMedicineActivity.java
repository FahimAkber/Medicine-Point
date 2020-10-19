package com.aversoft.medicinepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aversoft.medicinepoint.model.DailyMedicine;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DailyMedicineActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView lvDailyMedicine;
    FloatingActionButton fabDailyMedicine;
    ArrayList<DailyMedicine> dailyMedicines;
    SharedPreferences sp;
    String myCode;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Intent intent;
    String address;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_medicine);

        init();

        actionBar.setTitle("Daily Medicine");
        actionBar.setDisplayHomeAsUpEnabled(true);

        getValue();

        lvDailyMedicine.setAdapter(adapter);

        fabDailyMedicine.setOnClickListener(this);

        lvDailyMedicine.setOnItemClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getValue() {
        dailyMedicines.clear();
        reference.child("/"+myCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    DailyMedicine medicine = data.getValue(DailyMedicine.class);
                    dailyMedicines.add(medicine);
                    adapter.notifyDataSetChanged();
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
            return dailyMedicines.size();
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
            convertView = getLayoutInflater().inflate(R.layout.prescription_container, parent, false);
            TextView tvName = convertView.findViewById(R.id.tv_pres_container_date);
            tvName.setText(dailyMedicines.get(position).getSetName());
            return convertView;
        }
    };

    private void init() {
        lvDailyMedicine = findViewById(R.id.lv_daily_medicine);
        fabDailyMedicine = findViewById(R.id.fab_daily_medicine);
        dailyMedicines = new ArrayList<>();
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        intent = getIntent();
        address = intent.getStringExtra("patient_address");
        myCode = sp.getString("myId", "none");
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/Daily Medicine/");
        actionBar = getSupportActionBar();
    }

    @Override
    public void onClick(View v) {
        if(v == fabDailyMedicine){
            startActivity(new Intent(DailyMedicineActivity.this, DailyMedicineListAddActivity.class));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(DailyMedicineActivity.this, DailyMedicineDetailsActivity.class).putExtra("patient_address", address).putExtra("prescription", dailyMedicines.get(position).getMedicineList()));
    }
}
