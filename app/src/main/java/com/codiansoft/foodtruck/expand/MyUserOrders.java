package com.codiansoft.foodtruck.expand;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.DrawerActivity;
import com.codiansoft.foodtruck.Models.USERORDERMENUITEMS;
import com.codiansoft.foodtruck.Models.USERSORDERS;
import com.codiansoft.foodtruck.R;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyUserOrders extends Fragment {
    List<USERSORDERS> usersorderses = new ArrayList<>();
    List<USERORDERMENUITEMS> userordermenuitemses = new ArrayList<>();
    public GenreAdapter adapter;
    View rootView;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.users_order_layout, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        final ActionBar ab = activity.getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.optoin_icon);
        // searchfield = (EditText) view.findViewById(searchfield);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) getActivity()).opendrawel();
            }
        });

        ((DrawerActivity) getActivity()).hidetoolbar();


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerView.setLayoutManager(layoutManager);
        GETORDERS();


        return rootView;
    }

    void GETORDERS() {


        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.GET_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                usersorderses = new ArrayList<>();
                                String status = "Pending";
                                JSONObject maindata = Jobject.getJSONObject("data");
                                JSONArray Data12 = maindata.getJSONArray("orders_list");
                                if (Data12.length() > 0) {
                                    for (int j = 0; j < Data12.length(); j++) {

                                        userordermenuitemses = new ArrayList<>();
                                        JSONObject Data1 = Data12.getJSONObject(j);
                                        JSONArray items = Data1.getJSONArray("item_list");

                                        if (Data1.getString("status").equals("0")) {
                                            status = "Pending";
                                        } else if (Data1.getString("status").equals("1")) {
                                            status = "Accepted";
                                        } else if (Data1.getString("status").equals("2")) {
                                            status = "Rejected";
                                        } else if (Data1.getString("status").equals("3")) {
                                            status = "Competed";
                                        }
                                        if (items.length() > 0) {
                                            for (int k = 0; k < items.length(); k++) {
                                                JSONObject Dat = items.getJSONObject(k);
                                                USERORDERMENUITEMS userordermenuitems = new USERORDERMENUITEMS();


                                                userordermenuitems.setPrice("" + (Double.parseDouble(Dat.getString("quantity")) * Double.parseDouble(Dat.getString("price"))));
                                                userordermenuitems.setQuantity(Dat.getString("quantity"));
                                                userordermenuitems.setName(Dat.getString("item_name"));
                                                userordermenuitemses.add(userordermenuitems);
                                            }
                                        }


                                        USERSORDERS usersorder = new USERSORDERS(Data1.getString("order_id"), status, Data1.getString("total_amount"), Data1.getString("created_time"), Data1.getString("delivery_time"), Data1.getString("name"), Data1.getString("lat"), Data1.getString("long"), userordermenuitemses);
                                        usersorderses.add(usersorder);


                                    }
                                    adapter = new GenreAdapter(usersorderses, getActivity());

                                    recyclerView.setAdapter(adapter);

                                }


                            }
                        } catch (Exception ee) {
                        }


//                        getActivity().finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                Log.d("sx", "onErrorResponse: " + response.statusCode);
                if (response != null && response.data != null) {
                    switch (response.statusCode) {

                        case 400:
                            utilities.dialog("Connection xProblem", getActivity());
                            break;
                        default:
                            utilities.dialog("Connection xPraoblem", getActivity());
                            break;
                    }
                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();


                params.put("status", 4 + "");

                params.put("api_secret", MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }


    public Activity getact() {
        return getActivity();
    }
}
