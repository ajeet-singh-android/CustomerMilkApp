package com.app.veka.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;


import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.R;
import com.app.veka.UserModel.UserModel;
import com.app.veka.databinding.FragmentProfileBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
    ProgressDialog progressDialog;
    int gapday =0;
    UserModel userModel;
    String mshift = "",mdaytype = "";

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
   binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserDetails();

        binding.deliverytype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.alternate){
                    binding.numberlayout.setVisibility(View.VISIBLE);
                }else {
                    binding.numberlayout.setVisibility(View.GONE);
                }
            }
        });


        binding.one.setOnClickListener(vs ->{
            selectDay();
            binding.one.setBackgroundDrawable(getResources().getDrawable(R.drawable.selcted_background));
            binding.one.setTextColor(Color.parseColor("#ffffff"));
            gapday=1;
        });
        binding.two.setOnClickListener(vs ->{
            selectDay();
            binding.two.setBackgroundDrawable(getResources().getDrawable(R.drawable.selcted_background));
            binding.two.setTextColor(Color.parseColor("#ffffff"));
            gapday=2;
        });
        binding.three.setOnClickListener(vs ->{
            selectDay();
            binding.three.setBackgroundDrawable(getResources().getDrawable(R.drawable.selcted_background));
            binding.three.setTextColor(Color.parseColor("#ffffff"));
            gapday=3;
        });
//        binding.four.setOnClickListener(vs ->{
//            selectDay();
//            binding.four.setBackgroundDrawable(getResources().getDrawable(R.drawable.selcted_background));
//            binding.four.setTextColor(Color.parseColor("#ffffff"));
//            gapday=4;
//        });


        binding.submit.setOnClickListener(v->{
            String shift = "";
            String daytype = "";
            if(binding.morning.isChecked()){
                shift = "Morning";
            }else if(binding.evening.isChecked()){
                shift = "Evening";
            }else if(binding.both.isChecked()){
                shift = "Both";
            }

            if(binding.alternate.isChecked()){
                daytype = "Alternate";
            }else if(binding.daily.isChecked()){
                daytype = "Daily";
            }


            progreessDialog();

            Log.e("ass@23232", "onViewCreated: "+daytype+"  "+gapday+"  "+shift);

            String finalShift = shift;
            String finalDaytype = daytype;
            RetrofitInstance.getClient().updateprofile(new PrefrenceManager(getContext()).getuserid(),binding.name.getText().toString(),
                    binding.email.getText().toString(),userModel.getD_ltr(),daytype,
                    gapday+"",userModel.area,shift).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        if(jsonObject.getString("status").equalsIgnoreCase("success")){
                            progressDialog.dismiss();
                            Toasty.success(getActivity(), "Update Successfully", Toasty.LENGTH_SHORT, true).show();
                            addNotification(finalShift, finalDaytype);

                        }else {
                            Toasty.error(getActivity(), jsonObject.getString("message"), Toasty.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });


        });



    }

    public void selectDay(){
        binding.one.setBackgroundDrawable(getResources().getDrawable(R.drawable.number_background));
        binding.two.setBackgroundDrawable(getResources().getDrawable(R.drawable.number_background));
        binding.three.setBackgroundDrawable(getResources().getDrawable(R.drawable.number_background));
//        binding.four.setBackgroundDrawable(getResources().getDrawable(R.drawable.number_background));
        binding.one.setTextColor(Color.parseColor("#000000"));
        binding.two.setTextColor(Color.parseColor("#000000"));
        binding.three.setTextColor(Color.parseColor("#000000"));
//        binding.four.setTextColor(Color.parseColor("#000000"));
    }



    public void getUserDetails(){
        progreessDialog();
        RetrofitInstance.getClient().getUserDetails(new PrefrenceManager(getContext()).getuserid()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){

                        Log.e("aaa@223232", "onResponse: "+response.body().toString() );
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        userModel = new Gson().fromJson(jsonObject1.toString(), UserModel.class);
                        binding.name.setText(userModel.getName());
                        binding.email.setText(userModel.getEmail());
                        binding.mobilenumber.setText(userModel.getMobile());
                        binding.area.setText(userModel.getArea());


                        if(userModel.getGap().equalsIgnoreCase("1")){
                            selectDay();
                            binding.one.setBackgroundDrawable(getResources().getDrawable(R.drawable.selcted_background));
                            binding.one.setTextColor(Color.parseColor("#ffffff"));
                        }else if(userModel.getGap().equalsIgnoreCase("2")){
                            selectDay();
                            binding.two.setBackgroundDrawable(getResources().getDrawable(R.drawable.selcted_background));
                            binding.two.setTextColor(Color.parseColor("#ffffff"));
                        } else if(userModel.getGap().equalsIgnoreCase("3")){
                            selectDay();
                            binding.three.setBackgroundDrawable(getResources().getDrawable(R.drawable.selcted_background));
                            binding.three.setTextColor(Color.parseColor("#ffffff"));
                        }else {
                            binding.numberlayout.setVisibility(View.GONE);
                            binding.daily.setChecked(true);
                        }



                        if(userModel.getShift().equalsIgnoreCase("evening"))
                            binding.evening.setChecked(true);
                        else if(userModel.getShift().equalsIgnoreCase("morning"))
                            binding.morning.setChecked(true);
                        if(userModel.getShift().equalsIgnoreCase("both"))
                            binding.both.setChecked(true);

                        mshift = userModel.getShift();
                        mdaytype = userModel.getType();

                        if(userModel.getType().equalsIgnoreCase("alternate"))
                            binding.alternate.setChecked(true);
                        else
                            binding.daily.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
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


    private void addNotification(String shift, String daytype){
        RetrofitInstance.getClient().addNotificatin(new PrefrenceManager(getContext()).getuserid(),new PrefrenceManager(getContext()).getUserName()+" changed his shift from "+mshift+" "+mdaytype+ " to " +shift+" "+daytype,"Success").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}