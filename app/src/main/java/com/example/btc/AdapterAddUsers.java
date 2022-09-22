package com.example.btc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterAddUsers extends RecyclerView.Adapter<AdapterAddUsers.addUserHolder>{

    Context context;
    List<ModelClassAddUsers> users;

    public AdapterAddUsers(Context context, List<ModelClassAddUsers> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public addUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new addUserHolder(LayoutInflater.from(context).inflate(R.layout.row_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull addUserHolder holder, int position) {
        String userName = users.get(position).getName();
        holder.usernameAdd.setText(userName);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class addUserHolder extends RecyclerView.ViewHolder {
        TextView usernameAdd;

        public addUserHolder(@NonNull View itemView) {
            super(itemView);
            usernameAdd = itemView.findViewById(R.id.userNameAddField);
        }
    }
}
