package org.techindem.lindakura;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


public class ResultsForm extends AppCompatActivity {

    Button takePhoto;
    Double mLatitude, mLongitude;
    static String pollcode, pollcenter, pollstation, deviceID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_form);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        takePhoto = (Button) findViewById(R.id.photo);
        mLatitude = getIntent().getDoubleExtra("Longitude", 0.00);
        mLongitude = getIntent().getDoubleExtra("Latitude", 0.00);
        pollcode = getIntent().getStringExtra("Pollid");
        pollcenter = getIntent().getStringExtra("Pollcenter");
        pollstation = getIntent().getStringExtra("Pollstation");
        deviceID = getIntent().getStringExtra("Deviceid");
        TextView mPollcode = (TextView) findViewById(R.id.pollDetails);
        mPollcode.setText(pollcenter+pollstation+"-"+pollcode);
        submitForm();

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    public void submitForm() {
        takePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText jibu = (EditText)findViewById(R.id.editText1);
                Integer jibu1;
                try{
                    jibu1 = Integer.parseInt(jibu.getText().toString());
                }catch (NumberFormatException je){
                    jibu1 = -2;
                }
                jibu = (EditText)findViewById(R.id.editText2);
                Integer jibu2;
                try{
                    jibu2 = Integer.parseInt(jibu.getText().toString());
                }catch (NumberFormatException je){
                    jibu2 = -2;
                }
                jibu = (EditText)findViewById(R.id.editText3);
                Integer jibu3;
                try{
                    jibu3 = Integer.parseInt(jibu.getText().toString());
                }catch (NumberFormatException je){
                    jibu3 = -2;
                }
                jibu = (EditText)findViewById(R.id.editText4);
                Integer jibu4;
                try{
                    jibu4 = Integer.parseInt(jibu.getText().toString());
                }catch (NumberFormatException je){
                    jibu4 = -2;
                }
                jibu = (EditText)findViewById(R.id.editText5);
                Integer jibu5;
                try{
                    jibu5 = Integer.parseInt(jibu.getText().toString());
                }catch (NumberFormatException je){
                    jibu5 = -2;
                }
                jibu = (EditText)findViewById(R.id.editText6);
                Integer jibu6;
                try{
                    jibu6 = Integer.parseInt(jibu.getText().toString());
                }catch (NumberFormatException je){
                    jibu6 = -2;
                }
                jibu = (EditText)findViewById(R.id.editText7);
                Integer jibu7;
                try{
                    jibu7 = Integer.parseInt(jibu.getText().toString());
                }catch (NumberFormatException je){
                    jibu7 = -2;
                }

                if(jibu1 < 0){
                    Toast toast = Toast.makeText(ResultsForm.this, "Please fill entries for Raila Odinga", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(jibu2 < 0){
                    Toast toast = Toast.makeText(ResultsForm.this, "Please fill entries for Uhuru Kenyatta", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(jibu3 < 0){
                    Toast toast = Toast.makeText(ResultsForm.this, "Please fill entries for No of Registered Voters", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(jibu4 < 0){
                    Toast toast = Toast.makeText(ResultsForm.this, "Please fill entries for No of Rejected Ballots", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(jibu5 < 0){
                    Toast toast = Toast.makeText(ResultsForm.this, "Please fill entries for No of Objected Ballots", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(jibu6 < 0){
                    Toast toast = Toast.makeText(ResultsForm.this, "Please fill entries for No of Disputed Votes", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else if(jibu7 < 0){
                    Toast toast = Toast.makeText(ResultsForm.this, "Please fill entries for No of Valid Votes Cast", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {

                    Integer jibu8, jibu9;
                    if (((CheckBox)findViewById(R.id.checkBox)).isChecked()) {
                        jibu8 = 1;
                    } else {
                        jibu8 = 0;
                    }
                    if (((CheckBox)findViewById(R.id.checkBox2)).isChecked()) {
                        jibu9 = 1;

                    } else {
                        jibu9 = 0;
                    }


                    Intent i = new Intent(getBaseContext(), SubmitResults.class);
                    i.putExtra("Longitude", mLongitude);
                    i.putExtra("Latitude", mLatitude);
                    i.putExtra("Pollcode",pollcode);
                    i.putExtra("Pollcenter", pollcenter);
                    i.putExtra("Pollstation", pollstation);
                    i.putExtra("Deviceid",deviceID);
                    i.putExtra("Raila", jibu1);
                    i.putExtra("Uhuru", jibu2);
                    i.putExtra("Rgvoters", jibu3);
                    i.putExtra("Rjbp", jibu4);
                    i.putExtra("Robp", jibu5);
                    i.putExtra("Dvotes", jibu6);
                    i.putExtra("Vvotes", jibu7);
                    i.putExtra("Stamp",jibu8);
                    i.putExtra("Signed", jibu9);

                    finish();
                    startActivity(i);

                }
            }

        });
    }



}
