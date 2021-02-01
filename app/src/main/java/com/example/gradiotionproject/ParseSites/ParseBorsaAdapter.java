package com.example.gradiotionproject.ParseSites;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradiotionproject.R;

import java.util.ArrayList;

public class ParseBorsaAdapter extends RecyclerView.Adapter<ParseBorsaAdapter.ViewHolder> {

    private ArrayList<ParseBorsaItem> parseBorsaItems;
    private Context context;

    private String[] colors={"#9999ff","#6cf9eb","#75fbe2","#b9d3ee","#ccead5","#c7eae3","#f494ce","#d4e4d3"};

    private ParseBorsaAdapter.RecyclerViewClickListenerBorsa listenerBorsa;

    public ParseBorsaAdapter(ArrayList<ParseBorsaItem> parseBorsaItems, Context context, ParseBorsaAdapter.RecyclerViewClickListenerBorsa listenerBorsa) {
        this.parseBorsaItems = parseBorsaItems;
        this.context = context;
        this.listenerBorsa = listenerBorsa;
    }

    @NonNull
    @Override
    public ParseBorsaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_borsa_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseBorsaAdapter.ViewHolder holder, int position) {
        try {
            holder.bind(parseBorsaItems.get(position),colors,position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return parseBorsaItems.size();
    }

    public interface RecyclerViewClickListenerBorsa{
        void onClickBorsa(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewName;
        TextView textViewPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
        public void bind(ParseBorsaItem parseBorsa, String[] colors, Integer position) {

            itemView.setBackgroundColor(Color.parseColor(colors[position % 8]));
            textViewName = itemView.findViewById(R.id.textviewBorsa);
            textViewPrice = itemView.findViewById(R.id.textviewBorsaPrice);

            textViewName.setText(parseBorsa.getCompanyName());
            textViewPrice.setText(parseBorsa.getCompanyPrice());

        }

        @Override
        public void onClick(View v) {
            if (listenerBorsa != null) {
                listenerBorsa.onClickBorsa(v, getAdapterPosition());
            }
        }
    }
}
