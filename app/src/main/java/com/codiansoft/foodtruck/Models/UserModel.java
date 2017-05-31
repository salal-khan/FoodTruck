package com.codiansoft.foodtruck.Models;


public class UserModel {

    String id;
    String name;
    String Username;
    String email;
    String profilepic;
    String phonenumber;
    String Platenumber;
    String Api_secret;
    String Description;
    String Usertype;
    String istrackon;

    public UserModel() {
    }

    public UserModel(String id, String name, String username, String email, String profilepic, String phonenumber, String platenumber, String api_secret, String description, String usertype, String istrackon) {
        this.id = id;
        this.name = name;
        Username = username;
        this.email = email;
        this.profilepic = profilepic;
        this.phonenumber = phonenumber;
        Platenumber = platenumber;
        Api_secret = api_secret;
        Description = description;
        Usertype = usertype;
        this.istrackon = istrackon;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPlatenumber() {
        return Platenumber;
    }

    public void setPlatenumber(String platenumber) {
        Platenumber = platenumber;
    }

    public String getApi_secret() {
        return Api_secret;
    }

    public void setApi_secret(String api_secret) {
        Api_secret = api_secret;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUsertype() {
        return Usertype;
    }

    public void setUsertype(String usertype) {
        Usertype = usertype;
    }

    public String getIstrackon() {
        return istrackon;
    }

    public void setIstrackon(String istrackon) {
        this.istrackon = istrackon;
    }
}
