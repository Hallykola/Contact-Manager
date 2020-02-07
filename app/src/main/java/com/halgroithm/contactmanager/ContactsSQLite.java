package com.halgroithm.contactmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ContactsSQLite extends SQLiteOpenHelper {
    SQLiteDatabase db;


    public ContactsSQLite(@Nullable Context context) {
        super(context, ContactDBContract.DATABASE_NAME, null, ContactDBContract.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL(ContactDBContract.SQL_CREATE_CONTACTENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContactDBContract.SQL_DELETE_CONTACTENTRIES);
        onCreate(db);
    }

    public void removedata(String s) {
        db = getWritableDatabase();
        //mydb.delete(DB_TABLE, "recname = '" + s + "'", null);
        db.delete(ContactDBContract.ContactEntry.TABLE_NAME,ContactDBContract.ContactEntry._ID+ "=?",new String[]{s});
    }
    public ArrayList<Person> getAllPersons() {
        db = getWritableDatabase();
        //Cursor cur = mydb.rawQuery("select * from " + DB_TABLE, null);
        Cursor cur = db.query(ContactDBContract.ContactEntry.TABLE_NAME,null,null,null,null,null,null);
        ArrayList<Person> result = new ArrayList<>();
        // result2 = new ArrayList<String>();
        while (cur.moveToNext()) {
            int firstnameindex = cur.getColumnIndex(ContactDBContract.ContactEntry.COLUMN_NAME_FIRSTNAME);
            int lastnameindex = cur.getColumnIndex(ContactDBContract.ContactEntry.COLUMN_NAME_LASTNAME);
            int phoneindex = cur.getColumnIndex(ContactDBContract.ContactEntry.COLUMN_NAME_PHONE);
            int birthdayindex = cur.getColumnIndex(ContactDBContract.ContactEntry.COLUMN_NAME_BIRTHDAY);
            int addressindex = cur.getColumnIndex(ContactDBContract.ContactEntry.COLUMN_NAME_ADDRESS);
            int zipcodeindex = cur.getColumnIndex(ContactDBContract.ContactEntry.COLUMN_NAME_ZIPCODE);
            int idindex = cur.getColumnIndex(ContactDBContract.ContactEntry._ID);


            Person person = new Person(cur.getString(firstnameindex),cur.getString(lastnameindex),cur.getString(phoneindex),cur.getString(birthdayindex),cur.getString(addressindex),cur.getString(zipcodeindex));
            person.id = cur.getString(idindex);
            result.add(person);
           }
        cur.close();

        return result;
    }





}
