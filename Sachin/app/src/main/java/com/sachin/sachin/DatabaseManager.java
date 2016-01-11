package com.sachin.sachin;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import static org.bitbucket.dollar.Dollar.$;

/**
 * Created by samurai on 22-Nov-15.
 *
 * On selecting design + fabric, add to cart. Put in orders table - status pending for checkout
 * on checkout - send order to store + update status to order complete. [ other states to be incorporated later ]
 *
 * On Activity shopping cart - show current pending orders.
 * button for completed orders.
 *
 */
public class DatabaseManager extends  SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String ORDERS_TABLE_NAME = "orders";
    public static final String ORDERS_COLUMN_ID = "id";
    public static final String ORDERS_COLUMN_UID = "order_id";
    public static final String ORDERS_COLUMN_ORDER = "order_";
    public static final String ORDERS_COLUMN_ORDERDATE = "orderdate";
    public static final String CONTACTS_COLUMN_STREET = "street";
    public static final String CONTACTS_COLUMN_CITY = "place";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    public static final String ORDERS_COLUMN_ORDERSTATUS = "orderstatus";

    public static final String MEASUREMENTS_TABLE = "measurements";
    public static final String MEASUREMENTS_COLUMN_ID = "id";
    public static final String MEASUREMENTS_COLUMN_USER_MEASUREMENTS = "user_measurements";

    public static String lastOrderId = "";

    private HashMap hp;
    public String TAG= "DatabaseManager";

    public DatabaseManager(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        Log.d(TAG, "DatabaseManager");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

       /* db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,phone text,email text, street text,place text)"
        );*/
//        db.execSQL(
//                "create table orders " +
//                        "(id integer primary key, order text,orderdate text,phone text, street text,place text)"
//        );
        //String S = "CREATE TABLE IF NOT EXISTS ORDERS_TABLE_NAME (ORDERS_COLUMN_ID int auto_increment primary key, data_type int default 0, data float not null, time timestamp default current_timestamp)";

        //String CREATE_CONTACTS_TABLE = "CREATE TABLE " + ORDERS_TABLE_NAME + "("+ ORDERS_COLUMN_ID + " TEXT , " + ORDERS_COLUMN_ORDER + " TEXT , "+ ORDERS_COLUMN_ORDERDATE + " TEXT  ," + CONTACTS_COLUMN_STREET + " TEXT , " + CONTACTS_COLUMN_CITY + " TEXT , "+ CONTACTS_COLUMN_PHONE + " TEXT , "+"PRIMARY KEY("+ORDERS_COLUMN_ID+"))";
        ///db.execSQL(CREATE_CONTACTS_TABLE);
        /*db.execSQL(" CREATE TABLE " + ORDERS_TABLE_NAME + " (" +
                        ORDERS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        ORDERS_COLUMN_ORDER + " TEXT NOT NULL, " +
                        ORDERS_COLUMN_ORDERDATE + " TEXT NOT NULL, " +
                        CONTACTS_COLUMN_STREET + " TEXT NOT NULL, " +
                        CONTACTS_COLUMN_CITY + " INTEGER NOT NULL, " +
                        CONTACTS_COLUMN_PHONE + " INTEGER NOT NULL); "

        );*/
Log.d(TAG, "onCreate");


        /*
         db.execSQL(
                "create table orders " +
                        "(id integer primary key , order text , orderdate text , phone text , street text , place text)"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ORDERS_TABLE_NAME + " ( " +
                        ORDERS_COLUMN_ID + " TEXT PRIMARY KEY , " +
                        ORDERS_COLUMN_ORDER + " TEXT NOT NULL , " +
                        ORDERS_COLUMN_ORDERDATE + " TEXT NOT NULL " +
                        CONTACTS_COLUMN_STREET + " TEXT NOT NULL " +
                        CONTACTS_COLUMN_CITY + " TEXT NOT NULL " +
                        CONTACTS_COLUMN_PHONE + " TEXT NOT NULL );"
        );

        Last worked
        String CREATE_TABLE_PROJECT = "CREATE TABLE " + ORDERS_TABLE_NAME + "("
                + ORDERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ORDERS_COLUMN_ORDER + " TEXT,"
                + ORDERS_COLUMN_ORDERDATE + " TEXT,"
                + CONTACTS_COLUMN_STREET + " TEXT,"
                + CONTACTS_COLUMN_CITY  + " TEXT,"
                + CONTACTS_COLUMN_PHONE + " TEXT" +");";

        */


        String CREATE_TABLE_PROJECT = "CREATE TABLE " + ORDERS_TABLE_NAME + "("
                + ORDERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ORDERS_COLUMN_UID + " TEXT,"
                + ORDERS_COLUMN_ORDER + " TEXT,"
                + ORDERS_COLUMN_ORDERDATE + " TEXT,"
                + CONTACTS_COLUMN_STREET + " TEXT,"
                + CONTACTS_COLUMN_CITY  + " TEXT,"
                + CONTACTS_COLUMN_PHONE + " TEXT,"
                + ORDERS_COLUMN_ORDERSTATUS+ " TEXT" + ");";

        db.execSQL(CREATE_TABLE_PROJECT);

        String CREATE_TABLE_MEASUREMENTS = "CREATE TABLE " + MEASUREMENTS_TABLE + "("
                + MEASUREMENTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MEASUREMENTS_COLUMN_USER_MEASUREMENTS + " TEXT" + ");";

        db.execSQL(CREATE_TABLE_MEASUREMENTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS orders");

        onCreate(db);
    }

    public boolean insertOrder(String OrderId, String order, String orderdate, String phone, String street, String place, String Status)
    {
        Log.d(TAG, "insertOrder");
        lastOrderId = OrderId; //getGeneratedOrderID();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_UID, lastOrderId);
        contentValues.put(ORDERS_COLUMN_ORDER, order);
        contentValues.put(ORDERS_COLUMN_ORDERDATE, orderdate);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_STREET, street);
        contentValues.put(CONTACTS_COLUMN_CITY, place);
        contentValues.put(ORDERS_COLUMN_ORDERSTATUS, Status);
        db.insert("orders", null, contentValues);


        //String lastId=getLastInsertedID();
        //generateOrderID(Integer.parseInt(lastId));

        return true;
    }

    public boolean insertMeasurements(String Measurements){
        Log.d(TAG, "insertMeasurements");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MEASUREMENTS_COLUMN_USER_MEASUREMENTS, Measurements);
        db.insert(MEASUREMENTS_TABLE, null, contentValues);
        return true;
    }

    public void deleteAll(){

    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from orders where id="+id+"", null );
        return res;
    }

    public Cursor getX(String s){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from orders where place="+s+"", null );
        return res;
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ORDERS_TABLE_NAME);


        return numRows;
    }

    public boolean updateOrder(Integer id, String order, String orderdate, String phone, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("order_", order);
        contentValues.put("orderdate", orderdate);
        contentValues.put("phone", phone);
        contentValues.put("street", street);
        contentValues.put("place", place);

        db.update("orders", contentValues, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }



    public Integer deleteOrder (String orderID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete("orders",
                "order_id = ? ",
                new String[]{orderID});

        db.close();
        return i;

    }

    public ArrayList<String> getAllOrders()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from orders", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String s = res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS));
            if(s.equalsIgnoreCase("PAYMENT_PROGRESS")){//String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS
                //array_list.add(s);
                array_list.add(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDER)));
            }
            //array_list.add(res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDER)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllOrdersIds(){

        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from orders", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String s = res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS));
            if(s.equalsIgnoreCase("PAYMENT_PROGRESS")) {
                array_list.add(res.getString(res.getColumnIndex(ORDERS_COLUMN_ID)));
            }
            res.moveToNext();
        }
        return array_list;
    }

    //MEASUREMENTS_PROGRESS
    public int getOrderWithDNFSelected(){
        int i = 0;
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from orders", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String s = res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS));
            if(s.equalsIgnoreCase("PAYMENT_PROGRESS")){//String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS
                    array_list.add(s);
            }

            res.moveToNext();
        }
        i = array_list.size();
        return i;
    }

    //MEASUREMENTS_PROGRESS
    public int getOrderWithDNFSelectedForFabric(){
        int i = 0;
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from orders", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String s = res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS));
            if(s.equalsIgnoreCase("FABRIC_SELECTION_IN_PROGRESS")){//String.valueOf(ORDER_STATUS.FABRIC_SELECTION_IN_PROGRESS
                array_list.add(s);
            }

            res.moveToNext();
        }
        i = array_list.size();
        return i;
    }

    public String getLastInsertStatus(){
        String lastInsertstatus = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from orders", null );
        res.moveToLast();
        lastInsertstatus = res.getString(res.getColumnIndex(ORDERS_COLUMN_ORDERSTATUS));
        return lastInsertstatus;
    }

    public void setLastInsertStatus(String status, int orderID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_ORDERSTATUS, status);
        db.update("orders", contentValues, "order_id = ? ", new String[]{Integer.toString(orderID) } );
    }

    public String getLastInsertedID(){
        String lastID = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from orders", null );
        res.moveToLast();
        lastID = res.getString(res.getColumnIndex(ORDERS_COLUMN_ID));

        return lastOrderId;
    }

    public boolean updateOrderDetails(String orderDetails, String orderID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_ORDER, orderDetails);
        db.update("orders", contentValues, "order_id = ? ", new String[]{orderID } );
        return true;
    }

    public boolean updatePhone(String phone, int orderid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        db.update("orders", contentValues, "order_id = ? ", new String[]{Integer.toString(orderid) } );
        return true;
    }

    public boolean updateStreet(String street, int orderid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("street", street);
        db.update("orders", contentValues, "order_id = ? ", new String[]{Integer.toString(orderid) } );
        return true;
    }

    public boolean updateStatus(String status, String orderid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COLUMN_ORDERSTATUS, status);
        db.update("orders", contentValues, "order_id = ? ", new String[]{orderid } );
        return true;
    }



    public String getGeneratedOrderID(){
        String OrderId = randomString(12);
        Log.d(TAG, "generated id = " + OrderId);
        return OrderId;
    }

    String validCharacters = $('0', '9').join() + $('A', 'Z').join();

    String randomString(int length) {
        return $(validCharacters).shuffle().slice(length).toString();
    }



}
