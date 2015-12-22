package com.sachin.sachin;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Sachin on 10/22/2015.
 */
public class CartManager {

    //No variables should be accessible from anywhere, only methods should be provided.

    CartManager(){

    }

    public void loadImages(String s1, String s2){

                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


                    }

                    @Override
                    public void onBitmapFailed(Drawable d) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable r) {

                    }
                };
    }
    //private Map<String design , String fabric>  Order ;
    /*
    * add item to cart
     */
    public void addToCart(Object item){

        //things added to cart should be present in shared preferences
        //we should not push into DB
        //temp orders will be in shared preferences

    }

    /*
    * remove item from cart if existing
     */
    public void removeFromCart(Object itemToDelete){

    }

    /*
    * get recent orders from database
     */
    public void getRecentOrdersFromDatabase(){

    }

    /*
    * save order to database mngr - only completed order
     */
    public void saveOrderToDatabase(){

    }

    public void saveOrderToSharedPreferences(){

    }


}
