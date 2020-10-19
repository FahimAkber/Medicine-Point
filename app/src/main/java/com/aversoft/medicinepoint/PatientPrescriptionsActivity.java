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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aversoft.medicinepoint.model.Prescription;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientPrescriptionsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ListView lvPatientPresList;
    ProgressBar pbPatientPres;
    SharedPreferences sp;
    String myCode;
    Intent intent;
    String address;
    ArrayList<Prescription> myPrescription;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescriptions);

        init();

        actionBar.setTitle("My Prescriptions");
        actionBar.setDisplayHomeAsUpEnabled(true);

        pbPatientPres.setVisibility(View.VISIBLE);
        lvPatientPresList.setVisibility(View.GONE);

        checkPatientValidPress();

        lvPatientPresList.setAdapter(adapter);

        lvPatientPresList.setOnItemClickListener(this);

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
            return myPrescription.size();
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
            TextView tvDoctor = convertView.findViewById(R.id.tv_seller_container_name);
            TextView tvDate = convertView.findViewById(R.id.tv_seller_container_address);

            tvDoctor.setText(myPrescription.get(position).getDate());
            return convertView;
        }
    };

    private void checkPatientValidPress() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getKey().substring(10, 19).equals(myCode)){
                        reference.child("/"+data.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                for(DataSnapshot data1 : dataSnapshot1.getChildren()){
                                    Prescription prescription = data1.getValue(Prescription.class);
                                    myPrescription.add(prescription);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        lvPatientPresList.setVisibility(View.VISIBLE);
                        pbPatientPres.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/Prescription");
        lvPatientPresList = findViewById(R.id.lv_patient_pres_list);
        pbPatientPres = findViewById(R.id.pb_patient_pres);
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myCode = sp.getString("shortCode", "none");
        myPrescription = new ArrayList<>();
        intent = getIntent();
        address = intent.getStringExtra("patient_address");
        actionBar = getSupportActionBar();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(PatientPrescriptionsActivity.this, PatientPrescriptionDetailsActivity.class);
        intent.putExtra("prescription", myPrescription.get(position).getPrescription());
        intent.putExtra("patient_address", address);
        startActivity(intent);
    }
}
