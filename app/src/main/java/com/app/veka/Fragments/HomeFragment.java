package com.app.veka.Fragments;

import static com.app.veka.MainActivity.notificationtext;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.veka.Adapter.DialogNotificationAdapter;
import com.app.veka.Adapter.SliderAdapter;
import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.R;
import com.app.veka.UserModel.NotificationMdel;
import com.app.veka.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    List<NotificationMdel> notificationMdelList = new ArrayList<>();

    ///Notifiaction
    SharedPreferences sharedPreferences_frist;
    final String Notification_New="new_notification";


    ProgressDialog progressDialog;
    Timer timer;
    Handler handler;

    FragmentHomeBinding binding;

    private List<Integer> sliderList= new ArrayList<>();

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);





        getUserDetails();
        getCloseStatus();


        getNotification();


        sliderList.clear();

        sliderList.add(R.drawable.milk1);
        sliderList.add(R.drawable.one);
        sliderList.add(R.drawable.two);
        sliderList.add(R.drawable.three);
        sliderList.add(R.drawable.four);
        sliderList.add(R.drawable.five);
        sliderList.add(R.drawable.seven);
        sliderList.add(R.drawable.six);

        timer = new Timer();
        handler = new Handler();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    int i = binding.viewpager.getCurrentItem();

                    if(i==sliderList.size()-1){
                        i=0;
                        binding.viewpager.setCurrentItem(i,true);
                    }else {
                        i++;
                        binding.viewpager.setCurrentItem(i,true);
                    }


                });
            }
        },3000,3000);


        SliderAdapter  sliderAdapter = new SliderAdapter(sliderList,getActivity());
        binding.viewpager.setAdapter(sliderAdapter);

        binding.dotsIndicator.setViewPager(binding.viewpager);

         binding.suppliescard.setOnClickListener(vv ->{
             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new SupliesFragment()).addToBackStack(null).commit();
         });

        binding.billingcard.setOnClickListener(vv ->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new BillingFragment()).addToBackStack(null).commit();
        });

        binding.mydemandcard.setOnClickListener(vv ->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new DemandFragment()).addToBackStack(null).commit();
        });

        binding.changepassword.setOnClickListener(vv ->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChangePasswrodFragment()).addToBackStack(null).commit();
        });



    }




    public void progreessDialog(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbaritems);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    public void getCloseStatus(){
        progreessDialog();
        RetrofitInstance.getClient().getCloseStatus().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                        if(jsonObject.getString("dairy").equalsIgnoreCase("off")){
                            binding.messagelayout.setVisibility(View.VISIBLE);
                            binding.message.setText("Dairy Will be closed from "+jsonObject.getString("from_date")+" to "+jsonObject.getString("to_date"));
                            binding.message.setSelected(true);
                            binding.message.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        }
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


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences_Second;
        final String Notification_old="old_notification";
        sharedPreferences_Second = getActivity().getSharedPreferences(Notification_old, Context.MODE_PRIVATE);
        int second = sharedPreferences_Second.getInt("old",00);


        sharedPreferences_frist = getActivity().getSharedPreferences(Notification_New, Context.MODE_PRIVATE);
        int frist = sharedPreferences_frist.getInt("new_data",00);
        int original_notification;
        if(frist>0){
            original_notification = frist-second;
        }else {
            original_notification = frist;
        }

        notificationtext.setText(""+original_notification);

    }


    public void notificationSharePreferenceNew(int new_noti){
        sharedPreferences_frist = getActivity().getSharedPreferences(Notification_New,Context.MODE_PRIVATE);
        SharedPreferences.Editor new_editor = sharedPreferences_frist.edit();
        new_editor.putInt("new_data",new_noti);
        new_editor.apply();
        new_editor.commit();
        getNewNotification();
    }


    public void getNewNotification(){
        sharedPreferences_frist = getActivity().getSharedPreferences(Notification_New,Context.MODE_PRIVATE);
        int frist = sharedPreferences_frist.getInt("new_data",00);

        SharedPreferences sharedPreferences_Second;
        final String Notification_old="old_notification";
        sharedPreferences_Second = getActivity().getSharedPreferences(Notification_old,Context.MODE_PRIVATE);
        int second = sharedPreferences_Second.getInt("old",00);
        int all_notification = frist-second;

        if(all_notification==-1  || all_notification==0){
            notificationtext.setText("0");
        }else {

            notificationtext.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
            notificationtext.setText(all_notification+"");

            if(all_notification>0){
//                notificationDialog(all_notification,notificationMdelList);
            }


        }


    }


    public void getNotification(){
        RetrofitInstance.getClient().getNotification(new PrefrenceManager(getContext()).getuserid()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        notificationSharePreferenceNew(jsonArray.length());

                        for (int i=0;i<jsonArray.length();i++){
                          NotificationMdel  notificationMdel = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),NotificationMdel.class);
                            notificationMdelList.add(notificationMdel);
                        }

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

  public void notificationDialog(int size,List<NotificationMdel> list){
     Dialog dialog = new Dialog(getActivity());
      dialog.setContentView(R.layout.notification_dialog);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
      dialog.getWindow().setWindowAnimations(R.style.AnimationforDialog);

      RecyclerView recyclerView = dialog.findViewById(R.id.notificationrecyclerview);
      TextView okbtn = dialog.findViewById(R.id.ok);

      DialogNotificationAdapter notificationAdapter = new DialogNotificationAdapter(size,list);
      recyclerView.setAdapter(notificationAdapter);


      okbtn.setOnClickListener(v ->{
          dialog.dismiss();
      });

      dialog.show();
  }



    public void getUserDetails(){

        RetrofitInstance.getClient().getUserDetails(new PrefrenceManager(getContext()).getuserid()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Boolean notificationstatus = Boolean.parseBoolean(jsonObject.getString("delivery_status"));
                    if(!notificationstatus){
                        binding.tempcolsedate.setVisibility(View.VISIBLE);
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

