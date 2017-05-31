package com.codiansoft.foodtruck;

import android.app.Activity;
import android.app.Dialog;
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
import com.codiansoft.foodtruck.Adapter.FinalizeAdapter;
import com.codiansoft.foodtruck.Models.MENU;
import com.codiansoft.foodtruck.Models.MENUFINAL;
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


public class FragmentOrderFinalize extends Fragment {
    Dialog add_dialog;
    RecyclerView rv;
    private FinalizeAdapter adapter;
    View rootView;
    TextView totalamount;
    public ProgressBar progressBar;
    Button order_now;
    List<MENUFINAL> orderlist = new ArrayList<>();
    ImageButton addButton;
    long ownerid = 0L;
    String Comments = "";

    public FragmentOrderFinalize() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_finalize, container, false);
        ((DrawerActivity) getActivity()).hidetoolbar();
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
        progressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        rv = (RecyclerView) v.findViewById(R.id.usersitems);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        totalamount = (TextView) v.findViewById(R.id.total);
        rv.setNestedScrollingEnabled(false);
        order_now = (Button) v.findViewById(R.id.ordernow);
        addButton = (ImageButton) v.findViewById(R.id.addbtn);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            ownerid = bundle.getLong("owner_id");
            Comments = bundle.getString("comments");
        }


        List<MENUFINAL> menufinals = new ArrayList<>();
        for (MENU menu : MENU.listAll(MENU.class)) {
            if (menu.getSeleccted() && menu.getOwnerid() == ownerid) {
                MENUFINAL menufinal = new MENUFINAL();
                menufinal.setMenuid(menu.getId() + "");
                menufinal.setItemcode(menu.getItemcode());
                menufinal.setItemprice(menu.getItemprice());
                menufinal.setItemtitle(menu.getItemtitle());
                menufinal.setOwnerid(menu.getOwnerid());
                menufinal.setQty("1");
                menufinals.add(menufinal);

            }

        }
        orderlist = menufinals;
        adapter = new FinalizeAdapter(getContext(), menufinals, totalamount, FragmentOrderFinalize.this);
        rv.setAdapter(adapter);


    }

    void ClickEvents() {

        order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray = null;
                try {


                    jsonArray = new JSONArray();
                    for (MENUFINAL menu : orderlist) {

                        JSONObject object = new JSONObject();
                        object.put("id", menu.getMenuid() + "");
                        object.put("quantity", menu.getQty());
                        jsonArray.put(object);

                    }
                } catch (Exception s) {

                }
                if (jsonArray != null) {


                    Log.i("TALHA", Comments + ownerid + "--" + jsonArray.toString());
                    SEND_ORDER(Comments, ownerid + "", jsonArray.toString(), totalamount.getText().toString().split(":")[1]);
                } else {
                    utilities.dialog("Choose at least one item", getActivity());
                }
            }
        });
    }

    public void FinalOrderList(List<MENUFINAL> mmm) {
        orderlist = mmm;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void SEND_ORDER(final String comment, final String ownerid, final String jsonarray, final String totalamount) {


        progressBar.setVisibility(View.VISIBLE);
        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.NEW_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                Toast.makeText(getActivity(), "Order Successfully Sent", Toast.LENGTH_LONG).show();
                                getFragmentManager().popBackStack();
                            }
                        } catch (Exception ee) {
                        }


                        if (progressBar.isShown()) progressBar.setVisibility(View.GONE);

                        //                        getActivity().finish();


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
                            utilities.dialog("Invalid Credentials", getActivity());
                            break;
                        case 411:
                            utilities.dialog("Account not Verified yet.", getActivity());
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

                params.put("json_array", jsonarray);
                params.put("comment", comment);
                params.put("total_amount", totalamount);

                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }
}