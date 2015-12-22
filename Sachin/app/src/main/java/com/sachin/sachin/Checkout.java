package com.sachin.sachin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends Activity {

    String selectedSizeTxt ;
    String TAG = "Checkout";
    ArrayAdapter myAdapter;
    ListView orderList;
    List<String> orderIdsFromDb;
    List<String> listTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        updateCart();
        updateOrderIds();
    }

    public void updateCart(){

        int size = MainActivity.mydbmanager.getAllOrders().size();

        List list = MainActivity.mydbmanager.getAllOrders() ;


        listTitle = new ArrayList<String>();

        if(list.size() > 0 ) {
            for (int i = 0; i < list.size(); i++) {

                Log.d(TAG, list.get(i).toString());
                try {
                    JSONObject jsonObj = new JSONObject(list.get(i).toString());
                    //listTitle.add(i, jsonObj.toString());
                    listTitle.add(jsonObj.toString());
                    Log.d(TAG, "Add " + jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Log.d(TAG,"list size is zero ");
        }
        myAdapter = new shoppingCartCustomArrayAdapter(this, listTitle);
        //myAdapter = new ArrayAdapter(this, R.layout.row_layout, R.id.listText, listTitle);
        orderList = (ListView) findViewById(R.id.checkoutorderlist);
        orderList.setAdapter(myAdapter);

        orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.d(TAG, "Item clicked position " + String.valueOf(position));
                Object dataClicked = (Object) orderList.getItemAtPosition(position);

            }
        });


        //myAdapter = new ArrayAdapter(this, R.layout.row_layout, R.id.listText, listTitle);
        //getListView().setOnItemClickListener(this);
        //setListAdapter(myAdapter);

    }

    public void updateOrderIds(){
        orderIdsFromDb  = new ArrayList<String>();

        //Map of OrderIDs
        List listOrderIds = MainActivity.mydbmanager.getAllOrdersIds();
        if(listOrderIds.size() > 0 ) {
            for (int i = 0; i < listOrderIds.size(); i++) {

                Log.d(TAG, listOrderIds.get(i).toString());
                try {
                    //JSONObject jsonObj = new JSONObject(listOrderIds.get(i).toString());
                    //listTitle.add(i, jsonObj.toString());
                    String s = listOrderIds.get(i).toString();
                    orderIdsFromDb.add(s);
                    Log.d(TAG, "Add OrderIds" + s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            Log.d(TAG,"listOrderIds size is zero ");
        }
    }
    public void deleteFromCart(View v){
        View parentRow = (View) v.getParent();
        ListView listView = (ListView) parentRow.getParent();
        final int position = listView.getPositionForView(parentRow);
        /* myAdapter.remove(position);
        myAdapter.notifyDataSetChanged();
        orderList.setAdapter(myAdapter);*/

        ((shoppingCartCustomArrayAdapter)myAdapter).deleteItem(position);
        String idToDel =  orderIdsFromDb.get(position);
        try {
            int noofRowsEffected = MainActivity.mydbmanager.deleteOrder(Integer.parseInt(idToDel));
            Log.d(TAG, "Delete from DB " + idToDel + "Row Del " + String.valueOf(noofRowsEffected));
        }catch (SQLiteException e){
            e.printStackTrace();
        }

        if(position == 0) {
            MainActivity.cartNewOrder.clear();
        }

        updateCart();
        Log.d(TAG, "Delete from cart " + String.valueOf(position) );
    }

    public void selectSize(View v){

        String setTxt = "Done";
        String btntxt = "";
        Button btn = (Button) findViewById(R.id.Sizebtn); //selectSize
        btntxt = btn.getText().toString();

        if(btntxt.contains("Select Size")){

            // previously invisible view
            View myView = findViewById(R.id.sizelistView);
            // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;
            // get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();

            btn.setText(setTxt);
        }else {
            //Done
            // previously visible view
            final View myView = findViewById(R.id.sizelistView);

        // get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

        // get the initial radius for the clipping circle
            int initialRadius = myView.getWidth();

        // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            });

        // start the animation
            anim.start();
            RadioGroup radioSexGroup = null;
            RadioButton radioSexButton = null;
            radioSexGroup=(RadioGroup)findViewById(R.id.radiogroupsizelist);
            int selectedId=radioSexGroup.getCheckedRadioButtonId();
            radioSexButton=(RadioButton)findViewById(selectedId);

            //Toast.makeText(this,radioSexButton.getText(), Toast.LENGTH_SHORT).show();

            TextView selectedSize = (TextView) findViewById(R.id.selectedSizetxtView);

            if(selectedSize != null && radioSexButton !=null){
            selectedSizeTxt = radioSexButton.getText().toString();
            selectedSize.setText("Selected " + radioSexButton.getText());
            selectedSize.setTextColor(Color.parseColor("#83EB6D"));
            selectedSize.setVisibility(View.VISIBLE);
            }
            //selectedSize.setBackgroundColor(Color.parseColor("#83EB6D"));

            btn.setText("Select Size");
        }


    }

    public void placeOrder(View v){
        //Go to home

        SendMail sendMail = new SendMail();
        String orderDetails = "";
        String toEmailAddress = MainActivity.activeUserEmail;

        if( !toEmailAddress.contains("@")){
            Toast.makeText(this, "Sign in to place your order", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        }


        listTitle.size();
        String tmpline = "-----------------------------------------------------------------------------------------";
        List<String > OrderListToSend = new ArrayList<String>();
        try{
            for(int i=0 ; i< listTitle.size(); i++){
            Object a  =  listTitle.get(i);
            a.hashCode();
            Log.d(TAG, "Object " + a.toString());
               Object json = new JSONTokener(a.toString()).nextValue();
                if (json instanceof JSONObject) {
                    //you have an object
                    Object designName = ((JSONObject) json).get("designName");
                    Object fabricName = ((JSONObject) json).get("fabricName");
                    Object fabricFabCost = ((JSONObject) json).get("fabricFabCost");
                    Object designCost = ((JSONObject) json).get("designCost");
                    int totalCost = Integer.parseInt(fabricFabCost.toString()) + Integer.parseInt(designCost.toString());
                    String tempOrder = "\n\n<p> <b> Design : " + designName + " </b> \t Price : <b> Rs."+ designCost + "</b></p>"+ "\n<p><b>Fabric : " + fabricName + "</b>\t Price : <b>Rs." + fabricFabCost + "</b> </p>"+ "\n\n<p>Total : Rs."+ String.valueOf(totalCost) + "</p>\n\n" ;
                    OrderListToSend.add(tempOrder);
                    orderDetails +=  tempOrder ;
                    //holder.textView.setText("Design: " + designName.toString() + "\nFabric: " + fabricName.toString() ); //+ " Rs." + String.valueOf(i)
                    //holder.orderCost.setText("Rs."+ String.valueOf(i));
                }
                else if (json instanceof JSONArray) {
                    //you have an array
                    Object b = ((JSONArray) json).get(0);
                    b.hashCode();
                }
            }

            List listOrderIds = MainActivity.mydbmanager.getAllOrdersIds();
            if(listOrderIds.size() > 0 ) {
                for (int i = 0; i < listOrderIds.size(); i++) {
                    MainActivity.mydbmanager.updateStatus(String.valueOf(ORDER_STATUS.ORDER_PLACED_TO_STORE), Integer.parseInt(listOrderIds.get(i).toString()));
                }
            }

            //JSONArray ar = new JSONArray(getItem(position));
            // KO Object [] a =  getItem(position).toArray();
            //KO String s = getItem(position).toString();
            //KO JSONObject jsonObj = new JSONObject(getItem(position));
            /* JSONObject jsonObj = new JSONObject(getItem(position).toString());
            holder.textView.setText(jsonObj.getString("designName"));
            Log.d(TAG, "Populate the text position: " + String.valueOf(position) + "\t val: " + jsonObj.getString("designName"));
            JSON*/
        }catch (Exception e){
            e.printStackTrace();
        }
        if(OrderListToSend.size() > 0) {

            String testMail = "<p><b>Some Content</b></p>" + "<small><p>More content</p></small>";


            sendMail.sendMail(toEmailAddress, "Your order on Le Pape store", orderDetails);//orderDetails);
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(this, "Order placed.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Nothing to order.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(!MainActivity.cartNewOrder.checkIfDesignIsSelected()){
            startActivity(new Intent(this, Designs.class));
        }
    }
}