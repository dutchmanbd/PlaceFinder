package com.zxdmjr.placefinder.nav_drawer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.zxdmjr.placefinder.R;
import com.zxdmjr.placefinder.custom_fragment.CustomFragment;

public class AboutUs extends CustomFragment {

    WebView webView;

    public AboutUs() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.about_us, container, false);

        webView = (WebView)v.findViewById(R.id.webView);

        String htmlText = "<html><body style=\"text-align:justify;font-size:15px\"> %s </body></Html>";
        String myData = getResources().getString(R.string.details);

        webView.loadData(String.format(htmlText, myData), "text/html", "utf-8");

        return v;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        //Hide the action bar menu
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}

