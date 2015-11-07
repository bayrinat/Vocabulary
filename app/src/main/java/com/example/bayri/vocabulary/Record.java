package com.example.bayri.vocabulary;

/**
 * Simple record class
 */
public class Record {

    private long mId;
    private String mRussian;
    private String mEnglish;
    private int mCategory;
    private int mKnown;

    public Record() {
        mRussian = "";
        mEnglish = "";
        mCategory = 0;
        mKnown = 0;
    }

    public Record(String english, String russian) {
        mRussian = russian;
        mEnglish = english;
        mCategory = 0;
        mKnown = 0;
    }

    public Record(String english, String russian, int category) {
        mRussian = russian;
        mEnglish = english;
        mCategory = category;
        mKnown = 0;
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

    public int getCategory() {
        return mCategory;
    }

    public void setCategory(int mCategory) {
        this.mCategory = mCategory;
    }

    public int getUsed() {
        return mKnown;
    }

    public void setUsed(int mUsed) {
        this.mKnown = mUsed;
    }
}
