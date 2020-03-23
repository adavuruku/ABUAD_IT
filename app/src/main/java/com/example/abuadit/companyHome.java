package com.example.abuadit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class companyHome extends AppCompatActivity implements ListCompanyITStudent.OnFragmentInteractionListener,
        ListCompanyITApp.OnFragmentInteractionListener{
    private SharedPreferences MyId;
    String userID,allAttendance,companyID;
    Toolbar toolbar;
    dbHelper dbHelper;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    private SharedPreferences MyCompanyId;
//    String address = "http://192.168.1.64/abuadit/abuadrest.php";
String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MyCompanyId = getSharedPreferences("MyCompanyId",this.MODE_PRIVATE);
        companyID = MyCompanyId.getString("MyCompanyId", "");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setSaveEnabled(true);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        pd = new ProgressDialog(getApplicationContext());
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Processing Request ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    ListCompanyITStudent ListCompanyITStudent = new ListCompanyITStudent();
                    ListCompanyITStudent.setRetainInstance(true);
                    return ListCompanyITStudent;
                case 1:
                    ListCompanyITApp ListCompanyITApp = new ListCompanyITApp();
                    ListCompanyITApp.setRetainInstance(true);
                    return ListCompanyITApp;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "IT STUDENTS";
                case 1:
                    return "IT APPLICATIONS";

            }
            return null;
        }
    }
    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.companymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();;
        switch (id){
            case R.id.about:
                Intent intent = new Intent(getApplicationContext(), about.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.register:
                intent = new Intent(getApplicationContext(), CompanyRegister.class);
                intent.putExtra("USERID",userID);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.uploadServer:
                displaySaveMsg();
                break;
            case R.id.close:

                SharedPreferences.Editor editor;
                editor = MyCompanyId.edit();
                editor.putString("MyCompanyId", "");
                editor.apply();

                intent = new Intent(getApplicationContext(), LoginOption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void displayMessage(String msg) {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }
    public void displaySaveMsg(){
        String msg="Confirmation: Are You Sure You Want To Upload All Attendance Records " + System.getProperty("line.separator")+"On Your Local Server To ABUAD Server ? " + System.getProperty("line.separator")
                + "Please Confirm !";
        builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadlocalData();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alert.show();
    }

    //load all record in local db if any exist
    public void loadlocalData(){
        dbHelper = new dbHelper(getApplicationContext());
        Cursor cursor = dbHelper.getAllCompanyAttendance(companyID);
        if(cursor.getCount() >= 1) {
            int k = 1;
            JSONArray jsonarray = new JSONArray();
            while (cursor.moveToNext()) {
                JSONObject record = new JSONObject();
                try {
                    record.put("companyId",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_COMPANYID)));
                    record.put("stReg",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_REGNO)));
                    record.put("recordStatus",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_RECORSTATUS)));
                    record.put("recordDate",cursor.getString(cursor.getColumnIndex(dbColumnList.registerList.COLUMN_DATE)));
                    jsonarray.put(record);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONObject allrecord = new JSONObject();
            try {
                allrecord.put("allrecord",jsonarray);
                allAttendance = allrecord.toString();
                volleyJsonArrayRequestR(address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            if(pd.isShowing()){
                pd.hide();
            }
            displayMessage("Error: There Is No Record Of Attendance Available In The Server ..!!");
        }
    }

    public void volleyJsonArrayRequestR(String url) {
        String REQUEST_TAG = "com.volley.volleyJsonArrayRequestSave";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(pd.isShowing()){
                            pd.hide();
                        }
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            String error = jsonobject.getString("Error");
                            if(error.equals("Error")){
                                displayMessage("Success: Register Successfully Uploaded to Server !!!");
                                emptyAttendace();
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            displayMessage(e+ "Error: Unable to Upload Register Record to Server. Retry !!!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(pd.isShowing()){
                            pd.hide();
                        }
                        displayMessage("Error: Unable to Upload Register Record to Server. Retry !!!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("opr", "uploaddata");
                params.put("records", allAttendance);
                return params;
            }
        };
        AppSingleton.getInstance(this).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    //delete all record in database
    public void emptyAttendace(){
        dbHelper.deleteAttendace();
    }
}
