package com.santosh.creditmanagementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<User> nowUser;

    public UserAdapter(Context mContext, ArrayList<User> nowUser) {
        this.mContext = mContext;
        this.nowUser = nowUser;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        final User listItem = nowUser.get(position);
        holder.name.setText(listItem.getUser_name());
        holder.id.setText(listItem.getUser_id());
        holder.credit.setText(listItem.getUser_credits());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserActivity.class);
                intent.putExtra("id", listItem.getUser_id());
                intent.putExtra("name", listItem.getUser_name());
                intent.putExtra("email", listItem.getUser_email());
                intent.putExtra("phone", listItem.getUser_phone());
                intent.putExtra("credits", listItem.getUser_credits());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nowUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, id, credit;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.card_name);
            id = itemView.findViewById(R.id.card_id);
            credit = itemView.findViewById(R.id.card_credit);
            layout = itemView.findViewById(R.id.card_layout);
        }
    }
}