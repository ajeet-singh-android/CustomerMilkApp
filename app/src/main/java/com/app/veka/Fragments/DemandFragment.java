package com.app.veka.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.veka.Adapter.CustomCalenderAdapter;
import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.R;
import com.app.veka.UserModel.CalenderModel;
import com.app.veka.UserModel.UserModel;
import com.app.veka.databinding.FragmentDemandBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DemandFragment extends Fragment {


    FragmentDemandBinding binding;
    ProgressDialog progressDialog;
     UserModel userModel;
    String mdltr="";

    public DemandFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDemandBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        getUserDetails();



        binding.textView71.setOnClickListener(view1 -> {
            binding.tempselectdate.setText("");
        });


        binding.upbutton1.setOnClickListener(vas ->{
            Double num1 = Double.parseDouble(binding.regularedittext.getText().toString());
            Double addno = num1+0.5;
            binding.regularedittext.setText(addno+"");
        });

        binding.downbutton1.setOnClickListener(vsp -> {
                    Double num1 = Double.parseDouble(binding.regularedittext.getText().toString());
                    if (num1 > 0) {
                        Double sub = num1 - 0.5;
                        binding.regularedittext.setText(sub + "");

                    }
                });


        binding.regularupdate.setOnClickListener(v->{
            updateDailyleater(binding.regularedittext.getText().toString().trim());
        });

        binding.dateedittext.setOnClickListener(v ->{
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            String cdate =mYear+","+mMonth+","+mDay;
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text

//                            binding.dateedittext.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                            binding.dateedittext.setText(year + "-"+ (monthOfYear + 1) + "-" + dayOfMonth);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });


       /* binding.tempselectdate.setOnClickListener(v ->{
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            String cdate =mYear+","+mMonth+","+mDay;
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text

                            binding.tempselectdate.setText(year + "-"+ (monthOfYear + 1) + "-" + dayOfMonth);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        });*/


        binding.tempselectdate.setOnClickListener(view1 ->{
            List<CalenderModel> list = new ArrayList<>();
            for(int i=1;i<=31;i++){
                list.add(new CalenderModel(i,false));
            }
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_culender);
            dialog.getWindow().setWindowAnimations(R.style.AnimationforDialog);
            TextView submit = dialog.findViewById(R.id.submit);
            RecyclerView recyclerView = dialog.findViewById(R.id.customcolenderRecView);
            recyclerView.setAdapter(new CustomCalenderAdapter(getActivity(),list));
            dialog.show();
            recyclerView.setHasFixedSize(true);


            submit.setOnClickListener(view2 -> {
                Date d = new Date();
                CharSequence s  = DateFormat.format("MM-yyyy ", d.getTime());
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0;i<list.size();i++){
                    if(list.get(i).isSelect())
                        stringBuilder.append(list.get(i).getDate()+"-"+s+",");
                }
                String selecteddate = stringBuilder.toString().substring(0,stringBuilder.toString().length()-1);
                binding.tempselectdate.setText(selecteddate);

                Log.e("asss@3234", "onViewCreated: "+selecteddate );

                dialog.dismiss();
            });
        });



        binding.close.setOnClickListener(v ->{

            String data = binding.tempselectdate.getText().toString();
            if(!data.isEmpty()){
                tempClosemultipuldate(data);
            }else {
                binding.tempselectdate.setError("Please Select Date");
                binding.tempselectdate.requestFocus();
            }


        });


        binding.extrasubmit.setOnClickListener(v ->{
                String qunatity = binding.quantatiy.getText().toString();
                String date = binding.dateedittext.getText().toString();

                if(!qunatity.isEmpty()){
                    if(!date.isEmpty()){
                        progreessDialog();
                        RetrofitInstance.getClient().extaDemond(new PrefrenceManager(getContext()).getuserid(),qunatity,date)
                                .enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        progressDialog.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.body().toString());
                                            if(jsonObject.getString("status").equalsIgnoreCase("success")){
                                                addNotification(new PrefrenceManager(getContext()).getUserName()+" requested extra demand of "+qunatity+" ltrs on date - "+date);
                                                Toasty.success(getActivity(), "Extra Demand Update Successfully", Toast.LENGTH_SHORT, true).show();
                                            }else {
                                                Toasty.error(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT, true).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        progressDialog.dismiss();

                                    }   });
                    }else {
                        binding.dateedittext.setError("Please Select Date");
                        binding.dateedittext.requestFocus();
                    }
                }else {
                    binding.quantatiy.setError("Please Enter Quantity");
                    binding.quantatiy.requestFocus();
                }



        });


        binding.upbutton.setOnClickListener(vas ->{
            Double num = Double.parseDouble(binding.quantatiy.getText().toString());
             Double addno = num+0.5;
            binding.quantatiy.setText(addno+"");
        });

        binding.downbutton.setOnClickListener(vs ->{
            Double num = Double.parseDouble(binding.quantatiy.getText().toString());
            if(num>0){
                Double sub = num-0.5;
                binding.quantatiy.setText(sub+"");
            }



        });


        binding.paramanetclosebtn.setOnClickListener(v ->{
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.parament_close_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.AnimationforDialog);
            EditText editText = dialog.findViewById(R.id.editext);
            TextView submitbutton = dialog.findViewById(R.id.extrasubmit);

            submitbutton.setOnClickListener(view1 -> {
                String message = editText.getText().toString().trim();
                if(!message.isEmpty()){
                    paramanentClose(message,dialog);
                }else {
                    editText.setError("Please Eneter Message");
                    editText.requestFocus();
                }


            });

            dialog.show();
        });


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
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        userModel = new Gson().fromJson(jsonObject1.toString(), UserModel.class);
                        binding.regularedittext.setText(userModel.d_ltr);
                        mdltr = userModel.d_ltr;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    public void updateDailyleater(String dltr){
        progreessDialog();
        RetrofitInstance.getClient().updateDilyleater(new PrefrenceManager(getContext()).getuserid(),dltr).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                        Toasty.success(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT, true).show();
                        addNotification(new PrefrenceManager(getContext()).getUserName()+" changed Regular demand from "+ mdltr +" Ltrs to " +dltr +" Ltrs");
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

    public void paramanentClose(String message,Dialog dialog){
        progreessDialog();
        RetrofitInstance.getClient().parementColse(new PrefrenceManager(getContext()).getuserid(),message).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    addNotification(new PrefrenceManager(getContext()).getUserName()+" requested for Permanent Closure");
                    Toasty.success(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT, true).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                dialog.dismiss();
            }
        });
    }

    public void tempClosemultipuldate(String date){
        progreessDialog();
        RetrofitInstance.getClient().mutipulDate(new PrefrenceManager(getContext()).getuserid(),date,"close").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("true")){
                        Toasty.success(getActivity(), "requested for temporary closure success", Toast.LENGTH_SHORT, true).show();
                        addNotification(new PrefrenceManager(getContext()).getUserName()+" requested for temperory closure  on : "+date);
                    }else {
                        Toasty.error(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT, true).show();
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

//    public void tempClose(String date){
//        progreessDialog();
//        RetrofitInstance.getClient().todayCancelOrder(new PrefrenceManager(getContext()).getuserid(),"close",date).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                progressDialog.dismiss();
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().toString());
//                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
//                        Toasty.success(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT, true).show();
//                        addNotification(new PrefrenceManager(getContext()).getUserName()+" requested for temperory closure  on : "+date);
//                    }else {
//                        Toasty.error(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT, true).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    progressDialog.dismiss();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                progressDialog.dismiss();
//            }
//        });
//    }

    public void progreessDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbaritems);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    private void addNotification(String message){
        RetrofitInstance.getClient().addNotificatin(new PrefrenceManager(getContext()).getuserid(),message,"Success").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}