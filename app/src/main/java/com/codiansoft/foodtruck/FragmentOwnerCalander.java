package com.codiansoft.foodtruck;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Models.EDITMENU;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;
import com.codiansoft.foodtruck.decorators.EventDecorator;
import com.codiansoft.foodtruck.decorators.MySelectorDecorator;
import com.codiansoft.foodtruck.decorators.OneDayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentOwnerCalander extends Fragment implements OnDateSelectedListener {
    Dialog add_dialog;
    RecyclerView rv;
    String ownerid = "";

    View rootView;
    public ProgressBar progressBar;
    List<EDITMENU> editmenuList = new ArrayList<>();
    ImageButton addButton;
    ArrayList<CalendarDay> dates = new ArrayList<>();
    List<Date> d = new ArrayList<>();
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView widget;

    public FragmentOwnerCalander() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calanderview, container, false);
        widget = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_DECORATED_DISABLED);

        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

        widget.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        widget.addDecorators(
                new MySelectorDecorator(getActivity()),
                oneDayDecorator
        );
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            ownerid = bundle.getLong("owner_id") + "";

        }

        GET_BOOKINGS(ownerid);
        return rootView;
    }


    public void GET_BOOKINGS(final String ownerid) {


        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.GET_BOOKINGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            dates = new ArrayList<>();
                            Log.d("USERID", response.toString());
                            editmenuList = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data = Jobject.getJSONObject("data");
                                JSONArray BOOKINGS = Data.getJSONArray("bookings");
                                if (BOOKINGS.length() > 0) {
                                    for (int i = 0; i < BOOKINGS.length(); i++) {
                                        JSONObject single = BOOKINGS.getJSONObject(i);
                                        if (!single.getString("booking_date").equals("")) {
                                            if (getDate(single.getString("booking_date")) != null) {

                                                Date date = getDate(single.getString("booking_date"));
                                                CalendarDay day = CalendarDay.from(date);
                                                dates.add(day);
                                            }
                                        }

                                    }

                                }

                                widget.addDecorator(new EventDecorator(dates, getContext()));


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
                params.put("foodtruckowner_id", ownerid);
                params.put("status", "2");

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

    void ADD_BOOKING(final String date) {


        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.ADD_BOOING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());

                        } catch (Exception ee) {
                        }


                        getFragmentManager().popBackStack();

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
                if (progressBar.isShown()) progressBar.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();

                params.put("api_secret", MyApplication.getInstance().getPrefManger().getUserProfile().getApi_secret());
                params.put("foodtruckowner_id", ownerid);
                params.put("booking_date", date);


                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


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

    public static Date getDate(String val) {
        try {


            Date date;

            final String dateval = "yyyy-MM-dd";
            SimpleDateFormat sf = new SimpleDateFormat(dateval);
            date = sf.parse(val);
            return date;
        } catch (Exception v) {
            return null;
        }
    }

    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, final boolean selected) {
        if (!dates.contains(date)) {
            final String dateval = "yyyy-MM-dd";
            final SimpleDateFormat sf = new SimpleDateFormat(dateval);
            new MaterialDialog.Builder(getActivity())
                    .title("Booking")
                    .content("Are You Sure, You Want To Book Food Truck On " + sf.format(date.getDate()))
                    .positiveText("Yes, Proceed")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ADD_BOOKING(sf.format(date.getDate()));
                        }
                    })
                    .negativeText("Cancel").onNegative(new MaterialDialog.SingleButtonCallback() {

                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            })
                    .show();
        }
    }
}