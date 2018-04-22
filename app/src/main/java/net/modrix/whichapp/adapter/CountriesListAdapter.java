package net.modrix.whichapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.modrix.whichapp.R;
import net.modrix.whichapp.data.Country;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Modrix
 */

public class CountriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int T_HEADER = 0;
    private static final int T_NORMAL = 1;
    private static final int T_FOOTER = 2;

    private final ArrayList<Country> mCountriesArrayList;

    public CountriesListAdapter(ArrayList<Country> countriesArrayList) {
        this.mCountriesArrayList = countriesArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == T_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.countries_list_item, parent, false);
            return new CountriesListViewHolderNormal(view);
        } else if (viewType == T_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.countries_list_header, parent, false);
            return new CountriesListViewHolderHeader(view);
        } else if (viewType == T_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.countries_list_footer, parent, false);
            return new CountriesListViewHolderFooter(view);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            Country country = mCountriesArrayList.get(position - 1);
            ((CountriesListViewHolderNormal) holder).countryIsoTextview.setText(country.getIso());
            ((CountriesListViewHolderNormal) holder).countryNameTextview.setText(country.getName());
            ((CountriesListViewHolderNormal) holder).countryPhoneTextview.setText("+" + country.getPhone());
        }
    }

    @Override
    public int getItemCount() {
        return mCountriesArrayList.size() + 2;
    }

    class CountriesListViewHolderNormal extends RecyclerView.ViewHolder {

        @BindView(R.id.country_name_textview)
        TextView countryNameTextview;

        @BindView(R.id.country_iso_textview)
        TextView countryIsoTextview;

        @BindView(R.id.country_phone_textview)
        TextView countryPhoneTextview;

        CountriesListViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CountriesListViewHolderHeader extends RecyclerView.ViewHolder {

        CountriesListViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CountriesListViewHolderFooter extends RecyclerView.ViewHolder {

        CountriesListViewHolderFooter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        else if (position == mCountriesArrayList.size() + 1)
            return 2;
        else
            return 1;
    }
}
