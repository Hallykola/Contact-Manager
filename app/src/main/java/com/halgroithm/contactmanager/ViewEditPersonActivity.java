package com.halgroithm.contactmanager;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;

public class ViewEditPersonActivity extends AppCompatActivity {
    ImageView personImageHolder;
    EditText personFirstNameHolder,personLastNameHolder, personBirthdayHolder,personPhoneHolder,personAddressHolder, personZipHolder;
    SQLiteDatabase db ;
    Boolean edit = false;
    Person extra;
    String image;
    public static final String SP_PERSON_FIRSTNAME = "personfirstname";
    public static final String SP_PERSON_LASTNAME = "personlastname";
    public static final String SP_PERSON_BIRTHDAY = "personbirthday";
    public static final String SP_PERSON_PHONE = "personphone";
    public static final String SP_PERSON_ADDRESS = "personaddress";
    public static final String SP_PERSON_ZIPCODE = "personzipcode";
    private final static int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewandedit);

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
        extra = (Person) intent.getSerializableExtra("editperson");
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


        if (edit){
        ContactsSQLite sq = new ContactsSQLite(this);
        sq.removedata(extra.firstname);
        }
        Person persontuntun = new Person(firstname,lastName,phone,birthday,address,zipcode);
        persontuntun.save(db);
        Toast.makeText(this,"Item saved",Toast.LENGTH_LONG).show();
        Intent i1 = new Intent(this, MainActivity.class);
        startActivity(i1);
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

        personFirstNameHolder.setText(sp.getString(SP_PERSON_FIRSTNAME,""));
        personLastNameHolder.setText(sp.getString(SP_PERSON_LASTNAME,""));
        personPhoneHolder.setText(sp.getString(SP_PERSON_PHONE,""));
        personAddressHolder.setText(sp.getString(SP_PERSON_ADDRESS,""));
        personBirthdayHolder.setText(sp.getString(SP_PERSON_BIRTHDAY,""));
        personZipHolder.setText(sp.getString(SP_PERSON_ZIPCODE,""));
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

        SharedPreferences sp = getSharedPreferences("cupboard",MODE_PRIVATE);
        SharedPreferences.Editor akowe = sp.edit();
        akowe.putString(SP_PERSON_FIRSTNAME,firstname);
        akowe.putString(SP_PERSON_LASTNAME,lastName);
        akowe.putString(SP_PERSON_BIRTHDAY,birthday);
        akowe.putString(SP_PERSON_ZIPCODE,zipcode);
        akowe.putString(SP_PERSON_ADDRESS,address);
        akowe.putString(SP_PERSON_PHONE,phone);


        akowe.apply();
    }
}
