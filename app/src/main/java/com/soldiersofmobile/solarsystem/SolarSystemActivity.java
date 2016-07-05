package com.soldiersofmobile.solarsystem;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.byoutline.secretsauce.activities.BaseAppCompatActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SolarSystemActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MoonsFragment.Callback {

    private static final int PLANETS_TAB = 0;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.containerLayout)
    FrameLayout containerLayout;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    private SolarObject[] planets;
    private SolarObject[] withMoons;
    private SolarObject[] otherObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_system);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        planets = SolarObject.getPlanetsFromJson(this);
        otherObjects = SolarObject.getOthersFromJson(this);

        ArrayList<SolarObject> objectsWithMoons = new ArrayList<>();

        for (SolarObject planet : planets) {
            if (SolarObject.hasMoons(planet)) {
                objectsWithMoons.add(planet);
            }
        }

        for (SolarObject other : otherObjects) {
            if (SolarObject.hasMoons(other)) {
                objectsWithMoons.add(other);
            }
        }

        withMoons = objectsWithMoons.toArray(new SolarObject[0]);

        navigationView.setCheckedItem(R.id.nav_planets);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_planets));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_planets) {
            // Handle the camera action
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerLayout, PlanetsFragment.newInstance(planets))
                    .commit();

        } else if (id == R.id.nav_moons) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerLayout, MoonsFragment.newInstance(withMoons))
                    .commit();

        } else if (id == R.id.nav_other) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerLayout, PlanetsFragment.newInstance(otherObjects))
                    .commit();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showTabs(ViewPager viewPager) {
        tablayout.setVisibility(View.VISIBLE);
        tablayout.setupWithViewPager(viewPager);


    }

    @Override
    public void hideTabs() {
        tablayout.removeAllTabs();
        tablayout.setOnTabSelectedListener(null);
        tablayout.setVisibility(View.GONE);
    }
}
