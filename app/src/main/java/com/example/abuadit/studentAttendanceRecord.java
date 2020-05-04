package com.example.abuadit;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class studentAttendanceRecord extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    DatePickerDialog.OnDateSetListener setListener;
    int year, month, day;
    ArrayList<myModels.attendanceModel> newList;
    Button btnApply;
    private List<myModels.attendanceModel> allNoticeList;
    private NavigationView navigationView;
    private SharedPreferences MyId;
    String userID,DrawerFullname,DrawerEmail,allResult,currentDate;
    Toolbar toolbar;
    studentAttendanceAdapter recyclerAdapter;
    dbHelper dbHelper;
    private static int SPLASH_TIME_OUT = 500;//0.5seconds
    ProgressDialog pd;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
//    String address = "http://192.168.1.64/abuadit/abuadrest.php";
String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_record);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        progressBar =  findViewById(R.id.simpleProgressBar);

        allNoticeList = new ArrayList<>();

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volleyJsonArrayRequest(address);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        MyId = this.getSharedPreferences("MyId", this.MODE_PRIVATE);
        userID = MyId.getString("MyId", "");
        //retrieve student information
        dbHelper = new dbHelper(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                volleyJsonArrayRequest(address);
            }
        },SPLASH_TIME_OUT);

        pd = new ProgressDialog(getApplicationContext());
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

        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        studentAttendanceRecord.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                currentDate = df.format(cal.getTime());
                String DATE_FORMAT= "EEE, d MMM yyyy, HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                String displayDate = dateFormat.format(cal.getTime());
                btnApply.setText(displayDate);
                newList = new ArrayList<>();
                for(myModels.attendanceModel  cont : allNoticeList){
                    String date = cont.getAttendanceDate().toLowerCase();
                    if(date.contains(currentDate)){
                        newList.add(cont);
                    }
                }
                recyclerAdapter.setFilter(newList);
                recyclerAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerAdapter);


            }
        };


        initNavigationDrawer();
    }

    public void volleyJsonArrayRequest(String url){
        String  REQUEST_TAG = "com.volley.volleyJsonArrayRequest";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (response.length() <=2){
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
                params.put("opr", "loadStudentAttendance");
                params.put("userId", userID);
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest, REQUEST_TAG);
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                dbHelper = new dbHelper(getApplicationContext());
                allNoticeList.clear();
                dbHelper.deleteAttendace();
                JSONArray jsonarray = new JSONArray(allResult);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    //empty all existing attendance on db
                    dbHelper.saveRegister(
                            userID,
                            jsonobject.getString("companyId"),
                            jsonobject.getString("recordStatus"),
                            jsonobject.getString("recordDate")
                    );

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
                dbHelper = new dbHelper(getApplicationContext());
                allNoticeList.clear();
                Cursor allCompany = dbHelper.getAllStudentAttendance(userID);
                if (allCompany.getCount() > 0) {
                    while (allCompany.moveToNext()) {
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
        recyclerAdapter = new studentAttendanceAdapter( allNoticeList, getApplicationContext(), new studentAttendanceAdapter.OnItemClickListener() {

        });
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        progressBar.setVisibility(View.GONE);
    }

    public void initNavigationDrawer() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent;
                int id = menuItem.getItemId();
                drawerLayout.closeDrawers();
                switch (id) {
                    case R.id.about:
                        intent = new Intent(getApplicationContext(), about.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        break;
                    case R.id.appstatus:
                        intent = new Intent(getApplicationContext(), StudentCompany.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        break;
                    case R.id.register:
//                        intent = new Intent(getApplicationContext(), studentAttendanceRecord.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
//                        break;
                    case R.id.close:
                        SharedPreferences.Editor editor;
                        editor = MyId.edit();
                        editor.putString("MyId", "");
                        editor.apply();

                        intent = new Intent(getApplicationContext(), LoginOption.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        finish();
                        break;
                }
                return true;
            }
        });

        byte[] image_data=null;
        Cursor profpics = dbHelper.getAProfilePics(userID);
        if (profpics.getCount() >= 1) {
            profpics.moveToFirst();
            image_data = profpics.getBlob(profpics.getColumnIndex(dbColumnList.userProfilePics.COLUMN_PROFILEPICS));
        }
        profpics.close();

        profpics = dbHelper.getAStudent(userID);
        if (profpics.getCount() >= 1) {
            profpics.moveToFirst();
            DrawerEmail = profpics.getString(profpics.getColumnIndex(dbColumnList.abuadstudent.COLUMN_EMAIL));
            DrawerFullname = profpics.getString(profpics.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FULLNAME));
        }
        profpics.close();

        View header = navigationView.getHeaderView(0);
        TextView tv_email =  header.findViewById(R.id.email);
        TextView tv_fullname =  header.findViewById(R.id.uname);
        tv_email.setText(DrawerEmail);
        tv_fullname.setText(DrawerFullname);
        ImageView imageV = header.findViewById(R.id.profile_image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_data, 0, image_data.length);
        imageV.setImageBitmap(bitmap);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }


    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.about:
                intent = new Intent(getApplicationContext(), about.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            case R.id.appstatus:
                intent = new Intent(getApplicationContext(), StudentCompany.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            case R.id.home:
                intent = new Intent(getApplicationContext(), HomeScreen.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            case R.id.close:
                SharedPreferences.Editor editor;
                editor = MyId.edit();
                editor.putString("MyId", "");
                editor.apply();

                intent = new Intent(getApplicationContext(), LoginOption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }
}
