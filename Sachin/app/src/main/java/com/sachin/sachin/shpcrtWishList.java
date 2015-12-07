package com.sachin.sachin;

import java.util.HashMap;

/**
 * Created by samurai on 07-Nov-15.
 */
public class shpcrtWishList {
    static HashMap<String , shpcrtOrder > wishList = new HashMap<String, shpcrtOrder>();

    boolean addToWishlist(String orderID, shpcrtOrder order){

        if(wishList.put(orderID, order) != null ) {
            return true;
        }else {
            return false;
        }
    }

    boolean removeFromWishlist(String orderID){

        if(wishList.remove(orderID) != null ) {
            return true;
        }else {
            return false;
        }
    }

    HashMap getWishList(){
        return wishList;
    }

    void clearWishList(){
        //Ask user for confirmation
        wishList.clear();
    }
}
