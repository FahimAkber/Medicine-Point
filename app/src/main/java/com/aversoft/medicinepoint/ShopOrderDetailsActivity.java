package com.aversoft.medicinepoint;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aversoft.medicinepoint.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShopOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvDate, tvStatus, tvMedicine;
    Button btnAccept, btnCancel;
    Intent intent;
    Order order;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order_details);

        init();

        tvDate.setText(order.getDate());
        tvStatus.setText(order.getStatus());
        tvMedicine.setText(order.getContent());

        btnAccept.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void init() {
        tvDate = findViewById(R.id.tv_shop_order_details_date);
        tvStatus = findViewById(R.id.tv_shop_order_details_status);
        tvMedicine = findViewById(R.id.tv_shop_order_details_medicine);
        btnAccept = findViewById(R.id.btn_shop_order_details_accept);
        btnCancel = findViewById(R.id.btn_shop_order_details_cancel);
        intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Medicine Point DB/Order List/"+order.getId()+"/"+order.getOrderId());
    }

    @Override
    public void onClick(View v) {
        if(v == btnAccept){
           new AlertDialog.Builder(this)
                   .setTitle("Accept the Order")
                   .setMessage("Are You Sure to accept this order? Once you accept it, then you have to deliver it within 1 hour...")
                   .setNegativeButton("Check Again", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                       }
                   })
                   .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Order newOrder = new Order(order.getId(), order.getOrderId(), order.getPatientId(), order.getContent(), "Accepted", order.getDate());
                           reference.setValue(newOrder);
                           finish();
                       }
                   })
                   .show();
        }
        if(v == btnCancel){
            new AlertDialog.Builder(this)
                    .setTitle("Cancel the Order")
                    .setMessage("Are You Sure to cancel this order?")
                    .setNegativeButton("Check Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Order newOrder = new Order(order.getId(), order.getOrderId(), order.getPatientId(), order.getContent(), "Canceled", order.getDate());
                            reference.setValue(newOrder);
                            finish();
                        }
                    })
                    .show();
        }
    }
}
