package com.zxdmjr.placefinder.nav_drawer;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxdmjr.placefinder.Adapters.PagerAdapter;
import com.zxdmjr.placefinder.custom_fragment.CustomFragment;
import com.zxdmjr.placefinder.R;

public class Home extends CustomFragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("All").setTag("AllTab"));
        tabLayout.addTab(tabLayout.newTab().setText("Saved").setTag("SavedTab"));

        FragmentManager fragmentManager = getChildFragmentManager();
        PagerAdapter adapter = new PagerAdapter(fragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                Object tag = tab.getTag();
                Log.e("Object", String.valueOf(tag));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
    }

}
