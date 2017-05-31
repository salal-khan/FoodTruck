package com.codiansoft.foodtruck;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codiansoft.foodtruck.Adapter.MenuEachAdapter;
import com.codiansoft.foodtruck.Models.MENU;
import com.codiansoft.foodtruck.Models.TRUCKOWNERS;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.squareup.picasso.Picasso;


public class FragmentTruckMenu extends Fragment {
    private static final int PERMISSIONS_REQUEST_DEVICE = 100;
    private ProgressDialog progressDialog;
    RecyclerView rv;
    String requestBody = null;
    ProgressBar progressBar;
    ImageView header;
    private MenuEachAdapter adapter;
    View v;
    View rootView;
    static String id;
    EditText comments;
    ImageButton drawerbutton;
    TextView title, desctiption, distance;
    Button buy, calender;

    public FragmentTruckMenu() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.truck_menu, container, false);
        Init(rootView);
        ClickEvents();


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    Intent intent = new Intent(getActivity(), DrawerActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        return rootView;
    }

    void Init(View v) {
        comments = (EditText) v.findViewById(R.id.comments);
        header = (ImageView) v.findViewById(R.id.header_img);
        title = (TextView) v.findViewById(R.id.title);
        desctiption = (TextView) v.findViewById(R.id.description);
        distance = (TextView) v.findViewById(R.id.distance);
        buy = (Button) v.findViewById(R.id.buy);
        calender = (Button) v.findViewById(R.id.calender);
        progressBar = (ProgressBar) v.findViewById(R.id.spin_kit);
        rv = (RecyclerView) v.findViewById(R.id.usersitems);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setNestedScrollingEnabled(true);

        drawerbutton = (ImageButton) v.findViewById(R.id.drawerbuton);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("owner_id");
        }
        adapter = new MenuEachAdapter(getContext(), MENU.find(MENU.class, "ownerid=?", id));
        rv.setAdapter(adapter);
        TRUCKOWNERS truckowners = TRUCKOWNERS.findById(TRUCKOWNERS.class, Long.parseLong(id));
        title.setText(truckowners.getName());
        if (!truckowners.getPhoto().equals("")) {
            Picasso
                    .with(getActivity())
                    .load(AppConstants.IMAGE_URL + truckowners.getPhoto())
                    .fit() // will explain later
                    .into(header);
        }
        desctiption.setText(truckowners.getDescription());
        distance.setText("km "+truckowners.getKm());
    }

    void ClickEvents() {
        drawerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DrawerActivity) getActivity()).opendrawel();
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentOrderFinalize f1 = new FragmentOrderFinalize();
                Bundle bundle = new Bundle();
                bundle.putLong("owner_id", Long.parseLong(id));
                bundle.putString("comments", comments.getText().toString());
                f1.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, f1, "FragmentOrderFinalize").addToBackStack("FragmentOrderFinalize").commit();
//


            }
        });
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentOwnerCalander f1 = new FragmentOwnerCalander();
                Bundle bundle = new Bundle();
                bundle.putLong("owner_id", Long.parseLong(id));

                f1.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, f1, "FragmentOwnerCalander").addToBackStack("FragmentOwnerCalander").commit();
//
            }
        });
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