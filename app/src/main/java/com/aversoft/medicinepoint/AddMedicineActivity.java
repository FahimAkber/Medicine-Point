package com.aversoft.medicinepoint;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.Medicine;

import java.io.Serializable;

public class AddMedicineActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    EditText etMedicineName;
    CheckBox cbMorning, cbAfterNoon, cbEvening, cbNight;
    RadioGroup rgPeriod;
    RadioButton rbBefore, rbAfter;
    ActionBar actionBar;
    Button btnAddMedicine;
    int morning, afternoon, evening, night, before, after;
    String period;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        init();
        actionBar.setTitle("Add Medicine");
        actionBar.setDisplayHomeAsUpEnabled(true);
        cbMorning.setOnCheckedChangeListener(this);
        cbAfterNoon.setOnCheckedChangeListener(this);
        cbEvening.setOnCheckedChangeListener(this);
        cbNight.setOnCheckedChangeListener(this);
        rgPeriod.setOnCheckedChangeListener(this);
        btnAddMedicine.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        etMedicineName = findViewById(R.id.et_add_medicine_name);
        cbMorning = findViewById(R.id.cb_morning);
        cbAfterNoon = findViewById(R.id.cb_afternooon);
        cbEvening = findViewById(R.id.cb_Evening);
        cbNight = findViewById(R.id.cb_Night);
        rgPeriod = findViewById(R.id.rg_timePeriod);
        rbBefore = findViewById(R.id.rb_before_meal);
        rbAfter = findViewById(R.id.rb_after_meal);
        actionBar = getSupportActionBar();
        btnAddMedicine = findViewById(R.id.btn_add_medicine);
    }

    @Override
    public void onClick(View v) {
        if(v == btnAddMedicine){
            String medName = etMedicineName.getText().toString().trim();
            if(before == 0 && after == 1){
                period = "after";
            }else{
                period = "before";
            }
            if(medName.isEmpty()){
                Toast.makeText(this, "Provide Medicine Name", Toast.LENGTH_LONG).show();
                return;
            }
            Medicine medicine = new Medicine(medName, morning, afternoon, evening, night, period);
            Intent intent = new Intent();
            intent.putExtra("medicine", medicine);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView == cbMorning){
            morning = (cbMorning.isChecked()? 1:0);
        }
        if(buttonView == cbAfterNoon){
            afternoon = (cbAfterNoon.isChecked()? 1:0);
        }
        if(buttonView == cbEvening){
            evening = (cbEvening.isChecked()? 1:0);
        }
        if(buttonView == cbNight){
            night = (cbNight.isChecked()? 1:0);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.rb_after_meal){
            after = 1;
            before = 0;
        } else{
            before = 1;
            after = 0;
        }
    }
}
