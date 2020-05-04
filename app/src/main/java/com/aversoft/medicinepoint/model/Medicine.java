package com.aversoft.medicinepoint.model;

import java.io.Serializable;

public class Medicine implements Serializable {
    String medName;
    int morning;
    int afternoon;
    int evening;
    int night;
    String period;

    public Medicine() {
    }

    public Medicine(String medName, int morning, int afternoon, int evening, int night, String period) {
        this.medName = medName;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;
        this.period = period;
    }

    public String getMedName() {
        return medName;
    }

    public int getMorning() {
        return morning;
    }

    public int getAfternoon() {
        return afternoon;
    }

    public int getEvening() {
        return evening;
    }

    public int getNight() {
        return night;
    }

    public String getPeriod() {
        return period;
    }
}
