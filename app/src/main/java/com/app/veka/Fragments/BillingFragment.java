package com.app.veka.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.veka.Network.PrefrenceManager;
import com.app.veka.Network.RetrofitInstance;
import com.app.veka.R;
import com.app.veka.databinding.FragmentBillingBinding;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingFragment extends Fragment {

    FragmentBillingBinding binding;
    ProgressDialog progressDialog;

    ImageView barcodeimage;
    TextView upload;
    String encodeimage = "";
    Dialog dialog;

    Boolean paytype = true;

    public BillingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillingBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Billing();

        Log.d("akash@123", "onResponse: "+new PrefrenceManager(getContext()).getuserid());




        binding.paynow.setOnClickListener(v -> {
             dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.payment_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setWindowAnimations(R.style.AnimationforDialog);

            upload = dialog.findViewById(R.id.upload);
            barcodeimage = dialog.findViewById(R.id.image);

            RadioGroup radioGroup = dialog.findViewById(R.id.radigroup);
            EditText editText = dialog.findViewById(R.id.amounttext);
            TextView payment = dialog.findViewById(R.id.payment);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if(i==R.id.online){
                        paytype = true;
                        barcodeimage.setVisibility(View.VISIBLE);
                        upload.setVisibility(View.VISIBLE);
                        editText.setVisibility(View.GONE);
                        payment.setVisibility(View.GONE);
                    } else  if(i==R.id.cash){
                        paytype = false;
                        barcodeimage.setVisibility(View.GONE);
                        upload.setVisibility(View.GONE);
                        editText.setVisibility(View.VISIBLE);
                        payment.setVisibility(View.VISIBLE);

                    }
                }
            });


            payment.setOnClickListener(view1 -> {
                if(!paytype) {
                    String money = editText.getText().toString().trim();
                    if(!money.isEmpty()){
                        progreessDialog();
                        RetrofitInstance.getClient().sendCash(new PrefrenceManager(getContext()).getuserid(),money).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                progressDialog.dismiss();
                                try {
                                    JSONObject jsonObject  = new JSONObject(response.body().toString());
                                    if(jsonObject.getString("status").equalsIgnoreCase("success")){
                                        dialog.dismiss();
                                        addNotification(new PrefrenceManager(getContext()).getUserName()+" Payment Success : "+money +" Rupees");
                                        Toasty.success(getActivity(),"Payment Send Successfully",Toasty.LENGTH_SHORT).show();
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
                    }else {
                        Toasty.error(getActivity(),"Please Enter Amount",Toasty.LENGTH_SHORT).show();
                    }

                }
            });


            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!upload.getText().toString().equalsIgnoreCase("Upload Image")){
                        Dexter.withContext(getActivity()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, 101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.cancelPermissionRequest();
                            }
                        }).check();

                        dialog.show();
                    }
                    else {

                        if(encodeimage!=""){
                            progreessDialog();
                            RetrofitInstance.getClient().sendImage(new PrefrenceManager(getContext()).getuserid(),encodeimage).enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                    Log.d("akash@123", "onResponse: "+response.body().toString());
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().toString());
                                        if(jsonObject.getString("status").equalsIgnoreCase("success")){
                                            dialog.dismiss();
                                            Toasty.success(getActivity(), "Payment Screenshot Upload Successfully", Toast.LENGTH_SHORT, true).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Log.d("akash@123", "onResponse: "+t.toString());
                                    progressDialog.dismiss();
                                }
                            });
                        }else {
                            progressDialog.dismiss();
                            Toasty.error(getActivity(),"Please Select Image",Toasty.LENGTH_SHORT).show();
                        }




                    }
                }
            });
            dialog.show();

    });


    }
    public void Billing() {
        Log.e("TAG", "Billing: "+new PrefrenceManager(getContext()).getuserid() );

        progreessDialog();
        RetrofitInstance.getClient().Billing(new PrefrenceManager(getContext()).getuserid()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    binding.runningbill.setText(jsonObject.getString("running_bill"));
                    binding.previousbill.setText(jsonObject.getString("previous_bill"));
                    binding.unpaidbill.setText(jsonObject.getString("unpaid"));
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("last_pay");
                    binding.lastpaidbill.setText(jsonObject.getString("last_pay"));
//                    binding.unpaidbill.setText(jsonObject1.getString("panding"));
                    if(jsonObject.getString("advance")==null){
                        binding.adavace.setText("0");
                    }else {
                        binding.adavace.setText(jsonObject.getString("advance"));
                    }
                    if(Double.parseDouble(jsonObject.getString("unpaid"))==0)
                        binding.paynow.setEnabled(false);

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


    public void progreessDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progressbaritems);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            Uri imageUri = data.getData();
        barcodeimage.setImageURI(imageUri);
        upload.setText("Upload Image");

        InputStream inputStream = null;
        try {
            inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
        encodeBitmapImage(bitmap);


    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytesofimage =byteArrayOutputStream.toByteArray();
        encodeimage = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        Log.e("Addfs@2323", "encodeBitmapImage: "+encodeimage);
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