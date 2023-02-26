package com.app.veka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.veka.R;
import com.app.veka.UserModel.CalenderModel;
import com.app.veka.databinding.CustomculenderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class CustomCalenderAdapter extends RecyclerView.Adapter<CustomCalenderAdapter.exViewHolder> {
    Context context;
    List<CalenderModel> list = new ArrayList<>();
    int pos = -2;

    public CustomCalenderAdapter(Context context, List<CalenderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public exViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new exViewHolder(LayoutInflater.from(context).inflate(R.layout.customculender_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull exViewHolder holder, int position) {
        holder.binding.text.setText(String.valueOf(list.get(position).getDate()));

        holder.itemView.setOnClickListener(view -> {
            if(list.get(position).isSelect()){
                list.get(position).setSelect(false);
                holder.binding.text.setBackground(null);
            }else {
                list.get(position).setSelect(true);
                holder.binding.text.setBackground(ContextCompat.getDrawable(context, R.drawable.selcted_background));

            }

        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class exViewHolder extends RecyclerView.ViewHolder {
        CustomculenderItemBinding binding;

        public exViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomculenderItemBinding.bind(itemView);
        }
    }
}
