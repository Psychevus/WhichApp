package net.modrix.whichapp.util;

import net.modrix.whichapp.data.Country;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Modrix
 */

public interface WebService {

    @GET("countries")
    Call<Country[]> getCountriesListData();
}
