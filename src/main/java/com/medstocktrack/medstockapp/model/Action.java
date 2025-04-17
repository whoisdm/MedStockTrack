package com.medstocktrack.medstockapp.model;

public class Action {
    private final String actionUser;
    private final String actionType;
    private final String actionDate;
    private final String actionMedicine;
    private final String actionQuantity;

    public Action(String user, String type, String date, String medicine, String quantity) {
        this.actionUser = user;
        this.actionType = type;
        this.actionDate = date;
        this.actionMedicine = medicine;
        this.actionQuantity = quantity;
    }

    public String getActionUser() {
        return actionUser;
    }

    public String getActionType() {
        return actionType;
    }

    public String getActionDate() {
        return actionDate;
    }

    public String getActionMedicine() {
        return actionMedicine;
    }

    public String getActionQuantity() {
        return actionQuantity;
    }
}
