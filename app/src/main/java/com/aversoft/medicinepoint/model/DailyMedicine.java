package com.aversoft.medicinepoint.model;

import java.io.Serializable;

public class DailyMedicine implements Serializable {
    String setName;
    String medicineList;
    String id;
    String userId;

    public DailyMedicine() {
    }

    public DailyMedicine(String setName, String medicineList, String id, String userId) {
        this.setName = setName;
        this.medicineList = medicineList;
        this.id = id;
        this.userId = userId;
    }

    public String getSetName() {
        return setName;
    }

    public String getMedicineList() {
        return medicineList;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }
}
