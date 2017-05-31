package com.codiansoft.foodtruck.Models;


import com.orm.SugarRecord;

public class MENU extends SugarRecord {


    String itemtitle;
    String itemcode;
    String itemprice;
    long ownerid;
    Boolean isSeleccted;

    public MENU() {
    }

    public MENU(String itemtitle, String itemcode, String itemprice, long ownerid, Boolean isSeleccted) {
        this.itemtitle = itemtitle;
        this.itemcode = itemcode;
        this.itemprice = itemprice;
        this.ownerid = ownerid;
        this.isSeleccted = isSeleccted;
    }

    public Boolean getSeleccted() {
        return isSeleccted;
    }

    public void setSeleccted(Boolean seleccted) {
        isSeleccted = seleccted;
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
