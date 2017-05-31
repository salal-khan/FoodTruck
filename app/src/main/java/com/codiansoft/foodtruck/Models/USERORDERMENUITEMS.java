package com.codiansoft.foodtruck.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class USERORDERMENUITEMS implements Parcelable {

    private String name;
    private String price;
    private String quantity;

    public USERORDERMENUITEMS() {
    }

    public USERORDERMENUITEMS(String name, String quantity, String price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;

    }

    protected USERORDERMENUITEMS(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }


    public static final Creator<USERORDERMENUITEMS> CREATOR = new Creator<USERORDERMENUITEMS>() {
        @Override
        public USERORDERMENUITEMS createFromParcel(Parcel in) {
            return new USERORDERMENUITEMS(in);
        }

        @Override
        public USERORDERMENUITEMS[] newArray(int size) {
            return new USERORDERMENUITEMS[size];
        }
    };
}

