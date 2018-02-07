package co.kartoo.app.cards.suggestioncard.applycard;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.camera2.CaptureRequest;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.kartoo.app.EditProfile;
import co.kartoo.app.MainActivity;
import co.kartoo.app.R;
import co.kartoo.app.models.ApplyCreditCardPref_;
import co.kartoo.app.models.LoginPref_;

@EActivity(R.layout.activity_apply_scanning)
public class ApplyScanningActivity extends AppCompatActivity {
    @ViewById
    Toolbar mToolbar;
    @ViewById
    TextView mTVtitle, textIncome, textKtp, textNpwp;
    @ViewById
    Button mBtnNext;
    @ViewById
    ImageView mRVincome, mRVktp, mRVnpwp, imageIncome, imageKtp, imageNpwp;
    @ViewById
    ProgressBar progressBarIncome, progressBarKtp, progressBarNpwp;
    @Pref
    LoginPref_ loginPref;
    @Pref
    ApplyCreditCardPref_ applyCreditCardPref;

    String fileName;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    String which, cardName, mIVincomeKey, mIVktpKey, mIVnpwpKey;
    Uri selectedImage;

    SharedPreferences prefs;


    public static final String storageConnectionString =
            "DefaultEndpointsProtocol=https;AccountName=kartoostorage;AccountKey=uKFP4jlPuDGlmyH7WoPSXoi9plqPej2/43HaPEjiS9K9QR2P5wohYDZaaiPa4CLZdInXDGPEzjTSlOlQDffGFw==";

    @AfterViews
    void init() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        setUpNavDrawer();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTVtitle.setText(applyCreditCardPref.nameCardApplied().get());

        progressBarIncome.setVisibility(View.INVISIBLE);
        progressBarKtp.setVisibility(View.INVISIBLE);
        progressBarNpwp.setVisibility(View.INVISIBLE);

        String str = loginPref.email().get();
        fileName = str.replaceAll("[@.]", "");

        cardName = applyCreditCardPref.nameCardApplied().get();

        mIVnpwpKey = "";
        mIVincomeKey = "";
        mIVktpKey = "";

        prefs = getSharedPreferences(cardName, 0);

        final int THUMBSIZE = 512;
        if (prefs.getAll() != null || prefs.getAll().size()!=0) {

            Log.e("TAG", "mIVktpKey: "+prefs.getString("mIVktpKey", "") );
            Log.e("TAG", "mIVnpwpKey: "+prefs.getString("mIVnpwpKey", "") );
            Log.e("TAG", "mIVincomeKey: "+prefs.getString("mIVincomeKey", "") );


            if (!prefs.getString("mIVktpKey", "").equals("")){

                mIVktpKey = prefs.getString("mIVktpKey", "");

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+"ktp");
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, fileName + ".jpg");
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getAbsolutePath()),
                        THUMBSIZE, THUMBSIZE);

                textKtp.setText("Upload Successfull\n\nTap to reupload");
                textKtp.setTypeface(null, Typeface.BOLD);
                textKtp.setTextColor(getResources().getColor(R.color.ColorPrimaryYellow));

                imageKtp.setVisibility(View.INVISIBLE);
                mRVktp.setBackgroundDrawable(null);
                mRVktp.setBackgroundResource(0);
                mRVktp.setImageBitmap(null);
                mRVktp.setImageBitmap(ThumbImage);
            }
            if (!prefs.getString("mIVincomeKey", "").equals("")){

                mIVincomeKey = prefs.getString("mIVincomeKey", "");

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+"income");
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, fileName + ".jpg");
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getAbsolutePath()),
                        THUMBSIZE, THUMBSIZE);

                textIncome.setText("Upload Successfull\n\nTap to reupload");
                textIncome.setTypeface(null, Typeface.BOLD);
                textIncome.setTextColor(getResources().getColor(R.color.ColorPrimaryYellow));

                imageIncome.setVisibility(View.INVISIBLE);
                mRVincome.setBackgroundDrawable(null);
                mRVincome.setBackgroundResource(0);
                mRVincome.setImageBitmap(null);
                mRVincome.setImageBitmap(ThumbImage);
            }
            if (!prefs.getString("mIVnpwpKey", "").equals("")){

                mIVnpwpKey = prefs.getString("mIVnpwpKey", "");

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+"npwp");
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, fileName + ".jpg");
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getAbsolutePath()),
                        THUMBSIZE, THUMBSIZE);

                textNpwp.setText("Upload Successfull\n\nTap to reupload");
                textNpwp.setTypeface(null, Typeface.BOLD);
                textNpwp.setTextColor(getResources().getColor(R.color.ColorPrimaryYellow));

                imageNpwp.setVisibility(View.INVISIBLE);
                mRVnpwp.setBackgroundDrawable(null);
                mRVnpwp.setBackgroundResource(0);
                mRVnpwp.setImageBitmap(null);
                mRVnpwp.setImageBitmap(ThumbImage);
            }
        }

    }

    @Click(R.id.mBtnNext)
    public void mBtnNextClick() {
        sharedPreference();
        Intent intent = new Intent(this, ApplyDoneActivity_.class);
        startActivity(intent);
        finish();
    }

    @Click(R.id.mRVincome)
    public void mRVincomeClick() {
        check();
        which = "income";
        Log.e("TAG", "mRVincomeClick: "+which );
    }
    @Click(R.id.mRVktp)
    public void mRVktpClick() {
        check();
        which = "ktp";
        Log.e("TAG", "mRVktpClick: "+which );
    }
    @Click(R.id.mRVnpwp)
    public void mRVnpwpClick() {
        check();
        which = "npwp";
        Log.e("TAG", "mRVktpClick: "+which );
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyScanningActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    Log.e("TAG", "selectImage: "+which);
                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+which);
                    imagesFolder.mkdirs();

                    File image = new File(imagesFolder, fileName + ".jpg");
                    selectedImage = Uri.fromFile(image);

                    imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
                    Log.e("TAG", "onClick: "+selectedImage );
                    startActivityForResult(imageIntent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                imageIncome.setVisibility(View.INVISIBLE);

                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA){

                Log.e("TAG", "onActivityResult: "+selectedImage);

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+which);
                imagesFolder.mkdirs();
                File image = new File(imagesFolder, fileName + ".jpg");

                Log.e("TAG", "onActivityResult: "+image.getAbsolutePath());

                //Get Thumbnail Image
                final int THUMBSIZE = 512;
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getAbsolutePath()),
                        THUMBSIZE, THUMBSIZE);

                Bitmap mBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                FileOutputStream fo;
                try {
                    image.createNewFile();
                    fo = new FileOutputStream(image);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (which.equals("income")){
                    new BlobUploadIncome().execute();

                    imageIncome.setVisibility(View.INVISIBLE);
                    mRVincome.setBackgroundDrawable(null);
                    mRVincome.setBackgroundResource(0);
                    mRVincome.setImageBitmap(null);
                    mRVincome.setImageBitmap(ThumbImage);
                    mRVincome.getRotation();

                } else if(which.equals("ktp")){
                    new BlobUploadKTP().execute();

                    imageKtp.setVisibility(View.INVISIBLE);
                    mRVktp.setBackgroundDrawable(null);
                    mRVktp.setBackgroundResource(0);
                    mRVktp.setImageBitmap(null);
                    mRVktp.setImageBitmap(ThumbImage);
                    mRVktp.getRotation();

                }else if (which.equals("npwp")){
                    new BlobUploadNPWP().execute();

                    imageNpwp.setVisibility(View.INVISIBLE);
                    mRVnpwp.setBackgroundDrawable(null);
                    mRVnpwp.setBackgroundResource(0);
                    mRVnpwp.setImageBitmap(null);
                    mRVnpwp.setImageBitmap(ThumbImage);
                    mRVnpwp.getRotation();
                }

                Log.e("TAG", "selectedImageBackground: "+selectedImage );
            }
        }
    }

    String exception;

    private class BlobUploadNPWP extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient serviceClient = account.createCloudBlobClient();

                // Container name must be lower case.
                CloudBlobContainer container = serviceClient.getContainerReference("ccapp");
                container.createIfNotExists();

                // Upload an image file.
                CloudBlockBlob blob = container.getBlockBlobReference(which + "\\" + fileName + ".jpg");

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo" + which);
                File sourceFile = new File(imagesFolder, fileName + ".jpg");

                blob.getProperties().setContentType("image/jpeg");

                blob.upload(new FileInputStream(sourceFile), sourceFile.length());
                //String uri = blob.getUri().toString();
                //Log.e("TAG", "URI uri: "+uri);

            } catch (Exception e) {
                exception = e.toString();
                Log.e("TAG", "UPLOADBLOB: " + e);
            }
            return "resp";
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e("TAG", "onPostExecute: " + exception);
            if(which.equals("npwp")){
                textNpwp.setVisibility(View.VISIBLE);
                if (exception == null){
                    imageNpwp.setVisibility(View.INVISIBLE);
                    progressBarNpwp.setVisibility(View.INVISIBLE);
                    textNpwp.setText("Upload Successfull\n\nTap to reupload");
                    textNpwp.setTypeface(null, Typeface.BOLD);
                    textNpwp.setTextColor(getResources().getColor(R.color.ColorPrimaryYellow));

                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+"npwp");
                    imagesFolder.mkdirs();
                    File image = new File(imagesFolder, fileName + ".jpg");
                    mIVnpwpKey = image.getAbsolutePath();
                }
                else {
                    mRVnpwp.setImageResource(0);
                    mRVnpwp.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
                    progressBarNpwp.setVisibility(View.INVISIBLE);
                    textNpwp.setTextColor(getResources().getColor(R.color.white));
                    textNpwp.setText("Upload Failed. Tap to retry");
                }
            }
        }
        @Override
        protected void onPreExecute() {
            imageNpwp.setVisibility(View.INVISIBLE);
            textNpwp.setVisibility(View.INVISIBLE);
            progressBarNpwp.setVisibility(View.VISIBLE);
            exception = null;
        }
        @Override
        protected void onProgressUpdate(String... progress) {
        }
    }

    private class BlobUploadIncome extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient serviceClient = account.createCloudBlobClient();

                // Container name must be lower case.
                CloudBlobContainer container = serviceClient.getContainerReference("ccapp");
                container.createIfNotExists();

                // Upload an image file.
                CloudBlockBlob blob = container.getBlockBlobReference(which + "\\" + fileName + ".jpg");

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo" + which);
                File sourceFile = new File(imagesFolder, fileName + ".jpg");

                blob.getProperties().setContentType("image/jpeg");

                blob.upload(new FileInputStream(sourceFile), sourceFile.length());
                String uri = blob.getUri().toString();
                Log.e("TAG", "URI uri: "+uri);

            } catch (Exception e) {
                exception = e.toString();
                Log.e("TAG", "UPLOADBLOB: " + e);
            }
            return "resp";
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e("TAG", "onPostExecute: " + exception);
            if (which.equals("income")){
                textIncome.setVisibility(View.VISIBLE);
                if (exception == null){
                    imageIncome.setVisibility(View.INVISIBLE);
                    progressBarIncome.setVisibility(View.INVISIBLE);
                    textIncome.setText("Upload Successfull\n\nTap to reupload");
                    textIncome.setTypeface(null, Typeface.BOLD);
                    textIncome.setTextColor(getResources().getColor(R.color.ColorPrimaryYellow));

                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+"income");
                    imagesFolder.mkdirs();
                    File image = new File(imagesFolder, fileName + ".jpg");
                    mIVincomeKey = image.getAbsolutePath();
                }
                else {
                    mRVincome.setImageResource(0);
                    mRVincome.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
                    progressBarIncome.setVisibility(View.INVISIBLE);
                    textIncome.setTextColor(getResources().getColor(R.color.white));
                    textIncome.setText("Upload Failed. Tap to retry");
                }
            }
        }
        @Override
        protected void onPreExecute() {
            imageIncome.setVisibility(View.INVISIBLE);
            textIncome.setVisibility(View.INVISIBLE);
            progressBarIncome.setVisibility(View.VISIBLE);
            exception = null;
        }
        @Override
        protected void onProgressUpdate(String... progress) {
        }
    }

    private class BlobUploadKTP extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
                CloudBlobClient serviceClient = account.createCloudBlobClient();

                // Container name must be lower case.
                CloudBlobContainer container = serviceClient.getContainerReference("ccapp");
                container.createIfNotExists();

                // Upload an image file.
                CloudBlockBlob blob = container.getBlockBlobReference(which+"\\"+fileName+".jpg");

                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+which);
                File sourceFile = new File(imagesFolder, fileName+".jpg");

                blob.getProperties().setContentType("image/jpeg");

                blob.upload(new FileInputStream(sourceFile), sourceFile.length());

            }
            catch(Exception e){
                exception = e.toString();
                Log.e("TAG", "UPLOADBLOB: "+e );
            }
            return "resp";
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e("TAG", "onPostExecute: "+exception);

            if (which.equals("ktp")){
                textKtp.setVisibility(View.VISIBLE);
                if (exception == null){
                    imageKtp.setVisibility(View.INVISIBLE);
                    progressBarKtp.setVisibility(View.INVISIBLE);
                    textKtp.setText("Upload Successfull\n\nTap to reupload");
                    textKtp.setTypeface(null, Typeface.BOLD);
                    textKtp.setTextColor(getResources().getColor(R.color.ColorPrimaryYellow));

                    File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+"ktp");
                    imagesFolder.mkdirs();
                    File image = new File(imagesFolder, fileName + ".jpg");
                    mIVktpKey = image.getAbsolutePath();
                }
                else {
                    mRVktp.setImageResource(0);
                    mRVktp.setBackgroundColor(getResources().getColor(R.color.ColorPrimaryYellow));
                    progressBarKtp.setVisibility(View.INVISIBLE);
                    textKtp.setTextColor(getResources().getColor(R.color.white));
                    textKtp.setText("Upload Failed. Tap to retry");
                }
            }
            Log.e("TAG", "onPostExecute: "+"UPLOADSUCCES" );
        }

        @Override
        protected void onPreExecute() {
            imageKtp.setVisibility(View.INVISIBLE);

            textKtp.setVisibility(View.INVISIBLE);
            progressBarKtp.setVisibility(View.VISIBLE);
            exception = null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {

        }
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        selectedImage = data.getData();

        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImage, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        //Get Thumbnail Image
        final int THUMBSIZE = 512;
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImagePath),
                THUMBSIZE, THUMBSIZE);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 2000;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE) scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;

        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Kartoo"+which);
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, fileName + ".jpg");

        Log.e("TAG", "onActivityResult: "+image.getAbsolutePath());

        Bitmap mBitmap = bm;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

        FileOutputStream fo;
        try {
            image.createNewFile();
            fo = new FileOutputStream(image);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(which.equals("income")){
            new BlobUploadIncome().execute();

            mRVincome.setBackgroundDrawable(null);
            mRVincome.setBackgroundResource(0);
            mRVincome.setBackgroundColor(0);
            mRVincome.setImageBitmap(ThumbImage);
        }
        else if (which.equals("ktp")){
            new BlobUploadKTP().execute();

            mRVktp.setBackgroundDrawable(null);
            mRVktp.setBackgroundResource(0);
            mRVktp.setBackgroundColor(0);
            mRVktp.setImageBitmap(ThumbImage);
        }
        else if (which.equals("npwp")){
            new BlobUploadNPWP().execute();

            mRVnpwp.setBackgroundDrawable(null);
            mRVnpwp.setBackgroundResource(0);
            mRVnpwp.setBackgroundColor(0);
            mRVnpwp.setImageBitmap(ThumbImage);
        }
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void check() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            final List<String> permissionsList = new ArrayList<String>();
            if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                permissionsNeeded.add("Camera");
            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                permissionsNeeded.add("Write Storage");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
                permissionsNeeded.add("Read Storage");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "You need to grant access to " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showMessageOKCancel(message,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
            selectImage();

        }else{
            selectImage();

        }

    }
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(ApplyScanningActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectImage();

                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(ApplyScanningActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void sharedPreference(){
        prefs = getSharedPreferences(cardName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        Log.e("TAG", "mIVktpKeyAdd: "+ mIVktpKey );
        Log.e("TAG", "mIVnpwpKeyAdd: "+ mIVnpwpKey );
        Log.e("TAG", "mIVincomeKeyAdd: "+ mIVincomeKey );

        edit.putString("mIVincomeKey", mIVincomeKey);
        edit.putString("mIVktpKey", mIVktpKey);
        edit.putString("mIVnpwpKey", mIVnpwpKey);

        edit.apply();

    }

    public void onBackPressed() {
        sharedPreference();
        Intent intent = new Intent(this, ApplyConfirmCardActivity_.class);
        startActivity(intent);
        finish();
    }

    private void setUpNavDrawer() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_back_orange);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
