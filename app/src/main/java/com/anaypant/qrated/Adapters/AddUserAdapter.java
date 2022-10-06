package com.anaypant.qrated.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anaypant.qrated.R;
import com.anaypant.qrated.Frames.ModelUser;
import com.anaypant.qrated.Interfaces.AdapterListeners.AddUserListener;

import java.util.List;

public class AddUserAdapter extends RecyclerView.Adapter<AddUserAdapter.addUserHolder> {
    Context context;
    List<ModelUser> users;
    AddUserListener listener;

    public AddUserAdapter(Context context, List<ModelUser> arr, AddUserListener listener){
        this.context = context;
        this.users = arr;
        this.listener = listener;

    }


    @NonNull
    @Override
    public addUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new addUserHolder(LayoutInflater.from(context).inflate(R.layout.add_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull addUserHolder holder, int position) {
        String userName = users.get(position).getName();
        holder.userNameAdd.setText(userName);
        holder.friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddFriendClick(users.get(holder.getAdapterPosition()), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class addUserHolder extends RecyclerView.ViewHolder {
        TextView userNameAdd;
        ImageButton friendButton;
        public addUserHolder(@NonNull View itemView) {
            super(itemView);
            userNameAdd = itemView.findViewById(R.id.userNameAddField);
            friendButton = itemView.findViewById(R.id.friendUserButton);

        }
    }
}
