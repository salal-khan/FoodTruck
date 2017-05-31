package com.codiansoft.foodtruck.Models;


public class EDITMENU {


    String itemtitle;
    String itemcode;
    String itemprice;
    String item_id;

    public EDITMENU() {
    }

    public EDITMENU(String itemtitle, String itemcode, String itemprice, String item_id) {
        this.itemtitle = itemtitle;
        this.itemcode = itemcode;
        this.itemprice = itemprice;
        this.item_id = item_id;
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

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
