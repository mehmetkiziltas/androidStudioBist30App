package com.example.gradiotionproject.ParseSites;

public class ParseBorsaItem {
    private String companyName;
    private String companyPrice;
    private String differancePrice;
    private String time;
    private String hacimLot;
    private String hacimTl;
    private String yuksekFiyat;
    private String dusukFiyat;

    public ParseBorsaItem(String companyName, String companyPrice, String differancePrice, String time,String hacimLot,String hacimTl,String yuksekFiyat,String dusukFiyat) {
        this.companyName = companyName;
        this.companyPrice = companyPrice;
        this.differancePrice = differancePrice;
        this.time = time;
        this.hacimLot=hacimLot;
        this.hacimTl=hacimTl;
        this.yuksekFiyat=yuksekFiyat;
        this.dusukFiyat=dusukFiyat;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPrice() {
        return companyPrice;
    }

    public void setCompanyPrice(String companyPrice) {
        this.companyPrice = companyPrice;
    }

    public String getDifferancePrice() {
        return differancePrice;
    }

    public void setDifferancePrice(String differancePrice) {
        this.differancePrice = differancePrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHacimLot() {
        return hacimLot;
    }

    public void setHacimLot(String hacimLot) {
        this.hacimLot = hacimLot;
    }

    public String getHacimTl() {
        return hacimTl;
    }

    public void setHacimTl(String hacimTl) {
        this.hacimTl = hacimTl;
    }

    public String getYuksekFiyat() {
        return yuksekFiyat;
    }

    public void setYuksekFiyat(String yuksekFiyat) {
        this.yuksekFiyat = yuksekFiyat;
    }

    public String getDusukFiyat() {
        return dusukFiyat;
    }

    public void setDusukFiyat(String dusukFiyat) {
        this.dusukFiyat = dusukFiyat;
    }
}
