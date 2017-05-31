package com.codiansoft.foodtruck;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.codiansoft.foodtruck.Adapter.AutoCompleteAdapter;
import com.codiansoft.foodtruck.Models.PlacePredictions;
import com.codiansoft.foodtruck.Models.UserModel;
import com.codiansoft.foodtruck.Utils.AppConstants;
import com.codiansoft.foodtruck.Utils.AppUtils;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.VolleyJSONRequest;
import com.codiansoft.foodtruck.Utils.utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OwnerMapsFrament extends Fragment implements Response.ErrorListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private Location mLastLocation;


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    Marker marker;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    Context mContext;
    private LatLng mCenterLatLong;
    private LatLng firstLatlong;
    private LatLng secondLatlong;
    View view;
    Toolbar mToolbar;
    static private LocationManager manager;
    SupportMapFragment mapFragment;
    ListView mAutoCompleteList;

    SwitchCompat locationbtn;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mContext = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.freagment_maps_owner, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        final ActionBar ab = activity.getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.optoin_icon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) getActivity()).opendrawel();
            }
        });

        ((DrawerActivity) getActivity()).hidetoolbar();


        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);

        locationbtn = (SwitchCompat) view.findViewById(R.id.locationbtn);
        if (MyApplication.getInstance().getPrefManger().getUserProfile().getIstrackon().equals("0")) {
            locationbtn.setChecked(false);
        } else {
            locationbtn.setChecked(true);
        }
        locationbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    LocationStatus("1");
                } else {
                    LocationStatus("0");
                }

            }
        });
        toolbar.setNavigationIcon(R.drawable.optoin_icon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrawerActivity) getActivity()).opendrawel();
            }
        });

        ((DrawerActivity) getActivity()).hidetoolbar();
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        Init(view);
        onClick();
        mapFragment.getMapAsync(this);
        fetchLocation();
        if (checkPlayServices()) {
            if (!AppUtils.isLocationEnabled(mContext)) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }

        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    void Init(View v) {

    }

    void onClick() {


    }


    public void fetchLocation() {
        //Build google API client to use fused location
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private Bitmap getMarkerBitmapFromView() {
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker, null);
        ImageButton imageButton = (ImageButton) customMarkerView.findViewById(R.id.marketimage);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        customMarkerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "xyz", Toast.LENGTH_LONG).show();
            }
        });
        return returnedBitmap;
    }

    private Bitmap getMarkerBitmapFromView2() {
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.markero, null);
        ImageButton imageButton = (ImageButton) customMarkerView.findViewById(R.id.marketimage);

        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        customMarkerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "xyz", Toast.LENGTH_LONG).show();
            }
        });
        return returnedBitmap;
    }


    double lati;
    double longi;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        // new FetchAddress().execute(new String[]{location.getLatitude() + "", location.getLongitude() + ""});
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera postion change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;
                // mMap.clear();

                try {

                    Location mLocation = new Location("");

                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);
                    secondLatlong = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                    //     Distance_Field.setText("" g+ CalculationByDistance(firstLatlong, secondLatlong) + "km");
                    //   new FetchAddress().execute(new String[]{mCenterLatLong.latitude + "", mCenterLatLong.longitude + ""});
                    lati = mLocation.getLatitude();
                    longi = mLocation.getLongitude();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

        {

            return;
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if (mLastLocation != null) {
                LatLng latLong = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(15f).tilt(70).build();
                if (MyApplication.getInstance().getPrefManger().getUserProfile().getIstrackon().equals("1")) {
                    marker = mMap.addMarker(new MarkerOptions().position(latLong).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView())).title("My Truck"));

                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(latLong).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView2())).title("My Truck"));


                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

        //searchBtn.setVisibility(View.VISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }


    void LocationStatus(final String status) {
        progressBar.setVisibility(View.VISIBLE);
        final StringRequest req = new StringRequest(Request.Method.POST, AppConstants.SET_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        UserModel user;
                        try {
                            Log.d("USERID", response.toString());

                            JSONObject Jobject = new JSONObject(response);
                            JSONObject result = Jobject.getJSONObject("result");
                            if (result.get("status").equals("success")) {
                                UserModel userModel = MyApplication.getInstance().getPrefManger().getUserProfile();
                                userModel.setIstrackon(status);
                                MyApplication.getInstance().getPrefManger().setUserProfile(userModel);

                                if (MyApplication.getInstance().getPrefManger().getUserProfile().getIstrackon().equals("1")) {

                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()));

                                } else {

                                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView2()));

                                }
                            }
                        } catch (Exception ee) {
                        }


                        if (progressBar.isShown()) progressBar.setVisibility(View.GONE);

                        //                        getActivity().finish();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (locationbtn.isChecked()) {
                    locationbtn.setChecked(false);
                } else {
                    locationbtn.setChecked(true);
                }
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
                params.put("track_location", status);

                Log.d(TAG, "getParams: Lat: " + lati + " Long : " + longi);
                params.put("latitude", String.valueOf(lati));
                params.put("longitude", String.valueOf(longi));
                return params;
            }
        };

        req.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // TODO Auto-generated method stub
        MyApplication.getInstance().addToRequestQueue(req);

    }


}
