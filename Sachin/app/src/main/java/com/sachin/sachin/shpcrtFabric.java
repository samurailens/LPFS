package com.sachin.sachin;

import android.util.Log;

/**
 * Created by samurai on 07-Nov-15.
 */
public class shpcrtFabric {
    private String fab_name;
    private int fab_id;

    private int fab_price_unit_length;
    private double fab_length_required_for_design;

    private double fab_total_cost;
    private String TAG ="shpcrtFabric";

    void set_fabName(String name){
        fab_name = name;
    }

    void set_fabId(int Id){
        fab_id = Id;
    }

    void set_fabCost(int cost){
        fab_price_unit_length = cost;
    }

    void set_fabLenReq(int reqLen){
        fab_length_required_for_design = reqLen;
    }

    void set_fabTotalCost(int Totalcost){
        fab_total_cost = Totalcost;
    }

    String get_fabName(){
        return fab_name;
    }

    int get_fabId(){
        return fab_id ;
    }

    int get_fabCost(){
        return fab_price_unit_length ;
    }

    double get_fabLenReq(int cost){
        return fab_length_required_for_design ;
    }

    void setFabDetails(String name ,
                       int id,
                       int priceUnitLen,
                       double lenreq  ){
        fab_name = name;
        fab_id = id;

        fab_price_unit_length = priceUnitLen;
        fab_length_required_for_design = lenreq;
        fab_total_cost = fab_price_unit_length * fab_length_required_for_design ;

        Log.d(TAG,fab_name +  String.valueOf(fab_id) + String.valueOf(fab_price_unit_length) + String.valueOf(fab_total_cost) );
    }

}
