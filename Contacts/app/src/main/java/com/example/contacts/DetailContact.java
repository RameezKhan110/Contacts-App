package com.example.contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class DetailContact extends AppCompatActivity {
    ImageView detailimage;
    TextView detailname, detailphone, detailnote, detailemail, detailaddedtime, detialupdatedtime;
    private String id;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("ContactId");


        detailimage = findViewById(R.id.detailimage);
        detailname = findViewById(R.id.detailname);
        detailnote = findViewById(R.id.detailnote);
        detailphone = findViewById(R.id.detailphone);
        detailemail = findViewById(R.id.detailemail);
        detailaddedtime = findViewById(R.id.detailaddedtime);
        detialupdatedtime = findViewById(R.id.detailupdatedtime);

        loadDataById();

    }

    private void loadDataById() {
        //get data from database
        //query for find data by id
        String selectquery = "SELECT * FROM " +Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + id + "\"";
        SQLiteDatabase db =  dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectquery, null);
        if(cursor.moveToFirst()){
            do {
                        String image = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE));
                        String name = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NAME));
                        String phone = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PHONE));
                        String email = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL));
                        String note = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_NOTE));
                        String added_time = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIME));
                        String updated_time = ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME));

                Calendar calendar = Calendar.getInstance(Locale.getDefault());

                calendar.setTimeInMillis(Long.parseLong(added_time));
                String timeAdd = ""+ DateFormat.format("dd/MM/yy hh:mm:aa", calendar);

                calendar.setTimeInMillis(Long.parseLong(updated_time));
                String timeupdate = ""+ DateFormat.format("dd/MM/yy hh:mm:aa", calendar);

                detailname.setText(name);
                detailphone.setText(phone);
                detailemail.setText(email);
                detailnote.setText(note);
                detailaddedtime.setText(timeAdd);
                detialupdatedtime.setText(timeupdate);

                if(image.equals("")){
                    detailimage.setImageResource(R.drawable.ic_baseline_person_24);
                }else{
                    detailimage.setImageURI(Uri.parse(image));
                }

            }while (cursor.moveToNext());
        }
        db.close();
    }
}