package com.example.stockwatch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Stock> namesSymbols = new ArrayList<>();
    private ArrayList<Stock> Stocks = new ArrayList<>();
    private StockAdapter stockAdapter;
    private MainActivity mainActivity;
    private SwipeRefreshLayout swiper;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        mainActivity = this;
        stockAdapter = new StockAdapter(Stocks, this);

        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doRead(recyclerView);
        swiper = findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        new AsyncTasks(this).execute();

    }

    private boolean doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void doRefresh() {

        stockAdapter.notifyDataSetChanged();
        for (int i = 0; i < Stocks.size(); i++){
            new AsyncTask3(mainActivity).execute(Stocks.get(i).symbol);
        }
        swiper.setRefreshing(false);
    }

    protected void onPause(){
        super.onPause();
        doWrite(recyclerView);

    }

    public void doWrite(View v) {

        JSONArray jsonArray = new JSONArray();
        for (Stock n : Stocks) {
            try {
                JSONObject stockJSON = new JSONObject();
                stockJSON.put("symbol", n.symbol);
                stockJSON.put("name", n.name);
                stockJSON.put("price", n.price);
                stockJSON.put("change", n.changePrice);
                stockJSON.put("perChange", n.changePer);
                stockJSON.put("exchange", n.primaryEx);
                jsonArray.put(stockJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String jsonText = jsonArray.toString();

        Log.d(TAG, "doWrite: " + jsonText);

        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(
                            openFileOutput("Stocks.txt", Context.MODE_PRIVATE)
                    );

            outputStreamWriter.write(jsonText);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.d(TAG, "doWrite: File write failed: " + e.toString());
        }
    }

    public void doRead(View v) {
        Stocks.clear();
        try {
            InputStream inputStream = openFileInput("Stocks.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                String jsonText = stringBuilder.toString();

                try {
                    JSONArray jsonArray = new JSONArray(jsonText);
                    Log.d(TAG, "doRead: " + jsonArray.length());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String symbol = jsonObject.getString("symbol");
                        String name = jsonObject.getString("name");
                        double price = jsonObject.getDouble("price");
                        double change = jsonObject.getDouble("change");
                        double percent = jsonObject.getDouble("perChange");
                        String exchange = jsonObject.getString("exchange");
                        Stock n = new Stock(symbol, name, price, change, percent, exchange);
                        Stocks.add(n);
                    }

                    Log.d(TAG, "doRead: " + Stocks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        catch (FileNotFoundException e) {
            Log.d(TAG, "doRead: File not found: \" + e.toString()");
        } catch (IOException e) {
            Log.d(TAG, "doRead: Can not read file: " + e.toString());
        }
    }

    protected void onSaveInstanceState(Bundle outState){
        outState.putSerializable("Stocks", Stocks);
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstance){
        super.onRestoreInstanceState(savedInstance);
        Stocks = (ArrayList)savedInstance.getSerializable("Stocks");
        stockAdapter = new StockAdapter(Stocks, this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
    }

    public void showList(final ArrayList<Stock> listResult) {

        final String[] sArray = new String[listResult.size()];
        for (int i = 0; i < listResult.size(); i++)
            sArray[i] = listResult.get(i).symbol +" -  " + listResult.get(i).name;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a selection");

        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new AsyncTask2(mainActivity).execute(listResult.get(which).symbol);
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.add:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (doNetCheck()) {

                    builder.setMessage("Please Enter a Stock Symbol");
                    builder.setTitle("Stock Selection");
                    final EditText et = new EditText(this);
                    et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                    et.setGravity(Gravity.CENTER_HORIZONTAL);
                    builder.setView(et);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (et.getText().toString().length() == 4) {
                                new AsyncTask2(mainActivity).execute(et.getText().toString());
                            }
                            else if (et.getText().toString().length() <= 4){
                                 ArrayList<Stock> temp = new ArrayList<>();
                                for (int i = 0; i < namesSymbols.size(); i++){
                                    if (namesSymbols.get(i).symbol.contains(et.getText().toString())){
                                        temp.add(namesSymbols.get(i));
                                    }
                                }
                                showList(temp);
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                }
                else{
                    builder.setTitle("No network connection");
                    builder.setMessage("Stocks cannot be added without a network connection");
                }

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClick(View view) {
        String URL = "https://www.tradingview.com/symbols/";
        final int pos = recyclerView.getChildLayoutPosition(view);
        Stock thisStock = Stocks.get(pos);
        String symbol = thisStock.symbol;
        String exchange = thisStock.primaryEx;
        String exchangeAbb = "";
        if (exchange.equals("New York Stock Exchange")) exchangeAbb = "NYSE";
        if (exchange.equals("NASDAQ")) exchangeAbb = exchange;

        Uri url = Uri.parse(URL + exchangeAbb + "-" + symbol);
        Intent intent = new Intent(Intent.ACTION_VIEW, url);
        startActivity(intent);
    }


    public boolean onLongClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete stock symbol?");
        builder.setTitle("Delete Stock?");
        final int pos = recyclerView.getChildLayoutPosition(view);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Stocks.remove(pos);
                stockAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public void updateData(ArrayList<Stock> stockList) {
        namesSymbols.addAll(stockList);
        stockAdapter.notifyDataSetChanged();
    }

    public void acceptResults(Stock result) {
            boolean duplicate = false;
            int index = 0;
            for (int i = 0; i < Stocks.size(); i++){
                if (Stocks.get(i).symbol.equals(result.symbol)){
                    duplicate = true;
                }
            }
            if (duplicate) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Cannot add duplicate Stocks");
                builder.setTitle("Duplicate Stock");
                AlertDialog dialog = builder.create();
                dialog.show();

            }
            else{
                while (index < Stocks.size() && Stocks.get(index).symbol.compareTo(result.symbol) < 0){
                    index++;
                    if (index > Stocks.size()){
                        Stocks.add(result);
                        return;
                    }
                }
                Stocks.add(index, result);

                stockAdapter.notifyDataSetChanged();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void acceptUpdatedResults(Stock result) {
        boolean duplicate = false;
        int index = 0;
        for (int i = 0; i < Stocks.size(); i++){
            if (Stocks.get(i).symbol.equals(result.symbol)){
                duplicate = true;
                index = i;
            }
        }
        if (duplicate) {
            Stock toUpdate = Stocks.get(index);
            toUpdate.changePrice = result.changePrice;
            toUpdate.price = result.price;
            toUpdate.changePer = result.changePer;
        }
        else{
            while (Stocks.get(index).symbol.compareTo(result.symbol) < 0){
                index++;
            }
            Stocks.add(index, result);
            stockAdapter.notifyDataSetChanged();
        }
    }
}
