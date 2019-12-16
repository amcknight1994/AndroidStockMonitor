package com.example.stockwatch.AsyncTasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stockwatch.Main.MainActivity;
import com.example.stockwatch.Stock.Stock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncTasks extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity;

    private static final String DATA_URL =
            "https://api.iextrading.com/1.0/ref-data/symbols";

    private static final String TAG = "AsyncSymbolLoader";

    public AsyncTasks(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected void onPostExecute(String s) {

        ArrayList<Stock> StockList = parseJSON(s);

        if (StockList != null)
            mainActivity.updateData(StockList);
    }


    @Override
    protected String doInBackground(String... params) {

        Uri dataUri = Uri.parse(DATA_URL);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "doInBackground: ResponseCode: " + conn.getResponseCode());

            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            Log.d  (TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return sb.toString();
    }


    private ArrayList<Stock> parseJSON(String s) {

        ArrayList<Stock> StockList = new ArrayList<>();
        try {

            JSONArray jObjMain = new JSONArray(s);

            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject stock = (JSONObject) jObjMain.get(i);
                String name = stock.getString("name");
                String symbol = stock.getString("symbol");
                StockList.add(new Stock(symbol, name));

            }

            return StockList;
        } catch (Exception e) {

            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


}

