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

public class staffHome extends AppCompatActivity implements ListStaffITAttendance.OnFragmentInteractionListener,
        listStaffITStudent.OnFragmentInteractionListener{
    
    String userID,allAttendance,staffID;
    Toolbar toolbar;
    dbHelper dbHelper;
    private SharedPreferences MyStaffId;
    //    String address = "http://192.168.1.64/abuadit/abuadrest.php";
    String address = "https://abuadit.000webhostapp.com/abuadrest.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        if(getSupportActionBar()!=null)
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MyStaffId = getSharedPreferences("MyStaffId",this.MODE_PRIVATE);
        staffID = MyStaffId.getString("MyStaffId", "");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setSaveEnabled(true);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


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
                    listStaffITStudent listStaffITStudent = new listStaffITStudent();
                    listStaffITStudent.setRetainInstance(true);
                    return listStaffITStudent;
                case 1:
                    ListStaffITAttendance ListStaffITAttendance = new ListStaffITAttendance();
                    ListStaffITAttendance.setRetainInstance(true);
                    return ListStaffITAttendance;
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
                    return "ATTENDANCE LIST";

            }
            return null;
        }
    }
    //menu settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loginmenu, menu);
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
            case R.id.website:
                Intent i = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://abuadit.000webhostapp.com")
                );
                startActivity(i);
                break;
            case R.id.close:

                SharedPreferences.Editor editor;
                editor = MyStaffId.edit();
                editor.putString("MyStaffId", "");
                editor.apply();

                intent = new Intent(getApplicationContext(), LoginOption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }




}
