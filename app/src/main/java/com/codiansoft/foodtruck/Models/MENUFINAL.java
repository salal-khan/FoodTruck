package com.codiansoft.foodtruck.Models;


public class MENUFINAL {

    String menuid;
    String itemtitle;
    String itemcode;
    String itemprice;
    String qty;
    long ownerid;

    public MENUFINAL() {
    }

    public MENUFINAL(String menuid, String itemtitle, String itemcode, String itemprice, long ownerid, String qty) {
        this.itemtitle = itemtitle;
        this.itemcode = itemcode;
        this.itemprice = itemprice;
        this.ownerid = ownerid;
        this.qty = qty;
        this.menuid = menuid;

    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(long ownerid) {
        this.ownerid = ownerid;
    }
}
