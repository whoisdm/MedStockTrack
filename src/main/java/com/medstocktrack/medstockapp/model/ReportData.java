package com.medstocktrack.medstockapp.model;

public class ReportData {
    private final String id;
    private final String name;
    private final int quantity;
    private final double price;

    public ReportData(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
