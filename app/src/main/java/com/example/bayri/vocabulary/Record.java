package com.example.bayri.vocabulary;

/**
 * Created by bayri on 31.10.2015.
 */
public class Record {

    private long mId;
    private String mRussian;
    private String mEnglish;
    private int mUsed;

    public Record() {
        mRussian = "";
        mEnglish = "";
    }

    public Record(String english, String russian) {
        mRussian = russian;
        mEnglish = english;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getEnglish() {
        return mEnglish;
    }

    public void setEnglish(String mEnglish) {
        this.mEnglish = mEnglish;
    }

    public String getRussian() {
        return mRussian;
    }

    public void setRussian(String mRussian) {
        this.mRussian = mRussian;
    }

    public int getUsed() {
        return mUsed;
    }

    public void setUsed(int mUsed) {
        this.mUsed = mUsed;
    }
}
