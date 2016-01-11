package com.sachin.sachin;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by samurai on 07-Nov-15.
 */

enum ORDER_STATUS {
    DESIGN_SELECTION_IN_PROGRESS ,
    FABRIC_SELECTION_IN_PROGRESS,
    MEASUREMENTS_PROGRESS,
    PAYMENT_PROGRESS,
    ORDER_PLACED_TO_STORE,
    ORDER_COMPLETE
}

public class shpcrtOrder {
    private shpcrtDesign order_designComponent;
    private shpcrtFabric order_fabricComponent;
    private String orderID;
    private String TAG ="shpcrtOrder";
    private ORDER_STATUS order_complete_status ;

    /*
    public static void main(String[] args){

        for ( int i = 0; i < 5 ; i++){
            shpcrtOrder obj =  new shpcrtOrder();
            obj.addDesign("d1", i, 200*i, 0.5*i);
            obj.addFabric("f1", i, 100*i, 0.5*i);
            obj.addAddress();
            obj.processOrder();

            shpcrtWishList.wishList.put("a_"+String.valueOf(i), obj);
        }
    }

	/**/

    shpcrtOrder(){
        order_complete_status = ORDER_STATUS.DESIGN_SELECTION_IN_PROGRESS;
        order_designComponent = new shpcrtDesign();
        order_fabricComponent = new shpcrtFabric();
    }



    /**
     *addDesign(String name, int id, int cost, double d)
     */
    void addDesign(String name, int id, int cost, double d){
        Log.d(TAG, "addDesign " + name + "\t" + String.valueOf(id)  + "\t" + String.valueOf(cost) + "\t"+ String.valueOf(d));
        order_designComponent.setDesignDetails(name, id, cost, d);
        order_complete_status = ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS;
        //updateUI(); //Do not update here. Update after Fabric is selected

    }

    void addFabric(String name, int id, int priceUnitLen, double lenreq ){
        Log.d(TAG, "addFabric " + name + "\t" + String.valueOf(id) + "\t" + String.valueOf(priceUnitLen) + "\t" + String.valueOf(lenreq));
        order_fabricComponent.setFabDetails(name, id, priceUnitLen, lenreq);
    }

    void setOrderID(String orderId){
        orderID = orderId;
    }


    public boolean checkIfDesignIsSelected(){
       if(  order_designComponent.getDesignLenReq() > 0 ){
           return true;
       }else {
           return false;
       }
    }

    public boolean checkIfFabricIsSelected(){

        if(order_fabricComponent!=null && order_fabricComponent.get_fabName() != null && !order_fabricComponent.get_fabName().isEmpty()){
        if(  order_fabricComponent.get_fabName().length() > 0 ){
            return true;
        }else {
            return false;
        }
        }else{
            return false;
        }
    }

    public JSONObject getDetails(){
        JSONObject neworder = new JSONObject();


        try {
            neworder.put("designName", order_designComponent.getDesignName());
            neworder.put("designLenReq", order_designComponent.getDesignLenReq());
            neworder.put("designCost", order_designComponent.getDesignCost());
            neworder.put("fabricName", order_fabricComponent.get_fabName());
            neworder.put("fabricFabCost", order_fabricComponent.get_fabCost());
            neworder.put("fabricFabId", order_fabricComponent.get_fabId());
            neworder.put("orderId", orderID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String TAG = "newOrder";

        Log.d(TAG, order_designComponent.getDesignName() + neworder.toString());

        return neworder;
    }


    public void clear(){

        order_designComponent = null;
        order_fabricComponent = null;
        orderID = "";
        order_designComponent = new shpcrtDesign();
        order_fabricComponent = new shpcrtFabric();

        Log.d(TAG, "Cleared order details " + orderID);
    }

    public String getOrderID(){
        return orderID;
    }
}
