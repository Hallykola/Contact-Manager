package com.halgroithm.contactmanager;

import android.provider.BaseColumns;

public class ContactDBContract {
    public static final String DATABASE_NAME = "Contacts";
    public static final int DATABASE_VERSION = 1;


    public static final String SQL_CREATE_CONTACTENTRIES =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactEntry.COLUMN_NAME_FIRSTNAME + " TEXT," +
                    ContactEntry.COLUMN_NAME_LASTNAME+ " TEXT," +
                    ContactEntry.COLUMN_NAME_PHONE+ " TEXT," +
                    ContactEntry.COLUMN_NAME_BIRTHDAY+ " TEXT," +
                    ContactEntry.COLUMN_NAME_ADDRESS+ " TEXT," +
                    ContactEntry.COLUMN_NAME_ZIPCODE + " TEXT)";

    public static final String SQL_DELETE_CONTACTENTRIES =
            "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;
    
    public static class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "Contacts";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_PHONE= "phone";
        public static final String COLUMN_NAME_BIRTHDAY= "birthday";
        public static final String COLUMN_NAME_ADDRESS= "address";
        public static final String COLUMN_NAME_ZIPCODE= "zipcode";


    }
}
