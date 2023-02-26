package com.app.veka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.UserModel.UserModel;
import com.app.veka.databinding.ActivityLoginBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.submit.setOnClickListener(v ->{
            validateUser();
        });

    }


    public void validateUser(){
        String mobile = binding.mobile.getText().toString();
        String password = binding.password.getText().toString();
        if(!mobile.isEmpty()){
            if(mobile.length()==10){
                if(!password.isEmpty()){
                    binding.includelayout.progresslaout.setVisibility(View.VISIBLE);
                    loginUser(mobile,password);
                }else {
                    binding.password.setError("Please Enter Password");
                    binding.password.requestFocus();
                }
            }else {
                binding.mobile.setError("Please Enter Valid Mobile Number");
                binding.mobile.requestFocus();
            }
        }else {
            binding.mobile.setError("Please Enter Mobile Number");
            binding.mobile.requestFocus();
        }
    }

    private void loginUser(String mobile, String password) {
        RetrofitInstance.getClient().userLogin(mobile,password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                binding.includelayout.progresslaout.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        UserModel model = new Gson().fromJson(jsonObject1.toString(),UserModel.class);
                        new PrefrenceManager(getApplicationContext()).setuserinfo(model.cus_id,model.name,model.mobile,model.email);
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finishAffinity();
                    }else {
                        Toasty.error(getApplicationContext(), "Mobile Number and Password Not Match", Toast.LENGTH_SHORT, true).show();
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
}