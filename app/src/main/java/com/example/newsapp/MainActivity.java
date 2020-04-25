package com.example.newsapp;


import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Event>> {

    private ProgressBar progressBar;
    private TextView mEmptyStateTextView;
    private EventAdapter mAdapter;
    private static final int LOADER_ID = 1;
    private static final String REQUEST_URL_1 = "https://content.guardianapis.com/search?q=debates&api-key=test";
    private static final String REQUEST_URL_2 = "https://content.guardianapis.com/search?&tag=politics/politics&from-date=2014-01-01&api-key=test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeListView = findViewById(R.id.list_of_news);
        mAdapter = new EventAdapter(this, new ArrayList<Event>());
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Event currentEvent = mAdapter.getItem(position);

                assert currentEvent != null;
                Uri eventUri = Uri.parse(currentEvent.getURL());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, eventUri);
                startActivity(websiteIntent);
            }
        });

        progressBar = findViewById(R.id.loading_spinner);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_Internet);
            progressBar.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {

        Uri baseUri_1 = Uri.parse(REQUEST_URL_1);
        Uri.Builder uriBuilder_1 = baseUri_1.buildUpon();
        uriBuilder_1.appendQueryParameter("show-tags", "contributor");

        Uri baseUri_2 = Uri.parse(REQUEST_URL_2);
        Uri.Builder uriBuilder_2 = baseUri_2.buildUpon();
        uriBuilder_2.appendQueryParameter("show-tags", "contributor");

        return new NewsLoader(MainActivity.this, uriBuilder_1.toString(), uriBuilder_2.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> news) {
        progressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_news);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        mAdapter.clear();
    }
}
