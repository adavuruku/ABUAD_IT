package com.example.abuadit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyRegister extends AppCompatActivity {
    Button btnApply;
    DatePickerDialog.OnDateSetListener setListener;
    int year, month, day;
    Toolbar toolbar;
    private List<myModels.studentModel> allNoticeList;
    private static int SPLASH_TIME_OUT = 500;//0.5seconds
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String fullname, email,comingNews;
    private boolean isConnected = false;
    RecyclerView recyclerView;
    private studentAdapter recyclerAdapter;
    String search, allResult,userID,companyID, regNo, currentDate = "" ;
    ProgressBar progressBar;
    dbHelper dbHelper;
    byte[] byteArray;
    private SharedPreferences MyCompanyId;
    ArrayList<myModels.companyModel> noticeList;
//    String address = "http://192.168.1.64/abuadit/abuadrest.php";
String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnApply = findViewById(R.id.btnApply);
        Calendar calender = Calendar.getInstance();
        year = calender.get(Calendar.YEAR);
        month = calender.get(Calendar.MONTH);
        day = calender.get(Calendar.DAY_OF_MONTH);

        btnApply.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CompanyRegister.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                month +=1;
//                currentDate = year+"-"+month+"-"+dayOfMonth;
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                currentDate = df.format(cal.getTime());

                String DATE_FORMAT= "EEE, d MMM yyyy, HH:mm:ss";
                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                String displayDate = dateFormat.format(cal.getTime());
                btnApply.setText(displayDate);

//                String token = String.format("%1$tA %1$tb %1$td %1$tY",currentDate);
//                Toast.makeText(CompanyRegister.this,token, Toast.LENGTH_LONG).show();
            }
        };


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        progressBar =  findViewById(R.id.simpleProgressBar);

        allNoticeList = new ArrayList<>();

        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new LoadLocalData().execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        MyCompanyId = this.getSharedPreferences("MyCompanyId", this.MODE_PRIVATE);
        companyID = MyCompanyId.getString("MyCompanyId", "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new LoadLocalData().execute();
            }
        },SPLASH_TIME_OUT);

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Processing Request ...");
        pd.setTitle(R.string.app_name);
        pd.setIcon(R.mipmap.ic_launcher);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
    }

    //load local no internet connection
    class LoadLocalData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                dbHelper = new dbHelper(getApplicationContext());
                allNoticeList.clear();
                Cursor allCompany = dbHelper.getAllCompanyApplication(companyID, "2");
                if (allCompany.getCount() > 0) {
                    while (allCompany.moveToNext()) {

                        Cursor aStudent = dbHelper.getAStudent(
                                allCompany.getString(allCompany.getColumnIndex(dbColumnList.applicationList.COLUMN_REGNO))
                        );
                        if (aStudent.getCount() > 0) {
                            aStudent.moveToFirst();

                            //get student picture
                            Cursor studPics = dbHelper.getAProfilePics(allCompany.getString(allCompany.getColumnIndex(dbColumnList.applicationList.COLUMN_REGNO)));
                            if (studPics.getCount() > 0) {
                                studPics.moveToFirst();
                                byteArray = studPics.getBlob(studPics.getColumnIndex(dbColumnList.userProfilePics.COLUMN_PROFILEPICS));
                            }
                            studPics.close();

                            myModels.studentModel studentModel = new myModels().new studentModel(
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FULLNAME)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_FACULTY)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_DEPARTMENT)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_LEVEL)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_STATE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_LGOV)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_GENDER)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_EMAIL)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_PHONE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_REGNO)),
                                    byteArray,
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_MODE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_DEGREE)),
                                    aStudent.getString(aStudent.getColumnIndex(dbColumnList.abuadstudent.COLUMN_CONTACTADD)),"0",
                                    "mark"
                            );
                            allNoticeList.add(studentModel);
                        }
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
        recyclerAdapter = new studentAdapter( allNoticeList, this, new studentAdapter.OnItemClickListener() {
            @Override
            public void onNameClick(View v, int position) {
//                Toast.makeText(getContext(),"Welcome "+ postid + " To ABUAD IT - MOBILE APP",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onMarkClick(View v, int position) {
                String sta =  allNoticeList.get(position).getAttendanceStatus();
                if(sta.equals("1")){
                    allNoticeList.get(position).setAttendanceStatus("0");
                }else{
                    allNoticeList.get(position).setAttendanceStatus("1");
                }
            }

            @Override
            public void onImageClick(View v, int position) {
                View snackView = getLayoutInflater().inflate(R.layout.customlearn, null);

                ImageView imv = snackView.findViewById(R.id.profile_pic);
                Bitmap bitmap = BitmapFactory.decodeByteArray(allNoticeList.get(position).getProfilePic(), 0,allNoticeList.get(position).getProfilePic().length);
                imv.setImageBitmap(bitmap);

                TextView myvi = snackView.findViewById(R.id.txtUser);
                TextView pname = snackView.findViewById(R.id.pname);

                myvi.setText(allNoticeList.get(position).getStudName());
                pname.setText(allNoticeList.get(position).getStudName());

                TextView pfaculty = snackView.findViewById(R.id.pfaculty);
                pfaculty.setText(
                        "Faculty Of "+allNoticeList.get(position).getStudFaculty() + " / "+ allNoticeList.get(position).getStudDept()
                );

                TextView pregno = snackView.findViewById(R.id.pregno);
                pregno.setText(
                        allNoticeList.get(position).getRegno() + " / "+ allNoticeList.get(position).getStudLevel() + " Level"
                );

                TextView pemailphone = snackView.findViewById(R.id.pemailphone);
                pemailphone.setText(
                        allNoticeList.get(position).getStudEmail() + " / "+ allNoticeList.get(position).getStudPhone()
                );

                TextView pmodetype = snackView.findViewById(R.id.pemailphone);
                pmodetype.setText(
                        allNoticeList.get(position).getMode() + " / "+ allNoticeList.get(position).getDegree()
                );

                TextView pstate = snackView.findViewById(R.id.pstate);
                pstate.setText(
                        allNoticeList.get(position).getItState() + " State / "+ allNoticeList.get(position).getItLgov()
                );

                TextView pcontact = snackView.findViewById(R.id.pcontact);
                pcontact.setText(
                        allNoticeList.get(position).getContactAddress()
                );

                TextView pitphoneemail = snackView.findViewById(R.id.pitphoneemail);
                pitphoneemail.setVisibility(View.GONE);
                TextView pitaddress = snackView.findViewById(R.id.pitaddress);
                pitaddress.setVisibility(View.GONE);
                TextView pit = snackView.findViewById(R.id.pit);
                pit.setVisibility(View.GONE);

                final Dialog d = new Dialog(CompanyRegister.this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setCanceledOnTouchOutside(true);
                d.setContentView(snackView);
                d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                d.show();
            }
        });
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.GONE);
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

    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.companymenuregister, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        AlertDialog  alert;
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.about:
                Intent intent = new Intent(getApplicationContext(), about.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.home:
                intent = new Intent(getApplicationContext(), companyHome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            case R.id.save:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure You Want to Save / Upload this Attendance To the Server ... ?. ");
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        submitToLocalServe();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert = builder.create();
                alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                alert.show();
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

    public void submitToLocalServe() {
        if (pd.isShowing()) {
            pd.hide();
            pd.cancel();
        }
        pd.show();
        dbHelper = new dbHelper(getApplicationContext());
        if (!currentDate.equals("")){
            for (int i = 0; i < allNoticeList.size(); i++) {
                dbHelper.saveRegister(
                        allNoticeList.get(i).getRegno(),
                        companyID,
                        allNoticeList.get(i).getAttendanceStatus(),
                        currentDate
                );
            }
            if (pd.isShowing()) {
                pd.hide();
                pd.cancel();
            }
            displayMessage("Attendance Successfully Uploaded To Local Server !!! ");
        }else{
            if (pd.isShowing()) {
                pd.hide();
                pd.cancel();
            }
            displayMessage("Select A Valid Date For Attendance !!");
        }




    }
}
