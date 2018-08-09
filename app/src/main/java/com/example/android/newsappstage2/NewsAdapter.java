package com.example.android.newsappstage2;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NewsAdapter extends ArrayAdapter {


    public NewsAdapter(Activity context, ArrayList<Article> news_info) {
        super(context, 0, news_info);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }
        // Get the object located at this position in the list
        Article currentSelection = (Article) getItem(position);


        //Create title object to store article title info
        String title = currentSelection.getTitle();

        //Create author object to store info of the author
        String author = currentSelection.getAuthor();

        //Create a new date object to store the date the article was published in unix timestamp
        String whenPublished = (currentSelection.getPublished());
        String date = "";
        String time = "";
        String breakpoint = "T";
        String shortTime = "";

        if (whenPublished.contains(breakpoint)) {
            // Split it.
            String[] parts = whenPublished.split(breakpoint);
            date = parts[0];
            time = parts[1];
            shortTime = time.substring(0, 5);

        } else {
            time = null;
        }

        
        //Create section object to store section the article is from
        String section = currentSelection.getSection();

        // Find and update the TextView in the news_item.xml layout with the article title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(title);

        // Find and update the TextView in the news_item.xml layout with the article title
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        authorTextView.setText(getContext().getString(R.string.by, author));

        //check if byline present and hide the author text view if not available
        if (author == null) {
            authorTextView.setVisibility(View.GONE);
        }

        // Find and update the TextView in the news_item.xml layout with the date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(date);

        // Find and update the TextView in the news_item.xml layout with the date
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        timeTextView.setText(getContext().getString(R.string.at, shortTime));

        // Find and update the TextView in the news_item.xml layout with the section type
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        sectionTextView.setText(section);

        return listItemView;
    }


}

