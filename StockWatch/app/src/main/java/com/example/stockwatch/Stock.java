package com.example.stockwatch;

public class Stock {
    public String symbol;
    public String name;
    public double price;
    public double changePer;
    public double changePrice;

    public Stock(String _symbol, String _name, double _price, double _changePer, double _changePrice){
        symbol      = _symbol;
        price       = _price;
        name        = _name;
        changePer   = _changePer;
        changePrice = _changePrice;
    }
}
