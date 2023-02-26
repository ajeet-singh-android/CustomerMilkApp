package com.app.veka.UserModel;

import java.util.ArrayList;

public class SuppliesModel {
    public ArrayList<Running> running;
    public ArrayList<Previou> previous;

    public ArrayList<Running> getRunning() {
        return running;
    }

    public void setRunning(ArrayList<Running> running) {
        this.running = running;
    }

    public ArrayList<Previou> getPrevious() {
        return previous;
    }

    public void setPrevious(ArrayList<Previou> previous) {
        this.previous = previous;
    }
}
