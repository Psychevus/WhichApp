package net.modrix.whichapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.modrix.whichapp.R;
import net.modrix.whichapp.fragment.CountryListFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Modrix
 */

public class CountryListActivity extends AppCompatActivity {

    CountryListFragment mReciepeListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_list_activity);



        if(savedInstanceState == null) {
            addReciepeListFragment();
        }
    }

    private void addReciepeListFragment() {

        if(mReciepeListFragment == null){
            mReciepeListFragment = CountryListFragment.newInstance();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.country_fragment_container,mReciepeListFragment)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
