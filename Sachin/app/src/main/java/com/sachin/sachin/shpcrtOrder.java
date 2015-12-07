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
    ADDRESS_FILLED,
    ORDER_COMPLETE
}

public class shpcrtOrder {
    private shpcrtDesign order_designComponent;
    private shpcrtFabric order_fabricComponent;

    private String address_door_no;
    private String address_street_no;
    private String address_street_name;
    private String address_area_name;
    private String address_city;
    private String address_state;
    private String address_country;
    private String address_pincode;
    private String address_mobile_no;
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

    //Call this everytime something changes in the Order Object
    //can be on main thread
    void updateUI(){
        //Catch the view that is displaying Price and details.
        //update only that.
        //Can be done either on Thread or on Main thread.
        //dataClicked.getText() + "\n" + String.valueOf(dataClicked.getCost()
        Designs.selectedItemDescription.setText("This is " + order_designComponent.getDesignName() + ". It requires " +  String.valueOf(order_designComponent.getDesignLenReq()) + " of fabric. " );
        //Designs.cartTextView.setText( "Cart(0)"); //order_designComponent.getDesignName() + "\n" + order_designComponent.getDesignCost());
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
        Log.d(TAG, "addFabric " + name + "\t" + String.valueOf(id)  + "\t" + String.valueOf(priceUnitLen) + "\t"+ String.valueOf(lenreq));
        order_fabricComponent.setFabDetails(name, id, priceUnitLen, lenreq);
        updateUI();
    }

    void addAddress(){
        //Either pick up from the DB or
        //Ask for new address

        //show a new address form and get each of the field and save madi

        boolean newAddress = true; //get from user
        if(newAddress) {
            this.address_door_no ="";
            this.address_street_no ="";
            this.address_street_name="";
            this.address_area_name="";
            this.address_city="";
            this.address_state="";
            this.address_country="";
            this.address_pincode="";
            this.address_mobile_no="";
        }else {
            //Parse DB here and show list of address
            //After user selects, update all the fields.
        }

        order_complete_status = ORDER_STATUS.ADDRESS_FILLED;

    }

    boolean processOrder(){

        boolean status = false;
        if( order_complete_status.equals(ORDER_STATUS.ADDRESS_FILLED)){
            //Store in DB


            //Trigger SMS


            //Trigger EMAIL



            //Future : post it to server


        }else {
            //Log.d("", String.valueOf(order_complete_status.toString()));
            //order not completed, take him to activity where it is needed.
        }

        return status;
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String TAG = "newOrder";

        Log.d(TAG, order_designComponent.getDesignName() + neworder.toString());

        return neworder;
    }
}
