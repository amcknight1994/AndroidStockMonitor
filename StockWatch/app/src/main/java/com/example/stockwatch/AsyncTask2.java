package com.example.stockwatch;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncTask2 extends AsyncTask<String, Void, String> {
    private static final String TAG = "API_AsyncTask";
    @SuppressLint("StaticFieldLeak")
    private MainActivity mainActivity;

    private JSONArray allResults = new JSONArray();


    AsyncTask2(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(String s) {

        Stock selected = parseJSON(s);

        if (selected != null)
            mainActivity.acceptResults(selected);
    }


    @Override
    protected String doInBackground(String... params) {

        String urlToUse = "https://cloud.iexapis.com/stable/stock/" + params[0] + "/quote?token=sk_eb387372caf743af80d6eb6cf79f77e8";
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


    private Stock parseJSON(String s) {

        Stock toReturn = null;
        try {
            JSONObject jObjMain = new JSONObject();

                JSONObject jsonStock = new JSONObject(s);
                String name = jsonStock.getString("companyName");
                String symbol = jsonStock.getString("symbol");
                double price = jsonStock.getDouble("latestPrice");
                double change = jsonStock.getDouble("change");
                double percent = jsonStock.getDouble("changePercent");
                toReturn = new Stock(symbol, name, price, change, percent);

            return toReturn;
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

