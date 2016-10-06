package com.app.fbulou.inventory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        /*drawer.setDrawerListener(toggle);*/
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Admin");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //android.support.v4.app.FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = null;
        Class fragmentClass = null;

        switch (id) {
            case R.id.nav_category:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_products:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_inventory:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_orders:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_users:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_priviliges:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_chat:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_notifications:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_delivery:
                fragmentClass = CategoryFragment.class;
                break;

            case R.id.nav_log:
                fragmentClass = CategoryFragment.class;
                break;

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction().replace(R.id.content_admin, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
