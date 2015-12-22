package com.sachin.sachin;

import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCart extends ListActivity {

    CartManager objCartManager ;
    String TAG = "ShoppingCart";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

//        int titleId = getResources().getIdentifier("action_bar_title", "id",
//                "android");
//        TextView yourTextView = (TextView) findViewById(titleId);
        //yourTextView.setTextColor(getResources().getColor(R.color.black));
        TextView txtView = (TextView)   findViewById(R.id.shoppingcarttextview);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Regular.ttf");
        txtView.setTypeface(typeface);
        //yourTextView.setTypeface(typeface);

        updateCart();
    }

    public void updateCart(){

        int size = MainActivity.mydbmanager.getAllOrders().size();

        List list = MainActivity.mydbmanager.getAllOrders() ;
        ArrayAdapter myAdapter;

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
        setListAdapter(myAdapter);

        //myAdapter = new ArrayAdapter(this, R.layout.row_layout, R.id.listText, listTitle);
        //getListView().setOnItemClickListener(this);
        //setListAdapter(myAdapter);

    }

    public void deleteFromCart(){

    }



    public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
        // start BookActivity with extras the book id
        //Intent intent = new Intent(this, BookActivity.class);
        //intent.putExtra("book", list.get(arg2).getId());
        //startActivityForResult(intent, 1);
        }


@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
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
