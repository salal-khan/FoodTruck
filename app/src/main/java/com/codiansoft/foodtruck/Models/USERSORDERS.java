package com.codiansoft.foodtruck.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class USERSORDERS extends ExpandableGroup<USERORDERMENUITEMS> {


    private String orderid;
    private String status;
    private String datetime;
    private String Orderdatetime;
    private String name;
    private String totalprice;
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOrderdatetime() {
        return Orderdatetime;
    }

    public void setOrderdatetime(String orderdatetime) {
        Orderdatetime = orderdatetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public USERSORDERS(String orderid, String status, String totalprice, String datetime, String Orderdatetime, String name, String latitude, String longitude, List<USERORDERMENUITEMS> items) {
        super(status, items);
        this.orderid = orderid;
        this.totalprice = totalprice;
        this.datetime = datetime;
        this.name = name;
        this.Orderdatetime = Orderdatetime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }
}

