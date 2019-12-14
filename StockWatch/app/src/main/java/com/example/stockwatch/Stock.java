package com.example.stockwatch;

public class Stock {
    public String symbol;
    public String name;
    public double price;
    public double changePer;
    public double changePrice;
    public String primaryEx;

    public Stock(String _symbol, String _name){
        symbol      = _symbol;
        name        = _name;
    }

    public Stock(String _symbol, String _name, double _price, double _changePrice, double _changePer, String primary_ex){
        symbol      = _symbol;
        price       = _price;
        name        = _name;
        changePer   = _changePer;
        changePrice = _changePrice;
        primaryEx   = primary_ex;
    }
}
