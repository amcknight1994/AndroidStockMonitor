package com.example.stockwatch;

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
            //holder.PercentChange.(selectedNote.changePer);
            //holder.PriceChange.setText(selectedNote.changePrice);

            //holder.Price.setText(selectedNote.price);
        }


        @Override
        public int getItemCount() {
            return StockList.size();
        }
}


