package net.modrix.whichapp.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import net.modrix.whichapp.database.CountryDao;
import net.modrix.whichapp.database.CountryDatabase;
import net.modrix.whichapp.database.CountryTable;
import net.modrix.whichapp.util.NetworkResponse;
import net.modrix.whichapp.util.WebService;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Modrix
 */

public class CountryRepository {

    private CountryDao mCountryDao;
    private LiveData<List<CountryTable>> mAllCountries;

    public CountryRepository(Application application) {
        CountryDatabase db = CountryDatabase.getDatabase(application);
        mCountryDao = db.countryDao();
        mAllCountries = mCountryDao.getAllCountries();
    }


    public LiveData<List<CountryTable>> getAllCountries() {
        return mAllCountries;
    }

    public LiveData<NetworkResponse> getCountriesListData() {
        final MutableLiveData<NetworkResponse> getCountriesListDataResponseMutableLiveData = new MutableLiveData<>();

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.readTimeout(60, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS);
        okHttpClient.retryOnConnectionFailure(true);

        WebService webService = new Retrofit.Builder()
                .baseUrl("https://api.whichapp.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build()
                .create(WebService.class);

        Call<Country[]> couponsListDataResponseCall = webService.getCountriesListData();

        couponsListDataResponseCall.enqueue(new Callback<Country[]>() {
            @Override
            public void onResponse(Call<Country[]> call, Response<Country[]> response) {
                if (response.isSuccessful()) {

                    Country[] countries = response.body();

                    if (countries != null) {
                        getCountriesListDataResponseMutableLiveData.setValue(NetworkResponse.success(countries));
                    }
                } else {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        getCountriesListDataResponseMutableLiveData.setValue(NetworkResponse.unAuthorised("Session Expired"));
                    } else {
                        String errorMessage = "Error!";
                        getCountriesListDataResponseMutableLiveData.setValue(NetworkResponse.error(null, errorMessage));
                    }
                }
            }

            @Override
            public void onFailure(Call<Country[]> call, Throwable t) {
                String failureMessage = "Please check your network connection.";
//                getCountriesListDataResponseMutableLiveData.setValue(NetworkResponse.error(allCountries(), failureMessage));
            }
        });
        return getCountriesListDataResponseMutableLiveData;
    }

    Country[] allCountries() {
        Country[] array = new Country[mAllCountries.getValue().size()];
        for (int n = 0; n < mAllCountries.getValue().size(); n++) {
            array[n].setIso(mAllCountries.getValue().get(n).getIso());
            array[n].setName(mAllCountries.getValue().get(n).getName());
            array[n].setPhone(mAllCountries.getValue().get(n).getPhone());
        }
        return array;
    }

    public void insert(List<CountryTable> countryTables) {
        new insertAsyncTask(mCountryDao).execute(countryTables);
    }


    private static class insertAsyncTask extends AsyncTask<List<CountryTable>, Void, Void> {

        private CountryDao mAsyncTaskDao;

        insertAsyncTask(CountryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(List<CountryTable>[] lists) {
            for (CountryTable c : lists[0]) {
                mAsyncTaskDao.insert(c);
            }
            return null;
        }
    }
}
