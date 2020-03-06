package com.example.abuadit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbHelper extends SQLiteOpenHelper {
    // Database Info
    // Database Info
    public static final String DATABASE_NAME = "NAMSATBULO.db";
    public static final String DBLOCATION = "/data/data/com.example.atbunamsn/databases/";
    private static final int DATABASE_VERSION = 1;
    private Context mcontext;
    private SQLiteDatabase mdatabase;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mcontext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userDetails.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userPhone.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userLecturer.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userCourse.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userBest.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userPics.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.myAccount.TABLE_NAME);
            //recreate the tables
            onCreate(sqLiteDatabase);
        }
    }

    public void openDatabase(){
        String dbpath = mcontext.getDatabasePath(DATABASE_NAME).getPath();
        if(mdatabase != null && mdatabase.isOpen()){
            return;
        }
        mdatabase = SQLiteDatabase.openDatabase(dbpath,null,SQLiteDatabase.OPEN_READWRITE);
    }
    public void closeDatabase(){
        if(mdatabase != null){
            mdatabase.close();
        }
    }
/*******************************************************************/
    /****** USER DETAILS *********************************************/
//search all user by department
    public Cursor getLogin(String userName, String userPassword){
        openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.userDetails.TABLE_NAME,
                null,
                dbColumnList.userDetails.COLUMN_REGNO +" = ? AND " + dbColumnList.userDetails.COLUMN_PASSWORD +" = ?",
                new String[]{userName,userPassword},
                null,
                null,
                null);
    }
    public Cursor getAllUser(){
        openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.userDetails.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    //search user by id
    public Cursor getDetails(String searchField, String contentSearch){
        openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.userDetails.TABLE_NAME,
                null,
                searchField +" = ?",
                new String[]{contentSearch},
                null,
                null,
                null);
    }
    //Insert details
    public void addNewDetails(String userDept, String userBestMoment,String userCLgov,String userCPermAdd,String userCState,String userEmail,
                              String userGender,String userLevel,String userLgov,String fullName,String userPermAdd,String userQoute,String userRegNo,String userReligion,
                              String userId,String userState,String password) {
        openDatabase();
        // Create and/or open the database for writing
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.userDetails.COLUMN_DEPT, userDept);
        cv.put(dbColumnList.userDetails.COLUMN_BESTMOMENT, userBestMoment);
        cv.put(dbColumnList.userDetails.COLUMN_CLGOV, userCLgov);
        cv.put(dbColumnList.userDetails.COLUMN_CPERMADD, userCPermAdd);
        cv.put(dbColumnList.userDetails.COLUMN_CSTATE, userCState);
        cv.put(dbColumnList.userDetails.COLUMN_EMAIL, userEmail);
        cv.put(dbColumnList.userDetails.COLUMN_GENDER, userGender);
        cv.put(dbColumnList.userDetails.COLUMN_LEVEL, userLevel);
        cv.put(dbColumnList.userDetails.COLUMN_LGOV, userLgov);
        cv.put(dbColumnList.userDetails.COLUMN_NAME, fullName);
        cv.put(dbColumnList.userDetails.COLUMN_PERMADD, userPermAdd);
        cv.put(dbColumnList.userDetails.COLUMN_QOUTE, userQoute);
        cv.put(dbColumnList.userDetails.COLUMN_REGNO, userRegNo);
        cv.put(dbColumnList.userDetails.COLUMN_RELIGION, userReligion);
        cv.put(dbColumnList.userDetails.COLUMN_USERID, userId);
        cv.put(dbColumnList.userDetails.COLUMN_STATE, userState);
        cv.put(dbColumnList.userDetails.COLUMN_PASSWORD, password);
        //cv.put(dbColumnList.userDetails.COLUMN_PICS, byteArray);
        database.insert(dbColumnList.userDetails.TABLE_NAME,null,cv);
    }

    //Update details
    public void updateDetails(String userDept, String userBestMoment,String userCLgov,String userCPermAdd,String userCState,String userEmail,
                              String userGender,String userLevel,String userLgov,String fullName,String userPermAdd,String userQoute,String userRegNo,String userReligion,
                              String userId,String userState,String password) {
        openDatabase();
        // Create and/or open the database for writing
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.userDetails.COLUMN_DEPT, userDept);
        cv.put(dbColumnList.userDetails.COLUMN_BESTMOMENT, userBestMoment);
        cv.put(dbColumnList.userDetails.COLUMN_CLGOV, userCLgov);
        cv.put(dbColumnList.userDetails.COLUMN_CPERMADD, userCPermAdd);
        cv.put(dbColumnList.userDetails.COLUMN_CSTATE, userCState);
        cv.put(dbColumnList.userDetails.COLUMN_EMAIL, userEmail);
        cv.put(dbColumnList.userDetails.COLUMN_GENDER, userGender);
        cv.put(dbColumnList.userDetails.COLUMN_LEVEL, userLevel);
        cv.put(dbColumnList.userDetails.COLUMN_LGOV, userLgov);
        cv.put(dbColumnList.userDetails.COLUMN_NAME, fullName);
        cv.put(dbColumnList.userDetails.COLUMN_PERMADD, userPermAdd);
        cv.put(dbColumnList.userDetails.COLUMN_QOUTE, userQoute);
        cv.put(dbColumnList.userDetails.COLUMN_REGNO, userRegNo);
        cv.put(dbColumnList.userDetails.COLUMN_RELIGION, userReligion);
        cv.put(dbColumnList.userDetails.COLUMN_STATE, userState);
        cv.put(dbColumnList.userDetails.COLUMN_PASSWORD, password);
        // cv.put(dbColumnList.userDetails.COLUMN_PICS, byteArray);
        database.update(dbColumnList.userDetails.TABLE_NAME, cv,dbColumnList.userDetails.COLUMN_USERID + "= ?", new String[]{userId});
        // database.insert(dbColumnList.userDetails.TABLE_NAME,null,cv);
    }

    /*******************************************************************/
    public void insertForPics(String userID,byte[] insertValue){
        openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.userPics.COLUMN_USERID, userID);
        cv.put(dbColumnList.userPics.COLUMN_PICS, insertValue);
        database.insert(dbColumnList.userPics.TABLE_NAME,null,cv);
    }

    public void updateForPics(String userID,byte[] insertValue){
        openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.userPics.COLUMN_PICS, insertValue);
        database.update(dbColumnList.userPics.TABLE_NAME,cv,dbColumnList.userPics.COLUMN_USERID + " =?",
                new String[]{userID});
    }

    public Cursor searchForPics(String userID){
        openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.userPics.TABLE_NAME,
                null,
                dbColumnList.userPics.COLUMN_USERID + " =?",
                new String[]{userID},
                null,
                null,
                null);
    }


    /****** SEARCH FOR BEST FRIENDS,BEST COURSES, BEST LECTURER, BEST PHONES STATRT*********************************************/
//get all multiple other tables
    public Cursor getAllMultiple(String TableName, String fieldSearch1,String fieldSearch2, String value1,String value2){
        openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(TableName,
                null,
                fieldSearch1 + " = ? AND " + fieldSearch2 + " = ?",
                new String[]{value1,value2},
                null,
                null,
                null);
    }


    public Cursor getAllSingle(String TableName, String fieldSearch1, String value1){
        openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(TableName,
                null,
                fieldSearch1 + " = ?" ,
                new String[]{value1},
                null,
                null,
                null);
    }

    /****** UPDATE FOR BEST FRIENDS,BEST COURSES, BEST LECTURER, BEST PHONES STATRT*********************************************/

    public void updateForAll(String tableName, String updateField, String updateValue, String criteriaField, String criteria){
        openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(updateField, updateValue);
        database.update(tableName, cv, criteriaField + "= ?",
                new String[]{criteria});
    }

    /****** INSERT FOR BEST FRIENDS,BEST COURSES, BEST LECTURER, BEST PHONES STATRT*********************************************/

    public void insertForAll(String tableName, String insertField1,String insertField2, String insertValue1,String insertValue2){
        openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(insertField1, insertValue1);
        cv.put(insertField2, insertValue2);
        database.insert(tableName,null,cv);
    }
}
