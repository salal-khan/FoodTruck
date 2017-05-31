package com.codiansoft.foodtruck;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codiansoft.foodtruck.Adapter.OwnersListAdapter;
import com.codiansoft.foodtruck.Models.TRUCKOWNERS;

import java.util.List;


public class FragmentTrucksList extends Fragment {

    RecyclerView rv;
    private OwnersListAdapter adapter;
    View rootView;


    public FragmentTrucksList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.truck_owners, container, false);
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


        adapter = new OwnersListAdapter(getContext(), TRUCKOWNERS.listAll(TRUCKOWNERS.class));
        rv.setAdapter(adapter);


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
}