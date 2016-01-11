package com.sachin.sachin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.method.LinkMovementMethod;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.v7.app.ActionBar.NAVIGATION_MODE_STANDARD;

public class MainActivity extends  Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks ,

        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

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

    private static boolean init = true;
    private int loginSelected = 0;
    //Sign in
      /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;

    /* RequestCode for resolutions to get GET_ACCOUNTS permission on M */
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    /* Keys for persisting instance variables in savedInstanceState */
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";

    /* Client for accessing Google APIs */
    static private GoogleApiClient mGoogleApiClient;

    /* View to display current status (signed-in, signed-out, disconnected, etc) */
    static private TextView mStatus;

    // [START resolution_variables]
    /* Is there a ConnectionResult resolution in progress? */
    static private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    static private boolean mShouldResolve = false;
    // [END resolution_variables]

    static public String activeUserEmail = "";

    static Bundle savedInstanceStateFromFragment = null;

    static SignInButton signInButton = null;

    static Context context;

    static boolean signin4Firsttime = true;

    static boolean netAvailability ;

    public static final String LePapePreferences = "LePapePreferences" ;
    public static String LePapePreferences_EMAIL = "";
    public static String LePapePreferences_PASSWORD = "";

    public static LoginButton loginButton ;
    CallbackManager callbackManager;
    //~Sign in
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        //FB
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(TAG , "Login ok");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                        //Log.d(TAG, request.getGraphObject().toString() );
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TAG , "Login onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TAG , "Login onError");
                    }
                });


        //~FB

        savedInstanceStateFromFragment = savedInstanceState;
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer); //navigation_drawer
        mTitle = getTitle();
        mydbmanager = new DatabaseManager(this);
        cartNewOrder =  new shpcrtOrder();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // set an exit transition
        getWindow().setExitTransition(new Fade());
        netAvailability =  isNetworkAvailable();


            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(new Scope(Scopes.PROFILE))
                    .addScope(new Scope(Scopes.EMAIL))
                    .build();
        context = this;
        Log.d(TAG, "Network status  "+String.valueOf(netAvailability));
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.d(TAG, "onNavigationDrawerItemSelected");

        if(init){
            position = 1;
            loginSelected = 1;
            init = false;
        }
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();

        if(mTitle!=null){
           if(loginSelected > 0 )  {
                loginSelected = -1;
                callOnCreateOfView(savedInstanceStateFromFragment);
            }
        }

    }

    public void skipLogin(View v){
        //Hide all the sign in and show order
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.email_login_form);
        SignInButton signInButton = (SignInButton) findViewById(R.id.plus_sign_in_button);
        LoginButton linearLayout1 = (LoginButton) findViewById(R.id.login_button);

        linearLayout.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.GONE);

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.MenuList);
        relativeLayout.setVisibility(View.VISIBLE);

    }
    public void MycartView(View v){
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
        //orderlist
        ListView listView = (ListView)  findViewById(R.id.orderlist);
        listView.setAdapter(myAdapter);

        //myAdapter = new ArrayAdapter(this, R.layout.row_layout, R.id.listText, listTitle);
        //getListView().setOnItemClickListener(this);
        //setListAdapter(myAdapter);

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
                loginSelected = 1;
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
        Log.d(TAG, "restoreActionBar");
        android.app.ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        //actionBar.set

//        this.getActionBar().setDisplayShowCustomEnabled(true);
//        this.getActionBar().setDisplayShowTitleEnabled(false);
//
//        LayoutInflater inflator = LayoutInflater.from(this);
//        View v = inflator.inflate(R.layout.titleview, null);
//
////if you need to customize anything else about the text, do it here.
////I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
//        ((TextView)v.findViewById(R.id.title)).setText(mTitle);
//
////assign the view to the actionbar
//        this.getActionBar().setCustomView(v);

        /*
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf");
//        txtView.setTypeface(typeface);
//        txtView.setTextSize(24);
        int titleId = getResources().getIdentifier("action_bar_title", "id","android");
        TextView yourTextView = (TextView) findViewById(titleId);
        //yourTextView.setTextColor(getResources().getColor(R.color.black));
        yourTextView.setTypeface(typeface);
        */

    }

    public boolean isNetworkAvailable(){
        boolean ret = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo==null)
                return ret;

        NetworkInfo.State network = networkInfo.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            if(mTitle!=null && mTitle.toString().contains("Login")){
               callOnCreateOfView(savedInstanceStateFromFragment);
            }
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
        startActivity(new Intent(this, ShoppingCart.class));
    }

    public void showDesign(View v){

        if(netAvailability) {
            startActivity(new Intent(this, Designs.class));
        }else {
            Toast.makeText(this,"No Internet Connection.", Toast.LENGTH_LONG).show();
        }
    }

    //Sign in Related

    public  void callOnCreateOfView(Bundle savedInstanceState){
        Log.d(TAG,"callOnCreateOfView -->");
        // Restore from saved instance state
        // [START restore_saved_instance_state]
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        // [END restore_saved_instance_state]

        // Set up button click listeners
        this.findViewById(R.id.plus_sign_in_button).setOnClickListener(this);
        this.findViewById(R.id.plus_sign_out_button).setOnClickListener(this);
        this.findViewById(R.id.plus_disconnect_button).setOnClickListener(this);

        // Large sign-in
        ((SignInButton) this.findViewById(R.id.plus_sign_in_button)).setSize(SignInButton.SIZE_WIDE);

        // Start with sign-in button disabled until sign-in either succeeds or fails
        findViewById(R.id.plus_sign_in_button).setEnabled(false);

        // Set up view instances
        mStatus = (TextView) findViewById(R.id.status);

        // [START create_google_api_client]
        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();

        mGoogleApiClient.connect();
        // [END create_google_api_client]

        Log.d(TAG,"callOnCreateOfView <--");
    }


    private void updateUI(boolean isSignedIn) {
        Log.d(TAG, "updateUI -->");

        //check if the ui is actually visible
        //Sign in ui is visible only in one screen
        if(mTitle.toString().contains("Login")){
        Log.d(TAG, "Login screen available");

        if (isSignedIn) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if (currentPerson != null) {
                // Show signed-in user's name
                String name = currentPerson.getDisplayName();
                mStatus.setText(getString(R.string.signed_in_fmt, name));


                // Show users' email address (which requires GET_ACCOUNTS permission)
                if (checkAccountsPermission()) {
                    String currentAccount = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    // Email Form ((TextView) findViewById(R.id.email)).setText(currentAccount);
                    activeUserEmail = currentAccount;
                    //onNavigationDrawerItemSelected(0);
                    showUIafterLogin(name);
                }
            } else {
                // If getCurrentPerson returns null there is generally some error with the
                // configuration of the application (invalid Client ID, Plus API not enabled, etc).
                Log.w(TAG, getString(R.string.error_null_person));
                mStatus.setText(getString(R.string.signed_in_err));
            }

            // Set button visibility
           findViewById(R.id.plus_sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.plus_sign_out_buttons).setVisibility(View.VISIBLE);
        } else {
            // Show signed-out message and clear email field
            mStatus.setText(R.string.signed_out);
            //Email Form ((TextView) findViewById(R.id.email)).setText("");

            // Set button visibility
            findViewById(R.id.plus_sign_in_button).setEnabled(true);
            findViewById(R.id.plus_sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.plus_sign_out_buttons).setVisibility(View.GONE);

            showUIAfterSignOut();
        }

        }
        Log.d(TAG, "updateUI <--");
    }

    public void showUIafterLogin(String currentPersonName){
        //1. Hide the email , password and registration button.
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.email_login_form);
        linearLayout.setVisibility(View.GONE);
        //2. Show Welcome message
        TextView statusTxtView = (TextView) findViewById(R.id.status );
        statusTxtView.setText("Hi " + currentPersonName + ",");
        statusTxtView.setTextSize(18);
        statusTxtView.setVisibility(View.VISIBLE);
        //3. Show total number of items in cart.

        int size = MainActivity.mydbmanager.getAllOrders().size();
        //4. Show Sign out button

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.MenuList);
        relativeLayout.setVisibility(View.VISIBLE);

        TextView textView = (TextView) findViewById(R.id.textViewSkip);
        textView.setVisibility(View.GONE);

        LoginButton  linearLayout1 = (LoginButton ) findViewById(R.id.login_button);
        linearLayout1.setVisibility(View.GONE);
    }

    private void showUIAfterSignOut(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.email_login_form);
        linearLayout.setVisibility(View.VISIBLE);

        TextView statusTxtView = (TextView) findViewById(R.id.status );
        statusTxtView.setText("");
        statusTxtView.setTextSize(2);
        statusTxtView.setVisibility(View.GONE);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.MenuList);
        relativeLayout.setVisibility(View.GONE);

        TextView textView = (TextView) findViewById(R.id.textViewSkip);
        textView.setVisibility(View.VISIBLE);
        LoginButton  linearLayout1 = (LoginButton ) findViewById(R.id.login_button);
        linearLayout1.setVisibility(View.VISIBLE);
    }

    public void manualSignInOrRegister(View v){
        EditText editTextEmail =  (EditText) findViewById(R.id.email);
        EditText editTextPassword = (EditText) findViewById(R.id.password);

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            SharedPreferences sharedpreferences = getSharedPreferences(LePapePreferences, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            String existingEmail = sharedpreferences.getString(LePapePreferences_EMAIL, null);
            String existingPassword = sharedpreferences.getString(LePapePreferences_PASSWORD, null);
            if(existingEmail!=null && existingEmail.contains(email) ){
                //already user exists, check if matches,
                if(password.contains(existingPassword)){
                    Toast.makeText(this,"Login success.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"Incorrect credentials.", Toast.LENGTH_SHORT).show();
                }

            }else {
                //else create a new user , overwrite last user
                editor.putString(LePapePreferences_EMAIL, email);
                editor.putString(LePapePreferences_PASSWORD, password);
                editor.apply();
                editor.commit();
                showUIafterLogin(email);
                Toast.makeText(this,"Successfully registered!", Toast.LENGTH_SHORT).show();
            }
        }else {
            if(email.isEmpty()){
                Toast.makeText(this,"Email cannot be empty.", Toast.LENGTH_SHORT).show();
            }else if (password.isEmpty()){
                Toast.makeText(this,"Password cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        }

    }
    /**
     * Check if we have the GET_ACCOUNTS permission and request it if we do not.
     * @return true if we have the permission, false if we do not.
     */
    private boolean checkAccountsPermission() {
        Log.d(TAG, "checkAccountsPermission -->");
        final String perm = Manifest.permission.GET_ACCOUNTS;
        int permissionCheck = ContextCompat.checkSelfPermission(this, perm);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            // We have the permission
            return true;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
            // Need to show permission rationale, display a snackbar and then request
            // the permission again when the snackbar is dismissed.
            Snackbar.make(findViewById(R.id.main_layout),
                    R.string.contacts_permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Request the permission again.
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{perm},
                                    RC_PERM_GET_ACCOUNTS);
                        }
                    }).show();
            return false;
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{perm},
                    RC_PERM_GET_ACCOUNTS);
            return false;
        }


    }

    private void showSignedInUI() {
        updateUI(true);
    }

    private void showSignedOutUI() {
        updateUI(false);
    }

    // [START on_start_on_stop]
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart -->");
        //mGoogleApiClient.connect();
        netAvailability = isNetworkAvailable();
        Log.d(TAG, "onStart <--");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop -->");
        //mGoogleApiClient.disconnect();
        netAvailability = isNetworkAvailable();
        Log.d(TAG, "onStop <--");
    }
    // [END on_start_on_stop]
    // [START on_save_instance_state]
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
    }
    // [END on_save_instance_state]

    // [START on_activity_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_activity_result]
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:" + requestCode);
        if (requestCode == RC_PERM_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSignedInUI();
            } else {
                Log.d(TAG, "GET_ACCOUNTS Permission Denied.");
            }
        }
    }

    // [START on_connected]
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected -->");
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        Log.d(TAG, "onConnected:" + bundle);
        mShouldResolve = false;

        // Show the signed-in UI
        showSignedInUI();

//        if ( signin4Firsttime ) {
//            signin4Firsttime = false;
//            final Handler handler = new Handler();
//
//            final Runnable r = new Runnable() {
//                public void run() {
//
//                    handler.postDelayed(this, 2000);
//                    onNavigationDrawerItemSelected(0);
//                }
//            };
//
//            handler.postDelayed(r, 2000);
//
//
//        }

        //Go to store home

        Log.d(TAG, "onConnected <--");
    }
    // [END on_connected]

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Log.w(TAG, "onConnectionSuspended:" + i);
    }

    // [START on_connection_failed]
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed -->");
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

        signin4Firsttime = false;
        /*
        int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        boolean result = false;
        if (code == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            if (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= 4100000)
                result = true;// Meets minimum version requirements
        }else {
            result = true;
        }

        if (result) //!mIsResolving && mShouldResolve
             {
            if (connectionResult.hasResolution()) { //connectionResult.hasResolution()
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            showSignedOutUI();
        }
        */
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            showSignedOutUI();
        }

        Log.d(TAG, "onConnectionFailed <--");
    }
    // [END on_connection_failed]

    private void showErrorDialog(ConnectionResult connectionResult) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, RC_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                mShouldResolve = false;
                                showSignedOutUI();
                            }
                        }).show();
            } else {
                Log.w(TAG, "Google Play Services Error:" + connectionResult);
                String errorString = apiAvailability.getErrorString(resultCode);
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

                mShouldResolve = false;
                showSignedOutUI();
            }
        }
    }

    // [START on_sign_in_clicked]
    public void onSignInClicked(View v) {
        Log.d(TAG, "onSignInClicked -->");
        callOnCreateOfView(savedInstanceStateFromFragment);
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();

        // Show a message to the user that we are signing in.
        mStatus.setText(R.string.signing_in);
        Log.d(TAG, "onSignInClicked <--");
    }
    // [END on_sign_in_clicked]

    // [START on_sign_out_clicked]
    public void onSignOutClicked(View v) {
        // Clear the default account so that GoogleApiClient will not automatically
        // connect in the future.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }

        showSignedOutUI();
    }
    // [END on_sign_out_clicked]

    // [START on_disconnect_clicked]
    public void onDisconnectClicked(View v) {
        Log.d(TAG,"onDisconnectClicked -->" );
        // Revoke all granted permissions and clear the default account.  The user will have
        // to pass the consent screen to sign in again.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }

        showSignedOutUI();
        signin4Firsttime = false;
        Log.d(TAG, "onDisconnectClicked <--");
    }

    // [START on_click]
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick -->");
        switch (v.getId()) {
            case R.id.plus_sign_in_button:
                onSignInClicked(v);
                break;
            case R.id.plus_sign_out_button:
                onSignOutClicked(v);
                break;
            case R.id.plus_disconnect_button:
                onDisconnectClicked(v);
                break;
        }

        Log.d(TAG, "onClick <--");
    }
    // [END on_click]
    // [END on_disconnect_clicked]
    //~Sign in Related

    //Rate and Feedback
    public void rateApp(View v){

        SendMail sendMail = new SendMail();
        String thankYouNote = "Thanks for your valuable feedback on Le Pape Android app.";
        String toEmailAddress = MainActivity.activeUserEmail;

        String feedback = "";
        String ourMailAddress = "48.rohit@gmail.com";

        EditText editText = (EditText ) findViewById(R.id.commentstxtview);
        feedback = editText.getText().toString();

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        float numberOfStars = ratingBar.getRating();

        feedback = feedback + "Rating = " + String.valueOf(numberOfStars);

        if( !toEmailAddress.contains("@")){
            Toast.makeText(this, "Sign in to place your order", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        }else {
            sendMail.sendMail(toEmailAddress, "Le Pape - Feedback",thankYouNote );//orderDetails);
            sendMail.sendMail(ourMailAddress, "Le Pape - Feedback",feedback );//orderDetails);
        }

        Button btn = (Button) findViewById(R.id.ratebtn);
        btn.setVisibility(View.GONE);
        ratingBar.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);

        TextView txtViewThankYou = (TextView) findViewById(R.id.thankyouNote);
        txtViewThankYou.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
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
                    //ImageView imageView = (ImageView)rootView.findViewById(R.id.imageViewPica);
                    //Picasso.with(MainActivity.context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
                    TextView txtView = (TextView)   rootView.findViewById(R.id.CVtextView2);
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Regular.ttf");
                    txtView.setTypeface(typeface);
                    txtView.setTextSize(24);

                    TextView txtView2 = (TextView)   rootView.findViewById(R.id.CV2textView2);
                    Typeface typeface2 = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Regular.ttf");
                    txtView2.setTypeface(typeface);
                    txtView2.setTextSize(24);
                    break;
                case 1:
                    Log.d(TAG, "case 1 selected ");
                    rootView = inflater.inflate(R.layout.fragment_loginscreen, container, false);
                    //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                    //textView.setText("This is " + String.valueOf( 1));
                    savedInstanceStateFromFragment = savedInstanceState;

                    signInButton = (SignInButton) rootView.findViewById(R.id.plus_sign_in_button);
                    // Large sign-in
                    ((SignInButton) rootView.findViewById(R.id.plus_sign_in_button)).setSize(SignInButton.SIZE_WIDE);

                    //TextView textView = (TextView) rootView.findViewById(R.id.App_Title);
                    //Typeface typeface3 = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Regular.ttf");
                    //textView.setTypeface(typeface3);

                    //FB
                    loginButton = (LoginButton) rootView.findViewById(R.id.login_button);

                    loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));//"user_friends");
                    // If using in a fragment
                    //loginButton.setFragment(this);
                    // Other app specific specialization

                    // Callback registration
//                    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//                        @Override
//                        public void onSuccess(LoginResult loginResult) {
//                            // App code
//                        }
//
//                        @Override
//                        public void onCancel() {
//                            // App code
//                        }
//
//                        @Override
//                        public void onError(FacebookException exception) {
//                            // App code
//                        }
//                    });
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
                    //forgot password
                    TextView textViewForgotPassword = (TextView) rootView.findViewById(R.id.about_organization);
                    textViewForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

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
