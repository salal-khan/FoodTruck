package com.codiansoft.foodtruck;

import android.app.Activity;
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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.codiansoft.foodtruck.Adapter.AutoCompleteAdapter;
import com.codiansoft.foodtruck.Adapter.PlacesAutoCompleteAdapter;
import com.codiansoft.foodtruck.Models.PlacePredictions;
import com.codiansoft.foodtruck.Models.TRUCKOWNERS;
import com.codiansoft.foodtruck.Utils.AppUtils;
import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.Utils.VolleyJSONRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.codiansoft.foodtruck.R.layout.marker;

public class MapsFrament extends Fragment implements GoogleMap.OnMarkerClickListener, Response.ErrorListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

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


    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;


    private PlacesAutoCompleteAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            try {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addApi(Places.GEO_DATA_API)
                        .enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this)
                        .addConnectionCallbacks(this)
                        .build();

                manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                mContext = getActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.freagment_maps, container, false);
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
        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        Init(view);
        onClick();
        mapFragment.getMapAsync(this);


        mAutocompleteTextView = (AutoCompleteTextView) view.findViewById(R.id
                .autoCompleteTextView);

        mAutocompleteTextView.setThreshold(3);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlacesAutoCompleteAdapter(getActivity(), android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);


        return view;
    }


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            function(placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

        }
    };


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
        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(marker, null);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        List<String> providers = manager.getProviders(true);
        Location location = null;
        for (int i = providers.size() - 1; i >= 0; i--) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                location = manager.getLastKnownLocation(providers.get(i));
                if (location != null) {
                    break;
                }
            }
        }
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
        if (location != null) {
            LatLng latLong = new LatLng(location.getLatitude(), location.getLongitude());
            firstLatlong = new LatLng(location.getLatitude(), location.getLongitude());
            secondLatlong = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(15f).tilt(70).build();
            for (TRUCKOWNERS truckowners : TRUCKOWNERS.listAll(TRUCKOWNERS.class)) {
                if (!truckowners.getLat().trim().equals("")) {

                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(truckowners.getLat()), Double.parseDouble(truckowners.getLng()))).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView())).title(truckowners.getName()));
                    marker.setSnippet(truckowners.getCategory() + "");
                    marker.setZIndex(truckowners.getId());
                }
            }
            googleMap.setOnMarkerClickListener(this);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
                        //     Distance_Field.setText("" + CalculationByDistance(firstLatlong, secondLatlong) + "km");
                        //   new FetchAddress().execute(new String[]{mCenterLatLong.latitude + "", mCenterLatLong.longitude + ""});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }


    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
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

            if (mGoogleApiClient != null)
                mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
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

    void function(String placeid) {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeid)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place myPlace = places.get(0);
                            Log.i(TAG, "Place found: " + myPlace.getName());
                            LatLng latlangObj = myPlace.getLatLng();
                            Log.v("latitude:", "" + latlangObj.latitude);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(latlangObj).zoom(15f).tilt(70).build();
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.getUiSettings().setMapToolbarEnabled(false);
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            secondLatlong = myPlace.getLatLng();
                            // Distance_Field.setText("" + CalculationByDistance(firstLatlong, secondLatlong) + "km");
                            Log.v("longitude:", "" + latlangObj.longitude);
                        } else {
                            Log.e(TAG, "Place not found");
                        }
                        places.release();
                    }
                });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
//Toast.makeText(getContext(),""+marker.getSnippet(),Toast.LENGTH_LONG).show();
//        FragmentTruckMenu f1 = new FragmentTruckMenu();
//        Bundle bundle = new Bundle();
//        bundle.putString("owner_id", marker.getSnippet());
//        f1.setArguments(bundle);
//        getActivity().getSupportFragmentManager().beginTransaction().
//                replace(R.id.fragment_placeholder, f1, "FragmentTruckMenu").addToBackStack("FragmentTruckMenu").commit();
        final String OnwerId = String.valueOf((int) marker.getZIndex());

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub

                FragmentTruckMenu f1 = new FragmentTruckMenu();
                Bundle bundle = new Bundle();
                bundle.putString("owner_id", OnwerId);
                f1.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, f1, "FragmentTruckMenu").addToBackStack("FragmentTruckMenu").commit();


            }
        });
        return false;
    }
}
