package com.app.veka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.veka.Network.PrefrenceManager;
import com.app.veka.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Animation animation = AnimationUtils.loadAnimation(this,R.anim.left_enter);
        binding.card.setAnimation(animation);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(2100);
                    if(new PrefrenceManager(getApplicationContext()).getuserid()!="0")
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    else
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                    finishAffinity();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}