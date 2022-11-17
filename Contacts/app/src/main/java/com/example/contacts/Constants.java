package com.example.contacts;

public class Constants {

    public static final String DB_NAME = "Contacts.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "CONTACTS_TABLE";
    public static final String C_ID = "ID";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_NAME = "NAME";
    public static final String C_PHONE = "PHONE";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_NOTE = "NOTE";
    public static final String C_ADDED_TIME = "ADDED_TIME";
    public static final String C_UPDATED_TIME = "UPDATED_TIME";

    public static final String query = "CREATE TABLE " + TABLE_NAME + " ("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT,"
            + C_NAME + " TEXT,"
            + C_PHONE + " TEXT,"
            + C_EMAIL + " TEXT,"
            + C_NOTE + " TEXT,"
            + C_ADDED_TIME + " TEXT,"
            + C_UPDATED_TIME + " TEXT)";
}
