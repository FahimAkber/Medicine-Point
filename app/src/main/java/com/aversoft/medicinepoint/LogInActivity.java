package com.aversoft.medicinepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etNumber, etPassword;
    Spinner spRole;
    Button btnLogIn;
    TextView tvSignUp;
    ActionBar actionBar;
    String[] users;
    String user;
    String shortCode;
    String myId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    ArrayList<User> allUser;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        init();

        actionBar.hide();

        setRole();

        btnLogIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

    }

    private void setRole() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LogInActivity.this,android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);

        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = users[position];

                getUser(user);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getUser(String user) {
        DatabaseReference currentRef = reference.child("/User/"+user);
        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allUser.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    allUser.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        etNumber = findViewById(R.id.et_log_in_number);
        etPassword = findViewById(R.id.et_log_in_password);
        spRole = findViewById(R.id.sp_log_in_role);
        btnLogIn = findViewById(R.id.btn_log_in);
        tvSignUp = findViewById(R.id.tv_log_in_sign_up);
        actionBar = getSupportActionBar();
        users = getResources().getStringArray(R.array.user_role);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB");
        allUser = new ArrayList<>();
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogIn){
            String number = etNumber.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if(number.isEmpty()){
                Toast.makeText(this, "Please Provide Your Number!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.isEmpty()){
                Toast.makeText(this, "Please Provide Your Own Password!!!", Toast.LENGTH_SHORT).show();
                return;
            }

            for(int i = 0; i < allUser.size(); i++){
                if(number.equals(allUser.get(i).getNumber()) && password.equals(allUser.get(i).getPassword())){

                    shortCode = allUser.get(i).getShortCode();
                    myId = allUser.get(i).getId();
                    sp.edit().putBoolean("isLogged", true).putString("myId", myId).putString("user", user).putString("shortCode", shortCode).apply();

                    startActivity(new Intent(LogInActivity.this, HomeActivity.class));
                    finish();

                } else {
                    Toast.makeText(this, "Invalid Entry!!!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        if(v == tvSignUp){
            startActivity(new Intent(LogInActivity.this, RegistrationActivity.class));
            finish();
        }

    }

}
