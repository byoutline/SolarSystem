package com.soldiersofmobile.solarsystem;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.byoutline.secretsauce.activities.BaseAppCompatActivity;
import com.byoutline.secretsauce.utils.CustomTypefaceSpan;
import com.byoutline.secretsauce.utils.FontCache;
import com.byoutline.secretsauce.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

public class SolarSystemActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MoonsFragment.Callback, TabLayout.OnTabSelectedListener {

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

    @Bind(R.id.bottomBar)
    TabLayout bottomBar;

    @BindColor(R.color.colorAccent)
    int selectedTabIconColor;
    @BindColor(R.color.white)
    int unSelectedTabIconColor;

    private SolarObject[] planets;
    private SolarObject[] withMoons;
    private SolarObject[] otherObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_system);
        ButterKnife.bind(this);

        injectViewsAndSetUpToolbar(R.id.toolbar, R.id.toolbar_title_tv);
        setTitle(R.string.app_name);
        ViewCompat.setElevation(toolbar, ViewUtils.convertDpToPixel(0, this));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNavigationViewItemsFont(navigationView);

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
        setUpBottomBar();
    }

    private void setNavigationViewItemsFont(NavigationView navigationView) {
        if (navigationView != null) {
            Menu menu = navigationView.getMenu();
            Typeface mediumFont = FontCache.get(Constants.LighFont, this);
            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                if (menuItem != null) {
                    SpannableString spannableString = new SpannableString(menuItem.getTitle());
                    spannableString.setSpan(new CustomTypefaceSpan(mediumFont), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    menuItem.setTitle(spannableString);
                }
            }
        }
    }

    private void setUpBottomBar() {
        ViewCompat.setElevation(bottomBar, ViewUtils.convertDpToPixel(24, this));
        bottomBar.setOnTabSelectedListener(this);
        for (int i = 0; i < bottomBar.getTabCount(); i++) {
            TabLayout.Tab tab = bottomBar.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.bottom_bar_menu_item);
            }
        }

        //workaround for not selecting custom view on start
        TabLayout.Tab planetsTab = bottomBar.getTabAt(PLANETS_TAB);
        if (planetsTab != null && planetsTab.getCustomView() != null) {
            changeTabIconColor(planetsTab, selectedTabIconColor);
            planetsTab.getCustomView().setSelected(true);
        }
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        changeTabIconColor(tab, selectedTabIconColor);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        changeTabIconColor(tab, unSelectedTabIconColor);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void changeTabIconColor(TabLayout.Tab tab, int color) {
        if (tab.getIcon() != null) {
            tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
