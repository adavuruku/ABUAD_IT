package com.example.abuadit;

import android.provider.BaseColumns;

/**
 * Created by sherif146 on 03/01/2018.
 */

public class dbColumnList {

    public static class abuadItInformation implements BaseColumns{
        public static final String TABLE_NAME = "abuadItInformation";
        public static final String COLUMN_REGNO= "regno";
        public static final String COLUMN_STAFFID = "staffid";
        public static final String COLUMN_COMPANYID = "comppanyid";
        public static final String COLUMN_DATESTART = "dateStart";
        public static final String COLUMN_DATEEND = "dateEnd";
        public static final String COLUMN_DURATION = "duration";
    }
    public static class abuadCompany implements BaseColumns{
        public static final String TABLE_NAME = "abuadCompany";
        public static final String COLUMN_COMPANYNAME= "companyname";
        public static final String COLUMN_COMPANYPHONE = "companyphone";
        public static final String COLUMN_COMPANYEMAIL= "companyemail";
        public static final String COLUMN_COMPANYADDRESS= "companyaddress";
        public static final String COLUMN_COMPANYID = "companyid";
        public static final String COLUMN_COMPANYDESCRIPTION= "companydescription";
        public static final String COLUMN_COMPANYSTATE= "companystate";
        public static final String COLUMN_COMPANYLGOV= "companylgov";
    }
    public static class abuadNotice implements BaseColumns{
        public static final String TABLE_NAME = "abuadnotice";
        public static final String COLUMN_ID = "noticeid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_NOTICEDATE = "noticedate";
        public static final String COLUMN_STATUS = "delStatus";
    }

    public static class abuadLecturer implements BaseColumns{
        public static final String TABLE_NAME = "abuadLecturer";
        public static final String COLUMN_STAFFID = "staffid";
        public static final String COLUMN_FULLNAME= "fullname";
        public static final String COLUMN_FACULTY= "faculty";
        public static final String COLUMN_DEPARTMENT= "department";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE= "phone";
        public static final String COLUMN_STAFFADDRESS = "staffaddress";
    }
    public static class abuadstudent implements BaseColumns{
        public static final String TABLE_NAME = "abuadstudent";
        public static final String COLUMN_REGNO = "regno";
        public static final String COLUMN_FULLNAME = "fullname";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_FACULTY = "faculty";
        public static final String COLUMN_GENDER = "gender";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_DEGREE= "degree";
        public static final String COLUMN_MODE = "mode";
        public static final String COLUMN_STATE = "itstate";
        public static final String COLUMN_LGOV= "itlgov";
        public static final String COLUMN_LEVEL = "itlevel";
        public static final String COLUMN_CONTACTADD = "contactAddress";
    }

    public static class applicationList implements BaseColumns{
        public static final String TABLE_NAME = "applicationList";
        public static final String COLUMN_REGNO = "regno";
        public static final String COLUMN_COMPANYID= "companyid";
        public static final String COLUMN_ACCEPTSTATUS = "acceptStatus"; //student - 0 / 1 company - 2
    }

    public static class registerList implements BaseColumns{
        public static final String TABLE_NAME = "registerList";
        public static final String COLUMN_REGNO = "regNo";
        public static final String COLUMN_COMPANYID = "companyid";
        public static final String COLUMN_RECORSTATUS = "recordstatus";
        public static final String COLUMN_DATE = "recordDate";
    }

    public static class userProfilePics implements BaseColumns{
        public static final String TABLE_NAME = "userProfilePics";
        public static final String COLUMN_REGNO = "regNo";
        public static final String COLUMN_PROFILEPICS = "profilepics";
    }
}
