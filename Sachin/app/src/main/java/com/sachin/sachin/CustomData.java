package com.sachin.sachin;

/**
 * Created by Sachin on 10/22/2015.
 */

import android.util.Log;

/** This is just a simple class for holding data that is used to render our custom view */
public class CustomData {
    private String mBackgroundColor;
    private String mText;
    private double mcost;

    String TAG = "CustomData";
    public CustomData(String itemName, String text, double cost) {
        mBackgroundColor = itemName;
        mText = text;
        mcost = cost;
    }

    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        Log.d(TAG, String.valueOf(mBackgroundColor));
        return mBackgroundColor;
    }

    /**
     * @return the text
     */
    public String getText() {
        Log.d(TAG, mText);
        return mText;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        Log.d(TAG, String.valueOf(mcost) );
        return mcost;
    }


    /**
     * @set the background border
     */
    //public void setBackgroundBorder(int border ){
      //  mBackgroundColor = border;
    //}
}
