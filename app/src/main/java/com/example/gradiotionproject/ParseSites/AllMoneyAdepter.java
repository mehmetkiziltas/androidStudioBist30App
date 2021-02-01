package com.example.gradiotionproject.ParseSites;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradiotionproject.R;

import java.util.ArrayList;

public class AllMoneyAdepter extends RecyclerView.Adapter<AllMoneyAdepter.PostHolder>  {

    private ArrayList<String> userTypeList;

    private String[] colors={"#9999ff","#6cf9eb","#75fbe2","#b9d3ee","#ccead5","#c7eae3","#f494ce","#d4e4d3"};

    public AllMoneyAdepter(ArrayList<String> userTypeList) {
        this.userTypeList = userTypeList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.all_money_row, parent, false);
        return new AllMoneyAdepter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllMoneyAdepter.PostHolder holder, int position) {
        try {
            holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]));
            holder.userType.setText(userTypeList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return userTypeList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        private TextView userType;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            userType = itemView.findViewById(R.id.textViewAlaninDegeri);
        }
    }
}
