package com.aversoft.medicinepoint;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.aversoft.medicinepoint.model.Prescription;

public class PrePrescriptionDetailsActivity extends AppCompatActivity {

    Intent intent;
    Prescription prescription;
    TextView tvPrePresDetails;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_prescription_details);

        init();

        actionBar.setTitle("Prescription");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvPrePresDetails.setText(prescription.getPrescription());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        intent = getIntent();
        prescription = (Prescription) intent.getSerializableExtra("PreviousPrescription");
        tvPrePresDetails = findViewById(R.id.tv_pre_pres_details);
        actionBar = getSupportActionBar();
    }
}
