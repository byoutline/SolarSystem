package com.soldiersofmobile.solarsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlanetsAdapter extends RecyclerView.Adapter<PlanetViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private SolarObject[] solarObjects = new SolarObject[0];
    private PlanetClickedListener planetClickedListener;

    public PlanetsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setPlanetClickedListener(PlanetClickedListener planetClickedListener) {
        this.planetClickedListener = planetClickedListener;
    }

    @Override
    public PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.planet_item_content, parent, false);
        return new PlanetViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(PlanetViewHolder holder, int position) {
        holder.setSolarObject(solarObjects[position]);
    }

    @Override
    public int getItemCount() {
        return solarObjects.length;
    }

    public void setObjects(SolarObject[] solarObjects) {
        this.solarObjects = solarObjects;
        notifyDataSetChanged();
    }

    public void planetClicked(int adapterPosition) {
        if (planetClickedListener != null) {
            planetClickedListener.planetClicked(solarObjects[adapterPosition], adapterPosition);
        }
    }

    public interface PlanetClickedListener {
        void planetClicked(SolarObject solarObject, int postion);
    }
}

class PlanetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final PlanetsAdapter adapter;

    @Bind(R.id.itemImageView)
    ImageView itemImageView;
    @Bind(R.id.itemTextView)
    TextView itemTextView;

    private SolarObject solarObject;

    public PlanetViewHolder(View itemView, PlanetsAdapter adapter) {
        super(itemView);

        this.adapter = adapter;
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    public void setSolarObject(SolarObject solarObject) {
        this.solarObject = solarObject;

        itemTextView.setText(solarObject.getName());
        Glide.with(itemImageView.getContext())
                .load("file:///android_asset/" + solarObject.getImage())
                .placeholder(R.drawable.planet_placeholder)
                .into(itemImageView);
    }

    @Override
    public void onClick(View v) {
        adapter.planetClicked(getAdapterPosition());
    }
}
