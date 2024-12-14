package com.medstocktrack.medstockapp.model;

public class Storage {
    private final String medName;
    private final Integer medNumber;

    public Storage(String medName, Integer medNumber) {
        this.medName = medName;
        this.medNumber = medNumber;
    }

    public String getMedName() {
        return medName;
    }

    public Integer getMedNumber() {
        return medNumber;
    }

}
