package com.example.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Constants.query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);

    }

    public long insertContacts(String image, String name, String phone, String email, String note, String addedtime, String updatedtime){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.C_IMAGE, image);
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_PHONE, phone);
        values.put(Constants.C_EMAIL, email);
        values.put(Constants.C_NOTE, note);
        values.put(Constants.C_ADDED_TIME, addedtime);
        values.put(Constants.C_UPDATED_TIME, updatedtime);

        long id = database.insert(Constants.TABLE_NAME, null, values);
        database.close();
        return id;
    }

    public ArrayList<ModelContact> getAllData(String orderBy){
        ArrayList<ModelContact> arrayList = new ArrayList<>();
        String select = "SELECT * FROM " +Constants.TABLE_NAME + " ORDER BY " + orderBy;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(select, null);
        if(cursor.moveToFirst()){
            do {
                ModelContact modelContact = new ModelContact(
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME))
                );
                arrayList.add(modelContact);
            }while(cursor.moveToNext());
        }
        database.close();
        return arrayList;
    }

    public void updateContacts(String id, String image, String name, String phone, String email, String note, String addedtime, String updatedtime){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.C_IMAGE, image);
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_PHONE, phone);
        values.put(Constants.C_EMAIL, email);
        values.put(Constants.C_NOTE, note);
        values.put(Constants.C_ADDED_TIME, addedtime);
        values.put(Constants.C_UPDATED_TIME, updatedtime);

        database.update(Constants.TABLE_NAME, values, Constants.C_ID + " =? ", new String[]{id});
        database.close();
    }

    public void deleteContact(String id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(Constants.TABLE_NAME, Constants.C_ID + " =? ", new String[]{id});
        database.close();
    }

    public void deleteAllContacts(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " +Constants.TABLE_NAME);
        database.close();
    }

    public ArrayList<ModelContact> getsearchContacts(String query){

        ArrayList<ModelContact> searchcontacts = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String searchquery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " +Constants.C_NAME + " LIKE '%" + query + "%'";
        Cursor cursor = database.rawQuery(searchquery, null);

        if(cursor.moveToFirst()){
            do {
                ModelContact modelContact = new ModelContact(
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME))
                );
                searchcontacts.add(modelContact);
            }while(cursor.moveToNext());
        }
        database.close();
        return searchcontacts;
    }
}
