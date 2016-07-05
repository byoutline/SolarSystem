package com.soldiersofmobile.solarsystem;

import android.app.WallpaperManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SolarObjectActivity extends AppCompatActivity implements PlanetsAdapter.PlanetClickedListener {

    public static final String OBJECT = "object";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.detailImageView)
    ImageView detailImageView;

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.detailsTextView)
    TextView detailsTextView;
    @Bind(R.id.moonsLabel)
    TextView moonsLabel;
    @Bind(R.id.moonsRecycleView)
    RecyclerView moonsRecycleView;

    private SolarObject solarObject;
    private PlanetsAdapter planetsAdapter;

    public static void showObject(FragmentActivity activity, SolarObject solarObject) {
        Intent intent = new Intent(activity, SolarObjectActivity.class);
        intent.putExtra(SolarObjectActivity.OBJECT, solarObject);
        activity.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_object);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        solarObject = (SolarObject) getIntent().getSerializableExtra(OBJECT);


        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SolarObjectActivity.this);
            }
        });

        boolean hasVideo = !TextUtils.isEmpty(solarObject.getVideo());

        if (hasVideo) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    watchYoutubeVideo(solarObject.getVideo());
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }

        try {
            String textWithHtml = SolarObject.loadStringFromAssets(this, solarObject.getText());
            detailsTextView.setText(Html.fromHtml(textWithHtml));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this)
                .load("file:///android_asset/" + solarObject.getImage())
                .centerCrop()
//                .placeholder(R.drawable.planet_placeholder)
                .into(detailImageView);

        SolarObject[] moons = solarObject.getMoons();
        boolean hasMoons = moons != null && moons.length > 0;
        moonsLabel.setVisibility(hasMoons ? View.VISIBLE : View.GONE);
        moonsRecycleView.setVisibility(hasMoons ? View.VISIBLE : View.GONE);
        if (hasMoons) {
            planetsAdapter = new PlanetsAdapter(this);
            planetsAdapter.setPlanetClickedListener(this);
            planetsAdapter.setObjects(moons);
            moonsRecycleView.setAdapter(planetsAdapter);
            moonsRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            //https://code.google.com/p/android/issues/detail?id=177613
            moonsRecycleView.setNestedScrollingEnabled(false);
        }

    }


    public void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solar_object, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_set_wallpaper) {
            WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(getApplicationContext());
            try {
                myWallpaperManager.setStream(getAssets().open(solarObject.getImage()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void planetClicked(SolarObject solarObject, int postion) {
        showObject(this, solarObject);
    }
}

