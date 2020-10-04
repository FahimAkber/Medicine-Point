package com.aversoft.medicinepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.DailyMedicine;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class DailyMedicineListAddActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etMedicine;
    TextView tvMedicine;
    Button btnMedicine, btnAddToList;
    String medicineList="";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    SharedPreferences sp;
    String myCode;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_medicine_list_add);

        init();

        actionBar.setTitle("Add List");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnMedicine.setOnClickListener(this);
        btnAddToList.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        etName = findViewById(R.id.et_add_daily_medicine_name);
        etMedicine = findViewById(R.id.et_add_daily_medicine);
        tvMedicine = findViewById(R.id.tv_add_daily_medicine);
        btnMedicine = findViewById(R.id.btn_add_daily_medicine);
        btnAddToList = findViewById(R.id.btn_add_daily_medicine_list);
        firebaseDatabase = FirebaseDatabase.getInstance();
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myCode = sp.getString("myId", "none");
        reference = firebaseDatabase.getReference("Medicine Point DB/Daily Medicine");
        actionBar = getSupportActionBar();
    }

    @Override
    public void onClick(View v) {
        if(v == btnMedicine){
            String medicine = etMedicine.getText().toString().trim();
            etMedicine.setText(" ");
            if(medicine.isEmpty()){
                Toast.makeText(this, "Medicine name is required.", Toast.LENGTH_SHORT).show();
                return;
            }

            medicineList = medicineList + medicine+"\n";
            tvMedicine.setText(medicineList);

        }
        if(v == btnAddToList){
            String name = etName.getText().toString().trim();
            if(name.isEmpty()){
                Toast.makeText(this, "Set name is required.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(medicineList.isEmpty()){
                Toast.makeText(this, "Add atleast one medicine to the list.", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference ref =  reference.child('/'+myCode).push();
            DailyMedicine medicine = new DailyMedicine(name, medicineList, ref.getKey(), myCode);
            ref.setValue(medicine);

            finish();

        }
    }
}
