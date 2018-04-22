package net.modrix.whichapp.fragment;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.modrix.whichapp.R;
import net.modrix.whichapp.adapter.CountriesListAdapter;
import net.modrix.whichapp.data.Country;
import net.modrix.whichapp.viewModel.CountryViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Modrix
 */

public class CountryListFragment extends Fragment {


    private CountryViewModel mCountryViewModel;

    @BindView(R.id.countries_recyclerview)
    RecyclerView countriesRecyclerView;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    private CountriesListAdapter mCountriesListAdapter;
    private ArrayList<Country> mCountriesArrayList = new ArrayList<>();

    public CountryListFragment() {
        // Required empty public constructor
    }

    public static CountryListFragment newInstance() {
        CountryListFragment homeFragment = new CountryListFragment();
        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCountryViewModel = ViewModelProviders.of(this).get(CountryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.country_fragment, container, false);
        ButterKnife.bind(this, rootView);
        setupCountriesRecyclerView();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadCountries();
    }

    private void setupCountriesRecyclerView() {
        countriesRecyclerView.setHasFixedSize(true);
        countriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCountriesListAdapter = new CountriesListAdapter(mCountriesArrayList);
        countriesRecyclerView.setAdapter(mCountriesListAdapter);
    }

    private void loadCountries() {

        progressBar.setVisibility(View.VISIBLE);
        final LiveData<Country[]> countriesListDataMediatorLiveData = mCountryViewModel.getCountriesListData();
        Observer<Country[]> countriesListDataResponseObserver = new Observer<Country[]>() {
            @Override
            public void onChanged(@Nullable Country[] countries) {
                if (countries != null) {
                    updateCountriesListDataOnUI(countries);
                    countriesListDataMediatorLiveData.removeObserver(this);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        countriesListDataMediatorLiveData.removeObservers(this);
        countriesListDataMediatorLiveData.observe(CountryListFragment.this, countriesListDataResponseObserver);
    }

    private void updateCountriesListDataOnUI(Country[] countriesArray) {

        if (countriesArray.length > 0) {
            mCountriesArrayList.clear();
            List<Country> countries = Arrays.asList(countriesArray);
            mCountriesArrayList.addAll(countries);
            mCountriesListAdapter.notifyDataSetChanged();
        }

        progressBar.setVisibility(View.GONE);
    }

}
