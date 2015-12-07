package com.sachin.sachin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class Fabric extends Activity {

    public static boolean comingFromBackPressed = false;
    public static int lastSelectedIndex = 0;
    private HorizontalListView mHlvSimpleList;
    private HorizontalListView mHlvCustomList;
    private HorizontalListView mHlvCustomListWithDividerAndFadingEdge;

    private String  TAG = "Fabric";
    private String[] mSimpleListValues = new String[] { "Android", "List", "View" };//,
    //"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
    //"Linux", "OS/2" };

    static int previouslySelectedDesignPosition = 0;
    static int currentSelectedDesignPistion = 0;
    static TextView cartTextView;
    static TextView selectedItemDescription;
    public ImageView previewImageViewFabric;
    public ArrayList<String> existingOrdersinDb;
    public String lastInsertedOrderId = "-1";
    ProgressBar progressBar;

    private CustomData[] mCustomData = new CustomData[] {
            //Co;pr , Name , cost
            new CustomData(Color.RED, "Red", 100),
            new CustomData(Color.DKGRAY, "Dark Gray" , 150),
            new CustomData(Color.GREEN, "Green",200 ),
            new CustomData(Color.LTGRAY, "Light Gray", 250),
            new CustomData(Color.WHITE, "White", 300),
            new CustomData(Color.RED, "Red", 100),
            new CustomData(Color.BLACK, "Black", 100),
            new CustomData(Color.CYAN, "Cyan", 100),
            new CustomData(Color.DKGRAY, "Dark Gray", 100),
            new CustomData(Color.GREEN, "Green", 100),
            new CustomData(Color.RED, "Red", 100),
            new CustomData(Color.LTGRAY, "Light Gray", 100),
            new CustomData(Color.WHITE, "White", 100),
            new CustomData(Color.BLACK, "Black", 100),
            new CustomData(Color.CYAN, "Cyan", 100),
            new CustomData(Color.DKGRAY, "Dark Gray", 100),
            new CustomData(Color.GREEN, "Green", 100),
            new CustomData(Color.LTGRAY, "Light Gray", 100),
            new CustomData(Color.RED, "Red", 100),
            new CustomData(Color.WHITE, "White", 100),
            new CustomData(Color.DKGRAY, "Dark Gray", 100),
            new CustomData(Color.GREEN, "Green", 100)
//            new CustomData(Color.LTGRAY, "Light Gray", 100),
//            new CustomData(Color.WHITE, "White"),
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.BLACK, "Black"),
//            new CustomData(Color.CYAN, "Cyan"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.LTGRAY, "Light Gray"),
//            new CustomData(Color.RED, "Red"),
//            new CustomData(Color.WHITE, "White"),
//            new CustomData(Color.BLACK, "Black"),
//            new CustomData(Color.CYAN, "Cyan"),
//            new CustomData(Color.DKGRAY, "Dark Gray"),
//            new CustomData(Color.GREEN, "Green"),
//            new CustomData(Color.LTGRAY, "Light Gray")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_fabric);
        // Get references to UI widgets
        //mHlvSimpleList = (HorizontalListView) findViewById(R.id.hlvSimpleListfabric);
        mHlvCustomList = (HorizontalListView) findViewById(R.id.hlvCustomListfabric);
        //WithDivider
        //mHlvCustomListWithDividerAndFadingEdge = (HorizontalListView) findViewById(R.id.hlvCustomListWithDividerAndFadingEdgefabric);
        //mHlvCustomListWithDividerAndFadingEdge.setVisibility(View.GONE);
        //setupSimpleList();
        setupCustomLists();
        //Designs.cartNewOrder =  new shpcrtOrder();
        // if( comingFromBackPressed ){
        // then set selected to the previously selected index
        // }
        previouslySelectedDesignPosition = 0;
        currentSelectedDesignPistion = 0;
        previewImageViewFabric = (ImageView) findViewById(R.id.previewImageviewfabric);
        cartTextView = (TextView) findViewById(R.id.carttxtviewfabric);
        selectedItemDescription = (TextView) findViewById(R.id.selectedItemDescriptiontxtviewfabric);
        selectedItemDescription.setText("Content Description is shown here");
        existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
        lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(33);
        updateCartUI();
        Log.d(TAG, "OnCreate Complete");
    }




    private void setupSimpleList() {
        // Make an array adapter using the built in android layout to render a list of strings
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, mSimpleListValues);

        // Assign adapter to the HorizontalListView
        mHlvSimpleList.setAdapter(adapter);
    }

    private void setupCustomLists() {
        Log.d(TAG, "Setup Custom List");
        // Make an array adapter using the built in android layout to render a list of strings
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);

        // Assign adapter to HorizontalListView
        mHlvCustomList.setAdapter(adapter);

        mHlvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.d(TAG, "Item clicked position " + String.valueOf(position));
                CustomData dataClicked = (CustomData) mHlvCustomList.getItemAtPosition(position);
                if (previouslySelectedDesignPosition == 0) {
                    previouslySelectedDesignPosition = position;
                } else {
                    previouslySelectedDesignPosition = currentSelectedDesignPistion;
                    //change the background for this item in list
                }

                //update current selection
                currentSelectedDesignPistion = position;

                //TODO:: update the background color for this item in list.
                //dataClicked.
                previewImageViewFabric.setBackgroundColor(dataClicked.getBackgroundColor());
                //TODO:: update the cart

                //cartNewOrder.addDesign(dataClicked.getText(), position, (int) dataClicked.getCost(), 1);
                MainActivity.cartNewOrder.addFabric(dataClicked.getText(),position, (int ) dataClicked.getCost() , 1);// addDesign(dataClicked.getText(), position, (int) dataClicked.getCost(), 1);

                //cartTextView.setText(dataClicked.getText() + "\n" + String.valueOf(dataClicked.getCost()) );

                //Todo:: update content description
                selectedItemDescription.setText("This is "+ dataClicked.getText());
                //Object o = mHlvCustomList.getItemAtPosition(position);

                //saveSelectedFabric();
                //String  str=(prestationEco)o;//As you are using Default String Adapter
                Toast.makeText(getBaseContext(), "Selected " + dataClicked.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        //with divider
        //mHlvCustomListWithDividerAndFadingEdge.setAdapter(adapter);
    }



    public void itemSel(View v){
        /*Intent i = new Intent(this, Selected_Item.class);
        i.putExtra(Selected_Item.Name, "Item Selected" );

        startActivity(i);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fabric, menu);
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

    public void addtocart(View v){
        overridePendingTransition(0,0);
        Button btnAddToCart = (Button) findViewById(R.id.addtocartbtn);
        Date currentDate = new Date(System.currentTimeMillis());
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();

        //todo:
        //UPDATE THE CART UI
        //ADD THE ORDER TO db
        //SEND EMAIL / SMS CONFIRMATION TO STORE
        if ( MainActivity.cartNewOrder.checkIfFabricIsSelected() ) {
            boolean isOrderinCart = isOrderAlreadyInCart();
            if(!isOrderinCart){

                MainActivity.mydbmanager.insertOrder(neworder.toString(), currentDate.toString(), "000000000", "address1", "address2",String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS));
                existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
                lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
                Log.d(TAG, "lastInsertedOrderId = " + lastInsertedOrderId + "Status " + String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS));
            }else {
                updateExistingOrder();
                Toast.makeText(Fabric.this, "Updated existing order.", Toast.LENGTH_SHORT).show();
            }
            //Update cart size
            updateCartUI();
            btnAddToCart.setText("Update Cart");
            Log.d(TAG, "DB size " + String.valueOf(MainActivity.mydbmanager.getAllOrders().size()));
            progressBar.setProgress(66);
        }else {
            Toast.makeText(this, "Please select a fabric", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveSelectedFabric(){
        Date currentDate = new Date(System.currentTimeMillis());
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();
        if ( MainActivity.cartNewOrder.checkIfFabricIsSelected() ) {
            boolean isOrderinCart = isOrderAlreadyInCart();
            if(!isOrderinCart){
                MainActivity.mydbmanager.insertOrder(neworder.toString(), currentDate.toString(), "000000000", "address1", "address2",String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS));
                existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
                lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
                Log.d(TAG, "lastInsertedOrderId = " + lastInsertedOrderId);
            }else {
                updateExistingOrder();
                Toast.makeText(Fabric.this, "Updated existing order.", Toast.LENGTH_SHORT).show();
            }
            //Update cart size
            updateCartUI();

            Log.d(TAG, "DB size " + String.valueOf(MainActivity.mydbmanager.getAllOrders().size()));
        }else {
            Toast.makeText(this, "Please select a fabric", Toast.LENGTH_SHORT).show();
        }
    }

    public  void selectSize(View v){
        overridePendingTransition(0, 0);
        int i2 = MainActivity.mydbmanager.getOrderWithDNFSelectedForFabric();
        if( i2 > 0) { //MainActivity.mydbmanager.getAllOrders().size()
            Intent i = new Intent(this, Measurements.class);
            startActivity(i);
        }else {
            Toast.makeText(this,"Please select a design and fabric combination.", Toast.LENGTH_SHORT).show();
        }
    }

    public  void cart(View v){
        if( MainActivity.mydbmanager.getAllOrders().size() > 0) {
            Intent i = new Intent(this, ShoppingCart.class);
            startActivity(i);
        }
    }
    private void updateCartUI(){
        //MainActivity.cartNewOrder.updateCart(MainActivity.mydbmanager.getAllOrders().size());
        int i = 0 ;//MainActivity.mydbmanager.getAllOrders().size();
        //cartTextView.setText("Cart(" + String.valueOf(i) + ")");
        i = MainActivity.mydbmanager.getOrderWithDNFSelected();
        cartTextView.setText("Cart(" + String.valueOf(i) + ")");
        Log.d(TAG, "Cart items " + String.valueOf(i));
    }

    private void updateExistingOrder(){
        int i = Integer.parseInt(lastInsertedOrderId);
        if( i > 0) {
            MainActivity.mydbmanager.updateOrderDetails(MainActivity.cartNewOrder.getDetails().toString(),i );
            MainActivity.mydbmanager.updateStatus(String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS), i);
        }
        updateCartUI();
    }


    private boolean isOrderAlreadyInCart(){
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();
        boolean isNewOrderpresentInExistingOrders  = false ;
        Log.d(TAG, "isOrderAlreadyInCart Check for " + neworder.toString());
        isNewOrderpresentInExistingOrders = existingOrdersinDb.contains(neworder.toString());

        if(!isNewOrderpresentInExistingOrders){
            //Order not in Cart. Check any new Order added this session.
            int i = Integer.parseInt(lastInsertedOrderId);
            if( i > 0){
                //There was an order added in this session, update same order
                Log.d(TAG, "isOrderAlreadyInCart i="+String.valueOf(i));
                return true;
            }

        }

        return isNewOrderpresentInExistingOrders;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();
    }
}
