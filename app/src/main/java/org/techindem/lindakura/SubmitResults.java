package org.techindem.lindakura;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.UUID;

public class SubmitResults extends AppCompatActivity {
    Button btnCamera;
    ImageView capturedImage;
    Double mLatitude, mLongitude;
    static String pollcode, pollcenter, pollstation, deviceID;
    Integer jibu1, jibu2, jibu3, jibu4, jibu5, jibu6, jibu7, jibu8, jibu9;
    String uniqueID;
    DataBaseHelper myDbHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submite_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLatitude = getIntent().getDoubleExtra("Latitude", 0.00);
        mLongitude = getIntent().getDoubleExtra("Longitude", 0.00);
        pollcode = getIntent().getStringExtra("Pollcode");
        pollcenter = getIntent().getStringExtra("Pollcenter");
        pollstation = getIntent().getStringExtra("Pollstation");
        deviceID = getIntent().getStringExtra("Deviceid");
        jibu1 = getIntent().getIntExtra("Raila", 0);
        jibu2 = getIntent().getIntExtra("Uhuru", 0);
        jibu3 = getIntent().getIntExtra("Rgvoters", 0);
        jibu4 = getIntent().getIntExtra("Rjbp", 0);
        jibu5 = getIntent().getIntExtra("Robp", 0);
        jibu6 = getIntent().getIntExtra("Dvotes", 0);
        jibu7 = getIntent().getIntExtra("Vvotes", 0);
        jibu8 = getIntent().getIntExtra("Stamp", 0);
        jibu9 = getIntent().getIntExtra("Signed", 0);
        TextView mPollcode = (TextView) findViewById(R.id.textView8);
        mPollcode.setText(pollcenter+pollstation+"-"+pollcode);
        btnCamera = (Button) findViewById(R.id.btnCamera);
        capturedImage= (ImageView) findViewById(R.id.capturedImage);

        myDbHelper = new DataBaseHelper(this);
        //myDbHelper = new DataBaseHelper(this);
        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            //throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            //throw sqle;

        }
        uniqueID =myDbHelper.getUniqueid();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera permissions required to proceed. Please enable in settings:", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }else {

            openCamera();

            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast toast = Toast.makeText(SubmitResults.this,
                             "Thank you for submitting the results. If there is another stream please capture that data too.",
                            //uniqueID+"\n"+deviceID,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    finish();
                    startActivity(i);
                }

            });
        }
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent i = new Intent(getApplicationContext(), ResultsForm.class);
        i.putExtra("Longitude", mLongitude);
        i.putExtra("Latitude", mLatitude);
        i.putExtra("Pollid",pollcode);
        i.putExtra("Pollcenter", pollcenter);
        i.putExtra("Pollstation", pollstation);
        i.putExtra("Deviceid",deviceID);

        startActivityForResult(i, 0);
        return true;

    }
    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            capturedImage.setImageBitmap(bp);
        }
    }
}
