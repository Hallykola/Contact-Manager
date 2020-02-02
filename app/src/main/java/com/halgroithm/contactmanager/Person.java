package com.halgroithm.contactmanager;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Person {
    String firstname;
    String lastname;
    String number;
    String birthday;
    String address;
    String zipcode;


    public Person(String firstname, String lastname, String number, String birthday, String address, String zipcode) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.number = number;
        this.birthday = birthday;
        this.address = address;
        this.zipcode = zipcode;
    }

    public void save(SQLiteDatabase db){
        //mydb.execSQL("insert into " + DB_TABLE + " (recname,recpath, name, items, total, recnum, recdat) values('" + s1 + "','" + s2 + "','" + s3 + "','" + it + "','" + tot + "','" + rec + "','" + recdate + "');");

        ContentValues values = new ContentValues();
        values.put(ContactDBContract.ContactEntry.COLUMN_NAME_FIRSTNAME,this.firstname);
        values.put(ContactDBContract.ContactEntry.COLUMN_NAME_LASTNAME,this.lastname);
        values.put(ContactDBContract.ContactEntry.COLUMN_NAME_ADDRESS,this.address);
        values.put(ContactDBContract.ContactEntry.COLUMN_NAME_PHONE,this.number);
        values.put(ContactDBContract.ContactEntry.COLUMN_NAME_BIRTHDAY,this.birthday);
        values.put(ContactDBContract.ContactEntry.COLUMN_NAME_ZIPCODE,this.zipcode);

        db.insert(ContactDBContract.ContactEntry.TABLE_NAME, null,values);
    }
}
