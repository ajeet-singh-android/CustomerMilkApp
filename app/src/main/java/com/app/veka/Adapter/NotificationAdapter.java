package com.app.veka.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.veka.R;
import com.app.veka.UserModel.NotificationMdel;
import com.app.veka.databinding.NotificationItemsBinding;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationDataHolder> {

    List<NotificationMdel> list;

    public NotificationAdapter(List<NotificationMdel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationDataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationDataHolder holder, int position) {
        holder.binding.notificationtext.setText(list.get(position).getMsg());
        holder.binding.staus.setText(list.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NotificationDataHolder extends RecyclerView.ViewHolder {

        NotificationItemsBinding binding;

        public NotificationDataHolder(@NonNull View itemView) {
            super(itemView);
            binding = NotificationItemsBinding.bind(itemView.getRootView());
        }
    }
}
