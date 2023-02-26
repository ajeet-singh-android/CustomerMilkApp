package com.app.veka.UserModel;

public class BillingModel {
    public String status;
    public int running_bill;
    public int previous_bill;
    public int unpaid;
    public int last_pay;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRunning_bill() {
        return running_bill;
    }

    public void setRunning_bill(int running_bill) {
        this.running_bill = running_bill;
    }

    public int getPrevious_bill() {
        return previous_bill;
    }

    public void setPrevious_bill(int previous_bill) {
        this.previous_bill = previous_bill;
    }

    public int getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(int unpaid) {
        this.unpaid = unpaid;
    }

    public int getLast_pay() {
        return last_pay;
    }

    public void setLast_pay(int last_pay) {
        this.last_pay = last_pay;
    }
}
