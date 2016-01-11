package com.sachin.sachin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
            new CustomData("Variety pattern", "http://trycatchthrow.in/LPFS/images/patterns/variety.png", 100),
            new CustomData("Floral patter", "http://trycatchthrow.in/LPFS/images/patterns/floral_pattern.png" , 150),
            new CustomData("Red grey pattern", "http://trycatchthrow.in/LPFS/images/patterns/red_grey_pattern.png",200 ),
            new CustomData("Square red pattern", "http://trycatchthrow.in/LPFS/images/patterns/square_red_pattern.png", 250),
/*            new CustomData(Color.WHITE, "White", 300),
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
            new CustomData(Color.GREEN, "Green", 100)*/
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
        setContentView(R.layout.activity_fabric_newlayout);
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
        //selectedItemDescription.setText("Content Description is shown here");
        existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
        lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //progressBar.setProgress(33);
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
                //previewImageViewFabric.setBackgroundColor(dataClicked.getBackgroundColor());
                //TODO:: update the cart

                String url = Designs.selectedDesignUrl; //"http://trycatchthrow.in/LPFS/images/designs/oneshoulderwaistline.png";
                Picasso.with(MainActivity.context)
                        .load(url)
                        .placeholder(R.drawable.flower7272)
                        .into(previewImageViewFabric);

                int w = previewImageViewFabric.getWidth();
                int h = previewImageViewFabric.getHeight();

                //dataClicked.getText()
                MainActivity.cartNewOrder.addFabric(dataClicked.getBackgroundColor(), position, (int) dataClicked.getCost(), 1);// addDesign(dataClicked.getText(), position, (int) dataClicked.getCost(), 1);

                addtocart(view);
                //cartTextView.setText(dataClicked.getText() + "\n" + String.valueOf(dataClicked.getCost()) );

                //Todo:: update content description
                //selectedItemDescription.setText("This is " + dataClicked.getText());
                Log.d(TAG, "h , w " + String.valueOf(h) + "\t w " +  String.valueOf(w)) ;

                url = dataClicked.getText();
                Picasso.with(MainActivity.context).load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable d = new BitmapDrawable(getResources(), bitmap);
                        previewImageViewFabric.setBackground(d);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
                //Overlay -
/*                url = "http://trycatchthrow.in/LPFS/images/designs/oneshoulderwaistline.png";
                ImageView previewImageviewfabricandDesing = (ImageView) findViewById(R.id.previewImageviewfabricandDesing);
                Picasso.with(MainActivity.context)
                        .load(url)
                        .into(previewImageviewfabricandDesing);*/

//                Target target = new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable d) {
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable r) {
//
//                    }
//                };
//
//                //private void loadBitmap() {
//                Picasso.with(getApplicationContext()).load(url).into(target);
//                //};
//
////                Resources r = getResources();
////                Drawable[] layers = new Drawable[2];
////                layers[0] = r.getDrawable(Picasso.with(getApplicationContext()).load(url));
////                layers[1] = r.getDrawable(R.drawable.tt);
////                LayerDrawable layerDrawable = new LayerDrawable(layers);
////                previewImageViewFabric.setImageDrawable(layerDrawable);
//
//                Picasso.with(getApplicationContext()).load(url).into(new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        Drawable d = new BitmapDrawable(getResources(), bitmap);
//                        previewImageViewFabric.setBackground(d);
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//                    }
//                });
                //loadBitmap();

                CartManager cartManager = new CartManager();
                String url2 = "http://trycatchthrow.in/LPFS/images/designs/oneshoulderwaistline.png";
                cartManager.loadImages( url2 , url);
                //~Overlay

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
        //Button btnAddToCart = (Button) findViewById(R.id.addtocartbtn);
        Date currentDate = new Date(System.currentTimeMillis());
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();

        //todo:
        //UPDATE THE CART UI
        //ADD THE ORDER TO db
        //SEND EMAIL / SMS CONFIRMATION TO STORE
        if ( MainActivity.cartNewOrder.checkIfFabricIsSelected() ) {
            boolean isOrderinCart = isOrderAlreadyInCart();
            if(!isOrderinCart){
                MainActivity.mydbmanager.insertOrder(lastInsertedOrderId, neworder.toString(), currentDate.toString(), "000000000", "address1", "address2",String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS));
                existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
                Log.d(TAG, "lastInsertedOrderId = " + lastInsertedOrderId + "Status " + String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS));
            }else {
                updateExistingOrder();
                Toast.makeText(Fabric.this, "Updated order.", Toast.LENGTH_SHORT).show();
            }
            //Update cart size
            updateCartUI();
            Log.d(TAG, "DB size " + String.valueOf(MainActivity.mydbmanager.getAllOrders().size()));
        }else {
            Toast.makeText(this, "Select a fabric", Toast.LENGTH_SHORT).show();
        }
    }


    public  void selectSize(View v){
        overridePendingTransition(0, 0);
        int i2 = MainActivity.mydbmanager.getOrderWithDNFSelectedForFabric();
        if( i2 > 0) { //MainActivity.mydbmanager.getAllOrders().size()
            Intent i = new Intent(this, Measurements.class);
            startActivity(i);
        }else {
            Toast.makeText(this,"Select a design and fabric combination.", Toast.LENGTH_SHORT).show();
        }
    }

    public  void cart(View v){
        if( MainActivity.mydbmanager.getAllOrders().size() > 0) {
            Intent i = new Intent(this, ShoppingCart.class);
            startActivity(i);
        }
    }
    private void updateCartUI(){
        int i = 0 ;
        i = MainActivity.mydbmanager.getOrderWithDNFSelected();
        cartTextView.setText("Cart(" + String.valueOf(i) + ")");
        Log.d(TAG, "Cart items " + String.valueOf(i));
    }

    private void updateExistingOrder(){
        MainActivity.mydbmanager.updateOrderDetails(MainActivity.cartNewOrder.getDetails().toString(),lastInsertedOrderId );
        MainActivity.mydbmanager.updateStatus(String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS), lastInsertedOrderId);
        updateCartUI();
    }


    private boolean isOrderAlreadyInCart(){
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();
        boolean isNewOrderpresentInExistingOrders  = false ;
        Log.d(TAG, "isOrderAlreadyInCart Check for " + neworder.toString());
        isNewOrderpresentInExistingOrders = existingOrdersinDb.contains(neworder.toString());

        if(!isNewOrderpresentInExistingOrders){
            //Order not in Cart. Check any new Order added this session.

            if( !lastInsertedOrderId.isEmpty()){
                //There was an order added in this session, update same order
                Log.d(TAG, "isOrderAlreadyInCart i="+ lastInsertedOrderId);
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        updateCartUI();
    }


}
