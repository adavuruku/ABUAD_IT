package com.example.abuadit;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class profile extends Fragment {
    TextView pname,pregno,pfaculty,pemailphone,pcontact,pmodetype,
            sname,sfaculty,semailphone,cname,cemailphone,ccontact,cstatelgov,istart,iend,ileft;
    CardView companyCard;
    private dbHelper dbHelper;
    private SharedPreferences MyId;
    private OnFragmentInteractionListener mListener;
    String companyID, staffID,userID,allResult;
    String address = "https://abuadit.000webhostapp.com/abuadrest.php";

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
        companyCard = rootView.findViewById(R.id.company);

        MyId = getActivity().getSharedPreferences("MyId", getActivity().MODE_PRIVATE);
         userID = MyId.getString("MyId", "");
        //retrieve student information
        dbHelper = new dbHelper(getContext());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                volleyJsonArrayRequest(address);
            }
        },2000);


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
            staffID = ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_STAFFID));
            if(ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATESTART)).length() > 0){


                String DATE_FORMAT= "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);


                try {
                    Date END = sdf.parse(
                            ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATEEND))
                    );



                String dateDif = monthsBetweenDates(END);
                ileft.setText("Left : " + dateDif);

                    Date START = sdf.parse(
                            ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_DATESTART))
                    );


                    String DATE_FORMATTWO= "EEE, dd MMMM yyyy";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATTWO);

                istart.setText("Start Date : " + dateFormat.format(START));
                iend.setText("End Date : " + dateFormat.format(END));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

        //load company if one exist.
        checkCompanyAccepted();
        return rootView;
    }


    public void checkCompanyAccepted(){
        Cursor ITInfo = dbHelper.getAStudentItInfo(userID);

        if(ITInfo.getCount() >=1) {
            ITInfo.moveToFirst();
            companyID = ITInfo.getString(ITInfo.getColumnIndex(dbColumnList.abuadItInformation.COLUMN_COMPANYID));

            if (!companyID.equals("ABUAD")){
                Cursor companyInfo = dbHelper.getACompany(companyID);
                if(companyInfo.getCount() >=1) {
                    companyCard.setVisibility(View.VISIBLE);
                    companyInfo.moveToFirst();
                    cname.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYNAME)));

                    cemailphone.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYEMAIL)) +
                            " / " +  companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYPHONE)));

                    cstatelgov.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYSTATE)) +
                            " / " +  companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYLGOV)));

                    ccontact.setText(companyInfo.getString(companyInfo.getColumnIndex(dbColumnList.abuadCompany.COLUMN_COMPANYADDRESS)));

                }else{
                    companyCard.setVisibility(View.GONE);
                }
            }
            else{
                companyCard.setVisibility(View.GONE);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public String monthsBetweenDates(Date endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar endCalender = Calendar.getInstance();
        Calendar todayCalender = Calendar.getInstance();
        endCalender.setTimeInMillis(endDate.getTime());
        Date today = new Date();
        todayCalender.setTimeInMillis(today.getTime());

        int sYear = endCalender.get(Calendar.YEAR);
        int sMonth = endCalender.get(Calendar.MONTH);
        int sDay = endCalender.get(Calendar.DAY_OF_MONTH);

        int tYear = todayCalender.get(Calendar.YEAR);
        int tMonth = todayCalender.get(Calendar.MONTH);
        int tDay = todayCalender.get(Calendar.DAY_OF_MONTH);


        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(sYear, sMonth, sDay);

        date2.clear();
        date2.set(tYear, tMonth, tDay);

        long diff = date1.getTimeInMillis() - date2.getTimeInMillis();
        int dayCount=0;int monthCount=0;int weekCount=0;

        monthCount = (int) (diff / (2419200000f));
        if((diff % (2419200000f)) > 0){
            float weekdiff = diff - (monthCount*(2419200000f));
            weekCount = (int)(weekdiff/(7 * 24 * 60 * 60 * 1000));

            if (weekdiff % (7 * 24 * 60 * 60 * 1000) > 0 ){
                float daydiff = weekdiff - (weekCount * (7 * 24 * 60 * 60 * 1000));
                dayCount = (int) (daydiff / (24 * 60 * 60 * 1000));
                dayCount += (daydiff % (24 * 60 * 60 * 1000)) > 0 ? 1:0;
            }
        }

        return monthCount +" Months, " + weekCount +" Weeks, " + dayCount +" Days";
    }



    //load all available company in profile as well

    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (response.length()>2){
                            allResult = response;
                            new ReadJSON().execute();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "loadCompany");
                return params;
            }
        };
        AppSingleton.getInstance(getContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                dbHelper = new dbHelper(getContext());
                JSONArray jsonarray = new JSONArray(allResult);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    dbHelper.SaveCompanyInformation(
                            jsonobject.getString("companyName"),jsonobject.getString("companyPhone"),
                            jsonobject.getString("companyEmail"),jsonobject.getString("companyAddress"),
                            jsonobject.getString("companyDescription"),jsonobject.getString("companyState"),
                            jsonobject.getString("companyLocalGov"),jsonobject.getString("companyId")
                    );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
        }
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
