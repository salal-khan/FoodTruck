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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Adapter.EditMenuListAdapter;
import com.codiansoft.foodtruck.Models.EDITMENU;
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


public class FragmentEditMenu extends Fragment {
    Dialog add_dialog;
    RecyclerView rv;
    private EditMenuListAdapter adapter;
    View rootView;
    public ProgressBar progressBar;
    List<EDITMENU> editmenuList = new ArrayList<>();
    ImageButton addButton;

    public FragmentEditMenu() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menus_layout, container, false);

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
        rv.setNestedScrollingEnabled(false);
        addButton = (ImageButton) v.findViewById(R.id.addbtn);
        GET_MENU();
    }

    void ClickEvents() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_dialog = new Dialog(getActivity(), R.style.ThemeCustomDialog);
                add_dialog.setContentView(R.layout.add_menu_item);
                add_dialog.show();
                ImageButton cross = (ImageButton) add_dialog.findViewById(R.id.crossbtn);
                final EditText itemcode = (EditText) add_dialog.findViewById(R.id.itemcode);
                final EditText itemname = (EditText) add_dialog.findViewById(R.id.itemname);
                final EditText itemprice = (EditText) add_dialog.findViewById(R.id.itemprice);


                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        add_dialog.dismiss();

                    }
                });
                TextView submit = (TextView) add_dialog.findViewById(R.id.btn_submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validationChecker(itemcode, itemname, itemprice)) {
                            ADD_MENU(itemcode.getText().toString(), itemname.getText().toString(), itemprice.getText().toString());

                        }


                    }
                });


            }
        });
    }

    public static Boolean validationChecker(EditText itemcode, EditText itemname, EditText itemprice) {
        boolean valid = true;
        if (itemcode.getText().toString().isEmpty()) {
            itemcode.setError("enter  Item Code");
            valid = false;
        } else {
            itemcode.setError(null);
        }

        if (itemname.getText().toString().isEmpty()) {
            itemname.setError("enter Item Name");
            valid = false;
        } else {
            itemname.setError(null);
        }

        if (itemprice.getText().toString().isEmpty()) {
            itemprice.setError("enter Item Price");
            valid = false;
        } else {
            itemprice.setError(null);
        }

        return valid;
    }

    public void GET_MENU() {


        progressBar.setVisibility(View.VISIBLE);

        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.GET_MENU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());
                            editmenuList = new ArrayList<>();
                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                JSONObject Data = Jobject.getJSONObject("data");
                                JSONArray MENUS = Data.getJSONArray("menus");
                                if (MENUS.length() > 0) {
                                    for (int i = 0; i < MENUS.length(); i++) {
                                        JSONObject single = MENUS.getJSONObject(i);
                                        EDITMENU editmenu = new EDITMENU(single.getString("item_code"), single.getString("item_name"), single.getString("price"), single.getString("menu_id"));
                                        editmenuList.add(editmenu);
                                    }

                                }

                                adapter = new EditMenuListAdapter(getContext(), editmenuList, FragmentEditMenu.this);
                                rv.setAdapter(adapter);
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


                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);


    }

    void ADD_MENU(final String itemcode, final String itemname, final String itemprice) {


        progressBar.setVisibility(View.VISIBLE);

        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.ADD_MENU,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());
                            add_dialog.dismiss();
                            GET_MENU();

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
                params.put("item_code", itemcode);
                params.put("item_name", itemname);
                params.put("price", itemprice);


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

    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }
}