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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {


    // URL to find general news updates
    private static final String DATA_SOURCE_URL =
            "https://content.guardianapis.com/search?show-fields=byline&q=NOT%20corrections";

    private static final String mApiKey = "cf6b8892-08ce-42ad-b016-b194df810122";

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


        // Create a new ArrayAdapter of articles
        mAdapter = new NewsAdapter(this, articlesList);

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
        //retrieve the users preference settings
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //convert preferences to string for use in URL
        String categories = sharedPrefs.getString(getString(R.string.settings_key_categories),
                getString(R.string.settings_default_categories));
        String resultsLimit = sharedPrefs.getString(getString(R.string.settings_key_results_limit),
                getString(R.string.settings_default_results_limit));


        /*
          convert to int, check if numeric entry is within allowable range;
          if not in allowable range set to min or max available and notify user;
          convert back to string to make ready for URL
        */
        int resultsLimitChecker = Integer.parseInt(resultsLimit);
        if (resultsLimitChecker < 1) {
            int tempResultHolder = 1;
            resultsLimit = Integer.toString(tempResultHolder);
            Toast.makeText(this, R.string.toast_min_results, Toast.LENGTH_LONG).show();
        }
        if (resultsLimitChecker > 200) {
            int tempResultHolder = 200;
            resultsLimit = Integer.toString(tempResultHolder);
            Toast.makeText(this, R.string.toast_max_results, Toast.LENGTH_LONG).show();
        }


        Uri startURL = Uri.parse(DATA_SOURCE_URL);


        // add query selections or defaults and return the new URL
        Uri.Builder uriBuilder = startURL.buildUpon();

        uriBuilder.appendQueryParameter("api-key", mApiKey);
        uriBuilder.appendQueryParameter("section", categories);
        uriBuilder.appendQueryParameter("page-size", resultsLimit);

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
