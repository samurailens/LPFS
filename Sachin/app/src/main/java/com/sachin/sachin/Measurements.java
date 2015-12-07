package com.sachin.sachin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Date;

public class Measurements extends Activity {

    //TextView selectedSizeTxt;
    String selectedSizeTxt ;
    public ArrayList<String> existingOrdersinDb;
    public String lastInsertedOrderId = "-1";
    ProgressBar progressBar;
    TextView cartTextView;

    String TAG = "Measurements";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_measurements);
        //selectedSizeTxt = (TextView) findViewById(R.id.selectedSizeTxt)
        existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
        cartTextView = (TextView) findViewById(R.id.carttxtviewMeasurements);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(66);
        lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_measurements, menu);
        return true;
    }

    public void selectSize(View v) {
        String setTxt = "Done";
        String btntxt = "";

        //Show Edit Fields
        EditText M1 = (EditText) findViewById(R.id.M1);
        EditText M2 = (EditText) findViewById(R.id.M2);
        EditText M3 = (EditText) findViewById(R.id.M3);

        String sizeM1, sizeM2 , sizeM3;
        sizeM1 = M1.getText().toString();
        sizeM2 = M2.getText().toString();
        sizeM3 = M3.getText().toString();
        //capture fields
        M1.setVisibility(View.INVISIBLE);
        M2.setVisibility(View.INVISIBLE);
        M3.setVisibility(View.INVISIBLE);

        RadioGroup radiogroupsizelist = (RadioGroup) findViewById(R.id.radiogroupsizelist);
        radiogroupsizelist.setVisibility(View.VISIBLE);

        Button btn = (Button) findViewById(R.id.SizePredefinedMeasurementbtn); //selectSize
        btntxt = btn.getText().toString();

        RadioGroup RadioBtnGroup = null;
        RadioButton radiotexButton = null;
        RadioBtnGroup = (RadioGroup) findViewById(R.id.radiogroupsizelist);
        int selectedId = RadioBtnGroup.getCheckedRadioButtonId();
        radiotexButton = (RadioButton) findViewById(selectedId);

        //Toast.makeText(this,radioSexButton.getText(), Toast.LENGTH_SHORT).show();

        TextView selectedSize = (TextView) findViewById(R.id.selectedSizeMeasurmentTxt);
        if (selectedSize != null && radiotexButton != null) {
            selectedSizeTxt = radiotexButton.getText().toString();
            selectedSize.setText("Selected " + radiotexButton.getText());
            selectedSize.setTextColor(Color.parseColor("#83EB6D"));
            selectedSize.setVisibility(View.VISIBLE);
            //radiotexButton.setBackgroundColor(Color.parseColor("#FFE5FE"));
        }else {
            Toast.makeText(this,"select a standard size or customize.",Toast.LENGTH_SHORT).show();
        }

        btn.setText("Update Selection");

        Button thisBtn = (Button) findViewById(R.id.SizeManualbtn);
        thisBtn.setText("Custom Measurements");
        /*
        if (btntxt.contains("Select Size")) {

            // previously invisible view
            View myView = findViewById(R.id.sizeMeasuremenstslistView);
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
        } else {
            //Done
            // previously visible view
            final View myView = findViewById(R.id.sizeMeasuremenstslistView);

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
            RadioGroup RadioBtnGroup = null;
            RadioButton radiotexButton = null;
            RadioBtnGroup = (RadioGroup) findViewById(R.id.radiogroupsizelist);
            int selectedId = RadioBtnGroup.getCheckedRadioButtonId();
            radiotexButton = (RadioButton) findViewById(selectedId);

            //Toast.makeText(this,radioSexButton.getText(), Toast.LENGTH_SHORT).show();

            TextView selectedSize = (TextView) findViewById(R.id.selectedSizeMeasurmentTxt);

            if (selectedSize != null && radiotexButton != null) {
                selectedSizeTxt = radiotexButton.getText().toString();
                selectedSize.setText("Selected " + radiotexButton.getText());
                selectedSize.setTextColor(Color.parseColor("#83EB6D"));
                selectedSize.setVisibility(View.VISIBLE);
            }
            //selectedSize.setBackgroundColor(Color.parseColor("#83EB6D"));

            btn.setText("Select Size");
        }*/
    }

    public void selectSizeManually(View v){
        //hide radio group
        //show edit box
        //set button text to "Done"
        //on click, capture data and show
        //Set button text to edit

        Button btn = (Button) findViewById(R.id.SizePredefinedMeasurementbtn);
        btn.setText("Select Size");
        Button thisBtn = (Button) findViewById(R.id.SizeManualbtn);

        String btnTxt = thisBtn.getText().toString();
        //hide radio group
        RadioGroup radiogroupsizelist = (RadioGroup) findViewById(R.id.radiogroupsizelist);
        radiogroupsizelist.setVisibility(View.GONE);

        if(btnTxt.equalsIgnoreCase("Custom Measurements")){

            //Show Edit Fields
            EditText M1 = (EditText) findViewById(R.id.M1);
            EditText M2 = (EditText) findViewById(R.id.M2);
            EditText M3 = (EditText) findViewById(R.id.M3);

            M1.setVisibility(View.VISIBLE);
            M2.setVisibility(View.VISIBLE);
            M3.setVisibility(View.VISIBLE);

            thisBtn.setText("Done");

        }else {
            //Btn text is "Done"

            //Show Edit Fields
            EditText M1 = (EditText) findViewById(R.id.M1);
            EditText M2 = (EditText) findViewById(R.id.M2);
            EditText M3 = (EditText) findViewById(R.id.M3);

            String sizeM1, sizeM2 , sizeM3;
            sizeM1 = M1.getText().toString();
            sizeM2 = M2.getText().toString();
            sizeM3 = M3.getText().toString();
            //capture fields
            M1.setVisibility(View.VISIBLE);
            M2.setVisibility(View.VISIBLE);
            M3.setVisibility(View.VISIBLE);

            TextView selectedSize = (TextView) findViewById(R.id.selectedSizeMeasurmentTxt);
            selectedSize.setText("S1 " + sizeM1 + "S2 " + sizeM2 + "S3 " + sizeM3 );

            thisBtn.setText("Edit");
        }

    }

    public void checkout(View v){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(100);

        Button btnAddToCart = (Button) findViewById(R.id.checkout);
        String s = btnAddToCart.getText().toString();
        if ( s.contains("Check") ) {
            startActivity(new Intent(this, Checkout.class));
        }else {
            addToCart();
        }

    }


    public void addToCart(){
        //MEASUREMENTS_PROGRESS
        Date currentDate = new Date(System.currentTimeMillis());
        JSONObject neworder = MainActivity.cartNewOrder.getDetails();
        Button btnAddToCart = (Button) findViewById(R.id.checkout);
        //todo:
        //UPDATE THE CART UI
        //ADD THE ORDER TO db
        //SEND EMAIL / SMS CONFIRMATION TO STORE
        if ( MainActivity.cartNewOrder.checkIfFabricIsSelected() ) {
            boolean isOrderinCart = isOrderAlreadyInCart();
            if(!isOrderinCart){

                MainActivity.mydbmanager.insertOrder(neworder.toString(), currentDate.toString(), "000000000", "address1", "address2",String.valueOf(ORDER_STATUS.MEASUREMENTS_PROGRESS));
                existingOrdersinDb = MainActivity.mydbmanager.getAllOrders();
                lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
                Log.d(TAG, "lastInsertedOrderId = " + lastInsertedOrderId + "Status " + String.valueOf(ORDER_STATUS.MEASUREMENTS_PROGRESS));
            }else {
                updateExistingOrder();
                Toast.makeText(this, "Updated existing order.", Toast.LENGTH_SHORT).show();
            }
            //Update cart size
            updateCartUI();
            btnAddToCart.setText("Check out");
            Log.d(TAG, "DB size " + String.valueOf(MainActivity.mydbmanager.getAllOrders().size()));

        }else {
            Toast.makeText(this, "Please select a fabric", Toast.LENGTH_SHORT).show();
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
        lastInsertedOrderId = MainActivity.mydbmanager.getLastInsertedID();
        int i = Integer.parseInt(lastInsertedOrderId);

        Log.d(TAG, "lastInsertedOrderId " + lastInsertedOrderId);
        if( i > 0) {
            MainActivity.mydbmanager.updateOrderDetails(MainActivity.cartNewOrder.getDetails().toString(),i );
            MainActivity.mydbmanager.updateStatus(String.valueOf(ORDER_STATUS.PAYMENT_PROGRESS), i);
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
}
