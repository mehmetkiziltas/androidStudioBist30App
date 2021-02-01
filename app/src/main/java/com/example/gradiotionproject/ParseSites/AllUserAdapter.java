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

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.PostHolder> {
    private ArrayList<String> userNameList;
    private ArrayList<Float> userMoneyList;
    private ArrayList<String> userMoneyTeypeList;
    private ArrayList<Float> userKazancYuzdeligiList;

    private String[] colors={"#9999ff","#6cf9eb","#75fbe2","#b9d3ee","#ccead5","#c7eae3","#f494ce","#d4e4d3"};

    public AllUserAdapter(ArrayList<String> userEmailList, ArrayList<Float> userMoneyList, ArrayList<String> userMoneyTeypeList, ArrayList<Float> userKazancYuzdeligiList) {
        this.userNameList = userEmailList;
        this.userMoneyList = userMoneyList;
        this.userMoneyTeypeList = userMoneyTeypeList;
        this.userKazancYuzdeligiList = userKazancYuzdeligiList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.allusers_row, parent, false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.userName.setText(userNameList.get(position));
        holder.userMoney.setText(""+userMoneyList.get(position));
        holder.userMoneyType.setText(userMoneyTeypeList.get(position));
        if (userKazancYuzdeligiList.get(position) < 0) {
            holder.userYuzdelik.setTextColor(Color.parseColor("#d62828"));
        } else {
            holder.userYuzdelik.setTextColor(Color.parseColor("#19ffaf"));
        }
        holder.userYuzdelik.setText("%"+userKazancYuzdeligiList.get(position));
        holder.itemView.setBackgroundColor(Color.parseColor(colors[position % 8]));
    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        private TextView userName;
        private TextView userMoney;
        private TextView userMoneyType;
        private TextView userYuzdelik;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
//            itemView.setBackgroundColor(Color.parseColor(colors[position % 8]));
//            userName = itemView.findViewById(R.id.recyclerViewAllUsers);
            userName = itemView.findViewById(R.id.userNametxtView);
            userMoney = itemView.findViewById(R.id.userMoneytxtView);
            userMoneyType = itemView.findViewById(R.id.userMoneyTypetxtView);
            userYuzdelik = itemView.findViewById(R.id.yuzdeliktxtView);
        }
    }
}
