package com.sachin.sachin;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * Created by samurai on 23-Nov-15.
 */
public class shoppingCartCustomArrayAdapter extends ArrayAdapter<List> {
    private LayoutInflater mInflater;
    private String TAG="shoppingCartCustomArrayAdapter";
    List m_values;

    public shoppingCartCustomArrayAdapter(Context context, List values) {
        //super(context, R.layout.row_layout, values);
        super(context, R.layout.cartitem_redesigned_layout, values);
        m_values =values;
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.cartitem_redesigned_layout, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            //holder.textView = (TextView) convertView.findViewById(R.id.listText);
            //holder.orderCost = (TextView) convertView.findViewById(R.id.ordercost);


            //Order ID
            //Order - Design Name
            //Order - Fabric Name
            //Design Cost , Fabric Cost per unit, Fabric length Required
            holder.textViewOrderId = (TextView) convertView.findViewById(R.id.CVtextViewForOrderID);

            holder.textViewDesignName = (TextView)convertView.findViewById(R.id.CVLinLayoutDesignName);
            //holder.textViewDesignLenRequired = (TextView) convertView.findViewById(R.id.CVLinLayoutDesignLenReq);
            holder.textViewFabricName = (TextView) convertView.findViewById(R.id.CVtextViewFabricName);
            //holder.textViewFabricCost = (TextView) convertView.findViewById(R.id.CVtextViewFabricCost);
            holder.textViewTotalCost = (TextView) convertView.findViewById(R.id.CVtextViewTotalCost);
            holder.textViewSize = (TextView) convertView.findViewById(R.id.CVtextViewSize);

            holder.textviewRemove = (TextView) convertView.findViewById(R.id.textviewRemove);
            holder.textviewRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "HI \t" + String.valueOf(position));
                        View parentRow = (View) v.getParent();
                        View parentRow2 = (View) parentRow.getParent();
                        TextView orderId = (TextView) parentRow2.findViewById(R.id.CVtextViewForOrderID);
                        String id = orderId.getText().toString();
                        //todo find the id n sendfor delete

                        int indexofHash = id.lastIndexOf("#");
                        String orderID = id.substring(indexofHash+1);
                        Log.d(TAG, "deleteFromCart orderID = " + id + "\t " + orderID);


                        try {
                            int noofRowsEffected = MainActivity.mydbmanager.deleteOrder(orderID);
                            deleteItem(position);
                            Log.d(TAG, "Delete from DB " + orderID + "Row Del " + String.valueOf(noofRowsEffected));
                            MainActivity.cartNewOrder.clear();
                        }catch (SQLiteException e){
                            e.printStackTrace();
                        }
                    }
                });

            convertView.setTag(holder);
            //Log.d(TAG, String.valueOf(holder.textView.getText()));
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        try{
            Object a  =  getItem(position);
            a.hashCode();
            Log.d(TAG, "Object " + a.toString());
            Object json = new JSONTokener(a.toString()).nextValue();
            if (json instanceof JSONObject) {
                //you have an object
                Object designName = ((JSONObject) json).get("designName");
                Object fabricName = ((JSONObject) json).get("fabricName");
                Object fabricFabCost = ((JSONObject) json).get("fabricFabCost");
                Object designCost = ((JSONObject) json).get("designCost");
                Object orderId = ((JSONObject) json).get("orderId");
                int i = Integer.parseInt(fabricFabCost.toString()) + Integer.parseInt(designCost.toString());
                //holder.textView.setText("Design: " + designName.toString() + "\nFabric: " + fabricName.toString()); //+ " Rs." + String.valueOf(i)
                //holder.orderCost.setText("Rs."+ String.valueOf(i));
                holder.textViewDesignName.setText("Design : "+designName.toString() + "(Rs."+ designCost.toString()+")");
                //holder.textViewDesignLenRequired.setText("Rs."+ designCost.toString() + " " + "1.2m");
                holder.textViewFabricName.setText("Fabric : "+ fabricName.toString() + "(Rs."+fabricFabCost.toString() +")");
                //holder.textViewFabricCost.setText("Rs."+fabricFabCost.toString());
                holder.textViewTotalCost.setText("Total Rs."+String.valueOf(i));
                holder.textViewOrderId.setText("Order ID #"+orderId);
                holder.textViewSize.setText("Size "+ Measurements.measurementsSelected);
            }
            else if (json instanceof JSONArray) {
                //you have an array
                Object b = ((JSONArray) json).get(0);
                b.hashCode();
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
        // Set the color
        //convertView.setBackgroundColor(getItem(position));
        return convertView;
    }


    public void deleteItem(int position ) {
        m_values.remove(position);
        this.notifyDataSetChanged();
    }

    public void updateReceiptsList(List newlist) {
        m_values.clear();
        m_values.addAll(newlist);
        this.notifyDataSetChanged();
    }


    /** View holder for the views we need access to */
    private static class Holder {
        public TextView textView;
        public TextView orderCost;

        public TextView textViewOrderId;

        public TextView textViewDesignName;
        public TextView textViewDesignLenRequired;
        public TextView textViewFabricName;
        public TextView textViewFabricCost;

        public TextView textViewTotalCost;
        public TextView textviewRemove;
        public TextView textViewSize;

    }

}
