package com.example.smartweather.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.smartweather.ForecastFragment;
import com.example.smartweather.R;
import com.example.smartweather.RadarFragment;
import com.example.smartweather.SuggestionsFragment;
import com.example.smartweather.TodayFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //    @StringRes
//    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private static final String[] TAB_TITLES_STRINGS = new String[]{"Today", "Forecast", "Radar", "Suggestions"};
//    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
//        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return new TodayFragment();
            case 1:
                return new ForecastFragment();
            case 2:
                return new RadarFragment();
            case 3:
                return new SuggestionsFragment();
            default:
                return null;
        }
//        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        return mContext.getResources().getString(TAB_TITLES[position]);
        return TAB_TITLES_STRINGS[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES_STRINGS.length;
    }
}