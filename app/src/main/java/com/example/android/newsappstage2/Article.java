package com.example.android.newsappstage2;


public class Article {


    // decimal value
    private int mThumbnail;

    // String value
    private String mTitle;

    // String value
    private String mAuthor;

    // Long Integer value
    private String mWhenPublished;

    // String value
    private String mSection;

    // String value
    private String mUrl;


    /**
     * Constructs a new object containing thumbnail image, title, author, publish date & section
     */
    public Article(int thumbnail, String title, String author, String whenPublished, String section, String url) {
        mThumbnail = thumbnail;
        mTitle = title;
        mAuthor = author;
        mWhenPublished = whenPublished;
        mSection = section;
        mUrl = url;
    }

    /**
     * Constructs a new object containing title, author, publish date & section
     */
    public Article(String title, String author, String whenPublished, String section, String url) {
        mTitle = title;
        mAuthor = author;
        mWhenPublished = whenPublished;
        mSection = section;
        mUrl = url;
    }


    //get the thumbnail image
    public int getThumbnail() {
        return mThumbnail;
    }

    //get the title
    public String getTitle() {
        return mTitle;
    }

    //get the author
    public String getAuthor() {
        return mAuthor;
    }

    //get the date the article was published
    public String getPublished() {
        return mWhenPublished;
    }

    //get the section the article belongs to
    public String getSection() {
        return mSection;
    }

    //get the web address
    public String getUrl() {
        return mUrl;
    }

}
