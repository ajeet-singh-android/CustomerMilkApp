package com.app.veka;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.app.veka.Fragments.BillingFragment;
import com.app.veka.Fragments.DemandFragment;
import com.app.veka.Fragments.HomeFragment;
import com.app.veka.Fragments.NotificationFragment;
import com.app.veka.Fragments.ProfileFragment;
import com.app.veka.Fragments.SupliesFragment;
import com.app.veka.Network.PrefrenceManager;
import com.app.veka.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static TextView notificationtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificationtext = findViewById(R.id.notificationcount);


        binding.username.setText("Hii "+new PrefrenceManager(getApplicationContext()).getUserName()+"!");


        binding.notification.setOnClickListener(v ->{
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new NotificationFragment()).addToBackStack(null).commit();
        });

        Fragment  fragment = new HomeFragment();
        ExchengeFregment(fragment);



        Animation myFadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        Animation myFadeOutAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fadeout);
        binding.belllayout.startAnimation(myFadeInAnimation);
        binding.belllayout.startAnimation(myFadeOutAnimation);



        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment1 = null;
            switch (item.getItemId()) {
                case R.id.home:
                    binding.logout.setVisibility(View.GONE);
                    fragment1 = new HomeFragment();
                    break;

                case R.id.supplies:
                    binding.logout.setVisibility(View.GONE);
                    fragment1 = new SupliesFragment();
                    break;

                case R.id.billing:
                    binding.logout.setVisibility(View.GONE);
                    fragment1 = new BillingFragment();
                    break;

                case R.id.demand:
                    binding.logout.setVisibility(View.GONE);
                    fragment1 = new DemandFragment();
                    break;

                case R.id.profile:
                    binding.logout.setVisibility(View.VISIBLE);
                    fragment1 = new ProfileFragment();
                    break;
            }
            ExchengeFregment(fragment1);
            return true;
        });


        binding.logout.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("LogOut")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new PrefrenceManager(getApplicationContext()).logout();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finishAffinity();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        });





    }


    private void ExchengeFregment(Fragment selectedFragment) {
        if(selectedFragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
        }
    }


}