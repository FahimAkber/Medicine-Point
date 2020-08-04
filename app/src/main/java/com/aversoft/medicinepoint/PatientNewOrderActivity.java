package com.aversoft.medicinepoint;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PatientNewOrderActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    EditText etMedicineName, etMedicineQuantity;
    ListView lvMedicine;
    Button btnOk, btnConfirm;
    ArrayList<String> allMedicine;
    Intent intent;
    String patientAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_new_order);

        init();

        btnOk.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        lvMedicine.setAdapter(adapter);
        lvMedicine.setOnItemLongClickListener(this);

    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return allMedicine.size();
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
            convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
            TextView tvMedicine = convertView.findViewById(android.R.id.text1);

            tvMedicine.setText(allMedicine.get(position));

            return convertView;
        }
    };

    private void init() {
        etMedicineName = findViewById(R.id.et_patient_new_order_medicine_name);
        etMedicineQuantity = findViewById(R.id.et_patient_new_order_quantity);
        lvMedicine = findViewById(R.id.lv_patient_new_order_medicine_list);
        btnOk = findViewById(R.id.btn_patient_new_order_add_medicine);
        btnConfirm = findViewById(R.id.btn_patient_new_order_confirm_order);
        allMedicine = new ArrayList<>();
        intent = getIntent();
        patientAddress = intent.getStringExtra("patient_address");
    }

    @Override
    public void onClick(View v) {
        if(v == btnOk){
            String medName = etMedicineName.getText().toString().trim();
            String medQuantity = etMedicineQuantity.getText().toString().trim();

            if(medName.isEmpty()){
                Toast.makeText(this, "Provide Medicine Name!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(medQuantity.isEmpty()){
                Toast.makeText(this, "Provide Medicine Quantity!!", Toast.LENGTH_SHORT).show();
                return;
            }

            etMedicineQuantity.setText("");
            etMedicineName.setText("");

            String medicine = medName+" ("+medQuantity+")";
            allMedicine.add(medicine);
            adapter.notifyDataSetChanged();


        } else if(v == btnConfirm){
            if(allMedicine.size() == 0){
                Toast.makeText(this, "Add a medicine to order!!", Toast.LENGTH_SHORT).show();
                return;
            }
            String presOrder = "";
            for(int i = 0; i< allMedicine.size(); i++){
                presOrder = presOrder+allMedicine.get(i)+"\n";
            }

            startActivity(new Intent(PatientNewOrderActivity.this, SellerListActivity.class).putExtra("patient_address", patientAddress).putExtra("myMedicineList", presOrder));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        allMedicine.remove(position);
        adapter.notifyDataSetChanged();
        return false;
    }
}
