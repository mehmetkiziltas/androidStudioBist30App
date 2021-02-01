package com.example.gradiotionproject.ParseSites;

public class ParseDovizItem {
    private String currencyName;
    private String currencySale;
    private String currencyChange;
    private String currencyTime;

    public ParseDovizItem(String currencyName, String currencySale, String currencyChange, String currencyTime) {
        this.currencyName = currencyName;
        this.currencySale = currencySale;
        this.currencyChange = currencyChange;
        this.currencyTime = currencyTime;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySale() {
        return currencySale;
    }

    public void setCurrencySale(String currencySale) {
        this.currencySale = currencySale;
    }

    public String getCurrencyChange() {
        return currencyChange;
    }

    public void setCurrencyChange(String currencyChange) {
        this.currencyChange = currencyChange;
    }

    public String getCurrencyTime() {
        return currencyTime;
    }

    public void setCurrencyTime(String currencyTime) {
        this.currencyTime = currencyTime;
    }
}
