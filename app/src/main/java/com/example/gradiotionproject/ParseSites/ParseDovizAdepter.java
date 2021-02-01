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

public class ParseDovizAdepter extends RecyclerView.Adapter<ParseDovizAdepter.ViewHolder> {

    private ArrayList<ParseDovizItem> parseDovizItems;
    private Context context;
    private String[] colors={"#9999ff","#6cf9eb","#75fbe2","#b9d3ee","#ccead5","#c7eae3","#f494ce","#d4e4d3"};

    private RecyclerViewClickListener listener;

    public ParseDovizAdepter(ArrayList<ParseDovizItem> parseDovizItems, Context context, RecyclerViewClickListener listener) {
        this.parseDovizItems = parseDovizItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParseDovizAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_doviz_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
        public void onBindViewHolder(@NonNull ParseDovizAdepter.ViewHolder holder, int position) {
        try {
            holder.bind(parseDovizItems.get(position),colors,position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return parseDovizItems.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewName;
        TextView textViewPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(ParseDovizItem parseDovizItem, String[] colors, int position) {

            itemView.setBackgroundColor(Color.parseColor(colors[position % 8]));
            textViewName = itemView.findViewById(R.id.textviewDoviz);
            textViewPrice = itemView.findViewById(R.id.textviewDovizPrice);

            textViewName.setText(parseDovizItem.getCurrencyName());
            textViewPrice.setText(parseDovizItem.getCurrencySale());
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onClick(view, getAdapterPosition());
            }
        }
    }
}
