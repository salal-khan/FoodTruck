package com.codiansoft.foodtruck;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Models.MENU;
import com.codiansoft.foodtruck.Models.TRUCKOWNERS;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.Services.MyService;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */

public class Splash extends AppCompatActivity {
    ImageView logoSplash;
    String currentlat="0.0";
    String currentlongs="0.0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getLocation();
        if (!isMyServiceRunning(MyService.class)) {

            Intent i = new Intent(Splash.this, MyService.class);
            startService(i);
        }

        if (MyApplication.getInstance().getPrefManger().getUserProfile() == null) {
            GETOWNERS();


        } else if ((MyApplication.getInstance().getPrefManger().getUserProfile().getUsertype().equals("2")))
        {
            Log.i("API_SECERT",MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());
            int SPLASH_DISPLAY_LENGTH = 3000;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(Splash.this, DrawerActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }else if ((MyApplication.getInstance().getPrefManger().getUserProfile().getUsertype().equals("3"))){
            Log.i("API_SECERT",MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());
            GETOWNERS();
        }


    }


    private void getLocation()
    {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            currentlat="0.0";
            currentlongs="0.0";
        }
        else  if (location != null)
        {
            currentlat= String.valueOf(location.getLatitude());
            currentlongs= String.valueOf(location.getLongitude());
        }

    }


    public int distancekm(String lat,String longs){
        Location locationA = new Location("point A");
        locationA.setLatitude(Double.parseDouble(lat));
        locationA.setLongitude(Double.parseDouble(longs));
        Location locationB = new Location("point B");
        locationB.setLatitude(Double.parseDouble(currentlat));
        locationB.setLongitude(Double.parseDouble(currentlongs));
        int distance = (int) (locationA.distanceTo(locationB)/1000);
        //Toast.makeText(getApplicationContext(),"distance "+distance,Toast.LENGTH_LONG).show();
        return distance;
    }


    @Override
    protected void onPause() {

        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    void GETOWNERS() {


        final StringRequest req = new StringRequest(Request.Method.GET, AppConstants.GET_TRUCKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {


                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                TRUCKOWNERS.deleteAll(TRUCKOWNERS.class);
                                MENU.deleteAll(MENU.class);
                                JSONObject maindata = Jobject.getJSONObject("data");
                                JSONArray Data12 = maindata.getJSONArray("user_data");
                                if (Data12.length()>0){
                                    for (int j=0;j<Data12.length();j++){
                                        JSONObject Data1=Data12.getJSONObject(j);
                                        JSONObject Data = Data1.getJSONObject("user");
                                        TRUCKOWNERS userModel = new TRUCKOWNERS();

                                        String Driverlat =Data.getString("lat");
                                        String Driverlong =Data.getString("long");

                                        int distancekm =distancekm(Driverlat,Driverlong);

                                        if(distancekm<25){


                                            Log.d("xx", "onResponsess: currentlat"+currentlat+" long "+currentlongs+" lat "+Driverlat+ " long "+Driverlong + " distance " +distancekm);

                                            userModel.setUsername(Data.getString("name"));
                                            userModel.setId(Long.parseLong(Data.getString("user_id")));
                                            userModel.setEmail(Data.getString("email"));
                                            userModel.setName(Data.getString("name"));
                                            userModel.setPhoto(Data.getString("image"));
                                            userModel.setDescription(Data.getString("food_title"));
                                            userModel.setContact_no(Data.getString("contact_no"));
                                            userModel.setLat(Data.getString("lat"));
                                            userModel.setLng(Data.getString("long"));
                                            userModel.setCategory(Data.getString("truckCate"));
                                            userModel.setKm(String.valueOf(distancekm));
                                            userModel.save();

                                            MENU menu = new MENU();
                                            JSONArray menuonj = Data.getJSONArray("menu");
                                            if (menuonj.length() > 0) {
                                                for (int i = 0; i < menuonj.length(); i++) {
                                                    JSONObject menudata = menuonj.getJSONObject(i);
                                                    menu.setItemcode(menudata.getString("item_code"));
                                                    menu.setId(Long.parseLong(menudata.getString("menu_id")));
                                                    menu.setOwnerid(Long.parseLong(menudata.getString("foodtruckowner_id")));
                                                    menu.setItemtitle(menudata.getString("item_name"));
                                                    menu.setItemprice(menudata.getString("price"));
                                                    menu.setSeleccted(false);
                                                    menu.save();
                                                }
                                            }
                                        }

                                    }


                                }


                                if (MyApplication.getInstance().getPrefManger().getUserProfile() == null) {
                                    Intent mainIntent = new Intent(Splash.this, MapsActivity.class);
                                    Splash.this.startActivity(mainIntent);
                                    Splash.this.finish();
                                } else if (MyApplication.getInstance().getPrefManger().getUserProfile().getUsertype().equals("3")) {
                                    Intent mainIntent = new Intent(Splash.this, DrawerActivity.class);
                                    Splash.this.startActivity(mainIntent);
                                    Splash.this.finish();
                                }

                            }
                        } catch (Exception ee) {
                        }


//                        getActivity().finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Connecton",Toast.LENGTH_LONG).show();
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {

                        case 400:
                            utilities.dialog("Connection Problem", Splash.this);
                            break;
                        default:
                            utilities.dialog("Connection Problem", Splash.this);
                            break;
                    }
                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

}
