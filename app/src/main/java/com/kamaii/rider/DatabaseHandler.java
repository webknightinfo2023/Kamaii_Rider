package com.kamaii.rider;

import static com.google.firestore.v1.StructuredQuery.CompositeFilter.Operator.AND;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kamaii.rider.ui.models.OfflineImageDataModal;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager";
    private static final String TABLE_CONTACTS = "images";
    private static final String KEY_ID = "id";
    private static final String KEY_ORDER_ID = "booking_id";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_TRACKER = "tracker";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATETIME = "approxdatetime";
    private static final String KEY_REQUEST = "request";
    private static final String KEY_SERVICENAME = "servicename";

    public DatabaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ORDER_ID + " TEXT,"
                + KEY_IMAGE_PATH + " TEXT,"+ KEY_USER_ID +
                " TEXT,"+ KEY_AMOUNT + " TEXT,"+ KEY_DATETIME +
                " TEXT,"+ KEY_REQUEST + " TEXT,"
                + KEY_TRACKER + " TEXT" + ","
                + KEY_SERVICENAME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
//
//        // Create tables again
//        onCreate(db);
    }

    public void addContact(OfflineImageDataModal contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, contact.getBooking_id()); // OfflineImageDataModal Name  
        values.put(KEY_IMAGE_PATH, contact.getImage_path()); // OfflineImageDataModal Name  
        values.put(KEY_TRACKER, contact.getTracker()); // OfflineImageDataModal Phone
        values.put(KEY_USER_ID, contact.getUser_id()); // OfflineImageDataModal Phone
        values.put(KEY_AMOUNT, contact.getAmount()); // OfflineImageDataModal Phone
        values.put(KEY_DATETIME, contact.getApproxdatetime()); // OfflineImageDataModal Phone
        values.put(KEY_REQUEST, contact.getRequest()); // OfflineImageDataModal Phoned
        values.put(KEY_SERVICENAME, contact.getService_name()); // OfflineImageDataModal Phoned
        Log.e("imageData adddd ",values.toString());
        // Inserting Row

        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack  
        db.close(); // Closing database connection  
    }

    public int updateContact(OfflineImageDataModal contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("TAG", "onBindViewHolder: ahi ayu 66666 before update id "+contact.getBooking_id() );
        Log.e("TAG", "onBindViewHolder: ahi ayu 66666 before update image "+contact.getImage_path() );

        ContentValues values = new ContentValues();
//        values.put(KEY_ORDER_ID, contact.getBooking_id());
        values.put(KEY_IMAGE_PATH, contact.getImage_path());
        values.put(KEY_TRACKER, contact.getTracker()); // OfflineImageDataModal Phone
        values.put(KEY_USER_ID, contact.getUser_id()); // OfflineImageDataModal Phone
        values.put(KEY_AMOUNT, contact.getAmount()); // OfflineImageDataModal Phone
        values.put(KEY_DATETIME, contact.getApproxdatetime()); // OfflineImageDataModal Phone
        values.put(KEY_REQUEST, contact.getRequest()); // OfflineImageDataModal Phone
        values.put(KEY_SERVICENAME, contact.getService_name()); // OfflineImageDataModal Phone

        // updating row
        int l=0;
        try {
             l = db.update(TABLE_CONTACTS, values, KEY_ORDER_ID + " = ?",
                    new String[] { String.valueOf(contact.getBooking_id())});
        }catch (Error e)
        {
            Log.e("TAG", "onBindViewHolder: ahi ayu 66666 error:-- "+e.getMessage() );
        }


//        Log.e("TAG", "onBindViewHolder: ahi ayu 66666 update "+l );
       return l;
    }

    public void addOrderId(OfflineImageDataModal contact) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, contact.getBooking_id()); // OfflineImageDataModal Name
//        values.put(KEY_IMAGE_PATH, contact.getImage_path()); // OfflineImageDataModal Name
//        values.put(KEY_TRACKER, contact.getTracker()); // OfflineImageDataModal Phone
//        values.put(KEY_USER_ID, contact.getUser_id()); // OfflineImageDataModal Phone
//        values.put(KEY_AMOUNT, contact.getAmount()); // OfflineImageDataModal Phone
//        values.put(KEY_DATETIME, contact.getApproxdatetime()); // OfflineImageDataModal Phone
//        values.put(KEY_REQUEST, contact.getRequest()); // OfflineImageDataModal Phone
//
        Log.e("imageData offline: ",values.toString());
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single contact  
   public OfflineImageDataModal getContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ORDER_ID,
                        KEY_IMAGE_PATH, KEY_TRACKER }, KEY_ORDER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        OfflineImageDataModal contact = new OfflineImageDataModal();
        contact.setBooking_id(cursor.getString(1));
        // return contact  
        return contact;
    }

    // code to get all contacts in a list view  
    public List<OfflineImageDataModal> getAllContacts() {
        List<OfflineImageDataModal> contactList = new ArrayList<OfflineImageDataModal>();
        // Select All Query  
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {
            do {
                OfflineImageDataModal contact = new OfflineImageDataModal();
                contact.setBooking_id(cursor.getString(1));
                contact.setImage_path(cursor.getString(2));
                contact.setUser_id(cursor.getString(3));
                contact.setAmount(cursor.getString(4));
                contact.setApproxdatetime(cursor.getString(5));
                contact.setRequest(cursor.getString(6));
                contact.setTracker(cursor.getString(7));
                contact.setService_name(cursor.getString(8));
                // Adding contact to list
                Log.e("imageData offline: ",contact.getBooking_id()+" "+contact.getTracker()+" "+contact.getImage_path());
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list  
        return contactList;
    }

    public List<OfflineImageDataModal> getBookingId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<OfflineImageDataModal> contactList = new ArrayList<OfflineImageDataModal>();

//        db.execSQL("DELETE FROM "+TABLE_CONTACTS+" WHERE "+KEY_ORDER_ID+ " = ?"+ && +"" )
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+" WHERE "+KEY_ORDER_ID + " = "+id+" AND "+KEY_TRACKER+" = "+null;
        db.rawQuery(selectQuery,null);
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OfflineImageDataModal contact = new OfflineImageDataModal();
                contact.setBooking_id(cursor.getString(1));
                // Adding contact to list
                Log.e("imageData offline: gvhgvghvgh",contact.getBooking_id()+" "+contact.getTracker()+" "+contact.getImage_path());
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public void deleteContact(OfflineImageDataModal contact) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM "+TABLE_CONTACTS+" WHERE "+KEY_ORDER_ID+ " = ?"+ && +"" )
        db.delete(TABLE_CONTACTS, KEY_ORDER_ID + " = ? AND "+KEY_TRACKER+" = ?",
                new String[] { String.valueOf(contact.getBooking_id()),String.valueOf(contact.getTracker()) });
        db.close();
    }
}
