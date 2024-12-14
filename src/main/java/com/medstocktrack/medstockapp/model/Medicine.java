package com.medstocktrack.medstockapp.model;

public class Medicine {
    private final String medicineID;
    private final String medicineName;
    private final String medicineType;
    private final String medicineActive;
    private final String medicineSize;
    private final String medicineContr;
    private final String medicineForm;
    private final Double medicinePrice;
    private final Integer medicinePrescription;
    private final Producer producer;

    public Medicine(String medicineID, String medicineName, String medicineType, String medicineActive, String medicineSize,
                    String medicineContr, String medicineForm, Double medicinePrice, Integer medicinePrescription, String producerName, String producerCountry) {
        this.medicineID = medicineID;
        this.medicineName = medicineName;
        this.medicineType = medicineType;
        this.medicineActive = medicineActive;
        this.medicineSize = medicineSize;
        this.medicineContr = medicineContr;
        this.medicineForm = medicineForm;
        this.medicinePrice = medicinePrice;
        this.medicinePrescription = medicinePrescription;
        this.producer = new Producer(producerName, producerCountry);
    }

    public String getMedicineID() {
        return medicineID;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public String getMedicineActive() {
        return medicineActive;
    }

    public String getMedicineSize() {
        return medicineSize;
    }

    public String getMedicineContr() {
        return medicineContr;
    }

    public String getMedicineForm() {
        return medicineForm;
    }

    public double getMedicinePrice() {
        return medicinePrice;
    }

    public Integer isMedicinePrescription() {
        return medicinePrescription;
    }

    public Producer getProducer() {
        return producer;
    }
}
