package com.codiansoft.foodtruck;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Adapter.CustomerInfoContent;
import com.codiansoft.foodtruck.Models.Bookings;
import com.codiansoft.foodtruck.Models.EDITMENU;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.utilities;
import com.codiansoft.foodtruck.decorators.EventDecorator;
import com.codiansoft.foodtruck.decorators.EventDecoratorReject;
import com.codiansoft.foodtruck.decorators.MySelectorDecorator;
import com.codiansoft.foodtruck.decorators.NewRequestDecorator;
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


public class AdminCalender extends Fragment implements OnDateSelectedListener {
    Dialog add_dialog;
    RecyclerView rv;
    String ownerid = "";
    List<Bookings> bookingsList = new ArrayList<>();
    List<Bookings> CustomerInfo = new ArrayList<>();
    View rootView;
    public ProgressBar progressBar;
    List<EDITMENU> editmenuList = new ArrayList<>();
    ImageButton addButton;
    ArrayList<CalendarDay> NewReuestDates = new ArrayList<>();
    ArrayList<CalendarDay> acceptedDates = new ArrayList<>();
    ArrayList<CalendarDay> RejectDates = new ArrayList<>();
    List<Date> d = new ArrayList<>();
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    MaterialCalendarView widget;
    HashMap<CalendarDay, String> eventdetailsMap;

    public AdminCalender() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        progressBar = new ProgressBar(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calanderview, container, false);
        eventdetailsMap = new HashMap<>();
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

        widget = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        //  widget.setShowOtherDates(MaterialCalendarView.SHOW_DECORATED_DISABLED);

        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(Calendar.YEAR, 2017);
        instance1.set(Calendar.MONTH, 00);
        instance1.set(Calendar.DAY_OF_MONTH, 01);


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
        GET_BOOKINGS();
        return rootView;
    }


    public void GET_BOOKINGS() {


        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.GET_BOOKINGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            acceptedDates = new ArrayList<>();
                            NewReuestDates = new ArrayList<>();
                            bookingsList = new ArrayList<>();
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
                                                Bookings b = new Bookings();
                                                b.setBookingid(single.getString("booking_id"));
                                                b.setCustomerid(single.getString("user_id"));
                                                b.setBookingdate(day);
                                                bookingsList.add(b);
                                                eventdetailsMap.put(day, "" + single.getString("booking_id"));
                                                if (single.getString("booking_status").equals("0")) {
                                                    NewReuestDates.add(day);
                                                } else if (single.getString("booking_status").equals("2")) {
                                                    RejectDates.add(day);
                                                } else {
                                                    acceptedDates.add(day);
                                                }
                                            }
                                        }

                                    }

                                }
                                widget.addDecorator(new NewRequestDecorator(NewReuestDates, getContext()));
                                widget.addDecorator(new EventDecorator(acceptedDates, getContext()));
                                widget.addDecorator(new EventDecoratorReject(RejectDates, getContext()));
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
                params.put("foodtruckowner_id", MyApplication.getInstance().getPrefManger().getUserProfile().getId());
                params.put("status", "2");

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

    void UPDATEBOOKING(final String id, final String Updatetype) {


        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.UPDATE_BOOING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());
                            GET_BOOKINGS();
                        } catch (Exception ee) {
                        }


                        getFragmentManager().popBackStack();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
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
                params.put("booking_id", id);
                params.put("status", Updatetype);


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
            //
//            Calendar c = Calendar.getInstance();
//            c.setTime(sf.parse(val));
//            c.add(Calendar.MONTH, 1);
//            String dates=sf.format(c.getTime());
//            date = sf.parse(dates);

            return date;
        } catch (Exception v) {
            return null;
        }
    }

    String orderId;

    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, final boolean selected) {
        orderId = eventdetailsMap.get(date);
        if (acceptedDates.contains(date)) {


            final Dialog dialog = new Dialog(getActivity(), R.style.ThemeCustomDialog);
            dialog.setContentView(R.layout.bookingdialog);
            dialog.show();
            Button customer = (Button) dialog.findViewById(R.id.userinfo);
            customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final Dialog dialogs = new Dialog(getActivity(), R.style.ThemeCustomDialog);
                    dialogs.setContentView(R.layout.userinfodialog);


                    getUserInfo(dialogs, orderId);

                    dialogs.show();
                }
            });


            Button accept = (Button) dialog.findViewById(R.id.accept);
            Button reject = (Button) dialog.findViewById(R.id.reject);
            accept.setVisibility(View.GONE);
            reject.setVisibility(View.GONE);


        } else if (NewReuestDates.contains(date)) {


            final Dialog dialog = new Dialog(getActivity(), R.style.ThemeCustomDialog);
            dialog.setContentView(R.layout.bookingdialog);
            dialog.show();
            Button customer = (Button) dialog.findViewById(R.id.userinfo);
            customer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final Dialog dialogs = new Dialog(getActivity(), R.style.ThemeCustomDialog);
                    dialogs.setContentView(R.layout.userinfodialog);


                    getUserInfo(dialogs, orderId);

                    dialogs.show();
                }
            });


            Button accept = (Button) dialog.findViewById(R.id.accept);
            Button reject = (Button) dialog.findViewById(R.id.reject);
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UPDATEBOOKING(orderId, "1");
                    dialog.dismiss();


                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UPDATEBOOKING(orderId, "2");
                    dialog.dismiss();


                }
            });
            Toast.makeText(getActivity(), "date" + orderId + "new" + date, Toast.LENGTH_SHORT).show();
        }


    }


    CustomerInfoContent customerInfoContent;

    public CustomerInfoContent getUserInfo(Dialog dialogs, final String bookingId) {
        final TextView nametxt = (TextView) dialogs.findViewById(R.id.name);
        final TextView contacttxt = (TextView) dialogs.findViewById(R.id.contact);
        final TextView gendertxt = (TextView) dialogs.findViewById(R.id.gender);
        final TextView agetxt = (TextView) dialogs.findViewById(R.id.age);
        StringRequest req = new StringRequest(Request.Method.POST, AppConstants.CUSTOMERINFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        customerInfoContent = new CustomerInfoContent();
                        try {
                            //Log.d("data", response.toString());

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data1 = Jobject.getJSONObject("data");
                                JSONObject Data = Data1.getJSONObject("user_data");

                                customerInfoContent.setName(Data.getString("name"));
                                customerInfoContent.setContact(Data.getString("contact_no"));
                                customerInfoContent.setAge(Data.getString("age"));
                                customerInfoContent.setGender(Data.getString("gender"));
                                nametxt.setText(customerInfoContent.getName());
                                contacttxt.setText(customerInfoContent.getContact());
                                gendertxt.setText(customerInfoContent.getGender());
                                agetxt.setText(customerInfoContent.getAge());
                            }


                            //customerInfoContent.set
                        } catch (Exception ee) {
                            Log.d("Error", ee.getMessage());
                            Toast.makeText(getActivity(), "data" + ee.getMessage(), Toast.LENGTH_LONG).show();
                        }


                        getFragmentManager().popBackStack();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
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
                Toast.makeText(getActivity(), "error" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //userId=XXX&routeId=XXX&selected=XXX
                Map<String, String> params = new HashMap<String, String>();
                params.put("booking_id", bookingId);
                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


        return customerInfoContent;
    }


    Bookings getObject(CalendarDay c) {

        return null;
    }
}