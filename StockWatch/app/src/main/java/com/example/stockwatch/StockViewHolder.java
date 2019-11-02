package com.example.stockwatch;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class StockViewHolder extends RecyclerView.ViewHolder{

    TextView Name;
    TextView Symbol;
    TextView Price;
    TextView PriceChange;
    TextView PercentChange;

    StockViewHolder(View view){
        super(view);
        Name =         view.findViewById(R.id.stockName);
        Symbol =      view.findViewById(R.id.stockSymbol);
        //these values are going to be constantly updating, no need to set values from other place
        //Price =  view.findViewById(R.id.currentPrice);
        //PriceChange = view.findViewById(R.id.dailyPriceChange);
        //PercentChange = view.findViewById(R.id.dailyPercentChange);
    }

}
