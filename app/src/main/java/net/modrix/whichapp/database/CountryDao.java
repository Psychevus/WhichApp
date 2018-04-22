package net.modrix.whichapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Modrix
 */

@Dao
public interface CountryDao {

    @Insert
    void insert(CountryTable country);

    @Query("DELETE FROM country_table")
    void deleteAll();

    @Query("SELECT * from country_table")
    LiveData<List<CountryTable>> getAllCountries();
}