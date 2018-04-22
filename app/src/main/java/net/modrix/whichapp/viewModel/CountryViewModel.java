package net.modrix.whichapp.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.widget.Toast;

import net.modrix.whichapp.data.Country;
import net.modrix.whichapp.data.CountryRepository;
import net.modrix.whichapp.database.CountryTable;
import net.modrix.whichapp.util.NetworkResponse;
import net.modrix.whichapp.util.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Modrix
 */

public class CountryViewModel extends AndroidViewModel {

    private CountryRepository mCountryRepository;
    private SingleLiveEvent<Country[]> countriesListDataResponseMediatorLiveData = new SingleLiveEvent<>();
    private LiveData<List<CountryTable>> mAllCountries;
    private LiveData<NetworkResponse> networkResponseMutableLiveData;


    public CountryViewModel(@NonNull Application application) {
        super(application);
        this.mCountryRepository = new CountryRepository(application);
        this.mAllCountries = mCountryRepository.getAllCountries();
    }

    public void getToastMessage(String message) {
        Toast.makeText(this.getApplication(), message,
                Toast.LENGTH_LONG).show();
    }

    Country[] getAllCountries() {
        mAllCountries = mCountryRepository.getAllCountries();
        Country[] array = new Country[mAllCountries.getValue().size()];
        for (int n = 0; n < mAllCountries.getValue().size(); n++) {
            array[n].setIso(mAllCountries.getValue().get(n).getIso());
            array[n].setName(mAllCountries.getValue().get(n).getName());
            array[n].setPhone(mAllCountries.getValue().get(n).getPhone());
        }
        return array;
    }

    public void insert(Country[] countries) {
        List<CountryTable> countryTables = new ArrayList<>();
        for (Country country : countries) {
            CountryTable countryTable = new CountryTable();
            countryTable.setPhone(country.getPhone());
            countryTable.setIso(country.getIso());
            countryTable.setName(country.getName());
            countryTables.add(countryTable);
        }
        mCountryRepository.insert(countryTables);
    }


    public LiveData<Country[]> getCountriesListData() {
        networkResponseMutableLiveData = mCountryRepository.getCountriesListData();

        return Transformations.switchMap(networkResponseMutableLiveData, new Function<NetworkResponse, LiveData<Country[]>>() {
            @Override
            public LiveData<Country[]> apply(NetworkResponse networkResponse) {
                if (networkResponse == null) {
                    getToastMessage("Something went wrong,Try again!");
                    return null;
                }

                if (networkResponse.status == NetworkResponse.SUCCESS) {
                    if (networkResponse.data != null) {
                        countriesListDataResponseMediatorLiveData.setValue((Country[]) networkResponse.data);
                        insert((Country[]) networkResponse.data);
                    } else {
                        countriesListDataResponseMediatorLiveData.setValue(getAllCountries());
                    }
                } else if (networkResponse.status == NetworkResponse.BAD_REQUEST) {
                    if (networkResponse.data != null) {
                        countriesListDataResponseMediatorLiveData.setValue(getAllCountries());
                        getToastMessage(networkResponse.errorMessage);
                        return null;
                    }
                } else if (networkResponse.status == NetworkResponse.UNAUTHORISED) {
                    if (networkResponse.data != null) {
                        countriesListDataResponseMediatorLiveData.setValue(getAllCountries());
                        getToastMessage((String) networkResponse.data);
                        return null;
                    }
                } else if (networkResponse.status == NetworkResponse.FAILURE) {
                    if (networkResponse.errorMessage != null) {
                        countriesListDataResponseMediatorLiveData.setValue(getAllCountries());
                        getToastMessage(networkResponse.errorMessage);
                        return null;
                    }
                } else if (networkResponse.status == NetworkResponse.NO_NETWORK) {
                    if (networkResponse.data != null) {
                        countriesListDataResponseMediatorLiveData.setValue(getAllCountries());
                        getToastMessage((String) networkResponse.data);
                        return null;
                    }
                } else if (networkResponse.status == NetworkResponse.LOADING) {

                }
                return countriesListDataResponseMediatorLiveData;
            }
        });
    }
}
