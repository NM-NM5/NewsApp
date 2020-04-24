package com.example.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader<List<Event>> {

    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl1;
    private String mUrl2;

    public NewsLoader(Context context, String url1, String url2) {
        super(context);
        mUrl1 = url1;
        mUrl2 = url2;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Event> loadInBackground() {
        if (mUrl1 == null && mUrl2 == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        return QueryUtils.fetchNewsData(mUrl1, mUrl2);
    }
}