package com.aversoft.medicinepoint;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.Medicine;
import com.aversoft.medicinepoint.model.Prescription;
import com.aversoft.medicinepoint.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class  DoctorHomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvDate, tvDetails;
    Button btnMedicineAdd, btnPrescribe;
    ListView lvMedicineList;
    ActionBar actionBar;
    Intent intent;
    User user;
    Calendar calendar;
    ArrayList<Medicine> presMedicine;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    SharedPreferences sp;
    int dateCount = 0;
    String myCode;
    String userInfo;
    String prescriptionId;

    private static final int RQST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        init();

        actionBar.setTitle("Prescribe");
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        tvDetails.setText(userInfo);
        tvDate.setOnClickListener(this);
        btnMedicineAdd.setOnClickListener(this);
        lvMedicineList.setAdapter(adapter);
        btnPrescribe.setOnClickListener(this);

    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return presMedicine.size();
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

            convertView = getLayoutInflater().inflate(R.layout.medicine_container, parent, false);

            TextView tvMedName = convertView.findViewById(R.id.tv_layout_med_name);
            TextView tvMorning = convertView.findViewById(R.id.tv_layout_morning);
            TextView tvAfternoon = convertView.findViewById(R.id.tv_layout_afternoon);
            TextView tvEvening = convertView.findViewById(R.id.tv_layout_evening);
            TextView tvNight = convertView.findViewById(R.id.tv_layout_night);
            TextView tvPeriod = convertView.findViewById(R.id.tv_layout_period);

            tvMedName.setText(presMedicine.get(position).getMedName());
            tvMorning.setText(presMedicine.get(position).getMorning()+"");
            tvAfternoon.setText(presMedicine.get(position).getAfternoon()+"");
            tvEvening.setText(presMedicine.get(position).getEvening()+"");
            tvNight.setText(presMedicine.get(position).getNight()+"");
            tvPeriod.setText(presMedicine.get(position).getPeriod());


            return convertView;
        }
    };

    private void init() {
        tvDate = findViewById(R.id.tv_doctor_home_date);
        btnMedicineAdd = findViewById(R.id.btn_DoctorHome_add_medicine);
        btnPrescribe = findViewById(R.id.btn_DoctorHOme_Prescribe);
        lvMedicineList = findViewById(R.id.lv_DoctorHome_MedicineList);
        actionBar = getSupportActionBar();
        calendar = Calendar.getInstance();
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        tvDetails = findViewById(R.id.tv_doctor_home_details);
        presMedicine = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/Prescription");
        userInfo = "Name: "+user.getName()+" Age: "+user.getAge()+" Gender: "+user.getGender();
        myCode = sp.getString("shortCode", "none");
        prescriptionId =  myCode + "_" + user.getShortCode();
    }

    @Override
    public void onClick(View v) {
        if (v == tvDate) {
            dateCount = 1;
            DatePickerDialog.OnDateSetListener ods = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String format = "dd MMM yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    tvDate.setText(sdf.format(calendar.getTime()));

                }
            };
            DatePickerDialog dpd = new DatePickerDialog(DoctorHomeActivity.this, ods, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        }

        if (v == btnMedicineAdd) {
            Intent intent = new Intent(DoctorHomeActivity.this, AddMedicineActivity.class);
            startActivityForResult(intent, RQST_CODE);
        }

        if (v == btnPrescribe) {
            String prescription = " ";
            for (int i = 0; i < presMedicine.size(); i++) {
                String pres = presMedicine.get(i).getMedName() + " " +
                        presMedicine.get(i).getMorning() + "+" +
                        presMedicine.get(i).getAfternoon() + "+" +
                        presMedicine.get(i).getEvening() + "+" +
                        presMedicine.get(i).getNight() +
                        " (" + presMedicine.get(i).getPeriod() + ") ";

                prescription = prescription + pres + " \n";

            }

            if (dateCount == 0) {
                Toast.makeText(this, "Select the date!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (presMedicine.size() == 0) {
                Toast.makeText(this, "Please add a medicine!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            String presContainer = userInfo + "\n" + prescription;

            DatabaseReference child = reference.child(prescriptionId);
            DatabaseReference push = child.push();
            Prescription prescribe = new Prescription(prescriptionId, push.getKey(), tvDate.getText().toString().trim(), presContainer);
            push.setValue(prescribe);

            startActivity(new Intent(DoctorHomeActivity.this, HomeActivity.class));
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RQST_CODE && resultCode == RESULT_OK){
            Medicine medicine = (Medicine) data.getSerializableExtra("medicine");
            presMedicine.add(medicine);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctor_home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.doctor_home_menu_previous_prescription){
            startActivity(new Intent(DoctorHomeActivity.this, PreviousPrescriptionActiivity.class).putExtra("prescriptionId", prescriptionId));
        }
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
