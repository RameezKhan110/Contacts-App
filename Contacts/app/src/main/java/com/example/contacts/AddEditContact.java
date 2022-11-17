package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddEditContact extends AppCompatActivity {
    ImageView imageprofile;
    EditText nameET, emailET, phoneET, noteET;
    FloatingActionButton fab;
    Boolean isEditMode;
    private String id, added_time, updated_time, image, name, email, phone, note;

    public static final int CAMERA_PERMISSION_CODE = 100;
    public static final int STORAGE_PERMISSION_CODE = 200;
    public static final int IMAGE_FROM_GALLERY = 300;
    public static final int IMAGE_FROM_CAMERA = 400;

    private String[] cameraPermission;
    private String[] storagePermission;
    Uri imageuri;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

         dbHelper = new DBHelper(this);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageprofile = findViewById(R.id.profile);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        phoneET = findViewById(R.id.phoneET);
        noteET = findViewById(R.id.noteET);
        fab = findViewById(R.id.fab);

        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("IsEditMode", false);

        if(isEditMode){
            getSupportActionBar().setTitle("Update Contact");

            //get values from intent
            id = intent.getStringExtra("ID");
            image = intent.getStringExtra("IMAGE");
            name = intent.getStringExtra("NAME");
            phone = intent.getStringExtra("PHONE");
            email = intent.getStringExtra("EMAIL");
            note = intent.getStringExtra("NOTE");
            added_time = intent.getStringExtra("ADDED_TIME");
            updated_time = intent.getStringExtra("UPDATED_TIME");

            nameET.setText(name);
            phoneET.setText(phone);
            emailET.setText(email);
            noteET.setText(note);

            if(image.equals("")){
                imageprofile.setImageResource(R.drawable.ic_baseline_person_24);
            }else{
                imageprofile.setImageURI(Uri.parse(image));
            }
        }else{
            //add mode on
            getSupportActionBar().setTitle("Add Contact");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        imageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });
    }

    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){ //starts from 0 index
                    //camera selected
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromCamera();
                    }
                }
                if(which == 1){
                    //gallery selected
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }

            }
        }).create().show();
    }

    private void pickFromGallery() {
        //intent to pick from gallery
        Intent galleryintent = new Intent(Intent.ACTION_PICK);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, IMAGE_FROM_GALLERY);
    }

    private void pickFromCamera() {
        //Contentvalues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "IMAGE_DETAIL");
        imageuri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(cameraintent, IMAGE_FROM_CAMERA);
    }

    private void saveData() {
        name = nameET.getText().toString();
        email = emailET.getText().toString();
        phone = phoneET.getText().toString();
        note = noteET.getText().toString();

        String timeStamp = ""+System.currentTimeMillis();

        if(!name.isEmpty() || !phone.isEmpty() || !email.isEmpty() || !note.isEmpty()){

            if(isEditMode){
                //edit mode
                 dbHelper.updateContacts(
                        ""+id,
                        ""+imageuri,
                        ""+name,
                        ""+phone,
                        ""+email,
                        ""+note,
                        ""+added_time,
                        ""+timeStamp //updated time will be new time
                );
                Toast.makeText(this, "Updated Successfully...", Toast.LENGTH_SHORT).show();
            }else{
                //add mode
                long id = dbHelper.insertContacts(
                        ""+imageuri,
                        ""+name,
                        ""+phone,
                        ""+email,
                        ""+note,
                        ""+timeStamp,
                        ""+timeStamp
                );
                Toast.makeText(this, "Inserted Successfully... ", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
        }
    }


        @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result & result1;
    }

    public void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_CODE);
    }

    public boolean checkStoragePermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result1;
    }

    public void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && storageAccepted){
                        pickFromCamera();
                    }else{
                        Toast.makeText(this, "Camera and Storage Permission needed...", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_PERMISSION_CODE:
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(storageAccepted){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Storage Permission needed...", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_FROM_GALLERY){
                imageprofile.setImageURI(imageuri);
            }else if(requestCode == IMAGE_FROM_CAMERA){
                imageprofile.setImageURI(imageuri);
            }else{
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}