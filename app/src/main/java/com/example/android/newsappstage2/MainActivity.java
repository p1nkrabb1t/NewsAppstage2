package com.example.android.newsappstage2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {


    // URL to find news updates for July & August 2018
    private static final String DATA_SOURCE_URL =
            "https://content.guardianapis.com/search?show-fields=byline%2Cthumbnail" +
                    "&q=uk%20AND%20NOT%20corrections&api-key=cf6b8892-08ce-42ad-b016-b194df810122";

    public TextView noDataMessage;
    public ProgressBar progress;

    //Adapter for the list of news Articles
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check for network connection and update 'isConnected' variable with true or false value
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        // Create an ArrayList of news articles.
        ArrayList<Article> articlesList = new ArrayList<>();


        // Create a new {@link ArrayAdapter} of articles
        mAdapter = new NewsAdapter(this, articlesList);

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView) findViewById(R.id.list);

        noDataMessage = (TextView) findViewById(R.id.empty_text);
        listView.setEmptyView(noDataMessage);

        //Set progress bar to show app info is loading
        progress = (ProgressBar) findViewById(R.id.progress_bar);

        // Set the adapter on the Listview
        listView.setAdapter(mAdapter);


        // Set a click listener on the Article item
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Article currentSelection = (Article) mAdapter.getItem(position);
                Uri url = Uri.parse(currentSelection.getUrl());

                Intent i = new Intent(Intent.ACTION_VIEW, url);

                startActivity(i);
            }
        });

        //If connection is available then proceed with the load, otherwise cease and show message
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this).forceLoad();
        } else {
            progress.setVisibility(View.GONE);
            noDataMessage.setText(R.string.no_connection);
        }
    }

    //method to inflate the settings menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //open the settings activity on selecting settings option
    @Override
    public boolean onOptionsItemSelected(MenuItem itemSelected) {
        int id = itemSelected.getItemId();
        if (id == R.id.menu_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(itemSelected);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle bundle) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String subSection = prefs.getString(getString(R.string.settings_key_categories),
                getString(R.string.settings_default_categories));
        String numOfResults = prefs.getString(getString(R.string.settings_key_results_limit),
                getString(R.string.settings_default_results_limit));



        Uri startURL = Uri.parse(DATA_SOURCE_URL);

        // add query selections or defaults and return the new URL
        Uri.Builder uriBuilder = startURL.buildUpon();

        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("section", "news");
        uriBuilder.appendQueryParameter("subsection", subSection);
        uriBuilder.appendQueryParameter("results", numOfResults);

        return new ArticleLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articleInfo) {
        progress.setVisibility(View.GONE);
        noDataMessage.setText(R.string.no_results);
        // Clear the adapter of previous Article data
        mAdapter.clear();

        // If there Articles are present then add them to the adapter and update
        if (articleInfo != null && !articleInfo.isEmpty()) {
            mAdapter.addAll(articleInfo);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        // Clear the adapter of previous Article data
        mAdapter.clear();

    }


}
