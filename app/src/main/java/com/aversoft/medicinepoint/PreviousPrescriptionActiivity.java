package com.aversoft.medicinepoint;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.aversoft.medicinepoint.model.Prescription;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreviousPrescriptionActiivity extends AppCompatActivity {

    TextView tvPrePresOne;
    ListView lvPrePresContainer;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ActionBar actionBar;
    Intent intent;
    String prescriptionId;
    ArrayList<Prescription> allPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_prescription_actiivity);

        init();

        actionBar.setTitle("Previous Prescriptions");
        actionBar.setDisplayHomeAsUpEnabled(true);

        getValue();

        lvPrePresContainer.setAdapter(adapter);
        lvPrePresContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PreviousPrescriptionActiivity.this, PrePrescriptionDetailsActivity.class);
                intent.putExtra("PreviousPrescription", allPrescription.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return allPrescription.size();
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

            TextView tvDate = convertView.findViewById(R.id.tv_pres_container_date);
            tvDate.setText(allPrescription.get(position).getDate());

            return convertView;
        }
    };

    private void getValue() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Prescription prescription = data.getValue(Prescription.class);
                    allPrescription.add(prescription);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        tvPrePresOne = findViewById(R.id.tv_pre_pres_one);
        lvPrePresContainer = findViewById(R.id.lv_pre_pres_container);
        intent = getIntent();
        prescriptionId = intent.getStringExtra("prescriptionId");
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/Prescription/"+prescriptionId);
        actionBar = getSupportActionBar();
        allPrescription = new ArrayList<>();
    }


}
