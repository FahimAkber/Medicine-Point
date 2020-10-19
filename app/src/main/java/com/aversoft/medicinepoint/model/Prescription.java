package com.aversoft.medicinepoint.model;

import java.io.Serializable;

public class Prescription implements Serializable {
    String id;
    String presId;
    String date;
    String prescription;

    public Prescription() {
    }

    public Prescription(String id, String presId, String date, String prescription) {
        this.id = id;
        this.presId = presId;
        this.date = date;
        this.prescription = prescription;
    }

    public String getId() {
        return id;
    }

    public String getPresId() {
        return presId;
    }

    public String getDate() {
        return date;
    }

    public String getPrescription() {
        return prescription;
    }
}
