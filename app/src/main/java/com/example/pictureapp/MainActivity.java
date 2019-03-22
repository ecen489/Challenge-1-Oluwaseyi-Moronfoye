package com.example.pictureapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    SQLiteDatabase db;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("dataBase",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pictureDB ( ID INTEGER PRIMARY KEY, Image BLOB NOT NULL)");
                // +  + " (" + S_BUSSTOP_ID + " INTEGER, " + S_BUSSTOP_NAME + " TEXT, " + S_NAME_E + " TEXT, " + S_ARS_ID +  " TEXT, " + S_LATITUDE + " TEXT, " + S_LONGITUDE + " TEXT, " + S_NEXT_BUS + " TEXT, " + S_NUM + " TEXT, " + S_STATION_LIST_ID + " TEXT)");

//        db = openOrCreateDatabase("dBase",MODE_PRIVATE, null);
//        Scanner scan = new Scanner(getResources().openRawResource(R.raw.database));
//        String query ="";
//        while (scan.hasNextLine())
//        {// build and execute queries
//            query += scan.nextLine() + "\n";
//            if (query.trim().endsWith(";"))
//            {
//                db.execSQL(query);
//                query ="";
//            }
//        }

    }

    public void getImg(View view) {
        EditText IDnumber;
        IDnumber = (EditText) findViewById(R.id.IDnum);
        String name = IDnumber.getText().toString();


        String query = "SELECT image FROM pictureDB WHERE ID = " + name;
        Cursor cr = db.rawQuery(query, null);
        if (cr.moveToFirst())
        {
            byte[] imageBlob = cr.getBlob(cr.getColumnIndex("Image"));
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
            ImageView picture = (ImageView) findViewById(R.id.imageView);
            picture.setImageBitmap(imageBitmap);
        }
        cr.close();




    }

    public void captureImg(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView picture = (ImageView) findViewById(R.id.imageView);
            picture.setImageBitmap(imageBitmap);

            String IDnum = Integer.toString(++count);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] picBlob = bos.toByteArray();


            ContentValues values = new ContentValues();
            values.put("ID", IDnum);
            values.put("Image", picBlob);
            db.insert("pictureDB", null , values );
        }
    }
}
