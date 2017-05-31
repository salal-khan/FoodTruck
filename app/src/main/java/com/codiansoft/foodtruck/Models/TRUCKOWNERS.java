package com.codiansoft.foodtruck.Models;


import com.orm.SugarRecord;

public class TRUCKOWNERS extends SugarRecord {


    String name;
    String username;
    String email;
    String description;
    String photo;
    String contact_no;
    String lat;
    String lng;
    String Category;
    String km;
    public TRUCKOWNERS() {
    }


    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }


    public TRUCKOWNERS(String name, String username, String email, String description, String photo, String contact_no, String lat, String lng, String Category) {

        this.name = name;
        this.username = username;
        this.email = email;
        this.description = description;
        this.photo = photo;
        this.contact_no = contact_no;
        this.lat = lat;
        this.lng = lng;
        this.Category = Category;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }


}
