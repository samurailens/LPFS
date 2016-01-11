package com.sachin.sachin;

/**
 * Created by Sachin on 10/22/2015.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/** An array adapter that knows how to render views when given CustomData classes */
public class CustomArrayAdapter extends ArrayAdapter<CustomData> {
    private LayoutInflater mInflater;
    String TAG="CustomArrayAdapter";

    public CustomArrayAdapter(Context context, CustomData[] values) {
        super(context, R.layout.custom_data_view, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

//        SquaredImageView view = (SquaredImageView) convertView;
//        if (view == null) {
//            view = new SquaredImageView(getContext());
//        }
        String url = getItem(position).getText();
        Log.d(TAG, "getView " + url);
//
//
//
//        holder = new Holder();
//        holder.imageView = (ImageView) view.findViewById(R.id.imgviewforPicasso);
//        view.setTag(holder);
//        Picasso.with(getContext()).load(url).into(holder.imageView);

//        Picasso.with(getContext())
//                .load(url)
//                .resize(50, 50)
//                .centerCrop()
//                .into(view);


        //Try 3



        //Picasso.with(getContext()).load(url).into(imageView);
        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.custom_data_view, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            /*Picasso.with(getContext())
                .load(url)
                .resize(50, 50)
                .centerCrop()
                .into(holder.imageView);*/
            holder.imageView = (ImageView) convertView.findViewById(R.id.imgviewforPicasso);
            holder.textView = (TextView) convertView.findViewById(R.id.textViewForImage);
            convertView.setTag(holder);


            //Log.d(TAG, String.valueOf(holder.textView.getText()));
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        //holder.textView.setText(getItem(position).getText());
        Picasso.with(getContext())
                .load(url)
                .into(holder.imageView);

        holder.textView.setText(getItem(position).getBackgroundColor() +"\nRs."+getItem(position).getCost());
        //Picasso.with(MainActivity.context).load("http://i.imgur.com/DvpvklR.png").into(holder.imageView);
        //holder.imageView.setImageURI(Uri.parse(url));
        //Log.d(TAG, "Populate the text position: " + String.valueOf(position) + "\t val: " + getItem(position).getText());

        // Set the color
        //convertView.setBackgroundColor(getItem(position).getBackgroundColor());

        //~Try 3

        /* WORKING CODE
        if (convertView == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.custom_data_view, parent, false);

            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);


            Log.d(TAG, String.valueOf(holder.textView.getText()));
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        holder.textView.setText(getItem(position).getText());
        Log.d(TAG, "Populate the text position: " + String.valueOf(position) + "\t val: " + getItem(position).getText());

        // Set the color
        convertView.setBackgroundColor(getItem(position).getBackgroundColor());
        */
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView textView;
        public ImageView imageView;
    }
}
