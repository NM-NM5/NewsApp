package com.example.newsapp;

public class Event {
    private String mSection;
    private String mTitle;
    private String mDate;
    private String mTime;
    private String mUrl;

    public Event(String section, String name, String date, String time, String url) {
        mSection = section;
        mTitle = name;
        mDate = date;
        mTime = time;
        mUrl = url;
    }

    public String getSection() {
        return mSection;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getTime() {
        return mTime;
    }

    public String getURL() {
        return mUrl;
    }
}
