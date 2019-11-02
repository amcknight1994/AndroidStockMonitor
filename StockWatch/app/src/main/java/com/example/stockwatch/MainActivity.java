package com.example.stockwatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private ArrayList<Stock> Stocks = new ArrayList<>();
    private StockAdapter stockAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onPause(){
        super.onPause();
       // doWrite(recyclerView);

    }

    //JSON
    //Save for later, more convenient to not save our data at this point
    public void doWrite(View v) {

        JSONArray jsonArray = new JSONArray();
    }

    public void doRead(View v) {

    }

    protected void onSaveInstanceState(Bundle outState){
      //  outState.putSerializable("Stocks", Stocks);
       super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstance){
        super.onRestoreInstanceState(savedInstance);
       // Stocks = (ArrayList)savedInstance.getSerializable("Stocks");
        //stockAdapter = new StockAdapter(Stocks, this);
        //recyclerView.setAdapter(stockAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager((this)));
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case R.id.add:
                //if not online, show error dialog
                //else
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please Enter a Stock Symbol");
                builder.setTitle("Stock Selection");
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //use info in et to parse internet data
                        //if cannot find, return error
                        //if only matches one add to recycler view
                        //if matches several (partial symbol) shows list to choose from
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClick(View view) {
        //opens web page to this stock symbol

    }


    public boolean onLongClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete stock symbol?");
        builder.setTitle("Delete Stock?");
        builder.getContext();
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //remove from StockList
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
}
