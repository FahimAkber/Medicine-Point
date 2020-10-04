package com.aversoft.medicinepoint;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DailyMedicineDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvMedicine;
    EditText etMedicine;
    Button btnMedicine;
    Intent intent;
    String address, prescription;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_medicine_details);

        init();

        actionBar.setTitle("Medicine Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvMedicine.setText(prescription);

        btnMedicine.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        tvMedicine = findViewById(R.id.tv_daily_medicine_details);
        etMedicine = findViewById(R.id.et_daily_medicine_details);
        btnMedicine = findViewById(R.id.btn_daily_medicine_details);
        intent = getIntent();
        address = intent.getStringExtra("patient_address");
        prescription = intent.getStringExtra("prescription");
        actionBar = getSupportActionBar();

    }

    @Override
    public void onClick(View v) {
        if(v == btnMedicine){
            String quantity = etMedicine.getText().toString().trim();
            if(quantity.isEmpty()){
                Toast.makeText(this, "Medicine quantity is required.", Toast.LENGTH_SHORT).show();
                return;
            }
            prescription = prescription +"\n Medicine for "+quantity+" days";
            startActivity(new Intent(DailyMedicineDetailsActivity.this, SellerListActivity.class)
                        .putExtra("myMedicineList", prescription)
                        .putExtra("patient_address", address));
            finish();
        }
    }
}
