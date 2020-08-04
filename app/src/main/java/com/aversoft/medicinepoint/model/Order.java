package com.aversoft.medicinepoint.model;

public class Order {
    String id;
    String orderId;
    String content;
    String status;
    String date;

    public Order() {
    }

    public Order(String id, String orderId, String content, String status, String date) {
        this.id = id;
        this.orderId = orderId;
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
