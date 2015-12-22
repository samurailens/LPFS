package com.sachin.sachin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


public class Designs extends Activity {

    public static boolean comingFromBackPressed = false;
    public static int lastSelectedIndex = 0;
    private HorizontalListView mHlvSimpleList;
    private HorizontalListView mHlvCustomList;
    private HorizontalListView mHlvCustomListWithDividerAndFadingEdge;

    private String  TAG = "Designs";
    private String[] mSimpleListValues = new String[] { "Android", "List", "View" };//,
    //"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
    //"Linux", "OS/2" };

    static int previouslySelectedDesignPosition = 0;
    static int currentSelectedDesignPistion = 0;
    static TextView cartTextView;
    static TextView selectedItemDescription;
    ImageView previewImageView;
    TextView designTitletxtview; //designPreviewtxtView
    public ArrayList<String> existingOrdersinDb;
    public String  lastInsertedOrderId = "-1";
    ProgressBar progressBar;

    public static String selectedDesignUrl;

    //OverLay h, w
    static int Imgh,Imgw;

    private CustomData[] mCustomData = new CustomData[] {
            new CustomData("One shoulder waist line", "http://trycatchthrow.in/LPFS/images/designs/oneshoulderwaistline.png", 100),
            new CustomData("Scalloped collar", "http://trycatchthrow.in/LPFS/images/designs/scalloped_collar.png", 100),

            new CustomData("Square neck", "http://trycatchthrow.in/LPFS/images/designs/squareneck.png", 100),
            new CustomData("Sweetheart neckline", "http://trycatchthrow.in/LPFS/images/designs/sweetheartneckline.png", 100),


/*            new CustomData(Color.RED, "Red", 100),
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
        setContentView(R.layout.activity_designs);
        // Get references to UI widgets
        //mHlvSimpleList = (HorizontalListView) findViewById(R.id.hlvSimpleList);
        mHlvCustomList = (HorizontalListView) findViewById(R.id.hlvCustomList);
        //WithDivider
        //mHlvCustomListWithDividerAndFadingEdge = (HorizontalListView) findViewById(R.id.hlvCustomListWithDividerAndFadingEdge);
        //mHlvCustomListWithDividerAndFadingEdge.setVisibility(View.GONE);
        //setupSimpleList();
        setupCustomLists();

        previouslySelectedDesignPosition = 0;
        currentSelectedDesignPistion = 0;
        cartTextView = (TextView) findViewById(R.id.carttxtview);
        selectedItemDescription = (TextView) findViewById(R.id.selectedItemDescriptiontxtview);
        //selectedItemDescription.setText("Content Description is shown here");
        designTitletxtview = (TextView) findViewById(R.id.designPreviewtxtView); //designPreviewtxtView
        previewImageView = (ImageView) findViewById(R.id.previewImageview);
        existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(0);

        updateCartUI();


        //progressBar.setBackgroundColor(Color.parseColor("#4CAF50"));

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

        //Show selected item in cart
        mHlvCustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Log.d(TAG, "Item clicked position " + String.valueOf(position));
                CustomData dataClicked = (CustomData) mHlvCustomList.getItemAtPosition(position);

                //dataClicked.setBackgroundBorder(Color.parseColor("#FAD2DF"));

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

                //TODO:: update the cart

                //TODO: update the Preview Title and image
                //COLOR previewImageView.setBackgroundColor(dataClicked.getBackgroundColor());

                String url = dataClicked.getText();
                selectedDesignUrl = url;
                Picasso.with(MainActivity.context)
                        .load(url)
                        .into(previewImageView);

                //designTitletxtview.setText(dataClicked.getText());

                //TODO: Add Design to cart
                MainActivity.cartNewOrder.addDesign(dataClicked.getBackgroundColor(), position, (int) dataClicked.getCost(), 1);

                //Todo:: update content description
                //selectedItemDescription.setText("This is " + dataClicked.getText());

                //Object o = mHlvCustomList.getItemAtPosition(position);
                //String  str=(prestationEco)o;//As you are using Default String Adapter
                //saveSelectedDesign();
                //Toast.makeText(getBaseContext(), "Selected " + dataClicked.getText(), Toast.LENGTH_SHORT).show();

                addtocart(view);

                int w = previewImageView.getWidth();
                int h = previewImageView.getHeight();
                Imgh = h;
                Imgw = w;
                Log.d(TAG, "h , w " + String.valueOf(h) + "\t w " + String.valueOf(w)) ;

                Log.d(TAG, dataClicked.getText() + String.valueOf(position) + String.valueOf(dataClicked.getCost()) + String.valueOf(1));
            }
        });
        //with divider
        //mHlvCustomListWithDividerAndFadingEdge.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_designs, menu);
        return true;
    }

       public void selectFabric(View v){

        if ( MainActivity.cartNewOrder.checkIfDesignIsSelected() ) {
       /* SendMail sndMail = new SendMail();
        SendMail.myappContext = getApplicationContext();
        sndMail.sendMail("48.rohit@gmail.com", "Test Subj", "Test Mesg");*/

        Intent intent =  new Intent(this, Fabric.class);
            intent.putExtra("lenreq",1 );
            startActivity(intent);

        }else {
            Toast.makeText(getBaseContext(),"Select at least one design to continue.", Toast.LENGTH_SHORT).show();
        }
        //
    }

    private void updateCartUI(){
        //MainActivity.cartNewOrder.updateCart(MainActivity.mydbmanager.getAllOrders().size());
        int i = 0 ;// MainActivity.mydbmanager.getAllOrders().size();
        //Sachin : Dont show till both D & F are selected.
        //Show only of those Status is complete.
        i = MainActivity.mydbmanager.getOrderWithDNFSelected();
        cartTextView.setText("Cart(" + String.valueOf(i) + ")");
    }

    public  void cart(View v){
        overridePendingTransition(0, 0);
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();
        if( MainActivity.mydbmanager.getAllOrders().size() > 0) {
            Intent i = new Intent(this, ShoppingCart.class);
            startActivity(i);
        }else {
            Log.d(TAG, "Nothing to go to cart");
        }
    }

    public void addtocart(View v){
        overridePendingTransition(0,0);
        //Button btnAddToCart = (Button) findViewById(R.id.adddesgntocartbutton);
        Date currentDate = new Date(System.currentTimeMillis());
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();

        //todo:
        //UPDATE THE CART UI
        //ADD THE ORDER TO db
        //SEND EMAIL / SMS CONFIRMATION TO STORE
        if ( MainActivity.cartNewOrder.checkIfDesignIsSelected() ) {
            boolean isOrderinCart = isOrderAlreadyInCart();
            if(!isOrderinCart){
                MainActivity.mydbmanager.insertOrder(neworder.toString(), currentDate.toString(), "000000000", "address1", "address2",String.valueOf(ORDER_STATUS.DESIGN_SELECTION_IN_PROGRESS));
                existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
                lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
                Log.d(TAG, "lastInsertedOrderId = " + lastInsertedOrderId);
            }else {
                updateExistingOrder();
                Toast.makeText(Designs.this, "Updated order.", Toast.LENGTH_SHORT).show();
            }
            //Update cart size
            updateCartUI();
            //btnAddToCart.setText("Update");
            Log.d(TAG, "DB size " + String.valueOf(MainActivity.mydbmanager.getAllOrders().size()));
            progressBar.setProgress(33);
        }else {
            Toast.makeText(this, "Select a design", Toast.LENGTH_SHORT).show();
        }

    }

    public void saveSelectedDesign(){
        Date currentDate = new Date(System.currentTimeMillis());
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();

        if ( MainActivity.cartNewOrder.checkIfDesignIsSelected() ) {
            boolean isOrderinCart = isOrderAlreadyInCart();
            if(!isOrderinCart){
                MainActivity.mydbmanager.insertOrder(neworder.toString(), currentDate.toString(), "000000000", "address1", "address2",String.valueOf(ORDER_STATUS.DESIGN_SELECTION_IN_PROGRESS));
                existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
                lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
                Log.d(TAG, "lastInsertedOrderId = " + lastInsertedOrderId);
            }else {
                updateExistingOrder();
                Toast.makeText(Designs.this, "Updated order.", Toast.LENGTH_SHORT).show();
            }
            //Update cart size
            updateCartUI();

            Log.d(TAG, "DB size " + String.valueOf(MainActivity.mydbmanager.getAllOrders().size()));
        }else {
            Toast.makeText(this, "Select a design", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateExistingOrder(){
        int i = Integer.parseInt(lastInsertedOrderId);
        if( i > 0) {
            MainActivity.mydbmanager.updateOrderDetails(MainActivity.cartNewOrder.getDetails().toString(), i);
        }
    }

    private boolean isOrderAlreadyInCart(){
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();
        boolean isNewOrderpresentInExistingOrders  = false ;

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
        updateCartUI();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        updateCartUI();
        new Intent(this, MainActivity.class);
    }
}
