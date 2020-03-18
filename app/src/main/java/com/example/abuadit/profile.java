package com.example.abuadit;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class profile extends Fragment {
    TextView pname,pregno,pfaculty,pemailphone,pcontact,pmodetype,
            sname,sfaculty,semailphone,cname,cemailphone,ccontact,cstatelgov,istart,iend,ileft;
    private dbHelper dbHelper;
    private SharedPreferences MyId;
    private OnFragmentInteractionListener mListener;
    String companyID, staffID;


    public profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
        pname = rootView.findViewById(R.id.pname);
        pregno = rootView.findViewById(R.id.pregno);
        pfaculty = rootView.findViewById(R.id.pfaculty);
        pemailphone = rootView.findViewById(R.id.pemailphone);
        pcontact = rootView.findViewById(R.id.pcontact);
        pmodetype = rootView.findViewById(R.id.pmodetype);
        sname = rootView.findViewById(R.id.sname);
        sfaculty = rootView.findViewById(R.id.sfaculty);
        semailphone = rootView.findViewById(R.id.semailphone);
        cname = rootView.findViewById(R.id.cname);
        cemailphone = rootView.findViewById(R.id.cemailphone);
        ccontact = rootView.findViewById(R.id.ccontact);
        cstatelgov = rootView.findViewById(R.id.cstatelgov);
        istart = rootView.findViewById(R.id.istart);
        iend = rootView.findViewById(R.id.iend);
        ileft = rootView.findViewById(R.id.ileft);

        MyId = getActivity().getSharedPreferences("MyId", getActivity().MODE_PRIVATE);
        String userID = MyId.getString("MyId", "");
        //retrieve student information
        dbHelper = new dbHelper(getContext());

        Cursor studentInfo = dbHelper.getAStudent(userID);
        if(studentInfo.getCount() >=1) {
            studentInfo.moveToFirst();
            pname.setText(studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FULLNAME)));

            pregno.setText(studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_REGNO)) +
                    " / "+ studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_LEVEL )) + " Level");

            pfaculty.setText("Faculty Of " +studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FACULTY)) +
                    " / " + studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_DEPARTMENT)));

            pemailphone.setText(studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_EMAIL)) +
                    " / " + studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_PHONE)));

            pmodetype.setText(studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_MODE)) +
                    " / " + studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_DEGREE)));

            pcontact.setText(studentInfo.getString(studentInfo.getColumnIndex(dbColumnList.abuadstudent.COLUMN_CONTACTADD)));
        }
        //get supervisor Info.
        Cursor ITInfo = dbHelper.getAStudentItInfo(userID);
        if(ITInfo.getCount() >=1) {
            ITInfo.moveToFirst();
            companyID = ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_COMPANYID));
            staffID = ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_STAFFID));
            if(ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATESTART)).length() > 0){
                String dateDif = monthsBetweenDates(ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATESTART)),
                        ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATEEND)));
                ileft.setText("Left : " + dateDif);
                istart.setText("Start Date : " + ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATESTART)));
                iend.setText("End Date : " +ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATEEND)));
            }
        }

        //lecturer info
        Cursor lecturerInfo = dbHelper.getALecturerInfo(staffID);
        if(lecturerInfo.getCount() >=1) {
            lecturerInfo.moveToFirst();
            sname.setText(lecturerInfo.getString(lecturerInfo.getColumnIndex(dbColumnList.abuadLecturer.COLUMN_FULLNAME)));
            sfaculty.setText("Faculty Of "+lecturerInfo.getString(lecturerInfo.getColumnIndex(dbColumnList.abuadLecturer.COLUMN_FACULTY)) +
                    " / " +  lecturerInfo.getString(lecturerInfo.getColumnIndex(dbColumnList.abuadLecturer.COLUMN_DEPARTMENT)));
            semailphone.setText(lecturerInfo.getString(lecturerInfo.getColumnIndex(dbColumnList.abuadLecturer.COLUMN_EMAIL)) +
                    " / " +  lecturerInfo.getString(lecturerInfo.getColumnIndex(dbColumnList.abuadLecturer.COLUMN_PHONE)));
        }

        //lecturer info
        Cursor companyInfo = dbHelper.getACompany(companyID);
        if(companyInfo.getCount() >=1) {
            companyInfo.moveToFirst();
            cname.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYNAME)));

            cemailphone.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYEMAIL)) +
                    " / " +  companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYPHONE)));

            cstatelgov.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYSTATE)) +
                    " / " +  companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYLGOV)));

            ccontact.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYADDRESS)));

        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public String monthsBetweenDates(String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long msDiff = end.getTimeInMillis() - start.getTimeInMillis();
        long week = msDiff / (1000*60*60*24*7);
        Calendar calDiff = Calendar.getInstance();
        calDiff.setTimeInMillis(msDiff);

        return calDiff.get(Calendar.YEAR) - 1970 + " Year, " + calDiff.get(Calendar.MONTH) +" Months, " + week +" Weeks, " + calDiff.get(Calendar.DAY_OF_MONTH) +" Days";
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
