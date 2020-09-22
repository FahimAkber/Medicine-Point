package com.aversoft.medicinepoint;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class  HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    LinearLayout layoutSearch, layoutDoctor, layoutPatient, layoutSeller;
    EditText etSearch;
    Button btnSearch, btnShopRecord, btnShopOrder, btnShopAccepted;
    TextView tvShopName, tvDate;
    ProgressBar pbSearch, pbHome;
    GridView gvPatient;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ArrayList<User> allPatient;
    SharedPreferences sp;
    String myId, userRole, myName;
    User user;
    String address;
    String[] patientContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
        pbHome.setVisibility(View.VISIBLE);
        getUserInfo();

        if(userRole.equals("Doctor")){

            layoutDoctor.setVisibility(View.VISIBLE);
            layoutPatient.setVisibility(View.GONE);
            layoutSeller.setVisibility(View.GONE);

            getPatients();

            btnSearch.setOnClickListener(this);

        } else if(userRole.equals("Patient")){

            layoutPatient.setVisibility(View.VISIBLE);
            layoutDoctor.setVisibility(View.GONE);
            layoutSeller.setVisibility(View.GONE);

            setPatientContent();

        } else if(userRole.equals("Saller")){

            layoutSeller.setVisibility(View.VISIBLE);
            layoutDoctor.setVisibility(View.GONE);
            layoutPatient.setVisibility(View.GONE);

            setSellerContent();

        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setSellerContent() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        tvShopName.setText(myName);
        tvDate.setText(sdf.format(Calendar.getInstance().getTime()));

        btnShopOrder.setOnClickListener(this);
        btnShopAccepted.setOnClickListener(this);
        btnShopRecord.setOnClickListener(this);

    }

    private void setPatientContent() {
        gvPatient.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return patientContent.length;
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
                convertView = getLayoutInflater().inflate(R.layout.patient_content_container, parent, false);

                ImageView ivPatientContent = convertView.findViewById(R.id.iv_patient_content_container);
                TextView tvPatientContent = convertView.findViewById(R.id.tv_patient_content_container);

                tvPatientContent.setText(patientContent[position]);
                return convertView;
            }
        });

        gvPatient.setOnItemClickListener(this);
        gvPatient.setVerticalScrollBarEnabled(false);

    }

    private void getUserInfo() {
        reference.child("/"+userRole).child("/"+myId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               user =  dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pbHome.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.item_log_out){
            sp.edit().putBoolean("isLogged", false).putString("user", "none").putString("shortCode", null).apply();
            startActivity(new Intent(HomeActivity.this, SplashActivity.class));
            finish();
        } else if(id == R.id.item_about_me){
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("My Self")
                    .setMessage(
                            "Name: "+user.getName()
                            + "Age: "+user.getAge()
                            + "Gender: "+user.getGender()
                            + "Identity: "+user.getShortCode()
                            + "Address: "+user.getAddress())
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else if(id == R.id.item_medicine_point){

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPatients() {
        reference.child("/Patient").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    User user1 = data.getValue(User.class);
                    allPatient.add(user1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        layoutDoctor = findViewById(R.id.layout_doctor);
        layoutSearch = findViewById(R.id.layout_search_patient);
        layoutSeller = findViewById(R.id.layout_seller);
        tvShopName = findViewById(R.id.tv_seller_home_shop);
        tvDate = findViewById(R.id.tv_seller_home_date);
        btnShopOrder = findViewById(R.id.btn_seller_home_order);
        btnShopRecord = findViewById(R.id.btn_seller_home_summary);
        btnShopAccepted = findViewById(R.id.btn_seller_home_accepted);
        etSearch = findViewById(R.id.et_search_patient);
        btnSearch = findViewById(R.id.btn_search_patient);
        pbSearch = findViewById(R.id.pb_search_patient);
        layoutPatient = findViewById(R.id.layout_patient);
        gvPatient = findViewById(R.id.gv_patient_home);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/User/");
        allPatient = new ArrayList<>();
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myId = sp.getString("myId", "none");
        myName = sp.getString("myName", "none");
        userRole = sp.getString("user", "none");
        pbHome = findViewById(R.id.pb_home);
        patientContent = getResources().getStringArray(R.array.patient_content);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSearch){
            layoutSearch.setVisibility(View.GONE);
            pbSearch.setVisibility(View.VISIBLE);

            String patient = etSearch.getText().toString().trim();
            for (int i = 0; i < allPatient.size(); i++){
                if(allPatient.get(i).getShortCode().equals(patient)) {
                    pbSearch.setVisibility(View.GONE);
                    startActivity(new Intent(HomeActivity.this, DoctorHomeActivity.class).putExtra("user", allPatient.get(i)));
                    finish();
                } else{
                    pbSearch.setVisibility(View.GONE);
                    new AlertDialog.Builder(HomeActivity.this)
                            . setTitle("Warning")
                            . setMessage("Invalid Patient Code or The Patient Code doesn't existed in our database")
                            .setPositiveButton("Search Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    layoutSearch.setVisibility(View.VISIBLE);
                                    dialog.cancel();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        }

        if(v == btnShopOrder){
            startActivity(new Intent(getApplicationContext(), ShopOrderListActivity.class));
        }

        if(v == btnShopAccepted){
            startActivity(new Intent(getApplicationContext(), AcceptedOrdersActivity.class));
        }

        if(v == btnShopRecord){

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        address = user.getAddress();
        if(position == 0){
            startActivity(new Intent(HomeActivity.this, PatientNewOrderActivity.class).putExtra("patient_address", address));
        } else if(position == 1){
            startActivity(new Intent(HomeActivity.this, PatientOrderListActivity.class).putExtra("patient_address", address));
        } else if(position == 2){
            startActivity(new Intent(HomeActivity.this, PatientPrescriptionsActivity.class).putExtra("patient_address", address));
        } else if(position == 3){
            startActivity(new Intent(HomeActivity.this, DailyMedicineActivity.class).putExtra("patient_address", address));
        }
    }
}
