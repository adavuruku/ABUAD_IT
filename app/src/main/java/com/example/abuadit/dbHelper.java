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
    public static final String DATABASE_NAME = "ABUADOOO.db";
    public static final String DBLOCATION = "/data/data/com.example.abuadit/databases/";
    private static final int DATABASE_VERSION = 7;
    private Context mcontext;
    private SQLiteDatabase mdatabase;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mcontext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ABUADITINOFRMATION = "CREATE TABLE IF NOT EXISTS " + dbColumnList.abuadItInformation.TABLE_NAME +
                "(" +
                dbColumnList.abuadItInformation._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.abuadItInformation.COLUMN_REGNO + " VARCHAR, " +
                dbColumnList.abuadItInformation.COLUMN_STAFFID + " VARCHAR, " +
                dbColumnList.abuadItInformation.COLUMN_COMPANYID + " VARCHAR, " +
                dbColumnList.abuadItInformation.COLUMN_DATESTART + " VARCHAR, " +
                dbColumnList.abuadItInformation.COLUMN_DATEEND + " VARCHAR, " +
                dbColumnList.abuadItInformation.COLUMN_DURATION + " VARCHAR " +
                ")";

        String CREATE_TABLE_ABUADCOMPANY= "CREATE TABLE IF NOT EXISTS " + dbColumnList.abuadCompany.TABLE_NAME +
                "(" +
                dbColumnList.abuadCompany._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.abuadCompany.COLUMN_COMPANYADDRESS + " VARCHAR, " +
                dbColumnList.abuadCompany.COLUMN_COMPANYDESCRIPTION + " TEXT, " +
                dbColumnList.abuadCompany.COLUMN_COMPANYID + " VARCHAR, " +
                dbColumnList.abuadCompany.COLUMN_COMPANYEMAIL + " VARCHAR, " +
                dbColumnList.abuadCompany.COLUMN_COMPANYPHONE + " VARCHAR, " +
                dbColumnList.abuadCompany.COLUMN_COMPANYNAME + " VARCHAR, " +
                dbColumnList.abuadCompany.COLUMN_COMPANYLGOV + " VARCHAR, " +
                dbColumnList.abuadCompany.COLUMN_COMPANYSTATE + " VARCHAR " +
                ")";

        String CREATE_TABLE_ABUADNOTICE= "CREATE TABLE IF NOT EXISTS " + dbColumnList.abuadNotice.TABLE_NAME +
                "(" +
                dbColumnList.abuadNotice._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.abuadNotice.COLUMN_ID + " VARCHAR, " +
                dbColumnList.abuadNotice.COLUMN_DESCRIPTION + " TEXT, " +
                dbColumnList.abuadNotice.COLUMN_TITLE + " TEXT, " +
                dbColumnList.abuadNotice.COLUMN_AUTHOR + " VARCHAR, " +
                dbColumnList.abuadNotice.COLUMN_STATUS + " VARCHAR, " +
                dbColumnList.abuadNotice.COLUMN_NOTICEDATE + " VARCHAR " +
                ")";
        String CREATE_TABLE_ABUADLECTURER= "CREATE TABLE IF NOT EXISTS " + dbColumnList.abuadLecturer.TABLE_NAME +
                "(" +
                dbColumnList.abuadLecturer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.abuadLecturer.COLUMN_FULLNAME + " VARCHAR, " +
                dbColumnList.abuadLecturer.COLUMN_FACULTY + " VARCHAR, " +
                dbColumnList.abuadLecturer.COLUMN_DEPARTMENT + " VARCHAR, " +
                dbColumnList.abuadLecturer.COLUMN_EMAIL + " VARCHAR, " +
                dbColumnList.abuadLecturer.COLUMN_PHONE + " VARCHAR, " +
                dbColumnList.abuadLecturer.COLUMN_STAFFADDRESS + " TEXT, " +
                dbColumnList.abuadLecturer.COLUMN_STAFFID + " VARCHAR " +
                ")";
        String CREATE_TABLE_ABUADSTUDENT= "CREATE TABLE IF NOT EXISTS " + dbColumnList.abuadstudent.TABLE_NAME +
                "(" +
                dbColumnList.abuadstudent._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.abuadstudent.COLUMN_FULLNAME + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_FACULTY + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_DEPARTMENT + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_EMAIL + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_PHONE + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_GENDER + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_LEVEL + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_STATE + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_LGOV + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_MODE + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_DEGREE + " VARCHAR, " +
                dbColumnList.abuadstudent.COLUMN_CONTACTADD + " TEXT, " +
                dbColumnList.abuadstudent.COLUMN_REGNO + " VARCHAR " +
                ")";
        String CREATE_TABLE_APPLICATIONLIST= "CREATE TABLE IF NOT EXISTS " + dbColumnList.applicationList.TABLE_NAME +
                "(" +
                dbColumnList.applicationList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                dbColumnList.applicationList.COLUMN_COMPANYID + " VARCHAR, " +
                dbColumnList.applicationList.COLUMN_REGNO + " VARCHAR, " +
                dbColumnList.applicationList.COLUMN_ACCEPTSTATUS + " VARCHAR " +
                ")";
        String CREATE_TABLE_REGISTERLIST= "CREATE TABLE IF NOT EXISTS " + dbColumnList.registerList.TABLE_NAME +
                "(" +
                dbColumnList.registerList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                dbColumnList.registerList.COLUMN_RECORSTATUS + " VARCHAR, " +
                dbColumnList.registerList.COLUMN_REGNO + " VARCHAR, " +
                dbColumnList.registerList.COLUMN_DATE + " VARCHAR, " +
                dbColumnList.registerList.COLUMN_COMPANYID+ " VARCHAR " +
                ")";

        String CREATE_TABLE_PROFILEPICS= "CREATE TABLE IF NOT EXISTS " + dbColumnList.userProfilePics.TABLE_NAME +
                "(" +
                    dbColumnList.registerList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Define a primary key
                    dbColumnList.userProfilePics.COLUMN_REGNO + " VARCHAR, " +
                    dbColumnList.userProfilePics.COLUMN_PROFILEPICS + " blob " +
                ")";

        sqLiteDatabase.execSQL(CREATE_TABLE_ABUADITINOFRMATION);
        sqLiteDatabase.execSQL(CREATE_TABLE_ABUADCOMPANY);
        sqLiteDatabase.execSQL(CREATE_TABLE_ABUADNOTICE);
        sqLiteDatabase.execSQL(CREATE_TABLE_ABUADLECTURER);
        sqLiteDatabase.execSQL(CREATE_TABLE_ABUADSTUDENT);
        sqLiteDatabase.execSQL(CREATE_TABLE_APPLICATIONLIST);
        sqLiteDatabase.execSQL(CREATE_TABLE_REGISTERLIST);
        sqLiteDatabase.execSQL(CREATE_TABLE_PROFILEPICS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.abuadItInformation.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.abuadCompany.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.abuadLecturer.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.abuadNotice.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.abuadstudent.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.applicationList.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.registerList.TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + dbColumnList.userProfilePics.TABLE_NAME);
            //recreate the tables
            onCreate(sqLiteDatabase);
        }
    }

/*******************************************************************/
    /****** USER DETAILS *********************************************/

    //***********************************************************************
    public Cursor verifyCompanyExist(String companyId){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.abuadCompany.TABLE_NAME + " WHERE "+dbColumnList.abuadCompany.COLUMN_COMPANYID +"= '" + companyId +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void SaveCompanyInformation(String companyName, String companyPhone,String companyEmail, String companyAddress,
                           String companyDescription, String companyState,String companyLocalGov, String companyId){
//        openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYID, companyId);
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYNAME, companyName);
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYSTATE, companyState);
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYLGOV, companyLocalGov);
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYPHONE, companyPhone);
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYADDRESS, companyAddress);
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYDESCRIPTION, companyDescription);
        cv.put(dbColumnList.abuadCompany.COLUMN_COMPANYEMAIL, companyEmail);

        Cursor cursor = verifyCompanyExist(companyId);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.abuadCompany.TABLE_NAME, cv, dbColumnList.abuadCompany.COLUMN_COMPANYID + "= ?", new String[]{companyId});
        }else{
            database.insert(dbColumnList.abuadCompany.TABLE_NAME,null,cv);
        }
//        closeDatabase();
    }
    public Cursor getAllCompany(){
        //    openDatabase();
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.registerList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor getACompany(String companyID){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.abuadCompany.TABLE_NAME,
                null,
                dbColumnList.abuadCompany.COLUMN_COMPANYID +" = ? Limit 1",
                new String[]{companyID},
                null,
                null,null);
    }

    //*******************Student Start****************************************************

    public Cursor verifyStudentExist(String regno){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.abuadstudent.TABLE_NAME + " WHERE "+dbColumnList.abuadstudent.COLUMN_REGNO +"= '" + regno +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void saveStudentInformation(String regno, String fullname,
                                         String faculty, String department,String phone, String email,
                                         String contactAddress, String gender,String degree, String mode,
                                         String itState, String itLgov,String itLevel){
//        openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.abuadstudent.COLUMN_REGNO, regno);
        cv.put(dbColumnList.abuadstudent.COLUMN_FULLNAME, fullname);
        cv.put(dbColumnList.abuadstudent.COLUMN_FACULTY, faculty);
        cv.put(dbColumnList.abuadstudent.COLUMN_DEPARTMENT, department);
        cv.put(dbColumnList.abuadstudent.COLUMN_MODE, mode);
        cv.put(dbColumnList.abuadstudent.COLUMN_DEGREE, degree);
        cv.put(dbColumnList.abuadstudent.COLUMN_STATE, itState);
        cv.put(dbColumnList.abuadstudent.COLUMN_LGOV, itLgov);
        cv.put(dbColumnList.abuadstudent.COLUMN_LEVEL, itLevel);
        cv.put(dbColumnList.abuadstudent.COLUMN_GENDER, gender);
        cv.put(dbColumnList.abuadstudent.COLUMN_PHONE, phone);
        cv.put(dbColumnList.abuadstudent.COLUMN_EMAIL, email);
        cv.put(dbColumnList.abuadstudent.COLUMN_CONTACTADD, contactAddress);

        Cursor cursor = verifyStudentExist(regno);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.abuadstudent.TABLE_NAME, cv, dbColumnList.abuadstudent.COLUMN_REGNO + "= ?", new String[]{regno});
        }else{
            database.insert(dbColumnList.abuadstudent.TABLE_NAME,null,cv);
        }
//        closeDatabase();
    }
    public Cursor getAStudent(String regno){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.abuadstudent.TABLE_NAME,
                null,
                dbColumnList.abuadstudent.COLUMN_REGNO +" = ? Limit 1",
                new String[]{regno},
                null,
                null,null);
    }
    //*********************************LECTURERS*************************************************


    public Cursor verifyLecturerExist(String staffid){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.abuadLecturer.TABLE_NAME + " WHERE "+dbColumnList.abuadLecturer.COLUMN_STAFFID +"= '" + staffid +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void saveLecturerInformation(String staffid, String fullname,
                                         String faculty, String department,String phone, String email,String staffaddress){
//        openDatabase();
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.abuadLecturer.COLUMN_STAFFID, staffid);
        cv.put(dbColumnList.abuadLecturer.COLUMN_FULLNAME, fullname);
        cv.put(dbColumnList.abuadLecturer.COLUMN_FACULTY, faculty);
        cv.put(dbColumnList.abuadLecturer.COLUMN_DEPARTMENT, department);
        cv.put(dbColumnList.abuadLecturer.COLUMN_STAFFADDRESS, staffaddress);
        cv.put(dbColumnList.abuadLecturer.COLUMN_EMAIL, email);
        cv.put(dbColumnList.abuadLecturer.COLUMN_PHONE, phone);

        Cursor cursor = verifyLecturerExist(staffid);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.abuadLecturer.TABLE_NAME, cv, dbColumnList.abuadLecturer.COLUMN_STAFFID + "= ?", new String[]{staffid});
        }else{
            database.insert(dbColumnList.abuadLecturer.TABLE_NAME,null,cv);
        }
//        closeDatabase();
    }
    public Cursor getALecturerInfo(String staffid){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.abuadLecturer.TABLE_NAME,
                null,
                dbColumnList.abuadLecturer.COLUMN_STAFFID +" = ? Limit 1",
                new String[]{staffid},
                null,
                null,null);
    }

    //*********************** IT INFORMATION ***********************************************************

    public Cursor verifyInformationExist(String regno){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.abuadItInformation.TABLE_NAME + " WHERE "+dbColumnList.abuadItInformation.COLUMN_REGNO +"= '" + regno +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void saveITInformation(String regno,String startDate, String endDate,String duration, String staffid,
                                          String companyId){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.abuadItInformation.COLUMN_REGNO, regno);
        cv.put(dbColumnList.abuadItInformation.COLUMN_DATESTART, startDate);
        cv.put(dbColumnList.abuadItInformation.COLUMN_DATEEND, endDate);
        cv.put(dbColumnList.abuadItInformation.COLUMN_COMPANYID, companyId);
        cv.put(dbColumnList.abuadItInformation.COLUMN_STAFFID, staffid);
        cv.put(dbColumnList.abuadItInformation.COLUMN_DURATION, duration);

        Cursor cursor = verifyInformationExist(regno);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.abuadItInformation.TABLE_NAME, cv, dbColumnList.abuadItInformation.COLUMN_REGNO + "= ?", new String[]{regno});
        }else{
            database.insert(dbColumnList.abuadItInformation.TABLE_NAME,null,cv);
        }
    }
    public Cursor getAStudentItInfo(String regno){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.abuadItInformation.TABLE_NAME,
                null,
                dbColumnList.abuadItInformation.COLUMN_REGNO +" = ? Limit 1",
                new String[]{regno},
                null,
                null,null);
    }

    public Cursor getAllStaffITStudent(String staffID){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.abuadItInformation.TABLE_NAME,
                null,
                dbColumnList.abuadItInformation.COLUMN_STAFFID + " = ? ",
                new String[]{staffID},
                null,
                null,null);
    }

//    *************************************************************************
//*********************** NOTICE ***********************************************************

    public Cursor verifyNoticeExist(String noticeid){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.abuadNotice.TABLE_NAME + " WHERE "+dbColumnList.abuadNotice.COLUMN_ID +"= '" + noticeid +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void saveNotice(String noticeid,String noticedesc, String author,String title, String date, String status){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.abuadNotice.COLUMN_ID, noticeid);
        cv.put(dbColumnList.abuadNotice.COLUMN_TITLE, title);
        cv.put(dbColumnList.abuadNotice.COLUMN_DESCRIPTION, noticedesc);
        cv.put(dbColumnList.abuadNotice.COLUMN_NOTICEDATE, date);
        cv.put(dbColumnList.abuadNotice.COLUMN_AUTHOR, author);
        cv.put(dbColumnList.abuadNotice.COLUMN_STATUS, status);

        Cursor cursor = verifyNoticeExist(noticeid);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.abuadNotice.TABLE_NAME, cv, dbColumnList.abuadNotice.COLUMN_ID + " = ?", new String[]{noticeid});
        }else{
            database.insert(dbColumnList.abuadNotice.TABLE_NAME,null,cv);
        }
    }
    public Cursor getANotice(String noticeId){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.abuadNotice.TABLE_NAME,
                null,
                dbColumnList.abuadNotice.COLUMN_ID +" = ?  AND "+ dbColumnList.abuadNotice.COLUMN_STATUS + " = ? Limit 1",
                new String[]{noticeId,"0"},
                null,
                null,null);
    }

    public Cursor getAllNotice(){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.abuadNotice.TABLE_NAME,
                null,
                 dbColumnList.abuadNotice.COLUMN_STATUS + " = ?",
                new String[]{"0"},
                null,
                null,
                null);
    }



//    **************************PROFILE PICS********************************


    public Cursor verifyImageExist(String regno){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.userProfilePics.TABLE_NAME + " WHERE "+dbColumnList.userProfilePics.COLUMN_REGNO +"= '" + regno +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void saveProfilePics(String regno,byte[] imageData){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.userProfilePics.COLUMN_REGNO, regno);
        cv.put(dbColumnList.userProfilePics.COLUMN_PROFILEPICS, imageData);

        Cursor cursor = verifyImageExist(regno);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.userProfilePics.TABLE_NAME, cv, dbColumnList.userProfilePics.COLUMN_REGNO + " = ?", new String[]{regno});
        }else{
            database.insert(dbColumnList.userProfilePics.TABLE_NAME,null,cv);
        }
    }
    public Cursor getAProfilePics(String regno){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.userProfilePics.TABLE_NAME,
                null,
                dbColumnList.userProfilePics.COLUMN_REGNO + " = ? Limit 1",
                new String[]{regno},
                null,
                null,null);
    }


    //    **************************PROFILE PICS********************************


    public Cursor verifyApplicationExist(String regno, String companyId){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.applicationList.TABLE_NAME + " WHERE "+dbColumnList.applicationList.COLUMN_REGNO +"= '" + regno +"' AND " +
                dbColumnList.applicationList.COLUMN_COMPANYID +"= '" + companyId +"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void saveApplication(String regno,String companyId, String status){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.applicationList.COLUMN_REGNO, regno);
        cv.put(dbColumnList.applicationList.COLUMN_COMPANYID, companyId);
        cv.put(dbColumnList.applicationList.COLUMN_ACCEPTSTATUS, status);

        Cursor cursor = verifyApplicationExist(regno,companyId);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.applicationList.TABLE_NAME, cv, dbColumnList.applicationList.COLUMN_REGNO + " = ? AND "
                    + dbColumnList.applicationList.COLUMN_COMPANYID + " = ?" , new String[]{regno,companyId});
        }else{
            database.insert(dbColumnList.applicationList.TABLE_NAME,null,cv);
        }
    }
    public Cursor getAllStudentApplication(String regno){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.applicationList.TABLE_NAME,
                null,
                dbColumnList.applicationList.COLUMN_REGNO + " = ?",
                new String[]{regno},
                null,
                null,dbColumnList.applicationList.COLUMN_ACCEPTSTATUS + " desc");
    }
    public Cursor getAllCompanyApplication(String companyId, String status){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.applicationList.TABLE_NAME,
                null,
                dbColumnList.applicationList.COLUMN_COMPANYID + " = ? AND " +dbColumnList.applicationList.COLUMN_ACCEPTSTATUS + "= ?",
                new String[]{companyId, status},
                null,
                null,null);
    }






    public Cursor verifyRegisterExist(String regno, String companyId,String dateAttendance){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM "+dbColumnList.registerList.TABLE_NAME + " WHERE "+dbColumnList.registerList.COLUMN_REGNO +"= '" + regno +"' AND " +
                dbColumnList.registerList.COLUMN_COMPANYID +"= '" + companyId +"' AND "+dbColumnList.registerList.COLUMN_DATE+" = '"+dateAttendance+"' Limit 1";
        return database.rawQuery(sql, null);
    }
    public void saveRegister(String regno, String companyId, String status, String dateAttendance){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(dbColumnList.registerList.COLUMN_REGNO, regno);
        cv.put(dbColumnList.registerList.COLUMN_COMPANYID, companyId);
        cv.put(dbColumnList.registerList.COLUMN_DATE, dateAttendance);
        cv.put(dbColumnList.registerList.COLUMN_RECORSTATUS, status);

        Cursor cursor = verifyRegisterExist(regno, companyId, dateAttendance);
        if(cursor.getCount() >= 1) {
            database.update(dbColumnList.registerList.TABLE_NAME, cv, dbColumnList.registerList.COLUMN_REGNO + " = ? AND "
                    + dbColumnList.registerList.COLUMN_COMPANYID + " = ? AND " + dbColumnList.registerList.COLUMN_DATE + " = ?", new String[]{regno,companyId,dateAttendance});
        }else{
            database.insert(dbColumnList.registerList.TABLE_NAME,null,cv);
        }
    }
    public Cursor getAllStudentAttendance(String regno){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.registerList.TABLE_NAME,
                null,
                dbColumnList.registerList.COLUMN_REGNO + " = ?",
                new String[]{regno},
                null,
                null,dbColumnList.registerList.COLUMN_DATE + " desc");
    }
    public Cursor getAllAttendance(){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.registerList.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,dbColumnList.registerList.COLUMN_DATE + " desc");
    }
    public Cursor getAllCompanyAttendance(String companyId){
        SQLiteDatabase database = getReadableDatabase();
        return database.query(dbColumnList.registerList.TABLE_NAME,
                null,
                dbColumnList.registerList.COLUMN_COMPANYID + " = ?",
                new String[]{companyId},
                null,
                null,null);
    }

    //delete all attendance
    public void deleteAttendace(){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(dbColumnList.registerList.TABLE_NAME,
                null,null);
    }

}
