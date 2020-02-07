package com.halgroithm.contactmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.util.Calendar;

public class ViewEditPersonActivity extends AppCompatActivity {
    ImageView personImageHolder;
    EditText personFirstNameHolder,personLastNameHolder, personBirthdayHolder,personPhoneHolder,personAddressHolder, personZipHolder;
    SQLiteDatabase db ;
    Boolean edit = false, done = false;
    Calendar calendar;
    Person extra;
    String image;
    Activity activity;
    public static final String SP_PERSON_FIRSTNAME = "personfirstname";
    public static final String SP_PERSON_LASTNAME = "personlastname";
    public static final String SP_PERSON_BIRTHDAY = "personbirthday";
    public static final String SP_PERSON_PHONE = "personphone";
    public static final String SP_PERSON_ADDRESS = "personaddress";
    public static final String SP_PERSON_ZIPCODE = "personzipcode";
    private final static int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewandedit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar   = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        activity =this;
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        personImageHolder = findViewById(R.id.proimageholder);
        personFirstNameHolder = findViewById(R.id.firstnameholder);
        personLastNameHolder = findViewById(R.id.lastnameholder);
        personBirthdayHolder = findViewById(R.id.birthdayholder);
        personAddressHolder = findViewById(R.id.addressholder);
        personPhoneHolder =  findViewById(R.id.phoneholder);
        personZipHolder = findViewById(R.id.zipcodeholder);


       /* personBirthdayHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.performClick();

                return false;
            }




        }); */


        personBirthdayHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    hidekeyboardfrom(activity);
                }catch(Exception e){
                    Toast.makeText(getBaseContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                    Log.e("Hide Keypad",e.toString());
                }

                showSelector();
            }
        });


        personImageHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select an image"), 1);

            }
        });
        db = new ContactsSQLite(this).getWritableDatabase();
        Intent intent = getIntent();
        extra = (Person) intent.getSerializableExtra("editcontact");
        if (extra!=null){
            edit = true;
            placePerson(extra);
        }
    }

    private void placePerson(Person extra) {
        personImageHolder = findViewById(R.id.proimageholder);
        personImageHolder = findViewById(R.id.proimageholder);
        personFirstNameHolder = findViewById(R.id.firstnameholder);
        personLastNameHolder = findViewById(R.id.lastnameholder);
        personBirthdayHolder = findViewById(R.id.birthdayholder);
        personAddressHolder = findViewById(R.id.addressholder);
        personPhoneHolder =  findViewById(R.id.phoneholder);
        personZipHolder = findViewById(R.id.zipcodeholder);
        personFirstNameHolder.setText(extra.firstname);
        personLastNameHolder.setText(extra.lastname);
        personBirthdayHolder.setText(extra.birthday);
        personAddressHolder.setText(extra.address);
        personPhoneHolder.setText(extra.number);
        personZipHolder.setText(extra.zipcode);

        /*image = extra.images;
        personDescriptionHolder.setText(extra.description);
        try {
            File imageFile = new File(extra.images);
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            personImageHolder.setImageBitmap(imageBitmap);
        }catch(Exception e){
            Log.e("PersonImageError", e.toString());
        }
        */

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
                Uri selectedImageUri = data.getData();
                @SuppressWarnings("deprecation")
                String[] projection = {MediaStore.Images.Media.DATA};
                String imagePath;

                File imageFile;
                try (Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null)) {
                    cursor.moveToFirst();

                    int column_index = cursor.getColumnIndex(projection[0]);
                    imagePath = cursor.getString(column_index);
                    cursor.close();
                }

                try {
                    imageFile = new File(imagePath);

                    if (imageFile.exists()) {
                        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        personImageHolder.setImageBitmap(imageBitmap);
                        personImageHolder.setImageURI(selectedImageUri);
                        Drawable drawable = personImageHolder.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                        ImageView prodimg = findViewById(R.id.proimageholder);
                        prodimg.setImageBitmap(imageBitmap);
                        image = imagePath;
                        /*ImageView proimg = findViewById(R.id.proimageholder);
                        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        //image.setImageURI(selectedImageUri);
                        proimg.setImageBitmap(imageBitmap);

                        /*Drawable drawable = personImageHolder.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                        proimg.setImageBitmap(imageBitmap);*/
                        Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "something went wrong" + "working on it, could not set it and save it", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("proimage", e.toString());
        }


    }
    public void save(View view){
        personImageHolder = findViewById(R.id.proimageholder);
        personImageHolder = findViewById(R.id.proimageholder);
        personFirstNameHolder = findViewById(R.id.firstnameholder);
        personLastNameHolder = findViewById(R.id.lastnameholder);
        personBirthdayHolder = findViewById(R.id.birthdayholder);
        personAddressHolder = findViewById(R.id.addressholder);
        personPhoneHolder =  findViewById(R.id.phoneholder);
        personZipHolder = findViewById(R.id.zipcodeholder);
        String firstname, lastName,phone, address, zipcode, birthday;
        firstname = personFirstNameHolder.getText().toString();
        lastName = personLastNameHolder.getText().toString();
        phone = personPhoneHolder.getText().toString();
        birthday = personBirthdayHolder.getText().toString();
        address = personAddressHolder.getText().toString();
        zipcode = personZipHolder.getText().toString();

        if(firstname.equals("")|lastName.equals("")){
            Toast.makeText(this,"Please fill all input",Toast.LENGTH_LONG).show();
        }else {
            if (edit) {
                ContactsSQLite sq = new ContactsSQLite(this);
                sq.removedata(extra.id);
            }
            Person persontuntun = new Person(firstname, lastName, phone, birthday, address, zipcode);
            persontuntun.save(db);
            done = true;

            Toast.makeText(this, "Contact saved", Toast.LENGTH_LONG).show();
            Intent i1 = new Intent(this, MainActivity.class);
            startActivity(i1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        personImageHolder = findViewById(R.id.proimageholder);
        personImageHolder = findViewById(R.id.proimageholder);
        personFirstNameHolder = findViewById(R.id.firstnameholder);
        personLastNameHolder = findViewById(R.id.lastnameholder);
        personBirthdayHolder = findViewById(R.id.birthdayholder);
        personAddressHolder = findViewById(R.id.addressholder);
        personPhoneHolder =  findViewById(R.id.phoneholder);
        personZipHolder = findViewById(R.id.zipcodeholder);

        SharedPreferences sp = getSharedPreferences("cupboard",MODE_PRIVATE);
        if (edit){
            placePerson(extra);
        }else {
            personFirstNameHolder.setText(sp.getString(SP_PERSON_FIRSTNAME, ""));
            personLastNameHolder.setText(sp.getString(SP_PERSON_LASTNAME, ""));
            personPhoneHolder.setText(sp.getString(SP_PERSON_PHONE, ""));
            personAddressHolder.setText(sp.getString(SP_PERSON_ADDRESS, ""));
            personBirthdayHolder.setText(sp.getString(SP_PERSON_BIRTHDAY, ""));
            personZipHolder.setText(sp.getString(SP_PERSON_ZIPCODE, ""));
        }

        /*try {
            File imageFile = new File(sp.getString(SP_PRODUCT_IMAGE,""));
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            personImageHolder.setImageBitmap(imageBitmap);
        }catch(Exception e){
            Log.e("PersonImageError", e.toString());
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        personImageHolder = findViewById(R.id.proimageholder);
        personImageHolder = findViewById(R.id.proimageholder);
        personFirstNameHolder = findViewById(R.id.firstnameholder);
        personLastNameHolder = findViewById(R.id.lastnameholder);
        personBirthdayHolder = findViewById(R.id.birthdayholder);
        personAddressHolder = findViewById(R.id.addressholder);
        personPhoneHolder =  findViewById(R.id.phoneholder);
        personZipHolder = findViewById(R.id.zipcodeholder);
        String firstname, lastName,phone, address, zipcode, birthday;
        firstname = personFirstNameHolder.getText().toString();
        lastName = personLastNameHolder.getText().toString();
        phone = personPhoneHolder.getText().toString();
        birthday = personBirthdayHolder.getText().toString();
        address = personAddressHolder.getText().toString();
        zipcode = personZipHolder.getText().toString();

        if(!done) {
            SharedPreferences sp = getSharedPreferences("cupboard", MODE_PRIVATE);
            SharedPreferences.Editor akowe = sp.edit();
            akowe.putString(SP_PERSON_FIRSTNAME, firstname);
            akowe.putString(SP_PERSON_LASTNAME, lastName);
            akowe.putString(SP_PERSON_BIRTHDAY, birthday);
            akowe.putString(SP_PERSON_ZIPCODE, zipcode);
            akowe.putString(SP_PERSON_ADDRESS, address);
            akowe.putString(SP_PERSON_PHONE, phone);


            akowe.apply();
        }else {

            SharedPreferences sp = getSharedPreferences("cupboard", MODE_PRIVATE);
            SharedPreferences.Editor akowe = sp.edit();
            akowe.putString(SP_PERSON_FIRSTNAME, "");
            akowe.putString(SP_PERSON_LASTNAME, "");
            akowe.putString(SP_PERSON_BIRTHDAY, "");
            akowe.putString(SP_PERSON_ZIPCODE, "");
            akowe.putString(SP_PERSON_ADDRESS, "");
            akowe.putString(SP_PERSON_PHONE, "");


            akowe.apply();
        }
    }

    public void showSelector(){
        AlertDialog.Builder myalert = new AlertDialog.Builder(this);
        View layout = getLayoutInflater().inflate(R.layout.datecalendar,null);
        //TimePicker timep = (TimePicker)layout.findViewById(R.id.timepicker);
        DatePicker datep = (DatePicker) layout.findViewById(R.id.datepicker);
        //Spinner dropdown = layout.findViewById(R.id.freq);




        //if calendar default year is 1970, meaning no time or alarm is set. Use current date, else use alarm set date
        if (calendar.get(Calendar.YEAR)==1970){
            datep.init(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    calendar.set(i,i1,i2);
                }
            });}else{
            datep.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    calendar.set(i,i1,i2);
                }
            });
        }




        myalert.setView(layout);
        myalert.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        myalert.setPositiveButton("Use date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                personBirthdayHolder.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
                dialogInterface.dismiss();
            }
        });
        myalert.show();

    }
    public static void hidekeyboardfrom(Activity activity){


        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        //check if no view has focus
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView!=null) {
            imm.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
