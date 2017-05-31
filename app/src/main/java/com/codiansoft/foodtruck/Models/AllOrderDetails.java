package com.codiansoft.foodtruck.Models;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by codiansoft on 5/16/2017.
 */

public class AllOrderDetails {

    String order_id;
    String OnwerName;
    String created_time;
    String delivery_time;
    String total_amount;


    public AllOrderDetails() {


    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOnwerName() {
        return OnwerName;
    }

    public void setOnwerName(String onwerName) {
        OnwerName = onwerName;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }


}
