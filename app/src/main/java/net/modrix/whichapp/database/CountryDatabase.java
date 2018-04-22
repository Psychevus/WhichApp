package net.modrix.whichapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Modrix
 */

@Database(entities = {CountryTable.class}, version = 1)
public abstract class CountryDatabase extends RoomDatabase {

    public abstract CountryDao countryDao();

    private static CountryDatabase INSTANCE;


    public static CountryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CountryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CountryDatabase.class, "country_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}