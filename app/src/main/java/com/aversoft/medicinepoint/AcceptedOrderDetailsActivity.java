package com.aversoft.medicinepoint;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aversoft.medicinepoint.model.Order;
import com.aversoft.medicinepoint.model.Payment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AcceptedOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etAcceptedOrder;
    TextView tvAcceptedOrder;
    Button btnNext, btnDeliver;
    Intent intent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Order order;
    ArrayList<Integer> priceList;
    SharedPreferences  sp;
    String myId;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_order_details);

        init();
        actionBar.setTitle("Order Confirmation");
        actionBar.setDisplayHomeAsUpEnabled(true);
        tvAcceptedOrder.setText(order.getContent());
        btnNext.setOnClickListener(this);
        btnDeliver.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }

    private void init() {
        etAcceptedOrder = findViewById(R.id.et_accepted_order);
        tvAcceptedOrder = findViewById(R.id.tv_accepted_order);
        btnNext = findViewById(R.id.btn_accepted_order);
        btnDeliver = findViewById(R.id.btn_accepted_deliver);
        intent = getIntent();
        priceList = new ArrayList<>();
        order = (Order) intent.getSerializableExtra("order");
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB");
        sp = getSharedPreferences("logStatus", MODE_PRIVATE);
        myId = sp.getString("myId", "none");
        actionBar = getSupportActionBar();
    }

    @Override
    public void onClick(View v) {
        if(v == btnNext){
            String text = etAcceptedOrder.getText().toString().trim();
            etAcceptedOrder.setText("");
            etAcceptedOrder.setHint("Item Price");
            if(!text.isEmpty()){
                int price = Integer.parseInt(text);
                priceList.add(price);
            } else{
                Toast.makeText(this, "Please provide product price serially.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(v == btnDeliver){
            if(priceList.size() == 0){
                Toast.makeText(this, "You need to specify product price.", Toast.LENGTH_SHORT).show();
                return;
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("Sure to Deliver")
                        .setMessage("Once you press Deliver button, the order will be marked as DELIVERED")
                        .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Deliver", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int totalPrice = 0;
                                for(int price : priceList){
                                    totalPrice += price;
                                }
                                Order newOrder = new Order(order.getId(), order.getOrderId(), order.getPatientId(), order.getContent(), "Delivered", order.getDate());
                                reference.child("/Order List/"+order.getId()+"/"+order.getOrderId())
                                        .setValue(newOrder);
                                DatabaseReference push = reference.child("/Payment/" + myId + "/").push();
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                                SimpleDateFormat sdf1 = new SimpleDateFormat("MMM");
                                Payment payment = new Payment(push.getKey(), myId, order.getOrderId()
                                        , sdf.format(Calendar.getInstance().getTime()),
                                        sdf1.format(Calendar.getInstance().getTime()), totalPrice);
                                push.setValue(payment);
                                finish();
                            }
                        })
                        .show();
            }
        }
    }
}
