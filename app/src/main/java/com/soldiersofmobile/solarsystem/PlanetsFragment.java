package com.soldiersofmobile.solarsystem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlanetsFragment extends Fragment implements PlanetsAdapter.PlanetClickedListener {

    public static final String PLANETS = "planets";

    @Bind(R.id.planetsRecyclerView)
    RecyclerView planetsRecyclerView;

    private PlanetsAdapter adapter;
    private SolarObject[] solarObjects;

    public PlanetsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        solarObjects = (SolarObject[]) getArguments().getSerializable(PLANETS);

        adapter = new PlanetsAdapter(getContext(),false);
        adapter.setObjects(solarObjects);
        adapter.setPlanetClickedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planets, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        planetsRecyclerView.setAdapter(adapter);
        planetsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void planetClicked(SolarObject solarObject, int position) {

        SolarObjectActivity.showObject(getActivity(), solarObject);
    }

    public static PlanetsFragment newInstance(SolarObject[] planets) {
        PlanetsFragment fragment = new PlanetsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(PLANETS, planets);
        fragment.setArguments(bundle);
        return fragment;
    }
}
