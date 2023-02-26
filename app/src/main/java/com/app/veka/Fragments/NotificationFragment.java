package com.app.veka.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.veka.Adapter.NotificationAdapter;
import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.R;
import com.app.veka.UserModel.NotificationMdel;
import com.app.veka.databinding.FragmentNotificationBinding;
import com.app.veka.databinding.NotificationItemsBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationFragment extends Fragment {


    FragmentNotificationBinding binding;
    ProgressDialog progressDialog;

    List<NotificationMdel> notificationMdelList = new ArrayList<>();
    NotificationMdel notificationMdel;
    public NotificationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getNotification();

        notificationSharePreferenceNew();


    }


    public void getNotification(){
        progreessDialog();
        RetrofitInstance.getClient().getNotification(new PrefrenceManager(getContext()).getuserid()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        notificationSharePreferenceOld(jsonArray.length());
                        for (int i=0;i<jsonArray.length();i++){
                             notificationMdel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),NotificationMdel.class);
                            notificationMdelList.add(notificationMdel);
                        }
                        if(notificationMdelList.size()==0){
                            binding.notfound.setVisibility(View.VISIBLE);
                        }
                        binding.notificationrecyclerview.setAdapter(new NotificationAdapter(notificationMdelList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }




    public void progreessDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbaritems);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    public void notificationSharePreferenceNew(){
        int pp = 0;
        SharedPreferences sharedPreferences_frist;
        final String Notification_New="new_notification";
        sharedPreferences_frist = getActivity().getSharedPreferences(Notification_New, Context.MODE_PRIVATE);
        SharedPreferences.Editor new_editor = sharedPreferences_frist.edit();
        new_editor.putInt("new_data",pp);
        new_editor.apply();
        new_editor.commit();


    }


    public  void notificationSharePreferenceOld(int ii){
        SharedPreferences sharedPreferences_Second;
        final String Notification_old="old_notification";
        sharedPreferences_Second = getActivity().getSharedPreferences(Notification_old,Context.MODE_PRIVATE);
        SharedPreferences.Editor old_editot = sharedPreferences_Second.edit();
        old_editot.putInt("old",ii);
        old_editot.apply();
        old_editot.commit();
    }

}