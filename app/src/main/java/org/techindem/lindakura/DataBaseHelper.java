package org.techindem.lindakura;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DB_NAME = "POLLDB";
    private final Context myContext;
    String DB_PATH = null;
    private SQLiteDatabase myDataBase;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        DB_PATH = "/data/data/"+context.getPackageName()+"/databases/";
    }
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }
    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<String> getCounty() {
        List<String> countyList = new ArrayList<String>();
        //String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        String selectQuery = "SELECT  county FROM COUNTY";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            countyList.add("County Name");
            do {
                // Adding county to list
                countyList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return countyList;
    }

    public List<String> getConstituency(String county) {
        List<String> countyList = new ArrayList<String>();
        //county="MOMBASA";
        //String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        String selectQuery = "SELECT  constituency FROM CONSTITUENCY where countyid=(select _id from county where county=\""+county+"\")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            countyList.add("Constituency Name");
            do {
                // Adding county to list
                countyList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return countyList;
    }

    public List<String> getWard(String constituency) {

        List<String> countyList = new ArrayList<String>();
        //String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        String selectQuery = "SELECT  ward FROM ward where constituencyid=(select _id from constituency where constituency=\""+constituency+"\")";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            countyList.add("Ward Name");
            do {
                // Adding county to list
                countyList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return countyList;

    }

    public List<String> getPollc(String pollc) {
        List<String> countyList = new ArrayList<String>();
        //String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        String selectQuery = "SELECT  pollcentre FROM pollcentre where wardid=(select _id from ward where ward=\""+pollc+"\")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            countyList.add("Polling Center Name");
            do {
                // Adding county to list
                countyList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return countyList;
    }

    public List<String> getPollst(String pollst) {
        List<String> countyList = new ArrayList<String>();
        //String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        String selectQuery = "SELECT  stream FROM pollstation where pollcentreid=(select _id from pollcentre where pollcentre=\""+pollst+"\")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            countyList.add("Polling Station Stream");
            do {
                // Adding county to list
                countyList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return countyList;
    }
    public String getPollcode(String pollstation, String pollst) {
        String countyList = null;
        //String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        //String selectQuery = "SELECT  pollstcode FROM pollstation where stream='"+pollstation+"'pollcentreid=(select _id from pollcentre where pollcentre='"+pollst+"')";
        String selectQuery = "SELECT  pollstcode FROM pollstation where stream=\""+pollstation+"\" and pollcentreid=(select _id from pollcentre where pollcentre=\""+pollst+"\")";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                // Adding county to list
                countyList = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return countyList;

    }
    public String getUniqueid() {
        String uniqueId = null;
        String selectQuery = "SELECT  uniqueid FROM uniqueid where _id=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {

            do {
                uniqueId = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return uniqueId;

    }

    public void putUniqueid(String idunique) {
        String insertQuery = "insert or replace into uniqueid (_id, uniqueid) values (1, '"+idunique+"')";
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            db.execSQL(insertQuery);
        }
        catch (Exception e)
        {

        }
    }


}


