package com.codiansoft.foodtruck.NewOrderRequests;

import android.view.View;
import android.widget.TextView;

import com.codiansoft.foodtruck.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ArtistViewHolder extends ChildViewHolder {

    private TextView Itemname;
    private TextView Quantity;
    private TextView price;

    public ArtistViewHolder(View itemView) {
        super(itemView);
        Itemname = (TextView) itemView.findViewById(R.id.menuitem);
        Quantity = (TextView) itemView.findViewById(R.id.qty);
        price = (TextView) itemView.findViewById(R.id.price);
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
}
