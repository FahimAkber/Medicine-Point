package com.aversoft.medicinepoint.model;

public class Payment {
    String id;
    String sellerId;
    String orderId;
    String date;
    String month;
    int price;

    public Payment() {
    }

    public Payment(String id, String sellerId, String orderId, String date, String month, int price) {
        this.id = id;
        this.sellerId = sellerId;
        this.orderId = orderId;
        this.date = date;
        this.month = month;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public int getPrice() {
        return price;
    }
}
