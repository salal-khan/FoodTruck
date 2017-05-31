package com.codiansoft.foodtruck.expand;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.codiansoft.foodtruck.DirectionMap;
import com.codiansoft.foodtruck.FragmentTruckMenu;
import com.codiansoft.foodtruck.R;
import com.google.android.gms.maps.model.LatLng;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ArtistViewHolder extends ChildViewHolder {

    private TextView Itemname;
    private TextView Quantity;
    private TextView price;

    public TextView getOrderId() {
        return orderId;
    }

    public void setOrderId(TextView orderId) {
        this.orderId = orderId;
    }

    public TextView getOrderOwnerName() {
        return orderOwnerName;
    }

    public void setOrderOwnerName(TextView orderOwnerName) {
        this.orderOwnerName = orderOwnerName;
    }

    public TextView getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(TextView orderTime) {
        this.orderTime = orderTime;
    }

    public TextView getOrderDeliveryTime() {
        return orderDeliveryTime;
    }

    public void setOrderDeliveryTime(TextView orderDeliveryTime) {
        this.orderDeliveryTime = orderDeliveryTime;
    }

    public TextView getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(TextView orderTotal) {
        this.orderTotal = orderTotal;
    }

    public LinearLayout getLinearLayoutDetails() {
        return linearLayoutDetails;
    }

    public void setLinearLayoutDetails(LinearLayout linearLayoutDetails) {
        this.linearLayoutDetails = linearLayoutDetails;
    }

    private TextView orderId;
    private TextView orderOwnerName;
    private TextView orderTime;
    private TextView orderDeliveryTime;
    private TextView orderTotal;
    private LinearLayout linearLayoutDetails;
    private Button directionbtn;

    public ArtistViewHolder(View itemView) {
        super(itemView);
        Itemname = (TextView) itemView.findViewById(R.id.menuitem);
        Quantity = (TextView) itemView.findViewById(R.id.qty);
        price = (TextView) itemView.findViewById(R.id.price);
        orderId = (TextView) itemView.findViewById(R.id.OrderIdtxt);
        orderOwnerName = (TextView) itemView.findViewById(R.id.OrderOnwerNametxt);
        orderTime = (TextView) itemView.findViewById(R.id.OrderTimetxt);
        orderDeliveryTime = (TextView) itemView.findViewById(R.id.OrderDeliveryTimetxt);
        orderTotal = (TextView) itemView.findViewById(R.id.TotalAmounttxt);
        linearLayoutDetails = (LinearLayout) itemView.findViewById(R.id.listOrderDetail);
        directionbtn = (Button) itemView.findViewById(R.id.btnDirction);
    }

    public void setArtistName(String name) {
        Itemname.setText(name);
    }

    public void setArtistQuantity(String q) {
        Quantity.setText(q);
    }

    public void setArtistPrice(String p) {
        price.setText(p);
    }


    public LinearLayout getOrderDetails() {
        return linearLayoutDetails;
    }

    public void setOrderId(String i) {
        orderId.setText(i);
    }

    public void setOrderName(String i) {
        orderOwnerName.setText(i);
    }

    public void setOrderDatetime(String i) {
        orderTime.setText(i);
    }

    public void setOrderDeliveryDateTime(String i) {
        orderDeliveryTime.setText(i);
    }

    public void setOrderTotalAmount(String i) {
        orderTotal.setText(i);
    }


    public void dirctionlistner(final Context cc, final String lat, final String longs) {
        directionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cc, DirectionMap.class);
                intent.putExtra("lat", lat);
                intent.putExtra("long", longs);
                cc.startActivity(intent);


            }
        });
    }
}
