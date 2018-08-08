package com.example.android.newsappstage2;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    private String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {

        // if URL is null or not present, don't proceed with load
        if (mUrl == null) {
            return null;
        }
        // do HTTP request to get news info and handle returned data
        List<Article> newsList = Utils.getNewsData(mUrl);
        return newsList;

    }
}