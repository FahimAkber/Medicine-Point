package com.aversoft.medicinepoint;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PatientPrescriptionDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvPrescription;
    Button btnPrescription;
    Intent intent;
    String prescription, address;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescription_details);

        init();

        actionBar.setTitle("Prescription");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvPrescription.setText(""+prescription);
        btnPrescription.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        tvPrescription = findViewById(R.id.tv_patient_pres_details);
        btnPrescription = findViewById(R.id.btn_patient_pres_details);
        intent = getIntent();
        prescription = intent.getStringExtra("prescription");
        address = intent.getStringExtra("patient_address");
        actionBar = getSupportActionBar();
    }

    @Override
    public void onClick(View v) {
        if(v == btnPrescription){
            startActivity(new Intent(PatientPrescriptionDetailsActivity.this, SellerListActivity.class)
                        .putExtra("myMedicineList", prescription)
                        .putExtra("patient_address", address));
            finish();
        }
    }
}
