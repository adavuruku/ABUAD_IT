package com.example.abuadit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListStaffITAttendance extends Fragment {


    private OnFragmentInteractionListener mListener;

    public ListStaffITAttendance() {
        // Required empty public constructor
    }

    byte[] byteArray;
    private SharedPreferences MyStaffId;

    DatePickerDialog.OnDateSetListener setListener;
    int year, month, day;
    ArrayList<myModels.attendanceModel> newList;
    Button btnApply;
    private List<myModels.attendanceModel> allNoticeList;

    String userID,DrawerFullname,DrawerEmail,allResult,currentDate;
    Toolbar toolbar;
    studentAttendanceAdapter recyclerAdapter;
    dbHelper dbHelper;
    private static int SPLASH_TIME_OUT = 500;//0.5seconds
    ProgressDialog pd;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_staff_itattendance, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        progressBar =  rootView.findViewById(R.id.simpleProgressBar);

        allNoticeList = new ArrayList<>();

        mSwipeRefreshLayout = rootView.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyJsonArrayRequest(address);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        MyStaffId = getActivity().getSharedPreferences("MyStaffId", getActivity().MODE_PRIVATE);
        userID = MyStaffId.getString("MyStaffId", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                volleyJsonArrayRequest(address);
            }
        },SPLASH_TIME_OUT);

        pd = new ProgressDialog(getContext());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Processing Request ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(true);

        Calendar calender = Calendar.getInstance();
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        day = calender.get(Calendar.DAY_OF_MONTH);

        final Button btnApply = rootView.findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                currentDate = df.format(cal.getTime());
                String DATE_FORMAT = "EEE, d MMM yyyy, HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                String displayDate = dateFormat.format(cal.getTime());
                btnApply.setText(displayDate);
                newList = new ArrayList<>();
                for (myModels.attendanceModel cont : allNoticeList) {
                    String date = cont.getAttendanceDate().toLowerCase();
                    if (date.contains(currentDate)) {
                        newList.add(cont);
                    }
                }
                recyclerAdapter.setFilter(newList);
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerAdapter);
            }
        };

        return rootView;

    }

    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().length() <=2){
                            new LoadLocalData().execute();
                        }else{
                            allResult = response;
                            new ReadJSON().execute();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new LoadLocalData().execute();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "loadStaffAttendance");
                params.put("MyStaffId", userID);
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
                allNoticeList.clear();
                JSONArray jsonarray = new JSONArray(allResult);
                dbHelper.deleteAttendace();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    dbHelper.saveRegister(
                            jsonobject.getString("regno"),
                            jsonobject.getString("companyId"),
                            jsonobject.getString("recordStatus"),
                            jsonobject.getString("recordDate")
                    );

                    Cursor curs = dbHelper.getAStudent( jsonobject.getString("regno"));
                    if(curs.getCount()>0){
                        curs.moveToFirst();
                        DrawerFullname = curs.getString(curs.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FULLNAME));
                    }
                    myModels.attendanceModel attendanceModel = new myModels().new attendanceModel(
                            DrawerFullname,
                            jsonobject.getString("recordStatus"),
                            jsonobject.getString("recordDate")
                    );

                    allNoticeList.add(attendanceModel);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            loadData();
            super.onPostExecute(s);
        }
    }


    class LoadLocalData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                dbHelper = new dbHelper(getContext());
                allNoticeList.clear();
                Cursor allCompany = dbHelper.getAllAttendance();
                if (allCompany.getCount() > 0) {
                    while (allCompany.moveToNext()) {

                        Cursor curs = dbHelper.getAStudent(
                                allCompany.getString(allCompany.getColumnIndex(dbColumnList.registerList.COLUMN_REGNO))
                        );
                        if(curs.getCount()>0){
                            curs.moveToFirst();
                            DrawerFullname = curs.getString(curs.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FULLNAME));
                        }
                        curs.close();

                        myModels.attendanceModel attendanceModel = new myModels().new attendanceModel(
                                DrawerFullname,
                                allCompany.getString(allCompany.getColumnIndex(dbColumnList.registerList.COLUMN_RECORSTATUS)),
                                allCompany.getString(allCompany.getColumnIndex(dbColumnList.registerList.COLUMN_DATE))
                        );
                        allNoticeList.add(attendanceModel);
                    }
                }
            } finally { }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            loadData();
            super.onPostExecute(s);
        }
    }

    public void loadData(){
        recyclerAdapter = new studentAttendanceAdapter( allNoticeList, getContext(), new studentAttendanceAdapter.OnItemClickListener() {

        });
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        progressBar.setVisibility(View.GONE);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
