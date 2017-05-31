package com.codiansoft.foodtruck;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class FragmentPendingOrders extends Fragment {

    private DrawerLayout mDrawerLayout;
    ViewPager viewPager;
    RecyclerView rv;
    //  private CourcesAdapter adapter;

    public FragmentPendingOrders() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void init(View v) {
//        rv = (RecyclerView) v.findViewById(R.id.l);
//        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.neworders_, container, false);


        init(rootView);


        // Inflate the layout for this fragment
        return rootView;
    }

    public void showlist(List nember_contact) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}