package com.codiansoft.foodtruck;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.codiansoft.foodtruck.Adapter.MyBookingsListAdapter;
import com.codiansoft.foodtruck.Models.MYBOOKINGS;
import com.codiansoft.foodtruck.Models.TRUCKOWNERS;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentBookingsList extends Fragment {

    RecyclerView rv;
    private MyBookingsListAdapter adapter;
    View rootView;

    List<MYBOOKINGS> mybookingses = new ArrayList<>();

    public FragmentBookingsList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_bookings, container, false);

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


        Init(rootView);
        ClickEvents();
        return rootView;
    }

    void Init(View v) {

        rv = (RecyclerView) v.findViewById(R.id.usersitems);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setNestedScrollingEnabled(true);
        GET_BOOKINGS();

//        adapter = new MyBookingsListAdapter(getContext(), TRUCKOWNERS.listAll(TRUCKOWNERS.class));
//        rv.setAdapter(adapter);


    }

    void ClickEvents() {

    }

    public void showlist(List nember_contact) {
//        adapter = new DevicesAdapter(getActivity(), nember_contact);
//
//        rv.setNestedScrollingEnabled(false);
//        rv.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }


    public void GET_BOOKINGS() {


        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.GET_BOOKINGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            mybookingses = new ArrayList<>();
                            Log.d("USERID", response.toString());
                            //   editmenuList = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data = Jobject.getJSONObject("data");
                                JSONArray BOOKINGS = Data.getJSONArray("bookings");
                                if (BOOKINGS.length() > 0) {
                                    for (int i = 0; i < BOOKINGS.length(); i++) {
                                        JSONObject single = BOOKINGS.getJSONObject(i);
                                        if (!single.getString("booking_date").equals("")) {
                                            MYBOOKINGS mybook = new MYBOOKINGS();
                                            mybook.setOwnername(TRUCKOWNERS.findById(TRUCKOWNERS.class, Long.parseLong(single.getString("foodtruckowner_id"))).getName());
                                            mybook.setBookingdate(single.getString("booking_date"));
                                            if (single.getString("status").equals("0")) {
                                                mybook.setBookingstatus("Pending");
                                            } else if (single.getString("status").equals("1")) {
                                                mybook.setBookingstatus("Accepted");

                                            }
                                            mybookingses.add(mybook);

                                        }

                                    }

                                }
                                adapter = new MyBookingsListAdapter(getContext(), mybookingses);
                                rv.setAdapter(adapter);

                            }
                        } catch (Exception ee) {


                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 409:
                            utilities.dialog("Already Exist", getActivity());
                            break;
                        case 400:
                            utilities.dialog("Connection Problem", getActivity());
                            break;
                        default:
                            utilities.dialog("Connection Problem", getActivity());
                            break;
                    }
                }


            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("api_secret", MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());
                params.put("status", "2");

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }
}