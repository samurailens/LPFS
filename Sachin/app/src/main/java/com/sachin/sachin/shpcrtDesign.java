package com.sachin.sachin;

import android.util.Log;

/**
 * Created by samurai on 07-Nov-15.
 */
public class shpcrtDesign {


        private String des_name;
        private int des_id;

        private int des_price_unit_length;
        private double des_length_required_for_design;
        private int des_cost;
        private int des_total_cost;
        private String TAG = "shpcrtDesign";

        void setDesignDetails(String name ,
                              int id,
                              int cost,
                              double lenReq  ){
            des_name = name;
            des_id = id;

            des_length_required_for_design = lenReq;
            des_cost = cost;
            des_total_cost = des_cost  ; // When showing in the cart, we should inform user about the
            //length of the fabric that is required.
            Log.d(TAG, name +  String.valueOf(des_id) + String.valueOf(cost) + String.valueOf(lenReq) );

        }

        String getDesignName(){
            return des_name;
        }

        double getDesignCost(){
            return des_cost;
        }

        double getDesignLenReq(){
            return des_length_required_for_design;
        }



}
