package com.app.veka.UserModel;

public class CalenderModel {
    int date;
    boolean select;

    public CalenderModel(int date, boolean select) {
        this.date = date;
        this.select = select;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
