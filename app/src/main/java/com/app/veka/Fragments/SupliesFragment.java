package com.app.veka.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.R;
import com.app.veka.UserModel.RunningModel;
import com.app.veka.databinding.CalenderDilaogBinding;
import com.app.veka.databinding.FragmentSupliesBinding;
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
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;


public class SupliesFragment extends Fragment {


    List<RunningModel> runningModelList = new ArrayList<>();
    List<RunningModel> previousmodellist = new ArrayList<>();
    List<RunningModel> extramodellist = new ArrayList<>();

    FragmentSupliesBinding binding;
    ProgressDialog progressDialog;

    List<DateData> runningdate = new ArrayList<>();
    List<DateData> previousdate = new ArrayList<>();
    List<DateData> extradate = new ArrayList<>();


    public SupliesFragment() {
        // Required empty public constructor
    }


    int buttonclick = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSupliesBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSupplies();




        binding.thismonthclick.setOnClickListener(v ->{
            buttonclick=1;
            if(runningdate.size()>0){
                binding.calenderlayout.clayout.setVisibility(View.VISIBLE);
                UnmarkDate(previousdate);
                UnmarkDate(extradate);
                setUpDate(runningdate);

            }
            else
                Toast.makeText(getActivity(),"Running Date Not found",Toast.LENGTH_SHORT).toString();
        });

        binding.extramontclick.setOnClickListener(view1 -> {
            buttonclick=3;
            if(extradate.size()>0){
                binding.calenderlayout.clayout.setVisibility(View.VISIBLE);
                UnmarkDate(runningdate);
                UnmarkDate(previousdate);
                setUpDate(extradate);

            }
            else
                Toast.makeText(getActivity(),"Running Date Not found",Toast.LENGTH_SHORT).toString();
        });

        binding.previousdetails.setOnClickListener(v ->{
            buttonclick=2;
            if(previousdate.size()>0){
                binding.calenderlayout.clayout.setVisibility(View.VISIBLE);
                UnmarkDate(runningdate);
                UnmarkDate(extradate);
                setUpDate(previousdate);
            }
            else
                Toast.makeText(getActivity(),"Running Date Not found",Toast.LENGTH_SHORT).toString();
        });


        binding.calenderlayout.crossbtn.setOnClickListener(v->{
            binding.calenderlayout.clayout.setVisibility(View.GONE);
        });


        binding.calenderlayout.calenderview.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
               String mdate =  String.format("%d-%02d-%02d", date.getYear(),date.getMonth(),date.getDay());

                Log.e("asss@2232323", "onDateClick: "+runningModelList.size() +"   " );
               if(buttonclick==1){
                   for (int i=0;i<runningModelList.size();i++){
                       Log.e("asss@2232323", "onDateClick for: "+mdate);
                       if(mdate.equals(runningModelList.get(i).create_date)){
                           Log.e("asss@2232323", "onDateClick iff: "+mdate);
                           ltrDialog(runningModelList.get(i).ltr);
//                           Toasty.success(requireContext(),runningModelList.get(i).ltr + " ltr",Toast.LENGTH_SHORT).show();
                       }
                   }
                   
               }else if(buttonclick==2){
                   for (int i=0;i<previousmodellist.size();i++){
                       if(mdate.equals(previousmodellist.get(i).create_date)){
                           ltrDialog(previousmodellist.get(i).ltr);
//                           Toasty.success(requireContext(),previousmodellist.get(i).ltr + " ltr",Toast.LENGTH_SHORT).show();
                       }
                   }
               }else if(buttonclick==3){
                   for (int i=0;i<extramodellist.size();i++){
                       if(mdate.equals(extramodellist.get(i).create_date)){
                           ltrDialog(extramodellist.get(i).extra);
//                           Toasty.success(requireContext(),extramodellist.get(i).ltr + " ltr",Toast.LENGTH_SHORT).show();
                       }
                   }
               }


            }
        });
    }


    public void getSupplies(){
        progreessDialog();

        RetrofitInstance.getClient().getSupplies(new PrefrenceManager(getContext()).getuserid()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                        JSONArray runningarryay = jsonObject.getJSONArray("running");
                        JSONArray previousarray = jsonObject.getJSONArray("previous");



                        runningdate.clear();
                        previousdate.clear();
                        extradate.clear();


                        int extra =0;

                        for (int i=0;i<runningarryay.length();i++){
                            JSONObject jsonObject1 = runningarryay.getJSONObject(i);
                            RunningModel runningModel = new Gson().fromJson(runningarryay.getJSONObject(i).toString(),RunningModel.class);

                            String date = jsonObject1.getString("create_date");
                            String status = jsonObject1.getString("status");
                            Double ltr = Double.parseDouble(jsonObject1.getString("ltr"));
                            String[] splitstirng = date.split("-");
                            if(status.equals("Delivered") && ltr>0){
                                runningdate.add(new DateData(Integer.parseInt(splitstirng[0]),Integer.parseInt(splitstirng[1]),Integer.parseInt(splitstirng[2])));
                                runningModelList.add(runningModel);
                            }

                            Log.e("TAG@2323", "onResponsghgghe: "+runningdate.size() + "            "+previousarray.length() );

                            if(status.equals("Delivered") && Double.parseDouble(jsonObject1.getString("extra"))>0){
                                extramodellist.add(runningModel);

                                extradate.add(new DateData(Integer.parseInt(splitstirng[0]),Integer.parseInt(splitstirng[1]),Integer.parseInt(splitstirng[2])));
                                extra += Double.parseDouble(jsonObject1.getString("extra"));
                            }
                        }

                        binding.extrasupplyday.setText(extra+" Liter");

                        for (int i=0;i<previousarray.length();i++){
                            JSONObject jsonObject1 = previousarray.getJSONObject(i);

                            RunningModel runningModel = new Gson().fromJson(previousarray.getJSONObject(i).toString(),RunningModel.class);
                            previousmodellist.add(runningModel);

                            String date = jsonObject1.getString("create_date");
                            Log.e("TAG@2323", "onResponse: asp "+date );
                            String status = jsonObject1.getString("status");
                            String[] splitstirng = date.split("-");
                            Double ltr = Double.parseDouble(jsonObject1.getString("ltr"));
                            if(status.equals("Delivered") && ltr>0)
                                previousdate.add(new DateData(Integer.parseInt(splitstirng[0]),Integer.parseInt(splitstirng[1]),Integer.parseInt(splitstirng[2])));
                        }





                        binding.thismonthday.setText(runningdate.size()+" Days");
                        binding.previousmonthday.setText(previousdate.size()+" Days");


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


    public void setUpDate(List<DateData> list){
        for (int i=0;i<list.size();i++){
            binding.calenderlayout.calenderview.travelTo(list.get(i).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.BLUE)));
        }
    }

    public void UnmarkDate(List<DateData> list){
        for (int i=0;i<list.size();i++){
            binding.calenderlayout.calenderview.unMarkDate(list.get(i).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, Color.WHITE)));
        }
    }


    public void progreessDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbaritems);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }



    public void ltrDialog(String ltr){
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.ltr_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.AnimationforDialog);

       TextView text = dialog.findViewById(R.id.milkltr);
        TextView ok = dialog.findViewById(R.id.ok);
        text.setText(ltr);
        dialog.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}


