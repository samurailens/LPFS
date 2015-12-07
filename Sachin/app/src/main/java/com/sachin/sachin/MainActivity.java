package com.sachin.sachin;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import java.util.Date;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks  {

    static shpcrtOrder cartNewOrder;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    String TAG ="MainActivity";
    static DatabaseManager mydbmanager;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mydbmanager = new DatabaseManager(this);
        cartNewOrder =  new shpcrtOrder();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // set an exit transition
        getWindow().setExitTransition(new Fade());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d(TAG, "onNavigationDrawerItemSelected" );
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position ))
                .commit();


    }

    public void MycartView(View v){
        Intent i = new Intent(this, ShoppingCart.class);
        startActivity(i);
    }

    public void deleteFromCart(View v){

    }

    public void onSectionAttached(int number) {
        Log.d(TAG, "onSectionAttached");
        switch (number) {
            case 0:
                mTitle = "Store Home";
                Log.d(TAG, "case Home  ");
                break;
            case 1:
                mTitle = getString(R.string.title_section1);
                Log.d(TAG, "case register/sign-in selected ");
                break;
           /* case 2:
                mTitle = getString(R.string.title_section2);
                break;*/
            case 2:
                mTitle = getString(R.string.title_section_my_orders);
                break;
            case 3:
                mTitle = getString(R.string.title_section_my_wish_list);
                break;
            case 4:
                mTitle = getString(R.string.title_section_help_usage);
                break;
            case 5:
                mTitle = getString(R.string.title_section_rate_app);
                break;
            case 6:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        Log.d(TAG, "restoreActionBar" );
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public void showShoppingCart(View v){
        startActivity( new Intent(this, ShoppingCart.class));
    }

    public void showDesign(View v){

        startActivity( new Intent(this, Designs.class));
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        static String TAG = "PlaceholderFragment";
        static int SELECTED_SECTION_NUMBER = -1;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            Log.d(TAG, "newInstance sectionNumber " +  String.valueOf(sectionNumber) );
            SELECTED_SECTION_NUMBER = sectionNumber;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d(TAG, "onCreateView" );
            //int sectionToLoad = 1 ;// savedInstanceState.getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (SELECTED_SECTION_NUMBER) {
                case 0:
                    Log.d(TAG, "case 0 selected ");
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    break;
                case 1:
                    Log.d(TAG, "case 1 selected ");
                    rootView = inflater.inflate(R.layout.fragment_mainlogin, container, false);
                    //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    //textView.setText("This is " + String.valueOf( 1));
                    break;

                case 2:
                    Log.d(TAG, "case 2 selected - My Orders");
                    rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);


                    //rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);
                    break;
                case 3:
                    Log.d(TAG, "case 3 selected - My Wishlist");
                    rootView = inflater.inflate(R.layout.fragment_my_wishlist, container, false);

                    break;
                case 4:
                    Log.d(TAG, "case 4 selected - My Help");
                    rootView = inflater.inflate(R.layout.fragment_app_help, container, false);
                    break;
                case 5:
                    Log.d(TAG, "case 5 selected - Rate and Feedback");
                    rootView = inflater.inflate(R.layout.fragment_app_rate_feedback, container, false);
                    break;

                case 6:
                    rootView = inflater.inflate(R.layout.fragment_about, container, false);
                    break;

                default:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    break;
            }


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            Log.d(TAG, "onAttach");
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
