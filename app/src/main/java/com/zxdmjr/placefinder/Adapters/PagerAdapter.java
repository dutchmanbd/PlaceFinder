package com.zxdmjr.placefinder.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.zxdmjr.placefinder.tabitems.AllTab;
import com.zxdmjr.placefinder.tabitems.SavedTab;

/**
 * Created by user on 9/5/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter
{
    private int numberOfTabs;
    public PagerAdapter(FragmentManager fm, int tabNo)
    {
        super(fm);
        numberOfTabs = tabNo;
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new AllTab();
                break;
            case 1:
                fragment = new SavedTab();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount()
    {
        return numberOfTabs;
    }


}
