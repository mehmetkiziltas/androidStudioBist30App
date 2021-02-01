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

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.PostHolder> {
    private ArrayList<Float> userMoneyList;
    private ArrayList<String> userMoneyTeypeList;
    private ArrayList<String> userDateList;


    private String[] colors={"#9999ff","#6cf9eb","#75fbe2","#b9d3ee","#ccead5","#c7eae3","#f494ce","#d4e4d3"};

    public AccountAdapter(ArrayList<Float> userMoneyList, ArrayList<String> userMoneyTeypeList, ArrayList<String> userDateList) {
        this.userMoneyList = userMoneyList;
        this.userMoneyTeypeList = userMoneyTeypeList;
        this.userDateList = userDateList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.account_row, parent, false);

        return new AccountAdapter.PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]));
        //holder.userName.setText(userNameList.get(position));
        holder.userMoney.setText(""+userMoneyList.get(position));
        holder.userMoneyType.setText(userMoneyTeypeList.get(position));
        holder.userDate.setText(userDateList.get(position));
    }

    @Override
    public int getItemCount() {
        return userMoneyList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        private TextView userMoney;
        private TextView userMoneyType;
        private TextView userDate;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            userMoney = itemView.findViewById(R.id.textViewMoney);
            userMoneyType = itemView.findViewById(R.id.textViewMoneyType);
            userDate = itemView.findViewById(R.id.textViewMoneyDate);
        }
    }
}
