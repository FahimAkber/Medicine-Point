package com.aversoft.medicinepoint.model;

import java.io.Serializable;

public class Order implements Serializable {
    String id;
    String orderId;
    String patientId;
    String content;
    String status;
    String date;

    public Order() {
    }

    public Order(String id, String orderId, String patientId, String content, String status, String date) {
        this.id = id;
        this.orderId = orderId;
        this.patientId = patientId;
        this.content = content;
        this.status = status;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
