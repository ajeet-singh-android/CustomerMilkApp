package com.app.veka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.app.veka.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    List<Integer> imglist;
    Context context;

    public SliderAdapter(List<Integer> imglist, Context context) {
        this.imglist = imglist;
        this.context = context;

    }

    @Override
    public int getCount() {
        return imglist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_items,container,false);
        ImageView imageView = view.findViewById(R.id.img);
        imageView.setImageResource(imglist.get(position));
        Picasso.get().load(imglist.get(position)).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View) object);
    }

}


