package org.techindem.lindakura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextView mCoord;
    Double mLatitude, mLongitude;
    Cursor c=null;
    Button btnSubmit;
    String menumsg,  gpstext, pollid, pollstation, pollcenter, deviceID;
    private Spinner spinner1, spinner2, spinner3, spinner4, spinner5;
    DataBaseHelper myDbHelper;
    String county = "County Name";
    String consty = "Constituency Name";
    String ward = "Ward Name";
    String pollc = "Polling Center Name";
    String pollst = "Polling Station Stream";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        hideItems();
        deviceID =id(this);
        mCoord = (TextView) findViewById(R.id.mCoordinates);
        mLatitude = getIntent().getDoubleExtra("Latitude", 0.00);
        mLongitude = getIntent().getDoubleExtra("Longitude", 0.00);
        if(mLatitude !=0.00& mLongitude !=0.00) {
            //gpstext="Your GPS coordinates: (" + String.valueOf(mLatitude) + ", " + String.valueOf(mLongitude)+")";
            gpstext="Your GPS coordinates: (-1.249197, 36.9030401)";

        }else{
            gpstext = "Please enable your location service";
        }
        mCoord.setText(gpstext);
        addListenerOnButton();
        SpinnerListener();
    }
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized  String id(Context context) {
        String uniqueID = myDbHelper.getUniqueid();
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
           myDbHelper.putUniqueid(uniqueID);
        }
        return uniqueID;
    }
    public void SpinnerListener() {
        addItemsOnSpinner(spinner1, myDbHelper.getCounty());
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                menumsg = consty;
                String pollarray = parent.getItemAtPosition(position).toString();
                if (!pollarray.equals(county)) {
                    addItemsOnSpinner(spinner2,  myDbHelper.getConstituency(pollarray));
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            menumsg = ward;
                            String pollarray = parent.getItemAtPosition(position).toString();

                            if (!pollarray.equals(consty)) {
                                addItemsOnSpinner(spinner3, myDbHelper.getWard(pollarray));
                                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        menumsg = pollc;
                                        String pollarray = parent.getItemAtPosition(position).toString();
                                        if (!pollarray.equals(ward)) {
                                            addItemsOnSpinner(spinner4, myDbHelper.getPollc(pollarray));

                                            spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    menumsg = pollst;
                                                    String pollarray = parent.getItemAtPosition(position).toString();
                                                    if (!pollarray.equals(pollc)) {
                                                        addItemsOnSpinner(spinner5, myDbHelper.getPollst(pollarray));
                                                        pollcenter = pollarray;
                                                        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                            @Override
                                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                                String pollarray = parent.getItemAtPosition(position).toString();
                                                                if (!pollarray.equals( pollst)) {
                                                                    pollstation = pollarray;
                                                                    pollid = myDbHelper.getPollcode(pollarray,pollcenter);
                                                                    String str = "IEBC Code for Polling Station: "+pollid;
                                                                    TextView mPollcode = (TextView) findViewById(R.id.mPollcode);
                                                                    mPollcode.setVisibility(View.VISIBLE);
                                                                    mPollcode.setText(str);
                                                                    btnSubmit.setVisibility(View.VISIBLE);

                                                                }
                                                                else {
                                                                    btnSubmit.setVisibility(View.GONE);
                                                                    mCoord.setText(gpstext);
                                                                }

                                                            }
                                                            @Override
                                                            public void onNothingSelected(AdapterView<?> parent) {
                                                                //Another interface callback
                                                            }
                                                        });

                                                    } else {
                                                        mCoord.setText(gpstext);
                                                        btnSubmit.setVisibility(View.GONE);
                                                        spinner5.setVisibility(View.GONE);

                                                    }

                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {
                                                    //Another interface callback
                                                }
                                            });
                                        } else {
                                            mCoord.setText(gpstext);
                                            btnSubmit.setVisibility(View.GONE);
                                            spinner5.setVisibility(View.GONE);
                                            spinner4.setVisibility(View.GONE);

                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        //Another interface callback
                                    }
                                });
                            } else {
                                mCoord.setText(gpstext);
                                btnSubmit.setVisibility(View.GONE);
                                spinner5.setVisibility(View.GONE);
                                spinner4.setVisibility(View.GONE);
                                spinner3.setVisibility(View.GONE);

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //Another interface callback
                        }
                    });
                } else {
                    mCoord.setText(gpstext);
                    btnSubmit.setVisibility(View.GONE);
                    spinner5.setVisibility(View.GONE);
                    spinner4.setVisibility(View.GONE);
                    spinner3.setVisibility(View.GONE);
                    spinner2.setVisibility(View.GONE);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
    }

    public void addItemsOnSpinner(Spinner spinnerx, List list) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerx.setAdapter(dataAdapter);
        spinnerx.setVisibility(View.VISIBLE);

    }

    public void addListenerOnButton() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(getBaseContext(), ResultsForm.class);
                i.putExtra("Longitude", mLongitude);
                i.putExtra("Latitude", mLatitude);
                i.putExtra("Pollid",pollid);
                i.putExtra("Pollcenter", pollcenter);
                i.putExtra("Pollstation", pollstation);
                i.putExtra("Deviceid",deviceID);
                startActivity(i);
            }

        });
    }
    public void hideItems() {
        TextView mPollcode = (TextView) findViewById(R.id.mPollcode);
        mPollcode.setVisibility(View.GONE);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setVisibility(View.GONE);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setVisibility(View.GONE);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setVisibility(View.GONE);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner4.setVisibility(View.GONE);
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner5.setVisibility(View.GONE);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setVisibility(View.GONE);
    }

}
