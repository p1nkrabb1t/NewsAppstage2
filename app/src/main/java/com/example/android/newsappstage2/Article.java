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
    public Article(int Thumbnail, String Title, String Author, String whenPublished, String Section, String Url) {
        mThumbnail = Thumbnail;
        mTitle = Title;
        mAuthor = Author;
        mWhenPublished = whenPublished;
        mSection = Section;
        mUrl = Url;
    }

    /**
     * Constructs a new object containing title, author, publish date & section
     */
    public Article(String Title, String Author, String whenPublished, String Section, String Url) {
        mTitle = Title;
        mAuthor = Author;
        mWhenPublished = whenPublished;
        mSection = Section;
        mUrl = Url;
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
