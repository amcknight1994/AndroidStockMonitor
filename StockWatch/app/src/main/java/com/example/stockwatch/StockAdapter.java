package com.example.stockwatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {

        private ArrayList<Stock> StockList;
        private MainActivity mainActivity;

        StockAdapter(ArrayList<Stock> list, MainActivity mainActivity){

            StockList = list;
            this.mainActivity = mainActivity;
        }

        @NonNull
        @Override
        public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stockview, parent,false);
            itemView.setOnClickListener(mainActivity);
            itemView.setOnLongClickListener(mainActivity);
            return new StockViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
            Stock selectedNote = StockList.get(position);
            holder.Name.setText(selectedNote.name);
            holder.Symbol.setText(selectedNote.symbol);
            holder.Price.setText(String.format("%.2f",selectedNote.price));
            holder.PercentChange.setText("(%" +String.format("%.2f" ,selectedNote.changePer) + ")");
            if (selectedNote.changePrice >= 0) {
                holder.PriceChange.setText("▲" + String.format("%.2f", selectedNote.changePrice));
                holder.PriceChange.setTextColor(Color.GREEN);
                holder.Price.setTextColor(Color.GREEN);
                holder.Name.setTextColor(Color.GREEN);
                holder.Symbol.setTextColor(Color.GREEN);
                holder.PercentChange.setTextColor(Color.GREEN);

            }
            else{
                holder.PriceChange.setText("▼" + String.format("%.2f", selectedNote.changePrice));
                holder.PriceChange.setTextColor(Color.RED);
                holder.Price.setTextColor(Color.RED);
                holder.Name.setTextColor(Color.RED);
                holder.Symbol.setTextColor(Color.RED);
                holder.PercentChange.setTextColor(Color.RED);
            }
        }

        @Override
        public int getItemCount() {
            return StockList.size();
        }
}


