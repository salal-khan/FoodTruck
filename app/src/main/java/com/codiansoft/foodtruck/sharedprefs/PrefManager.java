package com.codiansoft.foodtruck.sharedprefs;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.codiansoft.foodtruck.Models.UserModel;
import com.google.gson.Gson;


public class PrefManager {
    private static final String TAG = PrefManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static Gson GSON = new Gson();
    // Sharedpref file name
    private static final String PREF_NAME = "com.codiansoft.foodtruck";

    private static final String KEY_LOGIN_TYPE = "login_type";
    private static final String IS_DEVICE_SYNC = "IS_DEVICE_SYNC";
    private static final String KEY_USER = "user";
    private static final String KEY_LASTORDERTIME = "KEY_LASTORDERTIME";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

    }

    public void setLasttime(long is) {
        editor = pref.edit();

        editor.putLong(KEY_LASTORDERTIME, is);

        // commit changes
        editor.commit();
    }

    public Long getLasttime() {

        return pref.getLong(KEY_LASTORDERTIME, 0L);
    }


    public void setLoginType(String type) {
        editor = pref.edit();

        editor.putString(KEY_LOGIN_TYPE, type);

        // commit changes
        editor.commit();
    }

    public void setIsDeviceSync(Boolean is) {
        editor = pref.edit();

        editor.putBoolean(IS_DEVICE_SYNC, is);

        // commit changes
        editor.commit();
    }

    public Boolean getIsDeviceSync() {

        return pref.getBoolean(IS_DEVICE_SYNC, false);
    }

    public String getLoginType() {
        return pref.getString(KEY_LOGIN_TYPE, "");
    }

    public void setUserProfile(UserModel obj) {


        pref.edit().putString(KEY_USER, GSON.toJson(obj)).apply();

    }

    public UserModel getUserProfile() {

        String gson = pref.getString(KEY_USER, "");
        if (gson.isEmpty()) return null;
        return GSON.fromJson(gson, UserModel.class);
    }


}