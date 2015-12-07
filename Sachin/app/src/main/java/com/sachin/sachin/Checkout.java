package com.sachin.sachin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

public class Checkout extends Activity {

    String selectedSizeTxt ;
    String TAG = "Checkout";
    ArrayAdapter myAdapter;
    ListView orderList;
    List<String> orderIdsFromDb;
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


        List<String> listTitle = new ArrayList<String>();

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
        Toast.makeText(this, "Thanks for placing order! We will contact you shortly.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));

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
}
