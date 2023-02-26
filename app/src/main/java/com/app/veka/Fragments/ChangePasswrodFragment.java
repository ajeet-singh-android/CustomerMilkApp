package com.app.veka.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.R;
import com.app.veka.databinding.FragmentChangePasswrodBinding;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswrodFragment extends Fragment {

    FragmentChangePasswrodBinding binding;

    public ChangePasswrodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswrodBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.submit.setOnClickListener(v->{
            String passwrod = binding.password.getText().toString();
            String cpasswrod = binding.confirmpassword.getText().toString();
            if(!passwrod.isEmpty()){
                if(!cpasswrod.isEmpty()){
                    if(passwrod.equals(cpasswrod)){
                        binding.includelayout.progresslaout.setVisibility(View.VISIBLE);
                        changePassword(passwrod,cpasswrod);
                    }else {
                        Toasty.error(requireContext(), "Password Not Match", Toast.LENGTH_SHORT, true).show();
                    }

                }else {
                    binding.confirmpassword.setError("Please Enter Confirm Password");
                    binding.confirmpassword.requestFocus();
                }
            }else {
                binding.password.setError("Please Enter Password");
                binding.password.requestFocus();
            }
        });

    }

    private void changePassword(String passwrod, String cpasswrod) {
        RetrofitInstance.getClient().changePasword(new PrefrenceManager(getContext()).getuserid(),passwrod,cpasswrod).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                binding.includelayout.progresslaout.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                        Toasty.success(requireContext(), "Password Change Successfully", Toast.LENGTH_SHORT, true).show();
                    }else {
                        Toasty.error(requireContext(), "Somthing Went Wrong", Toast.LENGTH_SHORT, true).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toasty.error(requireContext(), "Somthing Went Wrong"+t.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}