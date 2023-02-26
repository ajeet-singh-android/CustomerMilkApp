package com.app.veka.Network;


import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceManager {
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences saSharedPreferences;

    public PrefrenceManager(Context context2) {
        this.context = context2;
        SharedPreferences sharedPreferences = context2.getSharedPreferences("milk", 0);
        this.saSharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }



    public void setuserinfo(String userid, String username, String phone,String email) {
        this.editor.putString("UserId", userid);
        this.editor.putString("UserName", username);
        this.editor.putString("phone", phone);
        this.editor.putString("email", email);
        this.editor.apply();
        this.editor.commit();
    }

    public String getuserid() {
        return this.saSharedPreferences.getString("UserId", "0");
    }

    public String getUserName() {
        return this.saSharedPreferences.getString("UserName", "0");
    }


    public String getEmail() {
        return this.saSharedPreferences.getString("email", "0");
    }


    public String getPhone() {
        return this.saSharedPreferences.getString("phone", "0");
    }






    public void logout() {
        this.editor.clear().apply();
        this.editor.commit();
    }

    public void setnotification(int count){
        this.editor.putInt("noticount",count);
        editor.commit();
    }
    public int getnoticount(){
        return saSharedPreferences.getInt("noticount",0);
    }
}
