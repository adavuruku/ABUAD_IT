package com.example.abuadit;

import android.provider.BaseColumns;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbColumnList {
    public static class userDetails implements BaseColumns{
        //we dont need to create a column for the id because the BaseColumns
        //interface already automatically add _ID Column to the table
        public static final String TABLE_NAME = "userDetails";
       public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_QOUTE = "userQoute";
        public static final String COLUMN_NAME = "fullName";
        public static final String COLUMN_DEPT = "userDept";
        public static final String COLUMN_LEVEL = "userLevel";
        public static final String COLUMN_GENDER = "userGender";
        public static final String COLUMN_RELIGION = "userReligion";
        public static final String COLUMN_STATE = "userState";
        public static final String COLUMN_LGOV = "userLgov";
        public static final String COLUMN_PERMADD = "userPermAdd";
        public static final String COLUMN_CSTATE = "userCState";
        public static final String COLUMN_CLGOV = "userCLgov";
        public static final String COLUMN_CPERMADD = "userCPermAdd";
        public static final String COLUMN_BESTMOMENT = "userBestMoment";
        public static final String COLUMN_REGNO = "userRegNo";
        public static final String COLUMN_EMAIL = "userEmail";
        //public static final String COLUMN_PICS = "userPics";
        public static final String COLUMN_LASTLOGIN = "lastLogin";
    }
    public static class myAccount implements BaseColumns{
        public static final String TABLE_NAME = "myAccount";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_LASTLOGIN = "lastLogin";
    }
    public static class userPics implements BaseColumns{
        public static final String TABLE_NAME = "userPics";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_PICS = "userPics";
    }
    public static class userBest implements BaseColumns{
        public static final String TABLE_NAME = "userBest";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_BESTFRIEND = "bestFriend";
    }

    public static class userCourse implements BaseColumns{
        public static final String TABLE_NAME = "userCourse";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_BESTCOURSE = "bestCourses";
    }
    public static class userLecturer implements BaseColumns{
        public static final String TABLE_NAME = "userLecturer";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_BESTLECTURER = "bestLecturer";
    }
    public static class userPhone implements BaseColumns{
        public static final String TABLE_NAME = "userPhone";
        public static final String COLUMN_USERID = "userID";
        public static final String COLUMN_PHONE = "phone";
    }
}
