package com.aversoft.medicinepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    EditText etName, etNumber, etAge, etAddress, etPassword, etConfirmPassword;
    Spinner spRole;
    Button btnSignUp;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    TextView tvLogIn;
    ActionBar actionBar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String[] users;
    String user;
    String gender;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        actionBar.hide();

        setRole();
        rgGender.setOnCheckedChangeListener(this);
        btnSignUp.setOnClickListener(this);
        tvLogIn.setOnClickListener(this);



    }

    private void setRole() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrationActivity.this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);

        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = users[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init() {
        etName = findViewById(R.id.et_sign_up_name);
        etNumber = findViewById(R.id.et_sign_up_number);
        etAge = findViewById(R.id.et_sign_up_age);
        etAddress = findViewById(R.id.et_sign_up_address);
        etPassword = findViewById(R.id.et_sign_up_password);
        etConfirmPassword = findViewById(R.id.et_sign_up_confirm_password);
        spRole = findViewById(R.id.sp_sign_up_role);
        tvLogIn = findViewById(R.id.tv_sign_up_log_in);
        rgGender = findViewById(R.id.rg_sign_up_gender);
        rbMale = findViewById(R.id.rb_sign_up_male);
        rbFemale = findViewById(R.id.rb_sign_up_female);
        btnSignUp = findViewById(R.id.btn_sign_up);
        actionBar = getSupportActionBar();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB");
        users = getResources().getStringArray(R.array.user_role);
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);

    }

    @Override
    public void onClick(View v) {
        if(v == tvLogIn){
            startActivity(new Intent(RegistrationActivity.this, LogInActivity.class));
            finish();
        }
        if(v == btnSignUp){
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String number = etNumber.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if(name.isEmpty()){
                Toast.makeText(this, "Please provide your name!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(age.isEmpty()){
                Toast.makeText(this, "Please provide your age!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(number.isEmpty()){
                Toast.makeText(this, "Please provide your mobile number!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(address.isEmpty()){
                Toast.makeText(this, "Please provide your current address!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(password.isEmpty()){
                Toast.makeText(this, "Please provide your own password!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(confirmPassword.isEmpty()){
                Toast.makeText(this, "Provide your password again!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(confirmPassword)){
                etPassword.setText("");
                etConfirmPassword.setText("");
                Toast.makeText(this, "Please provide same password!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(gender.isEmpty()){
                Toast.makeText(this, "Select your gender!!!", Toast.LENGTH_SHORT).show();
            }
            if(user.isEmpty()){
                Toast.makeText(this, "Select User Role!!!", Toast.LENGTH_SHORT).show();
            }

            DatabaseReference childRef  = reference.child("/User/"+user);
            DatabaseReference pushRef = childRef.push();

            String shortCode = name.substring(0, 1).toUpperCase()+" "+pushRef.getKey().substring(6, 11) + number.substring(9, 11);

            User newUser = new User(pushRef.getKey(), name, number, age, address, password, user, gender, shortCode);
            pushRef.setValue(newUser);
            sp.edit().putBoolean("isLogged", true).putString("myName", name).putString("myId", pushRef.getKey()).putString("user", user).putString("shortCode", shortCode).apply();


            startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId == R.id.rb_sign_up_male){
            gender = "Male";
        } else{
            gender = "Female";
        }
    }
}
