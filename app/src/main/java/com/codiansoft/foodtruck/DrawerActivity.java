package com.codiansoft.foodtruck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.codiansoft.foodtruck.Utils.MyApplication;
import com.codiansoft.foodtruck.expand.MyUserOrders;
import com.google.firebase.iid.FirebaseInstanceId;


public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("TAG", "onCreate: tokens" + FirebaseInstanceId.getInstance().getToken());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (MyApplication.getInstance().getPrefManger().getUserProfile().getUsertype().equals("3")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_Analysis).setVisible(false);
            nav_Menu.findItem(R.id.nav_food).setVisible(false);
            nav_Menu.findItem(R.id.nav_calender).setVisible(false);

            nav_Menu.findItem(R.id.nav_menu).setVisible(false);

            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new MapsFrament(), "MapsFrament").commit();
            navigationView.setCheckedItem(R.id.home);
        } else {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new OwnerMapsFrament(), "OwnerMapsFrament").commit();
            navigationView.setCheckedItem(R.id.home);
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_orders).setVisible(false);
            nav_Menu.findItem(R.id.nav_trucks).setVisible(false);
            nav_Menu.findItem(R.id.nav_bookings).setVisible(false);


        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        final FragmentTruckMenu fragmentTruckMenu = (FragmentTruckMenu) getSupportFragmentManager().findFragmentByTag("FragmentTruckMenu    ");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentTruckMenu != null && fragmentTruckMenu.isVisible()) {
            fragmentTruckMenu.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_Profile) {
            if (MyApplication.getInstance().getPrefManger().getUserProfile().getUsertype().equals("3")) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, new EditUser(), "EditUser").commit();
            } else {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, new EditTruckOwner(), "EditTruckOwner").commit();

            }

        } else if (id == R.id.nav_menu) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new FragmentEditMenu(), "FragmentEditMenu").commit();
        } else if (id == R.id.nav_orders) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new MyUserOrders(), "FragmentCompletedOrders").commit();
        } else if (id == R.id.nav_food) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new FragmentAllOeders(), "FragmentAllOeders").commit();
        } else if (id == R.id.nav_Analysis) {

        } else if (id == R.id.nav_calender) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new AdminCalender(), "AdminCalender").commit();

        } else if (id == R.id.nav_bookings) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new FragmentBookingsList(), "FragmentBookingsList").commit();
        } else if (id == R.id.nav_trucks) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_placeholder, new FragmentTrucksList(), "FragmentTrucksList").commit();

        } else if (id == R.id.nav_logout) {
            MyApplication.getInstance().getPrefManger().setUserProfile(null);
            startActivity(new Intent(DrawerActivity.this, MapsActivity.class));
            finish();
        } else if (id == R.id.home) {
            if (MyApplication.getInstance().getPrefManger().getUserProfile().getUsertype().equals("3")) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, new MapsFrament(), "MapsFrament").commit();
            } else {

                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_placeholder, new OwnerMapsFrament(), "OwnerMapsFrament").commit();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hidetoolbar() {
        toolbar.setVisibility(View.GONE);
    }

    public void opendrawel() {
        drawer.openDrawer(GravityCompat.START);
    }
}
